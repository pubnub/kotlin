package com.pubnub.api;

public class HttpUtil {
    public static final int HTTP_MOVED_PERM   = 301;
    public static final int HTTP_MOVED_TEMP   = 302;
    public static final int HTTP_SEE_OTHER    = 303;
    public static final int HTTP_OK           = 200;
    public static final int HTTP_FORBIDDEN    = 403;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_BAD_REQUEST  = 400;
    public static final int HTTP_BAD_GATEWAY  = 502;
    public static final int HTTP_CLIENT_TIMEOUT = 408;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;
    public static final int HTTP_INTERNAL_ERROR = 500;

    public static boolean isRedirect(int rc) {
        return (rc == HttpUtil.HTTP_MOVED_PERM
                || rc == HttpUtil.HTTP_MOVED_TEMP || rc == HttpUtil.HTTP_SEE_OTHER);
    }
    public static boolean isOk(int rc) {
        return (rc == HttpUtil.HTTP_OK);
    }

    public static boolean checkResponse(int rc) {
        return (rc == HttpUtil.HTTP_OK || isRedirect(rc));
    }

    public static boolean checkResponseSuccess(int rc) {
        return (rc == HttpUtil.HTTP_OK);
    }
}
