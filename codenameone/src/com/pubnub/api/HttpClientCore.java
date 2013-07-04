package com.pubnub.api;

import static com.pubnub.api.PubnubError.PNERROBJ_READINPUT;
import static com.pubnub.api.PubnubError.getErrorObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import com.codename1.impl.CodenameOneImplementation;
import com.codename1.io.*;

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
        System.out.println(url);
        PubnubCn1Response pcr = connection.fetch(url, requestTimeout, headers);
    	return new HttpResponse(pcr.getResponseStatusCode(), pcr.getResponse());
    }

    public void shutdown() {
        if (connection != null) connection.disconnect();
    }
}

