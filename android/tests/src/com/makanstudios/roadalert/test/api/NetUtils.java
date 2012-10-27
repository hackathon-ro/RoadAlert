
package com.makanstudios.roadalert.test.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Common utilities used to call internet web services
 * 
 * @author ka
 */
public class NetUtils {

    public static javax.net.ssl.SSLSocketFactory getFactory(File file,
            String storeType, String storePass) {
        try {
            InputStream keyInput = new FileInputStream(file);

            KeyStore keyStore = KeyStore.getInstance(storeType);
            keyStore.load(keyInput, storePass.toCharArray());
            keyInput.close();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance("SunX509");
            keyManagerFactory.init(keyStore, storePass.toCharArray());

            InputStream in = new FileInputStream(file);
            SSLContext context = SSLContext.getInstance("TLS");
            context
                    .init(keyManagerFactory.getKeyManagers(),
                            new X509TrustManager[] {
                                new MyX509TrustManager(
                                        keyStore)
                            }, new SecureRandom());

            in.close();

            return context.getSocketFactory();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (KeyStoreException ioe) {
            ioe.printStackTrace();
        } catch (CertificateException ce) {
            ce.printStackTrace();
        } catch (KeyManagementException kme) {
            kme.printStackTrace();
        } catch (UnrecoverableKeyException uke) {
            uke.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class MyX509TrustManager implements X509TrustManager {

        protected ArrayList<X509TrustManager> x509TrustManagers = new ArrayList<X509TrustManager>();

        protected MyX509TrustManager(KeyStore... additionalkeyStores) {
            final ArrayList<TrustManagerFactory> factories = new ArrayList<TrustManagerFactory>();

            try {
                // The default Trustmanager with default keystore
                final TrustManagerFactory original = TrustManagerFactory
                        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                original.init((KeyStore) null);
                factories.add(original);

                for (KeyStore keyStore : additionalkeyStores) {
                    final TrustManagerFactory additionalCerts = TrustManagerFactory
                            .getInstance(TrustManagerFactory
                                    .getDefaultAlgorithm());
                    additionalCerts.init(keyStore);
                    factories.add(additionalCerts);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            /*
             * Iterate over the returned trustmanagers, and hold on to any that
             * are X509TrustManagers
             */
            for (TrustManagerFactory tmf : factories)
                for (TrustManager tm : tmf.getTrustManagers())
                    if (tm instanceof X509TrustManager)
                        x509TrustManagers.add((X509TrustManager) tm);

            if (x509TrustManagers.size() == 0)
                throw new RuntimeException(
                        "Couldn't find any X509TrustManagers");

        }

        /*
         * Delegate to the default trust manager.
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            final X509TrustManager defaultX509TrustManager = x509TrustManagers
                    .get(0);
            defaultX509TrustManager.checkClientTrusted(chain, authType);
        }

        /*
         * Loop over the trustmanagers until we find one that accepts our server
         */
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            for (X509TrustManager tm : x509TrustManagers) {
                try {
                    tm.checkServerTrusted(chain, authType);
                    return;
                } catch (CertificateException e) {
                    // ignore
                }
            }
            throw new CertificateException();
        }

        public X509Certificate[] getAcceptedIssuers() {
            final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
            for (X509TrustManager tm : x509TrustManagers)
                list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
            return list.toArray(new X509Certificate[list.size()]);
        }
    }
}
