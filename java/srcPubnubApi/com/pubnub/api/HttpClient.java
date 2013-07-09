package com.pubnub.api;

import java.io.IOException;
import java.util.Hashtable;
import com.pubnub.api.PubnubException;

abstract class HttpClient {

    protected Hashtable _headers;

    public static HttpClient getClient(int connectionTimeout, int requestTimeout, Hashtable headers) {
        return new HttpClientCore(connectionTimeout, requestTimeout, headers);
    }

    public void reset() {
        shutdown();
    }

    public abstract int getRequestTimeout();

    public abstract void setRequestTimeout(int requestTimeout);

    public abstract int getConnectionTimeout();

    public abstract void setConnectionTimeout(int connectionTimeout);

    public abstract void shutdown();

    public abstract HttpResponse fetch(String url) throws IOException,
                PubnubException;

    public abstract HttpResponse fetch(String url, Hashtable headers)
    throws IOException, PubnubException;
}
