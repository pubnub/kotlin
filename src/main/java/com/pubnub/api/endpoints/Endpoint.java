package com.pubnub.api.endpoints;


import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNErrorData;
import com.pubnub.api.models.consumer.PNStatus;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class Endpoint<Input, Output> {

    protected PubNub pubnub;

    public Endpoint(PubNub pubnub) {
        this.pubnub = pubnub;
    }


    public final Output sync() throws PubNubException {
        this.validateParams();

        Call<Input> call = doWork(createBaseParams());
        Response<Input> serverResponse;
        Output response;

        try {
            serverResponse = call.execute();
        } catch (IOException e) {
            throw PubNubException.builder()
                    .pubnubError(PubNubError.PNERROBJ_PARSING_ERROR)
                    .errormsg(e.toString())
                    .build();
        }

        if (!serverResponse.isSuccessful() || serverResponse.code() != 200) {
            String responseBodyText;

            try {
                responseBodyText = serverResponse.errorBody().string();
            } catch (IOException e) {
                responseBodyText = "N/A";
            }

            throw PubNubException.builder()
                    .pubnubError(PubNubError.PNERROBJ_HTTP_ERROR)
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
        } catch (PubNubException e) {

            PubNubException pubnubException = PubNubException.builder()
                    .pubnubError(PubNubError.PNERROBJ_HTTP_ERROR)
                    .errormsg(e.getMessage())
                    .build();

            callback.onResponse(null, createStatusResponse(PNStatusCategory.PNBadRequestCategory, null, pubnubException));
        }

        call.enqueue(new retrofit2.Callback<Input>() {

            @Override
            public void onResponse(final Call<Input> call, final Response<Input> response) {
                Output callbackResponse;
                PNStatus pnErrorStatus = PNStatus.builder().build();


                if (!response.isSuccessful() || response.code() != 200) {

                    String responseBodyText;

                    try {
                        responseBodyText = response.errorBody().string();
                    } catch (IOException e) {
                        responseBodyText = "N/A";
                    }

                    PubNubException ex = PubNubException.builder()
                            .pubnubError(PubNubError.PNERROBJ_HTTP_ERROR)
                            .errormsg(responseBodyText)
                            .statusCode(response.code())
                            .build();

                    callback.onResponse(null, createStatusResponse(PNStatusCategory.PNBadRequestCategory, response, ex));
                    return;
                }

                try {
                    callbackResponse = createResponse(response);
                } catch (PubNubException e) {

                    PubNubException pubnubException = PubNubException.builder()
                            .pubnubError(PubNubError.PNERROBJ_HTTP_ERROR)
                            .errormsg(e.getMessage())
                            .statusCode(response.code())
                            .build();

                    callback.onResponse(null, createStatusResponse(PNStatusCategory.PNBadRequestCategory, response, pubnubException));
                    return;
                }

                callback.onResponse(callbackResponse, createStatusResponse(null, response, null));
            }

            @Override
            public void onFailure(final Call<Input> call, final Throwable throwable) {
                PNStatus pnErrorStatus = PNStatus.builder().build();

                PubNubException pubnubException = PubNubException.builder()
                        .pubnubError(PubNubError.PNERROBJ_HTTP_ERROR)
                        .errormsg(throwable.getMessage())
                        .build();

                callback.onResponse(null, createStatusResponse(PNStatusCategory.PNBadRequestCategory, null, pubnubException));

            }
        });

        return call;
    }

    private PNStatus createStatusResponse(PNStatusCategory category, Response<Input> response, Exception throwable) {
        PNStatus.PNStatusBuilder pnStatus = PNStatus.builder();

        if (response == null || throwable != null) {
            pnStatus.error(true);
        }

        if (throwable != null) {
            PNErrorData pnErrorData = new PNErrorData(throwable.getMessage(), throwable);
            pnStatus.errorData(pnErrorData);
        }

        if (response != null) {
            pnStatus.statusCode(response.code());
            pnStatus.TLSEnabled(response.raw().request().url().isHttps());
            pnStatus.origin(response.raw().request().url().host());
            pnStatus.uuid(response.raw().request().url().queryParameter("uuid"));
            pnStatus.authKey(response.raw().request().url().queryParameter("auth"));
            pnStatus.clientRequest(response.raw().request());
        }


        pnStatus.operation(getOperationType());
        pnStatus.category(category);
        pnStatus.affectedChannels(getAffectedChannels());
        pnStatus.affectedChannelGroups(getAffectedChannelGroups());

        return pnStatus.build();
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
                    || getOperationType() == PNOperationType.PNSubscribeOperation
                    || getOperationType() == PNOperationType.PNHistoryOperation) {
                params.put("auth", pubnub.getConfiguration().getAuthKey());
            }
        }


        return params;
    }

    protected abstract boolean validateParams();

    protected abstract Call<Input> doWork(Map<String, String> baseParams) throws PubNubException;
    protected abstract Output createResponse(Response<Input> input) throws PubNubException;

    // add hooks for timeout
    protected abstract int getConnectTimeout();
    protected abstract int getRequestTimeout();
    protected abstract PNOperationType getOperationType();

    protected List<String> getAffectedChannels() {
        return null;
    }

    protected List<String> getAffectedChannelGroups() {
        return null;
    }


}
