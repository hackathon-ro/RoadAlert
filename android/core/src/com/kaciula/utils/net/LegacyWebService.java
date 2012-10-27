
package com.kaciula.utils.net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.kaciula.utils.misc.DebugUtils;
import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.MiscConstants;
import com.kaciula.utils.misc.NetUtils;
import com.kaciula.utils.misc.StreamUtils;

/**
 * LegacyWebService.java - Web service implementation used for Android versions
 * prior to Gingerbread
 * 
 * @author ka
 */
public class LegacyWebService implements IWebService {

    private static final String TAG = "WebService";

    private final HttpClient httpClient;

    public LegacyWebService() {
        httpClient = NetUtils.getHttpClient();
    }

    public LegacyWebService(boolean trustAll) {
        if (trustAll)
            httpClient = NetUtils.getTrustfulHttpsClient();
        else
            httpClient = NetUtils.getHttpClient();
    }

    public LegacyWebService(int certRawId, String storeType, String storePass, int type) {
        httpClient = NetUtils.getHttpsClient(certRawId, storeType, storePass, type);
    }

    @Override
    public String get(String uri) throws ServiceException {
        return get(uri, null, null);
    }

    @Override
    public String get(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException {
        HttpGet httpGet = null;
        String body = null;

        if (params != null && !params.isEmpty()) {
            int i = 0;
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (i == 0) {
                    uri += "?";
                } else {
                    uri += "&";
                }

                try {
                    uri += param.getKey() + "="
                            + URLEncoder.encode(param.getValue(), MiscConstants.ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }

                i++;
            }
        }

        try {
            LogUtils.d(TAG, "GET URI=" + uri);

            httpGet = new HttpGet(uri);

            if (headers != null) {
                Header[] properHeaders = new Header[headers.size()];
                int i = 0;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    properHeaders[i] = new BasicHeader(header.getKey(), header.getValue());
                    i++;
                }
                httpGet.setHeaders(properHeaders);
            }

            DebugUtils.logHeaders(httpGet);

            // response = new BasicResponseHandler();
            // body = httpClient.execute(httpGet, response);
            HttpResponse response = httpClient.execute(httpGet);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HttpStatus.SC_OK)
                throw new ServiceException(responseCode);

            body = StreamUtils.readString(response.getEntity().getContent(),
                    MiscConstants.ENCODING_UTF8);

            LogUtils.d(TAG, "GET RESPONSE=" + body);

            return body;
        } catch (Throwable t) {
            httpGet.abort();
            Log.e(TAG, "Exception in http get " + t.getMessage());
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

        HttpEntityEnclosingRequestBase httpRequest = null;
        String body = null;

        try {
            LogUtils.d(TAG, "POST/PUT URI=" + uri);

            if (POST.equals(method)) {
                httpRequest = new HttpPost(uri);
            } else {
                httpRequest = new HttpPut(uri);
            }

            if (headers != null) {
                Header[] properHeaders = new Header[headers.size()];
                int i = 0;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    properHeaders[i] = new BasicHeader(header.getKey(), header.getValue());
                    i++;
                }
                httpRequest.setHeaders(properHeaders);
            }

            if (params != null && !params.isEmpty()) {
                List<NameValuePair> properParams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : params.entrySet())
                    properParams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                httpRequest.setEntity(new UrlEncodedFormEntity(properParams));

                LogUtils.d(TAG, "POST/PUT BODY=" + EntityUtils.toString(httpRequest.getEntity()));
            } else if (outBody != null) {
                httpRequest.setEntity(new StringEntity(outBody));
                LogUtils.d(TAG, "POST/PUT BODY=" + EntityUtils.toString(httpRequest.getEntity()));
            } else if (file != null) {
                FileEntity fileEntity = new FileEntity(file,
                        MiscConstants.NET_HEADER_CONTENT_TYPE_BINARY);
                httpRequest.setEntity(fileEntity);
                LogUtils.d(TAG, "POST/PUT FILE " + file.getAbsolutePath());
            } else if (multipartParams != null && !multipartParams.isEmpty()) {
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                for (MultipartParam param : multipartParams) {
                    switch (param.type) {
                        case STRING:
                            entity.addPart(param.key, new StringBody(param.value));
                            break;
                        case FILE:
                            entity.addPart(param.key, new FileBody(new File(param.value)));
                            break;
                    }
                }

                httpRequest.setEntity(entity);
                LogUtils.d(TAG, "Sending multipart params...");
            }

            DebugUtils.logHeaders(httpRequest);

            // response = new BasicResponseHandler();
            // body = httpClient.execute(httpPost, response);

            HttpResponse response = httpClient.execute(httpRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            LogUtils.d(TAG, "responseCode = " + responseCode);

            if (responseCode != HttpStatus.SC_OK) {
                if (responseCode > 200 && responseCode < 300) {
                    // Do nothing. It's ok
                    return null;
                } else
                    throw new ServiceException(responseCode);
            }
            // --> Mugur - 28.06.2012 - this should be better; not dependent on
            // UTF8 encoding
            body = EntityUtils.toString(response.getEntity());
            // body = StreamUtils.readString(response.getEntity().getContent(),
            // MiscConstants.ENCODING_UTF8);
            // <--

            LogUtils.d(TAG, "POST RESPONSE=" + body);

            return body;
        } catch (HttpResponseException hre) {
            httpRequest.abort();
            hre.printStackTrace();
            Log.e(TAG, "Exception in http post (" + hre.getStatusCode() + ") " + hre.getMessage());
        } catch (Throwable t) {
            httpRequest.abort();
            t.printStackTrace();
            Log.e(TAG, "Exception in http post " + t.getMessage());
        }

        return null;
    }

    @Override
    public String delete(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException {
        HttpDelete httpDelete = null;
        String body = null;

        if (params != null && !params.isEmpty()) {
            int i = 0;
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (i == 0) {
                    uri += "?";
                } else {
                    uri += "&";
                }

                try {
                    uri += param.getKey() + "="
                            + URLEncoder.encode(param.getValue(), MiscConstants.ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }

                i++;
            }
        }

        try {
            LogUtils.d(TAG, "DELETE URI=" + uri);

            httpDelete = new HttpDelete(uri);

            if (headers != null) {
                Header[] properHeaders = new Header[headers.size()];
                int i = 0;
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    properHeaders[i] = new BasicHeader(header.getKey(), header.getValue());
                    i++;
                }
                httpDelete.setHeaders(properHeaders);
            }

            DebugUtils.logHeaders(httpDelete);

            HttpResponse response = httpClient.execute(httpDelete);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HttpStatus.SC_OK)
                throw new ServiceException(responseCode);

            body = StreamUtils.readString(response.getEntity().getContent(),
                    MiscConstants.ENCODING_UTF8);

            LogUtils.d(TAG, "DELETE RESPONSE=" + body);

            return body;
        } catch (Throwable t) {
            httpDelete.abort();
            Log.e(TAG, "Exception in http get " + t.getMessage());
        }

        return null;
    }
}
