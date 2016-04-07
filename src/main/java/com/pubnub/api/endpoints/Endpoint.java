package com.pubnub.api.endpoints;


import com.pubnub.api.core.*;
import com.pubnub.api.core.utils.Base64;
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

public abstract class Endpoint<Input, Output> {

    public Output sync() throws PubnubException {
        this.validateParams();

        PnResponse<Output> response;
        Call<Input> call = doWork();
        try {
            response = createResponse(call.execute());
        } catch (IOException e) {
            throw new PubnubException(e.getMessage());
        }

        if (!response.isSuccessful()) {
            throw new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, response.getErrorBody(), response.getStatusCode());
        }

        return response.getPayload();
    }

    public final void async(final PnCallback<Output> callback) {
        this.validateParams();

        Call<Input> call = null;
        try {
            call = doWork();
        } catch (PubnubException e) {
            PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_INVALID_ARGUMENTS, e.getMessage());
            ErrorStatus errorStatus = new ErrorStatus();
            errorStatus.setThrowable(pubnubException);
            callback.status(errorStatus);
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
                PnResponse<Output> callbackResponse = null;
                try {
                    callbackResponse = createResponse(response);
                } catch (PubnubException e) {
                    PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, e.getMessage(), response.code());
                    ErrorStatus errorStatus = prepareError(pubnubException, call);
                    callback.status(errorStatus);
                }

                if (callbackResponse.isSuccessful()) {
                    callback.result(callbackResponse.getPayload());
                } else {
                    PubnubException pubnubException = new PubnubException(PubnubError.PNERROBJ_HTTP_ERROR, response.message(), response.code());
                    ErrorStatus errorStatus = prepareError(pubnubException, call);
                    callback.status(errorStatus);
                }

            }

            @Override
            public void onFailure(final Call<Input> call, final Throwable throwable) {
                ErrorStatus errorStatus = prepareError(throwable, call);
                callback.status(errorStatus);

            }
        });
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
        return new Retrofit.Builder()
                .baseUrl(pubnub.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    protected final Map<String, Object> createBaseParams() {
        Map<String, Object> params = new HashMap<String, Object>();

        // TODO: put PNSDK here

        return params;
    }

    protected abstract boolean validateParams();

    protected abstract Call<Input> doWork() throws PubnubException;
    protected abstract PnResponse<Output> createResponse(Response<Input> input) throws PubnubException;

}
