package com.pubnub.internal.vendor;

import com.pubnub.internal.PubNubImpl;
import com.pubnub.internal.PubNubUtil;
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
import okio.Timeout;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppEngineFactory implements Call {
    private Request request;
    private PubNubImpl pubNub;

    AppEngineFactory(Request request, PubNubImpl pubNub) {
        this.request = request;
        this.pubNub = pubNub;
    }

    @NotNull
    @Override
    public Request request() {
        return request;
    }

    @NotNull
    @Override
    public Response execute() throws IOException {
        request = PubNubUtil.INSTANCE.signRequest(request, pubNub.getConfiguration(), pubNub.timestamp$pubnub_kotlin_core());

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
                        String contentLengthField = connection.getHeaderField("content-length");
                        long contentLength;
                        try {
                            contentLength = Long.parseLong(contentLengthField);
                        } catch (NumberFormatException ignored) {
                            contentLength = -1;
                        }
                        return contentLength;
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

    @NotNull
    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }

    @NotNull
    @Override
    public Call clone() {
        try {
            return (Call) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static class Factory implements Call.Factory {
        private final PubNubImpl pubNub;

        public Factory(PubNubImpl pubNub) {
            this.pubNub = pubNub;
        }

        @NotNull
        @Override
        public Call newCall(Request request) {
            return new AppEngineFactory(request, pubNub);
        }
    }
}
