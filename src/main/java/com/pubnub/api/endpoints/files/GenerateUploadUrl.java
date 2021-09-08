package com.pubnub.api.endpoints.files;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.remoteaction.RemoteAction;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.server.files.FileUploadRequestDetails;
import com.pubnub.api.models.server.files.FormField;
import com.pubnub.api.models.server.files.GenerateUploadUrlPayload;
import com.pubnub.api.models.server.files.GeneratedUploadUrlResponse;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class GenerateUploadUrl extends Endpoint<GeneratedUploadUrlResponse, FileUploadRequestDetails> {
    private final String channel;
    private final String fileName;

    GenerateUploadUrl(String channel,
                      String fileName,
                      PubNub pubNub,
                      TelemetryManager telemetryManager,
                      RetrofitManager retrofitManager, TokenManager tokenManager) {
        super(pubNub, telemetryManager, retrofitManager, tokenManager);
        this.channel = channel;
        this.fileName = fileName;
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
    protected FileUploadRequestDetails createResponse(Response<GeneratedUploadUrlResponse> input) throws
            PubNubException {
        if (input != null && input.body() != null) {
            FormField keyFormField = getKeyFormField(input.body());

            GeneratedUploadUrlResponse response = input.body();
            return new FileUploadRequestDetails(
                    response.getStatus(),
                    response.getData(),
                    response.getFileUploadRequest().getUrl(),
                    response.getFileUploadRequest().getMethod(),
                    response.getFileUploadRequest().getExpirationDate(),
                    keyFormField,
                    response.getFileUploadRequest().getFormFields()
            );
        } else {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_INTERNAL_ERROR)
                    .build();
        }
    }

    private FormField getKeyFormField(GeneratedUploadUrlResponse response) throws PubNubException {
        List<FormField> formFields = response.getFileUploadRequest().getFormFields();
        FormField found = null;
        for (FormField formField : formFields) {
            if (formField.getKey().equals("key")) {
                found = formField;
            }
        }
        if (found == null) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_INTERNAL_ERROR)
                    .errormsg("GenerateUploadUrl response do not contain \"key\" form param")
                    .build();
        }
        return found;
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNFileAction;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    protected Call<GeneratedUploadUrlResponse> doWork(Map<String, String> baseParams) {
        return getRetrofit().getFilesService().generateUploadUrl(getPubnub().getConfiguration().getSubscribeKey(),
                channel,
                new GenerateUploadUrlPayload(fileName),
                baseParams);
    }

    static class Factory {
        private final PubNub pubNub;
        private final TelemetryManager telemetryManager;
        private final RetrofitManager retrofitManager;
        private final TokenManager tokenManager;

        Factory(PubNub pubNub, TelemetryManager telemetryManager, RetrofitManager retrofitManager, TokenManager tokenManager) {

            this.pubNub = pubNub;
            this.telemetryManager = telemetryManager;
            this.retrofitManager = retrofitManager;
            this.tokenManager = tokenManager;
        }

        RemoteAction<FileUploadRequestDetails> create(String channel, String fileName) {
            return new GenerateUploadUrl(channel, fileName, pubNub, telemetryManager, retrofitManager, tokenManager);
        }

    }
}
