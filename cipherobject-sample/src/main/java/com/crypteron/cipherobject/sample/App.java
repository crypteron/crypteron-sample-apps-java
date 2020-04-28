package com.crypteron.cipherobject.sample;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import com.crypteron.CipherObject;
import com.crypteron.Opt;
import com.crypteron.ciphercore.crypto.CrypteronDefaultConstants;
import com.crypteron.ciphercore.crypto.CrypteronObjectProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class App {
  public static void main(String[] args) throws Exception {
    // if testing in a custom enterprise environment prior to PKI based SSL certificates
    // this is obviously dangerous anywhere outside test environments
    // trustSelfSignedCertificates();

    ////////////////////////////////////
    System.out.println("Object level encryption");
    final Customer cust = new Customer();
    cust.setId(1);
    cust.setSsn(new BigInteger("123456789"));
    cust.setName("John Smith");
    cust.setCardNumber("4567-8901-2345-6789");
    cust.setAccountNotes("Budget is $50,000/store");

    System.out.println("Original\n" + cust.toString() + '\n');

    CipherObject.seal(cust);
    System.out.println("Encrypted\n" + cust.toString() + '\n');

    CipherObject.unseal(cust);
    System.out.println("Decrypted\n" + cust.toString() + '\n');

    CipherObject.seal(cust);
    CipherObject.unseal(cust, CrypteronDefaultConstants.DEFAULT_SEC_PART_ID, "mask");
    System.out.println("Masked\n" + cust.toString() + '\n');

    System.out.println("-----------------------------------");

    ////////////////////////////////////
    System.out.println("\nField level encryption");
    EncryptedString es = new EncryptedString();
    es.setString("123 Main St, Unit 100, San Diego, CA 92101");
    System.out.println("Original : " + es.getString() + '\n');

    CipherObject.seal(es);
    System.out.println("Encrypted: " + es.getString() + '\n');

    CipherObject.unseal(es);
    System.out.println("Decrypted: " + es.getString() + '\n');

    CipherObject.seal(es);
    CipherObject.unseal(es, CrypteronDefaultConstants.DEFAULT_SEC_PART_ID, "mask");
    System.out.println("Masked   : " + es.getString() + '\n');

    System.out.println("-----------------------------------");

    ////////////////////////////////////
    System.out.println("\nProperty collection encryption");
    ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
    final Collection<CrypteronObjectProperty> properties = Arrays.asList(createProperty(false, "id", 1, Opt.NONE, null),
        createProperty(true, "ssn", new BigInteger("123456789"), Opt.TOKEN, null),
        createProperty(false, "name", "John Smith", Opt.NONE, null),
        createProperty(true, "cardNumber", "4567-8901-2345-6789", Opt.TOKEN, null),
        createProperty(true, "accountNotes", "Budget is $50,000/store", Opt.NONE, "6*"));
    System.out.println("Original\n" + writer.writeValueAsString(properties) + '\n');

    final Collection<CrypteronObjectProperty> sealedProperties = CipherObject.seal(properties);
    System.out.println("Encrypted\n" + writer.writeValueAsString(sealedProperties) + '\n');

    final Collection<CrypteronObjectProperty> unsealedProperties = CipherObject.unseal(sealedProperties);
    System.out.println("Decrypted\n" + writer.writeValueAsString(unsealedProperties) + '\n');

    final Collection<CrypteronObjectProperty> unsealedMaskedProperties = CipherObject.unseal(sealedProperties,
        CrypteronDefaultConstants.DEFAULT_SEC_PART_ID, "mask");
    System.out.println("Masked\n" + writer.writeValueAsString(unsealedMaskedProperties) + '\n');
  }

  private static CrypteronObjectProperty createProperty(boolean secure, String name, Object value, int opts,
      String mask) {
    CrypteronObjectProperty property = new CrypteronObjectProperty();
    property.setSecure(secure);
    property.setName(name);
    property.setValue(value);
    property.setOpts(opts);
    property.setMask(mask);
    return property;
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
