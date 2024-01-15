package com.pubnub.api.endpoints.files;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.BuilderSteps.ChannelStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileIdStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.FilesBuilderSteps.FileNameStep;
import com.pubnub.api.endpoints.files.requiredparambuilder.ChannelFileNameFileIdBuilder;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.PNErrorData;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class GetFileUrl extends Endpoint<ResponseBody, PNFileUrlResult> {

    private final String channel;
    private final String fileId;
    private final String fileName;
    private PNCallback<PNFileUrlResult> cachedCallback;
    private final ExecutorService executorService;

    public GetFileUrl(String channel,
                      String fileName,
                      String fileId,
                      PubNub pubnubInstance,
                      TelemetryManager telemetry,
                      RetrofitManager retrofitInstance, TokenManager tokenManager) {
        super(pubnubInstance, telemetry, retrofitInstance, tokenManager);
        this.channel = channel;
        this.fileId = fileId;
        this.fileName = fileName;
        this.executorService = retrofitInstance.getTransactionClientExecutorService();
    }

    @Override
    protected List<String> getAffectedChannels() {
        return Collections.singletonList(channel);
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return Collections.emptyList();
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null
                || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }

        if (channel == null || channel.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
        }
    }

    @Override
    public PNFileUrlResult sync() throws PubNubException {
        try {
            Map<String, String> baseParams = createBaseParams();
            Call<ResponseBody> call = getRetrofit().getFilesService()
                    .downloadFile(getPubnub().getConfiguration().getSubscribeKey(),
                            channel,
                            fileId,
                            fileName,
                            baseParams);
            Request signedRequest = PubNubUtil.signRequest(call.request(),
                    getPubnub().getConfiguration(),
                    getPubnub().getTimestamp());
            return new PNFileUrlResult(signedRequest.url().toString());
        } catch (Exception e) {
            throw PubNubException.builder().cause(e).build();
        }
    }

    @Override
    public void async(@NotNull PNCallback<PNFileUrlResult> callback) {
        this.cachedCallback = callback;
        executorService.execute(() -> {
            try {
                PNFileUrlResult res = sync();
                callback.onResponse(res, PNStatus.builder().statusCode(HttpURLConnection.HTTP_OK).build());
            } catch (PubNubException ex) {
                callback.onResponse(null, PNStatus.builder()
                        .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                        .errorData(new PNErrorData(ex.getErrormsg(), ex))
                        .error(true)
                        .build());
            }
        });

    }

    @Override
    protected Call<ResponseBody> doWork(Map<String, String> baseParams) throws PubNubException {
        throw PubNubException.builder().build();
    }


    @Override
    protected PNFileUrlResult createResponse(Response<ResponseBody> input) throws PubNubException {
        throw PubNubException.builder().build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNFileAction;
    }

    @Override
    public void retry() {
        async(cachedCallback);
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    public static class Builder extends ChannelFileNameFileIdBuilder<GetFileUrl> {
        private Builder(ChannelStep<FileNameStep<FileIdStep<GetFileUrl>>> builder) {
            super(builder);
        }
    }

    public static Builder builder(PubNub pubNub,
                                  TelemetryManager telemetryManager,
                                  RetrofitManager retrofitManager,
                                  TokenManager tokenManager) {
        return new Builder(ChannelFileNameFileIdBuilder.create((channel, fileName, fileId) ->
                new GetFileUrl(channel, fileName, fileId, pubNub, telemetryManager, retrofitManager, tokenManager)));
    }
}
