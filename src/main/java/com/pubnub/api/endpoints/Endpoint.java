package com.pubnub.api.endpoints;


import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.consumer_facing.PNErrorData;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import com.pubnub.api.core.utils.Base64;
import okhttp3.OkHttpClient;
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

    protected Pubnub pubnub;

    public Endpoint(Pubnub pubnub) {
        this.pubnub = pubnub;
    }


    public final Output sync() throws PubnubException {
        this.validateParams();

        Call<Input> call = doWork(createBaseParams());
        Response<Input> serverResponse;
        Output response;

        try {
            serverResponse = call.execute();
        } catch (IOException e) {
            throw new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR);
        }

        if (!serverResponse.isSuccessful() || serverResponse.code() != 200) {
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
            call = doWork(createBaseParams());
        } catch (PubnubException e) {
            PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS, e.getMessage());
            PNErrorStatus pnErrorStatus = new PNErrorStatus();
            writeFieldsToResponse(null, pnErrorStatus, true, pubnubException);
            callback.onResponse(null, pnErrorStatus);
        }

        call.enqueue(new retrofit2.Callback<Input>() {

            @Override
            public void onResponse(final Call<Input> call, final Response<Input> response) {
                Output callbackResponse;
                PNErrorStatus pnErrorStatus = new PNErrorStatus();


                if (!response.isSuccessful() || response.code() != 200) {

                    String responseBodyText;

                    try {
                        responseBodyText = response.errorBody().string();
                    } catch (IOException e) {
                        responseBodyText = "N/A";
                    }

                    PubnubException ex = new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, responseBodyText, response.code());
                    writeFieldsToResponse(response, pnErrorStatus, true, ex);
                    callback.onResponse(null, pnErrorStatus);
                    return;
                }

                try {
                    callbackResponse = createResponse(response);
                } catch (PubnubException e) {
                    PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, e.getMessage(), response.code());
                    writeFieldsToResponse(response, pnErrorStatus, true, pubnubException);
                    callback.onResponse(null, pnErrorStatus);
                    return;
                }

                writeFieldsToResponse(response, pnErrorStatus, false, null);
                callback.onResponse(callbackResponse, pnErrorStatus);
            }

            @Override
            public void onFailure(final Call<Input> call, final Throwable throwable) {
                PNErrorStatus pnErrorStatus = new PNErrorStatus();
                PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, throwable.getMessage(), null);
                writeFieldsToResponse(null, pnErrorStatus, true, pubnubException);
                callback.onResponse(null, pnErrorStatus);

            }
        });

        return call;
    }

    private void writeFieldsToResponse(Response<Input> response, PNErrorStatus pnErrorStatus, boolean isError, Exception throwable) {

        if (response != null) {
            pnErrorStatus.setStatusCode(response.code());
        }

        pnErrorStatus.setOperation(getOperationType());
        pnErrorStatus.setError(isError);

        if (isError) {
            PNErrorData errorData = new PNErrorData(null, throwable);
            pnErrorStatus.setErrorData(errorData);
        }

    }

    protected String signSHA256(String key, String data) throws PubnubException {
        Mac sha256_HMAC;
        byte[] hmacData;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");

        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
           throw new PubnubException(PubnubError.PNERROBJ_CRYPTO_ERROR, e.getMessage(), null);
        }

        try {
            sha256_HMAC.init(secretKey);
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


    protected final Retrofit createRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(this.getRequestTimeout(), TimeUnit.SECONDS);
        httpClient.connectTimeout(this.getConnectTimeout(), TimeUnit.SECONDS);

        if (pubnub.getConfiguration().isVerboseLogging()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return new Retrofit.Builder()
                .baseUrl(pubnub.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    protected final Map<String, String> createBaseParams() {
        Map<String, String> params = new HashMap<>();

        params.put("pnsdk", "Java/" + this.pubnub.getVersion());

        return params;
    }

    protected abstract boolean validateParams();

    protected abstract Call<Input> doWork(Map<String, String> baseParams) throws PubnubException;
    protected abstract Output createResponse(Response<Input> input) throws PubnubException;

    // add hooks for timeout
    protected abstract int getConnectTimeout();
    protected abstract int getRequestTimeout();
    protected abstract PNOperationType getOperationType();

}
