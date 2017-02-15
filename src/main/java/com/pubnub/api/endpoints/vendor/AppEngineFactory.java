package com.pubnub.api.endpoints.vendor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class AppEngineFactory implements Call {
    private Request request;

    AppEngineFactory(Request request) {
        this.request = request;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response execute() throws IOException {
        URL url = request.url().url();
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod(request.method());

        Headers headers = request.headers();
        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                String name = headers.name(i);
                connection.setRequestProperty(name, headers.get(name));
            }
        }

        if (request.body() != null) {
            BufferedSink outbuf;
            outbuf = Okio.buffer(Okio.sink(connection.getOutputStream()));
            request.body().writeTo(outbuf);
            outbuf.close();
        }

        connection.connect();

        final BufferedSource source = Okio.buffer(Okio.source(connection.getInputStream()));
        if (connection.getResponseCode() != 200) {
            throw new IOException("Fail to call " + " :: " + source.readUtf8());
        }
        Response response = new Response.Builder()
                .code(connection.getResponseCode())
                .message(connection.getResponseMessage())
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse(connection.getContentType());
                    }

                    @Override
                    public long contentLength() {
                        return connection.getContentLengthLong();
                    }

                    @Override
                    public BufferedSource source() {
                        return source;
                    }
                })
                .build();
        return response;
    }

    @Override
    public void enqueue(Callback responseCallback) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call clone() {
        try {
            return (Call) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static class Factory implements Call.Factory {
        @Override
        public Call newCall(Request request) {
            return new AppEngineFactory(request);
        }
    }
}
