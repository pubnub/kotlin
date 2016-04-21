package com.pubnub.api.endpoints;


import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubError;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.consumer_facing.PNErrorData;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
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
            throw PubnubException.builder().pubnubError(PubnubError.PNERROBJ_HTTP_ERROR).build();
        }

        if (!serverResponse.isSuccessful() || serverResponse.code() != 200) {
            String responseBodyText;

            try {
                responseBodyText = serverResponse.errorBody().string();
            } catch (IOException e) {
                responseBodyText = "N/A";
            }

            throw PubnubException.builder()
                    .pubnubError(PubnubError.PNERROBJ_HTTP_ERROR)
                    .errormsg(responseBodyText)
                    .statusCode(serverResponse.code())
                    .build();
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

            PubnubException pubnubException = PubnubException.builder()
                    .pubnubError(PubnubError.PNERROBJ_INVALID_ARGUMENTS)
                    .errormsg(e.getMessage())
                    .build();

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

                    PubnubException ex = PubnubException.builder()
                            .pubnubError(PubnubError.PNERROBJ_HTTP_ERROR)
                            .errormsg(responseBodyText)
                            .statusCode(response.code())
                            .build();

                    writeFieldsToResponse(response, pnErrorStatus, true, ex);
                    callback.onResponse(null, pnErrorStatus);
                    return;
                }

                try {
                    callbackResponse = createResponse(response);
                } catch (PubnubException e) {

                    PubnubException pubnubException = PubnubException.builder()
                            .pubnubError(PubnubError.PNERROBJ_HTTP_ERROR)
                            .errormsg(e.getMessage())
                            .statusCode(response.code())
                            .build();

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

                PubnubException pubnubException = PubnubException.builder()
                        .pubnubError(PubnubError.PNERROBJ_HTTP_ERROR)
                        .errormsg(throwable.getMessage())
                        .build();

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
        params.put("uuid", this.pubnub.getConfiguration().getUuid());

        // add the auth key for publish and subscribe.
        if (this.pubnub.getConfiguration().getAuthKey() != null) {
            if (getOperationType() == PNOperationType.PNPublishOperation
                    || getOperationType() == PNOperationType.PNSubscribeOperation) {
                params.put("auth", pubnub.getConfiguration().getAuthKey());
            }
        }


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
