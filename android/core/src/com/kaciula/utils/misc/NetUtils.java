
package com.kaciula.utils.misc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.SparseArray;

import com.kaciula.utils.ui.BasicApplication;

/**
 * Common utilities used to call internet web services
 * 
 * @author ka
 */
public class NetUtils {

    // private static final String TAG = "NetUtils";

    private static HttpClient httpClient;

    private static SparseArray<HttpClient> httpsClients;

    private static HttpClient trustfulHttpsClient;

    public static final int SECOND_IN_MILLIS = (int) DateUtils.SECOND_IN_MILLIS;

    public static final int CONNECTION_TIMEOUT = 20; // seconds

    private static final int SOCKET_TIMEOUT = 20; // seconds

    private static final int SOCKET_BUFFER_SIZE = 8192;

    private static final int MAX_CONNECTIONS = 50;

    public static HttpClient getHttpClient() {
        if (httpClient == null)
            httpClient = createHttpClient(false, 0, "", "");

        return httpClient;
    }

    static {
        httpsClients = new SparseArray<HttpClient>();
    }

    public static HttpClient getHttpsClient(int certRawId, String storeType, String storePass,
            int type) {
        HttpClient httpsClient = httpsClients.valueAt(type);

        if (httpsClient == null) {
            httpsClient = createHttpClient(true, certRawId, storeType, storePass);
            httpsClients.put(type, httpsClient);
        }

        return httpsClient;
    }

    public static HttpClient getTrustfulHttpsClient() {
        if (trustfulHttpsClient == null)
            trustfulHttpsClient = createTrustfulHttpsClient();

        return trustfulHttpsClient;
    }

