package com.codename1.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import com.codename1.impl.CodenameOneImplementation;
import com.pubnub.api.HttpUtil;
import java.io.UnsupportedEncodingException;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class PubnubCn1Connection {

    private CodenameOneImplementation impl = Util.getImplementation();
    private Object connection = null;
    private InputStream input = null;

    private byte[] data = null;

    public PubnubCn1Response fetch(String url, int timeout, Hashtable _headers) throws UnsupportedEncodingException, IOException {
        int rc = 0;
        String page = null;
        int contentLength = 0;

        while (HttpUtil.isRedirect(rc) || rc == 0) {
            try {
                connection = impl.connect(url, true, false, timeout);
            } catch (IOException ex) {
                throw ex;
            }
            impl.setPostRequest(connection, false);

            if (_headers != null) {
                Enumeration e = _headers.keys();
                while (e.hasMoreElements()) {
                    String k = (String) e.nextElement();
                    String value = (String) _headers.get(k);
                    impl.setHeader(connection, k, value);
                }
            }
            try {
                rc = impl.getResponseCode(connection);
            } catch (IOException ex) {
                throw ex;
            }
            if (HttpUtil.isRedirect(rc)) {
                String uri;
                try {
                    uri = impl.getHeaderField("location", connection);
                } catch (IOException ex) {
                    throw ex;
                }
                if (!(uri.startsWith("http://") || uri.startsWith("https://"))) {
                    url = Util.relativeToAbsolute(url, uri);
                } else {
                    url = uri;
                }
            }
        }
        contentLength = impl.getContentLength(connection);
        try {
            input = impl.openInputStream(connection);
            data = Util.readInputStream(input);
            input.close();
        } catch (IOException ex) {
            return new PubnubCn1Response(rc, ex.getMessage() );
        }

        input = null;

        return new PubnubCn1Response(rc, new String(data, "UTF-8"));
    }

    public void disconnect() {
    }
}
