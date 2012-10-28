
package com.kaciula.utils.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.MiscConstants;
import com.kaciula.utils.misc.NetUtils;
import com.kaciula.utils.misc.StreamUtils;

/**
 * GingerbreadWebService.java - Web service implementation used for Gingerbread
 * and above
 * 
 * @author ka
 */
public class GingerbreadWebService implements IWebService {

    public GingerbreadWebService() {
    }

    public GingerbreadWebService(int certRawId, String storeType, String storePass, int notUsed) {
        HttpsURLConnection.setDefaultSSLSocketFactory(NetUtils.getFactory(certRawId, storeType,
                storePass));

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    @Override
    public String get(String uri) throws ServiceException {
        return get(uri, null, null);
    }

    @Override
    public String get(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException {
        try {
            if (params != null && !params.isEmpty()) {
                boolean first = true;
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        sb.append("&");

                    sb.append(param.getKey() + "="
                            + URLEncoder.encode(param.getValue(), MiscConstants.ENCODING_UTF8));
                }

                uri += "?" + sb.toString();
            }

            LogUtils.d("GET URI=" + uri);

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                if (headers != null && !headers.isEmpty())
                    for (Map.Entry<String, String> header : headers.entrySet())
                        conn.setRequestProperty(header.getKey(), header.getValue());

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setConnectTimeout(NetUtils.CONNECTION_TIMEOUT * NetUtils.SECOND_IN_MILLIS);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    String body = StreamUtils.readString(in, MiscConstants.ENCODING_UTF8);
                    in.close();

                    LogUtils.d("GET RESPONSE=" + body);

                    return body;
                }
                else if (responseCode > 200 && responseCode < 300) {
                    // Do nothing. It's ok
                }
                else
                    throw new ServiceException(responseCode);
            } catch (IOException ioe) {
                LogUtils.printStackTrace(ioe);
            } finally {
                conn.disconnect();
            }
        } catch (MalformedURLException murle) {
            LogUtils.printStackTrace(murle);
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        }

