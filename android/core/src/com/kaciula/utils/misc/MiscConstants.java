
package com.kaciula.utils.misc;

/**
 * Common constants used in projects
 * 
 * @author ka
 */
public interface MiscConstants {

    String ENCODING_UTF8 = "UTF-8";

    String ENCODING_UTF16 = "UTF-16";

    String ENCODING_US_ASCII = "US-ASCII";

    String ENCODING_GZIP = "gzip";

    String CONTENT_SCHEME_TEL = "tel:";

    String CONTENT_SCHEME_SMS = "sms:";

    String EXTRA_SMS_BODY = "sms_body";

    String CONTENT_SCHEME_MAILTO = "mailto:";

    String CONTENT_SCHEME_RESOURCE = "android.resource://";

    String MIME_TYPE_EMAIL = "message/rfc822";

    String MIME_TYPE_TEXT = "text/*";

    String MIME_TYPE_PLAIN = "text/plain";

    String MIME_TYPE_HTML = "text/html";

    String MIME_TYPE_IMAGE = "image/*";

    String MIME_TYPE_PNG = "image/png";

    String MIME_TYPE_JPG = "image/jpg";

    String MIME_TYPE_APPLICATION = "application/*";

    String NET_SCHEME_GOOGLE_MAPS = "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f";

    String NET_SCHEME_HTTP = "http";

    String NET_SCHEME_HTTPS = "https";

    String NET_HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    String NET_HEADER_AUTHORIZATION = "Authorization";

    String NET_HEADER_USER_AGENT = "User-Agent";

    String NET_HEADER_HOST = "Host";

    String NET_HEADER_CONNECTION = "Connection";

    String NET_HEADER_CONNECTION_KEEP_ALIVE = "Keep-Alive";

    String NET_HEADER_CONTENT_TYPE = "Content-Type";

    String NET_HEADER_CONTENT_LENGTH = "Content-Length";

    String NET_HEADER_CONTENT_TYPE_JSON = "application/json; charset=utf-8";

    String NET_HEADER_CONTENT_TYPE_WWW = "application/x-www-form-urlencoded; charset=utf-8";

    String NET_HEADER_CONTENT_TYPE_BINARY = "binary/octet-stream";

    String NET_HEADER_CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary=";

    String NET_HEADER_ACCEPT = "Accept";

    String NET_TYPE_APPLICATION_JSON = "application/json";

    String REGEX_EMAIL = "^([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)$";

    String REGEX_ALL_DIGITS = "\\d+";

    String REGEX_ALL_DIGITS_WITH_DOT = "\\d+.\\d+";

    String REGEX_AT_LEAST_ONE_NUMBER = ".*[0-9]+.*";

    String REGEX_NOT_PRINTABLE_ASCII = "[^\\x00-\\x7f]";

    String DECIMAL_FORMAT_2_DIGITS = "0.00";

    String ACCOUNT_TYPE_GOOGLE = "com.google";

    int DISPLAY_HEIGHT_PHONE_SMALL = 480;

    int DISPLAY_HEIGHT_PHONE_NORMAL = 800;

    int DISPLAY_HEIGHT_PHONE_LARGE = 854;

    boolean SUPPORTS_FROYO = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO;

    boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;

    boolean SUPPORTS_HONEYCOMB = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;

    boolean SUPPORTS_HONEYCOMB_MR1 = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1;
}
