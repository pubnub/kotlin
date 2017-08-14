package com.pubnub.api.endpoints;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;
import com.pubnub.api.models.server.DeleteMessagesEnvelope;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)

public class DeleteMessages extends Endpoint<DeleteMessagesEnvelope, PNDeleteMessagesResult> {

    private static final int SERVER_RESPONSE_SUCCESS = 200;

    @Setter
    private List<String> channels;
    @Setter
    private Long start;
    @Setter
    private Long end;

    private interface DeleteHistoryService {
        @DELETE("v3/history/sub-key/{subKey}/channel/{channels}")
        Call<DeleteMessagesEnvelope> deleteMessages(@Path("subKey") String subKey,
                                                  @Path("channels") String channels,
                                                  @QueryMap Map<String, String> options);
    }

    public DeleteMessages(PubNub pubnubInstance, Retrofit retrofitInstance) {
        super(pubnubInstance, retrofitInstance);
        channels = new ArrayList<>();
    }

    @Override
    protected List<String> getAffectedChannels() {
        return channels;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return null;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channels == null || channels.size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING).build();
        }
    }

    @Override
    protected Call<DeleteMessagesEnvelope> doWork(Map<String, String> params) throws PubNubException {
        DeleteHistoryService service = this.getRetrofit().create(DeleteHistoryService.class);

        if (start != null) {
            params.put("start", Long.toString(start).toLowerCase());
        }
        if (end != null) {
            params.put("end", Long.toString(end).toLowerCase());
        }

        return service.deleteMessages(this.getPubnub().getConfiguration().getSubscribeKey(), PubNubUtil.joinString(channels, ","), params);
    }

    @Override
    protected PNDeleteMessagesResult createResponse(Response<DeleteMessagesEnvelope> input) throws PubNubException {
        if (input.body() == null || input.body().getStatus() == null || input.body().getStatus() != SERVER_RESPONSE_SUCCESS) {
            String errorMsg = null;

            if (input.body() != null && input.body().getErrorMessage() != null) {
                errorMsg = input.body().getErrorMessage();
            } else {
                errorMsg = "n/a";
            }

            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR)
                    .errormsg(errorMsg)
                    .build();
        }

        return PNDeleteMessagesResult.builder().build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNDeleteMessagesOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }
}
