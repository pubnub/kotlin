package com.pubnub.internal.vendor;

import com.pubnub.api.v2.PNConfiguration;
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
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.functions.Function0;
import kotlin.reflect.KClass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppEngineFactory implements Call {
    private Request request;
    private final PNConfiguration configuration;

    AppEngineFactory(Request request, PNConfiguration configuration) {
        this.request = request;
        this.configuration = configuration;
    }

    @NotNull
    @Override
    public Request request() {
        return request;
    }

    @NotNull
    @Override
    public Response execute() throws IOException {
        request = PubNubUtil.INSTANCE.signRequest(request, configuration, PubNubImpl.timestamp());

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

    @Nullable
    @Override
    public <T> T tag(@NotNull KClass<T> type) {
        return null;
    }

    @Nullable
    @Override
    public <T> T tag(@NotNull Class<? extends T> type) {
        return null;
    }

    @Nullable
    @Override
    public <T> T tag(@NotNull KClass<T> type, @NotNull Function0<? extends T> initializer) {
        return null;
    }

    @Nullable
    @Override
    public <T> T tag(@NotNull Class<T> type, @NotNull Function0<? extends T> initializer) {
        return null;
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
        private final PNConfiguration configuration;

        public Factory(PNConfiguration configuration) {
            this.configuration = configuration;
        }

        @NotNull
        @Override
        public Call newCall(Request request) {
            return new AppEngineFactory(request, configuration);
        }
    }
}
