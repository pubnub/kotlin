package com.pubnub.api;

import static com.pubnub.api.PubnubError.PNERROBJ_READINPUT;
import static com.pubnub.api.PubnubError.getErrorObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import com.codename1.impl.CodenameOneImplementation;
import com.codename1.io.Util;

class HttpClientCore extends HttpClient {
    private int requestTimeout = 310000;
    private int connectionTimeout = 5000;
    //HttpURLConnection connection;
    protected static Logger log = new Logger(Worker.class);

    private void init() {
        //HttpURLConnection.setFollowRedirects(true);
    }

    public HttpClientCore(int connectionTimeout, int requestTimeout, Hashtable headers) {
        init();
        this.setRequestTimeout(requestTimeout);
        this.setConnectionTimeout(connectionTimeout);
        this._headers = headers;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public boolean isRedirect(int rc) {
       /* return (rc == HttpURLConnection.HTTP_MOVED_PERM
                || rc == HttpURLConnection.HTTP_MOVED_TEMP || rc == HttpURLConnection.HTTP_SEE_OTHER);
        */
        return true;
    }

    public boolean checkResponse(int rc) {
        return true; //return (rc == HttpURLConnection.HTTP_OK || isRedirect(rc));
    }

    public boolean checkResponseSuccess(int rc) {
        return true; //return (rc == HttpURLConnection.HTTP_OK);
    }
    public HttpResponse fetch(String url) throws PubnubException { //, SocketTimeoutException {
        return fetch(url, null);
    }

    public synchronized HttpResponse fetch(String url, Hashtable headers)
            throws PubnubException {     //    log.verbose("URL = " + url + " : RESPONSE = " + page);
        return null;//new HttpResponse(rc, page);
    }

    public boolean isOk(int rc) {
        //return (rc == HttpURLConnection.HTTP_OK);
        return true;
    }

    public void shutdown() {
        //if (connection != null) connection.disconnect();
    }
}

