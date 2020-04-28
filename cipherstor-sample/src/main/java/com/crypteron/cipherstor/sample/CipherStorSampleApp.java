package com.crypteron.cipherstor.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypteron.CipherStor;

public class CipherStorSampleApp {
  private static final Logger LOG                       = LoggerFactory.getLogger(CipherStorSampleApp.class);
  private static final String TARGET_FOLDER_PREFIX      = "target";
  private static final String TARGET_FOLDER_NAME        = "classes/Files";
  private static final String DECRYPTED_FILE_CLASSIFIER = ".recover.";
  private final InputStream   in;
  private final PrintStream   out;
  private boolean             compress                  = false;
  private String              fileName                  = "TestText.txt";
  private final File          testFolder;
  private final CipherStor    cipherStor;

  public CipherStorSampleApp(final InputStream in, final PrintStream out) {
    super();
    this.in = in;
    this.out = out;
    this.testFolder = getTestFolder();
    this.cipherStor = new CipherStor();
  }

  private File getTestFolder() {
    File targetFolder = new File("").getAbsoluteFile();
    if (!TARGET_FOLDER_PREFIX.equals(targetFolder.getName())) {
      targetFolder = new File(targetFolder, TARGET_FOLDER_PREFIX);
    }
    final File testFolder = new File(targetFolder, TARGET_FOLDER_NAME);
    testFolder.mkdirs();
    return testFolder;
  }

  private File getCurrentFile() {
    return new File(this.testFolder, this.fileName);
  }

  private String getAbsolutePath() {
    return getCurrentFile().getAbsolutePath();
  }

  private File getEncryptedFile() {
    final String encryptedFilePath = getAbsolutePath() + CipherStor.CIPHER_FILE_EXTENSION;
    return new File(encryptedFilePath);
  }

  private File getDecryptedFile() {
    final String decryptedFilePath = getAbsolutePath() + DECRYPTED_FILE_CLASSIFIER
        + com.google.common.io.Files.getFileExtension(getAbsolutePath());
    return new File(decryptedFilePath);
  }

  private void printMenu() {
    this.out.println("====================================================");
    this.out.println("E : (E)ncrypt test file");
    this.out.println("D : (D)ecrypt test file");
    this.out.println(String.format("C : Toggle (C)ompression[%b]", this.compress));
    this.out.println(String.format("F : Change (F)ile[%s]", this.fileName));
    this.out.println("Q : (Q)uit");
    this.out.print(String.format("\nAll files are in the '%s' folder", this.testFolder));
    this.out.print("\nEnter your selection: ");
  }

  public void run() {
    boolean ongoing = true;
    while (ongoing) {
      printMenu();
      try {
        final Optional<String> userInput = Optional.of(getUserInput());
        this.out.println();
        final char option = userInput.map(String::toLowerCase).filter(s -> !s.isEmpty()).map(string -> string.charAt(0))
            .orElse(Character.MIN_VALUE);
        switch (option) {
          case 'e':
            encryptFile();
            break;
          case 'd':
            decryptFile();
            break;
          case 'c':
            this.compress = !this.compress;
            this.out.println(String.format("Compression is now:[%b]", this.compress));
            break;
          case 'f':
            setLocalTestFile();
            break;
          case 'q':
            ongoing = false;
            break;
          default:
            this.out.println("Unknown option: " + option);
            break;
        }
      } catch (final Exception e) {
        LOG.error("uh oh", e);
        ongoing = false;
      }
      this.out.println();
    }
  }

  private String getUserInput() throws IOException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(this.in));
    return reader.readLine();
  }

  private void encryptFile() throws IOException, GeneralSecurityException {
    this.cipherStor.encryptLocalFile(getCurrentFile(), getEncryptedFile(), this.compress);
  }

  private void decryptFile() throws IOException, GeneralSecurityException {
    this.cipherStor.decryptLocalFile(getEncryptedFile(), getDecryptedFile());
  }

  private void setLocalTestFile() throws IOException {
    // Erase any temp files that may exist from prev runs
    for (final File existingFile : this.testFolder.listFiles(
        (dir, name) -> name.contains(DECRYPTED_FILE_CLASSIFIER) || name.endsWith(CipherStor.CIPHER_FILE_EXTENSION))) {
      existingFile.delete();
    }

    // Read again after delete
    final File[] fileNames = this.testFolder.listFiles();
    this.out.println(String.format("Choices are files in test folder[%s]:", this.testFolder));
    for (final File file : fileNames) {
      this.out.println(file.getName());
    }

    this.out.print("Enter filename : ");
    this.fileName = getUserInput();
  }

  public static void main(final String[] args) throws Exception {
    // if testing in a custom enterprise environment prior to PKI based SSL certificates
    // this is obviously dangerous anywhere outside test environments
    // trustSelfSignedCertificates();

    final CipherStorSampleApp sampleApp = new CipherStorSampleApp(System.in, System.out);
    sampleApp.run();
  }

  private static void trustSelfSignedCertificates() throws Exception {
    HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    final SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, new X509TrustManager[] { new X509TrustManager() {
      @Override
      public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
      }

      @Override
      public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
      }

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    } }, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
  }
}
