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

    private static String readInput(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte bytes[] = new byte[1024];

        int n = in.read(bytes);

        while (n != -1) {
            out.write(bytes, 0, n);
            n = in.read(bytes);
        }

        return new String(out.toString());
    }

    public HttpResponse fetch(String url) throws PubnubException { //, SocketTimeoutException {
        return fetch(url, null);
    }

    public synchronized HttpResponse fetch(String url, Hashtable headers)
            throws PubnubException { //, SocketTimeoutException {
    	
    	CodenameOneImplementation impl  = Util.getImplementation();
    	/*
        URL urlobj = null;
        try {
            urlobj = new URL(url);
        } catch (MalformedURLException e3) {
            throw new PubnubException(getErrorObject(PNERROBJ_MALFORMED_URL,url));
        }
        try {
            connection = (HttpURLConnection) urlobj.openConnection();
        } catch (IOException e2) {
            throw new PubnubException(getErrorObject(PNERROBJ_URL_OPEN, url));
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e1) {
            throw new PubnubException(PNERROBJ_PROTOCOL_EXCEPTION);
        }
        */
        if (_headers != null) {
            Enumeration en = _headers.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String val = (String) _headers.get(key);
                //connection.addRequestProperty(key, val);
            }
        }
        if (headers != null) {
            Enumeration en = headers.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String val = (String) headers.get(key);
                //connection.addRequestProperty(key, val);
            }
        }
        //connection.setReadTimeout(requestTimeout);
        //connection.setConnectTimeout(connectionTimeout);

/*
        try {
            connection.connect();
        }
        catch (SocketTimeoutException  e) {
            throw e;
        }
        
        catch (IOException e) {
            throw new PubnubException(getErrorObject(PNERROBJ_CONNECT_EXCEPTION, url));
        }
        */

        int rc = 0 ;//HttpURLConnection.HTTP_CLIENT_TIMEOUT;
/*
        try {
            rc = connection.getResponseCode();
        } catch (IOException e) {
            throw new PubnubException(getErrorObject(PNERROBJ_HTTP_RC_ERROR, url));
        }
*/

        InputStream is = null;
        //String encoding = connection.getContentEncoding();

        /*
        if(encoding == null || !encoding.equals("gzip")) {
            try {
                is = connection.getInputStream();
            } catch (IOException e) {
                if (rc == HttpURLConnection.HTTP_OK)
                    throw new PubnubException(getErrorObject(PNERROBJ_GETINPUTSTREAM, 1, url));
                is = connection.getErrorStream();
            }

        } else {
            try {
                is = new GZIPInputStream(connection.getInputStream());
            } catch (IOException e) {
                if (rc == HttpURLConnection.HTTP_OK)
                    throw new PubnubException(getErrorObject(PNERROBJ_GETINPUTSTREAM, 2, url));
                is = connection.getErrorStream();
            }
        }
*/
        String page = null;
        try {
            page = readInput(is);
        } catch (IOException e) {
            throw new PubnubException(getErrorObject(PNERROBJ_READINPUT, url));
        }
/*
        switch (rc) {
        case HttpURLConnection.HTTP_FORBIDDEN:
            throw new PubnubException(getErrorObject(PNERROBJ_FORBIDDEN, page));
        case HttpURLConnection.HTTP_UNAUTHORIZED:
            throw new PubnubException(getErrorObject(PNERROBJ_UNAUTHORIZED, page));
        case HttpURLConnection.HTTP_BAD_REQUEST:
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
        case HttpURLConnection.HTTP_BAD_GATEWAY:
            throw new PubnubException(getErrorObject(PNERROBJ_BAD_GATEWAY, url));
        case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
            throw new PubnubException(getErrorObject(PNERROBJ_CLIENT_TIMEOUT, url));
        case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
            throw new PubnubException(getErrorObject(PNERROBJ_GATEWAY_TIMEOUT, url));
        case HttpURLConnection.HTTP_INTERNAL_ERROR:
            throw new PubnubException(getErrorObject(PNERROBJ_INTERNAL_ERROR, url));
        default:
            break;
        }
  */
        log.verbose("URL = " + url + " : RESPONSE = " + page);
        return new HttpResponse(rc, page);
    }

    public boolean isOk(int rc) {
        //return (rc == HttpURLConnection.HTTP_OK);
    	return true;
    }

    public void shutdown() {
        //if (connection != null) connection.disconnect();
    }
}

