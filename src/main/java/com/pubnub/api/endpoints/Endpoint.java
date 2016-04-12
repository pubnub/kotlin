package com.pubnub.api.endpoints;


import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.core.*;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import com.pubnub.api.core.utils.Base64;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class Endpoint<Input, Output> {

    public final Output sync() throws PubnubException {
        this.validateParams();

        Call<Input> call = doWork();
        Response<Input> serverResponse;
        Output response;

        try {
            serverResponse = call.execute();
        } catch (IOException e) {
            throw new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR);
        }

        if (!serverResponse.isSuccessful()) {
            String responseBodyText;

            try {
                responseBodyText = serverResponse.errorBody().string();
            } catch (IOException e) {
                responseBodyText = "N/A";
            }

            throw new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, responseBodyText, serverResponse.code());
        }

        response = createResponse(serverResponse);

        return response;
    }

    public final Call<Input> async(final PNCallback<Output> callback) {
        this.validateParams();

        Call<Input> call = null;
        try {
            call = doWork();
        } catch (PubnubException e) {
            PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS, e.getMessage());
            ErrorStatus errorStatus = new ErrorStatus();
            errorStatus.setThrowable(pubnubException);
            callback.onResponse(null, errorStatus);
        }

        call.enqueue(new retrofit2.Callback<Input>() {


            private ErrorStatus prepareError(final Throwable throwable, final Call<Input> call) {
                ErrorStatus errorStatus = new ErrorStatus();
                errorStatus.setExecutedCall(call);
                errorStatus.setThrowable(throwable);
                errorStatus.setCallbacks(this);
                return errorStatus;
            }

            @Override
            public void onResponse(final Call<Input> call, final Response<Input> response) {
                Output callbackResponse = null;
                PNErrorStatus pnErrorStatus = new PNErrorStatus();
                pnErrorStatus.setOperation(getOperationType());

                try {
                    callbackResponse = createResponse(response);
                } catch (PubnubException e) {
                    PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, e.getMessage(), response.code());
                    ErrorStatus errorStatus = prepareError(pubnubException, call);
                    callback.onResponse(null, errorStatus);
                }

                if (response.isSuccessful()) {
                    callback.onResponse(callbackResponse, null);
                } else {
                    PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, response.message(), response.code());
                    ErrorStatus errorStatus = prepareError(pubnubException, call);
                    callback.onResponse(null, errorStatus);
                }

            }

            @Override
            public void onFailure(final Call<Input> call, final Throwable throwable) {
                ErrorStatus errorStatus = prepareError(throwable, call);
                callback.onResponse(null, errorStatus);

            }
        });

        return call;
    }

    protected String signSHA256(String key, String data) throws PubnubException {
        Mac sha256_HMAC;
        byte[] hmacData;
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");

        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
           throw new PubnubException(PubnubError.PNERROBJ_CRYPTO_ERROR, e.getMessage(), null);
        }

        try {
            sha256_HMAC.init(secret_key);
        } catch (InvalidKeyException e) {
            throw new PubnubException(PubnubError.PNERROBJ_CRYPTO_ERROR, e.getMessage(), null);
        }

        try {
            hmacData = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new PubnubException(PubnubError.PNERROBJ_CRYPTO_ERROR, e.getMessage(), null);
        }

        return new String(Base64.encode(hmacData, 0)).replace('+', '-').replace('/', '_');
    }


    protected final Retrofit createRetrofit(Pubnub pubnub) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(this.getRequestTimeout(), TimeUnit.SECONDS);
        httpClient.connectTimeout(this.getConnectTimeout(), TimeUnit.SECONDS);

        // TODO: REMOVE MEEEE
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        // TODO: REMOVE MEEEE

        return new Retrofit.Builder()
                .baseUrl(pubnub.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    protected final Map<String, Object> createBaseParams() {
        Map<String, Object> params = new HashMap<String, Object>();

        // TODO: put PNSDK here

        return params;
    }

    protected abstract boolean validateParams();

    protected abstract Call<Input> doWork() throws PubnubException;
    protected abstract Output createResponse(Response<Input> input) throws PubnubException;

    // add hooks for timeout
    protected abstract int getConnectTimeout();
    protected abstract int getRequestTimeout();
    protected abstract PNOperationType getOperationType();

}
