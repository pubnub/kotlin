package com.pubnub.api;

import static com.pubnub.api.PubnubError.*;
import static com.pubnub.api.PubnubError.getErrorObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import com.codename1.impl.CodenameOneImplementation;
import com.codename1.io.*;
import org.json.*;

class HttpClientCore extends HttpClient {


    PubnubCn1Connection connection;
    private int requestTimeout = 310000;
    private int connectionTimeout = 5000;


    protected static Logger log = new Logger(Worker.class);

    private void init() {
        connection = new PubnubCn1Connection();
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

    public HttpResponse fetch(String url) throws PubnubException, IOException {
        return fetch(url, null);
    }

    public synchronized HttpResponse fetch(String url, Hashtable headers)
    throws PubnubException, IOException {
        IOException excp = null;
        PubnubCn1Response pcr = null;
        try {
            pcr = connection.fetch(url, requestTimeout, PubnubUtil.hashtableClone(headers,_headers));
        } catch (IOException ex) {
            excp = ex;
        }
        String page = pcr.getResponse();
        switch (pcr.getResponseStatusCode()) {
        case HttpUtil.HTTP_FORBIDDEN:
            throw new PubnubException(getErrorObject(PNERROBJ_FORBIDDEN, page));
        case HttpUtil.HTTP_UNAUTHORIZED:
            throw new PubnubException(getErrorObject(PNERROBJ_UNAUTHORIZED, page));
        case HttpUtil.HTTP_BAD_REQUEST:
            try {
                JSONArray jsarr = new JSONArray(page);
                String error = jsarr.get(1).toString();
                throw new PubnubException(getErrorObject(PNERROBJ_BAD_REQUEST, 1, error));
            } catch (JSONException e) {
                JSONObject jso;
                try {
                    jso = new JSONObject(page);
                    throw new PubnubException(getErrorObject(PNERROBJ_BAD_REQUEST, 2, jso.toString()));
                } catch (JSONException e1) {
                    throw new PubnubException(getErrorObject(PNERROBJ_INVALID_JSON, 2));
                }
            }
        case HttpUtil.HTTP_BAD_GATEWAY:
            throw new PubnubException(getErrorObject(PNERROBJ_BAD_GATEWAY, url));
        case HttpUtil.HTTP_CLIENT_TIMEOUT:
            throw new PubnubException(getErrorObject(PNERROBJ_CLIENT_TIMEOUT, url));
        case HttpUtil.HTTP_GATEWAY_TIMEOUT:
            throw new PubnubException(getErrorObject(PNERROBJ_GATEWAY_TIMEOUT, url));
        case HttpUtil.HTTP_INTERNAL_ERROR:
            throw new PubnubException(getErrorObject(PNERROBJ_INTERNAL_ERROR, url));
        default:
            if (excp != null) throw excp;
            break;
        }
        return new HttpResponse(pcr.getResponseStatusCode(), pcr.getResponse());
    }

    public void shutdown() {
        if (connection != null) connection.disconnect();
    }
}

