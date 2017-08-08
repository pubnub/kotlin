package com.pubnub.api.interceptors;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


public class SignatureInterceptor implements Interceptor {

    private PubNub pubNub;

    public SignatureInterceptor(PubNub pubNubInstance) {
        this.pubNub = pubNubInstance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = PubNubUtil.requestSigner(originalRequest, pubNub.getConfiguration(), pubNub.getTimestamp());
        return chain.proceed(request);
    }
}
