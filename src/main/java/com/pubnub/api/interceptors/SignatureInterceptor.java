package com.pubnub.api.interceptors;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import lombok.extern.java.Log;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Log
public class SignatureInterceptor implements Interceptor {

    private PubNub pubNub;

    public SignatureInterceptor(PubNub pubNubInstance) {
        this.pubNub = pubNubInstance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // only sign if we have a secret key in place.
        if (this.pubNub.getConfiguration().getSecretKey() == null) {
            return chain.proceed(originalRequest);
        }

        HttpUrl url = chain.request().url();
        String requestURL = url.encodedPath();
        int timestamp = this.pubNub.getTimestamp();
        Map<String, String> queryParams = new HashMap<>();
        String signature = "";

        for (String queryKey: url.queryParameterNames()) {
            queryParams.put(queryKey, url.queryParameter(queryKey));
        }

        queryParams.put("timestamp", String.valueOf(timestamp));

        String signInput = pubNub.getConfiguration().getSubscribeKey() + "\n"
                + pubNub.getConfiguration().getPublishKey() + "\n";

        if (requestURL.startsWith("/v1/auth/audit")) {
            signInput += "audit" + "\n";
        } else if (requestURL.startsWith("/v1/auth/grant")) {
            signInput += "grant" + "\n";
        } else {
            signInput += requestURL + "\n";
        }

        signInput += PubNubUtil.preparePamArguments(queryParams);

        try {
            signature = PubNubUtil.signSHA256(pubNub.getConfiguration().getSecretKey(), signInput);
        } catch (PubNubException e) {
            log.warning("signature failed on SignatureInterceptor: " + e.toString());
        }

        HttpUrl rebuiltUrl = url.newBuilder()
                .addQueryParameter("timestamp", String.valueOf(timestamp))
                .addQueryParameter("signature", signature)
                .build();
        Request request = chain.request().newBuilder().url(rebuiltUrl).build();
        return chain.proceed(request);
    }
}
