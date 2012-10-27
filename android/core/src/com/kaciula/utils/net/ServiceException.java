
package com.kaciula.utils.net;

public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public int httpResponseCode;

    public int errorType;

    public static final int ERROR_HTTP = 1;

    public static final int ERROR_OTHER = 2;

    public ServiceException() {
        errorType = ERROR_OTHER;
    }

    public ServiceException(int httpResponseCode) {
        errorType = ERROR_HTTP;
        this.httpResponseCode = httpResponseCode;
    }
}
