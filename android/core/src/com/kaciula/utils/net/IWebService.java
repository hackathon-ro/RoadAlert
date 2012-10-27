
package com.kaciula.utils.net;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * IWebService.java - Interface used to call web services. Android versions
 * prior to Gingerbread use HttpClient and newer versions use the recommended
 * HttpUrlConnection
 * 
 * @author ka
 */
public interface IWebService {
	
	public static final String POST = "POST";
	public static final String PUT = "PUT";

    public String get(String uri) throws ServiceException;

    public String get(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException;

    public String post(String uri) throws ServiceException;

    public String post(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException;

    public String post(String uri, Map<String, String> headers, List<MultipartParam> multipartParams)
            throws ServiceException;

    public String post(String uri, Map<String, String> headers, Map<String, String> params,
            String outBody) throws ServiceException;

    public String post(String uri, Map<String, String> headers, Map<String, String> params,
            File file) throws ServiceException;

    public String put(String uri) throws ServiceException;

    public String put(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException;

    public String put(String uri, Map<String, String> headers, List<MultipartParam> multipartParams)
            throws ServiceException;

    public String put(String uri, Map<String, String> headers, Map<String, String> params,
            String outBody) throws ServiceException;

    public String put(String uri, Map<String, String> headers, Map<String, String> params,
            File file) throws ServiceException;
    
    public String delete(String uri, Map<String, String> headers, Map<String, String> params)
            throws ServiceException;
}
