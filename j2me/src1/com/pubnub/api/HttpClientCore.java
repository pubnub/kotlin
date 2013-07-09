package com.pubnub.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.tinyline.util.GZIPInputStream;

import org.json.me.*;

public class HttpClientCore extends HttpClient {
    private int requestTimeout = 310000;
    private int connectionTimeout = 5000;
    private HttpConnection  hc;

    private void init() {

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

    private String readResponse(HttpConnection hconn) {
        InputStream in = null;
        String prefix = "";
        try {
            StringBuffer b = new StringBuffer();
            int ch;
            b.append(prefix);
            in = hconn.openInputStream();
            if ("gzip".equals(hconn.getEncoding()))
                in= new GZIPInputStream(in);

            byte[] data = null;
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();

            while ((ch = in.read()) != -1) {
                tmp.write(ch);
            }
            data = tmp.toByteArray();
            tmp.close();
            b.append(new String(data, "UTF-8"));

            if (b.length() > 0) {
                return b.toString();
            } else
                return null;

        } catch (IOException ioe) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public boolean isRedirect(int rc) {
        return (rc == HttpConnection.HTTP_MOVED_PERM
                || rc == HttpConnection.HTTP_MOVED_TEMP
                || rc == HttpConnection.HTTP_SEE_OTHER || rc == HttpConnection.HTTP_TEMP_REDIRECT);
    }

    public boolean isOk(int rc) {
        return (rc == HttpConnection.HTTP_OK);
    }

    public boolean checkResponse(int rc) {

        return (rc == HttpConnection.HTTP_OK || isRedirect(rc));
    }

    public HttpResponse fetch(String url) throws PubnubException, IOException {
        return fetch(url, null);
    }

    public HttpResponse fetch(String url, Hashtable headers) throws PubnubException, IOException {
        if (url == null)
            throw new IOException("Invalid Url");

        int follow = 5;
        int rc = 0;
        hc = null;
        String response = null;

        while (follow-- > 0) {

            hc = (HttpConnection) Connector.open(url, Connector.READ_WRITE,
                                                 true);
            hc.setRequestMethod(HttpConnection.GET);
            if (_headers != null) {
                Enumeration en = _headers.keys();
                while (en.hasMoreElements()) {
                    String key = (String) en.nextElement();
                    String val = (String) _headers.get(key);
                    hc.setRequestProperty(key, val);
                }
            }
            if (headers != null) {
                Enumeration en = headers.keys();
                while (en.hasMoreElements()) {
                    String key = (String) en.nextElement();
                    String val = (String) headers.get(key);
                    hc.setRequestProperty(key, val);
                }
            }

            rc = hc.getResponseCode();



            if (!checkResponse(rc)) {
                break;
            } else if (!isRedirect(rc)) {
                break;
            }

            url = hc.getHeaderField("Location");

            if (url == null) {
                throw new IOException("No Location header");
            }

            if (url.startsWith("/")) {
                StringBuffer b = new StringBuffer();
                b.append("http://");
                b.append(hc.getHost());
                b.append(':');
                b.append(hc.getPort());
                b.append(url);
                url = b.toString();
            } else if (url.startsWith("ttp:")) {
                url = "h" + url;
            }
            hc.close();
        }

        if (follow == 0) {
            throw new IOException("Too many redirects");
        }

        response = readResponse(hc);
        switch (rc) {
        case HttpUtil.HTTP_FORBIDDEN:
            throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_FORBIDDEN, response));
        case HttpUtil.HTTP_UNAUTHORIZED:
            throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_UNAUTHORIZED, response));
        case HttpUtil.HTTP_BAD_REQUEST:
            try {
                JSONArray jsarr = new JSONArray(response);
                String error = jsarr.get(1).toString();
                throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_BAD_REQUEST, 1, error));
            } catch (JSONException e) {
                JSONObject jso;
                try {
                    jso = new JSONObject(response);
                    throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_BAD_REQUEST, 2, jso.toString()));
                } catch (JSONException e1) {
                    throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_INVALID_JSON, 2));
                }
            }
        case HttpUtil.HTTP_BAD_GATEWAY:
            throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_BAD_GATEWAY, url));
        case HttpUtil.HTTP_CLIENT_TIMEOUT:
            throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_CLIENT_TIMEOUT, url));
        case HttpUtil.HTTP_GATEWAY_TIMEOUT:
            throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_GATEWAY_TIMEOUT, url));
        case HttpUtil.HTTP_INTERNAL_ERROR:
            throw new PubnubException(PubnubError.getErrorObject(PubnubError.PNERROBJ_INTERNAL_ERROR, url));
        default:
            break;
        }

        hc.close();
        return new HttpResponse(rc, response);
    }

    public int getConnectionTimeout() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        // TODO Auto-generated method stub

    }

    public void abortCurrentRequest() {
        // TODO Auto-generated method stub

    }

    public boolean checkResponseSuccess(int rc) {
        return (rc == HttpConnection.HTTP_OK);
    }

    public void shutdown() {
        try {
            if (hc != null) {
                hc.close();
            }

        } catch (IOException e) {

        }
    }
}