        return null;
    }

    @Override
    public String post(String uri) throws ServiceException {
        return post(uri, null, null, null, null, null);
    }

    @Override
    public String post(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException {
        return post(uri, headers, params, null, null, null);
    }

    @Override
    public String post(String uri, Map<String, String> headers, List<MultipartParam> multipartParams)
            throws ServiceException {
        return post(uri, headers, null, null, null, multipartParams);
    }

    @Override
    public String post(String uri, Map<String, String> headers, Map<String, String> params,
            String outBody) throws ServiceException {
        return post(uri, headers, params, outBody, null, null);
    }

    @Override
    public String post(String uri, Map<String, String> headers, Map<String, String> params,
            File file) throws ServiceException {
        return post(uri, headers, params, null, file, null);
    }

    public String post(String uri, Map<String, String> headers, Map<String, String> params,
            String outBody, File file, List<MultipartParam> multipartParams)
            throws ServiceException {
        return executeRequest(uri, headers, params, outBody, file, multipartParams, POST);
    }

    @Override
    public String put(String uri) throws ServiceException {
        return put(uri, null, null, null, null, null);
    }

    @Override
    public String put(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException {
        return put(uri, headers, params, null, null, null);
    }

    @Override
    public String put(String uri, Map<String, String> headers, List<MultipartParam> multipartParams)
            throws ServiceException {
        return put(uri, headers, null, null, null, multipartParams);
    }

    @Override
    public String put(String uri, Map<String, String> headers, Map<String, String> params,
            String outBody) throws ServiceException {
        return put(uri, headers, params, outBody, null, null);
    }

    @Override
    public String put(String uri, Map<String, String> headers, Map<String, String> params, File file)
            throws ServiceException {
        return put(uri, headers, params, null, file, null);
    }

    public String put(String uri, Map<String, String> headers, Map<String, String> params,
            String outBody, File file, List<MultipartParam> multipartParams)
            throws ServiceException {
        return executeRequest(uri, headers, params, outBody, file, multipartParams, PUT);
    }

    /*
     * Only one of params, outBody, file or multipartParams is not null
     */
    private String executeRequest(String uri, Map<String, String> headers,
            Map<String, String> params, String outBody, File file,
            List<MultipartParam> multipartParams, String method) throws ServiceException {

        try {
            LogUtils.d("POST/PUT URI=" + uri);

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                if (headers != null && !headers.isEmpty())
                    for (Map.Entry<String, String> header : headers.entrySet())
                        conn.setRequestProperty(header.getKey(), header.getValue());

                conn.setRequestMethod(method);
                conn.setDoInput(true);
                conn.setConnectTimeout(NetUtils.CONNECTION_TIMEOUT * NetUtils.SECOND_IN_MILLIS);

                if (params != null && !params.isEmpty()) {
                    boolean first = true;
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, String> param : params.entrySet()) {
                        if (first)
                            first = false;
                        else
                            sb.append("&");

                        sb.append(param.getKey() + "="
                                + URLEncoder.encode(param.getValue(), MiscConstants.ENCODING_UTF8));
                    }
                    String postBody = sb.toString();

                    conn.setDoOutput(true);

                    OutputStream out = conn.getOutputStream();
                    out.write(postBody.getBytes(MiscConstants.ENCODING_UTF8));
                    out.close();

                    LogUtils.d("POST/PUT BODY=" + postBody);
                } else if (outBody != null) {
                    conn.setDoOutput(true);

                    OutputStream out = conn.getOutputStream();
                    out.write(outBody.getBytes(MiscConstants.ENCODING_UTF8));
                    out.close();

                    LogUtils.d("POST/PUT BODY=" + outBody);
                } else if (file != null) {
                    conn.setDoOutput(true);
                    FileInputStream fis = new FileInputStream(file);
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                    // create a buffer of maximum size
                    int bytesAvailable = fis.available();
                    int maxBufferSize = 8192;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];

                    // read file and write it into form...
                    int bytesRead = fis.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fis.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fis.read(buffer, 0, bufferSize);
                    }

                    LogUtils.d("POST/PUT FILE " + file.getAbsolutePath());
                } else if (multipartParams != null && !multipartParams.isEmpty()) {
                    // LATER: This part of code must be tested on various
                    // services to see if it works
                    // conn.setUseCaches(false);
                    conn.setRequestProperty(MiscConstants.NET_HEADER_CONNECTION,
                            MiscConstants.NET_HEADER_CONNECTION_KEEP_ALIVE);
                    conn.setRequestProperty(MiscConstants.NET_HEADER_CONTENT_TYPE,
                            MiscConstants.NET_HEADER_CONTENT_TYPE_MULTIPART + boundary);
                    conn.setDoOutput(true);

                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                    for (MultipartParam param : multipartParams) {
                        switch (param.type) {
                            case STRING:
                                dos.writeBytes(twoHyphens + boundary + lineEnd);
                                dos.writeBytes("Content-Disposition: form-data; name=\""
                                        + param.key + "\"");
                                dos.writeBytes(lineEnd);
                                dos.writeBytes(lineEnd);
                                dos.writeBytes(param.value);
                                dos.writeBytes(lineEnd);
                                break;
                            case FILE:
                                dos.writeBytes(twoHyphens + boundary + lineEnd);
                                dos.writeBytes("Content-Disposition: form-data; name=\""
                                        + param.key + "\"; filename=\""
                                        + param.value + "\"");
                                dos.writeBytes(lineEnd);
                                dos.writeBytes("Content-Type: application/octet-stream");
                                dos.writeBytes(lineEnd);
                                dos.writeBytes(lineEnd);

                                FileInputStream fis = new FileInputStream(new File(param.value));

                                // create a buffer of maximum size
                                int bytesAvailable = fis.available();
                                int maxBufferSize = 8192;
                                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                byte[] buffer = new byte[bufferSize];

                                // read file and write it into form...
                                int bytesRead = fis.read(buffer, 0, bufferSize);

                                while (bytesRead > 0) {
                                    dos.write(buffer, 0, bufferSize);
                                    bytesAvailable = fis.available();
                                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                    bytesRead = fis.read(buffer, 0, bufferSize);
                                }

                                dos.writeBytes(lineEnd);
                                break;
                        }
                    }

                    dos.writeBytes(twoHyphens + boundary + twoHyphens);
                    dos.flush();
                    dos.close();

                    LogUtils.d("Sending multipart params...");
                }

                int responseCode = conn.getResponseCode();
                LogUtils.d("POST RESPONSE CODE=" + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    String body = StreamUtils.readString(in, MiscConstants.ENCODING_UTF8);
                    in.close();

                    LogUtils.d("POST RESPONSE=" + body);

                    return body;
                } else if (responseCode > 200 && responseCode < 300) {
                    // Do nothing. It's ok
                }
                else
                    throw new ServiceException(responseCode);
            } catch (IOException ioe) {
                LogUtils.printStackTrace(ioe);
            } finally {
                conn.disconnect();
            }
        } catch (MalformedURLException murle) {
            LogUtils.printStackTrace(murle);
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        }

        return null;
    }

    private String lineEnd = "\r\n";

    private String twoHyphens = "--";

    private String boundary = "nvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnvnv";

    @Override
    public String delete(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException {
        try {
            if (params != null && !params.isEmpty()) {
                boolean first = true;
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        sb.append("&");

                    sb.append(param.getKey() + "="
                            + URLEncoder.encode(param.getValue(), MiscConstants.ENCODING_UTF8));
                }

                uri += "?" + sb.toString();
            }

            LogUtils.d("DELETE URI=" + uri);

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                if (headers != null && !headers.isEmpty())
                    for (Map.Entry<String, String> header : headers.entrySet())
                        conn.setRequestProperty(header.getKey(), header.getValue());

                conn.setRequestMethod("DELETE");
                conn.setDoInput(true);
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    String body = StreamUtils.readString(in, MiscConstants.ENCODING_UTF8);
                    in.close();

                    LogUtils.d("DELETE RESPONSE=" + body);

                    return body;
                } else
                    throw new ServiceException(responseCode);
            } catch (IOException ioe) {
                LogUtils.printStackTrace(ioe);
            } finally {
                conn.disconnect();
            }
        } catch (MalformedURLException murle) {
            LogUtils.printStackTrace(murle);
        } catch (IOException ioe) {
            LogUtils.printStackTrace(ioe);
        }

        return null;
    }
}
