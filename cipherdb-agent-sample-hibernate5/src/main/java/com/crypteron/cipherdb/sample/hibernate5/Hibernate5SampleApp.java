package com.crypteron.cipherdb.sample.hibernate5;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import com.crypteron.cipherdb.sample.SampleApp;

public class Hibernate5SampleApp {
  public static void main(final String[] args) throws Exception {
	  
    // if testing in a custom enterprise environment prior to PKI based SSL certificates
    // this is obviously dangerous anywhere outside test environments 
    // trustSelfSignedCertificates();
    
    final SampleApp sampleApp = new SampleApp(System.in, System.out);
    sampleApp.run();
  }
  
  private static void trustSelfSignedCertificates() throws Exception {
    HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    final SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, new X509TrustManager[] { 
      new X509TrustManager() {
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
      } 
    }, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
  }
}