    /**
     * Create a https client that trusts all certificates
     * 
     * @return
     */
    public static HttpClient createTrustfulHttpsClient() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme(MiscConstants.NET_SCHEME_HTTP, PlainSocketFactory
                .getSocketFactory(), 80));

        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme(MiscConstants.NET_SCHEME_HTTPS, socketFactory, 443));

        DefaultHttpClient client = new DefaultHttpClient();
        ConnManagerParams.setMaxTotalConnections(client.getParams(), MAX_CONNECTIONS);
        ClientConnectionManager mgr = new ThreadSafeClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

        // Set verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        return httpClient;
    }

    /**
     * Create a HttpClient either for http or https
     * 
     * @param context
     * @param useCert
     * @param certRawId
     * @param storeType
     * @param storePass
     * @return
     */
    private static HttpClient createHttpClient(boolean useCert, int certRawId, String storeType,
            String storePass) {
        Context ctx = BasicApplication.getContext();
        final HttpParams params = new BasicHttpParams();

        // Use generous timeouts for slow mobile networks
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT * SECOND_IN_MILLIS);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT * SECOND_IN_MILLIS);

        HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
        HttpProtocolParams.setUserAgent(params, buildUserAgent());

        final DefaultHttpClient client;
        if (useCert) {
            client = new BasicHttpClient(params, ctx.getResources().openRawResource(certRawId),
                    storeType, storePass);
        } else {
            // Create and initialize scheme registry
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme(MiscConstants.NET_SCHEME_HTTP, PlainSocketFactory
                    .getSocketFactory(), 80));

            ConnManagerParams.setMaxTotalConnections(params, MAX_CONNECTIONS);
            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
            client = new DefaultHttpClient(cm, params);
        }

        client.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest request, HttpContext context) {
                // Add header to accept gzip content
                if (!request.containsHeader(MiscConstants.NET_HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(MiscConstants.NET_HEADER_ACCEPT_ENCODING,
                            MiscConstants.ENCODING_GZIP);
                }
            }
        });

        client.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext context) {
                // Inflate any responses compressed with gzip
                final HttpEntity entity = response.getEntity();
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(MiscConstants.ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            break;
                        }
                    }
                }
            }
        });

        return client;
    }

    /**
     * Build and return a user-agent string that can identify this application
     * to remote servers. Contains the package name and version code.
     */
    private static String buildUserAgent() {
        try {
            Context context = BasicApplication.getContext();
            final PackageManager manager = context.getPackageManager();
            final PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            // Some APIs require "(gzip)" in the user-agent string.
            return info.packageName + "/" + info.versionName + " (" + info.versionCode + ") (gzip)";
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }

    /**
     * Check internet availability
     * 
     * @param ctx
     * @return
     */
    public static boolean isInternetAvailable() {
        NetworkInfo info = ((ConnectivityManager) BasicApplication.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        return info != null && info.isAvailable() && info.isConnected();
    }

    /**
     * Returns the text from a url
     * 
     * @param url
     * @return
     */
    public static String getStringResponse(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        try {
            ResponseHandler<String> response = new BasicResponseHandler();
            String body = client.execute(get, response);
            return body;
        } catch (Throwable t) {
            get.abort();
        } finally {
            client.getConnectionManager().shutdown();
        }

        return null;
    }

    /**
     * Return an image from an url
     * 
     * @param imageUrl
     * @return
     */
    public static byte[] getImageData(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            byte[] imageData;

            // determine the image size and allocate a buffer
            int fileSize = connection.getContentLength();
            if (fileSize > 0) {
                imageData = new byte[fileSize];

                // download the file
                LogUtils.d("NetUtils", "fetching image " + imageUrl + " (" + fileSize + ")");
                BufferedInputStream istream = new BufferedInputStream(connection.getInputStream());
                int bytesRead = 0;
                int offset = 0;
                while (bytesRead != -1 && offset < fileSize) {
                    bytesRead = istream.read(imageData, offset, fileSize - offset);
                    offset += bytesRead;
                }

                // clean up
                istream.close();
            } else {
                LogUtils.d("NetUtils", "fetching image " + imageUrl + " (no file size known)");
                imageData = StreamUtils.readByte(new BufferedInputStream(connection
                        .getInputStream()));
            }

            connection.disconnect();

            return imageData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * HttpClient used for https. It uses a certificate store from the
     * application.
     * 
     * @author ka
     */
    private static class BasicHttpClient extends DefaultHttpClient {

        private InputStream isCert;

        private String storeType;

        private String storePass;

        public BasicHttpClient(HttpParams params, InputStream isCert, String storeType,
                String storePass) {
            super(params);
            this.isCert = isCert;
            this.storeType = storeType;
            this.storePass = storePass;
        }

        @Override
        protected ClientConnectionManager createClientConnectionManager() {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme(MiscConstants.NET_SCHEME_HTTP, PlainSocketFactory
                    .getSocketFactory(), 80));

            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLSocketFactory socketFactory = newSslSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme(MiscConstants.NET_SCHEME_HTTPS, socketFactory, 443));

            ConnManagerParams.setMaxTotalConnections(getParams(), MAX_CONNECTIONS);
            return new ThreadSafeClientConnManager(getParams(), registry);
        }

        private SSLSocketFactory newSslSocketFactory() {
            try {
                KeyStore trusted = KeyStore.getInstance(storeType);
                try {
                    trusted.load(isCert, storePass.toCharArray());
                } finally {
                    isCert.close();
                }
                return new SSLSocketFactory(trusted);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }

    public static javax.net.ssl.SSLSocketFactory getFactory(int certRawId, String storeType,
            String storePass) {
        try {
            InputStream keyInput = BasicApplication.getContext().getResources()
                    .openRawResource(certRawId);

            KeyStore keyStore = KeyStore.getInstance(storeType);
            keyStore.load(keyInput, storePass.toCharArray());
            keyInput.close();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, storePass.toCharArray());

            InputStream in = BasicApplication.getContext().getResources()
                    .openRawResource(certRawId);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), new X509TrustManager[] {
                new MyX509TrustManager(keyStore)
            }, new SecureRandom());

            in.close();

            return context.getSocketFactory();
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        } catch (NoSuchAlgorithmException nsae) {
            LogUtils.printStackTrace(nsae);
        } catch (KeyStoreException ioe) {
            LogUtils.printStackTrace(ioe);
        } catch (CertificateException ce) {
            LogUtils.printStackTrace(ce);
        } catch (KeyManagementException kme) {
            LogUtils.printStackTrace(kme);
        } catch (UnrecoverableKeyException uke) {
            LogUtils.printStackTrace(uke);
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
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
                            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    additionalCerts.init(keyStore);
                    factories.add(additionalCerts);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            /*
             * Iterate over the returned trustmanagers, and hold on
             * to any that are X509TrustManagers
             */
            for (TrustManagerFactory tmf : factories)
                for (TrustManager tm : tmf.getTrustManagers())
                    if (tm instanceof X509TrustManager)
                        x509TrustManagers.add((X509TrustManager) tm);

            if (x509TrustManagers.size() == 0)
                throw new RuntimeException("Couldn't find any X509TrustManagers");

        }

        /*
         * Delegate to the default trust manager.
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            final X509TrustManager defaultX509TrustManager = x509TrustManagers.get(0);
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
