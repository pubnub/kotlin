package com.pubnub.internal.java.endpoints.message_actions;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.endpoints.message_actions.GetMessageActions;
import com.pubnub.api.models.consumer.PNBoundedPage;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class GetMessageActionsImpl extends PassthroughEndpoint<PNGetMessageActionsResult> implements GetMessageActions {

    private String channel;
    private Long start;
    private Long end;
    private Integer limit;

    public GetMessageActionsImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PubNubError.CHANNEL_MISSING);
        }
    }

    @Override
    @NotNull
    protected Endpoint<PNGetMessageActionsResult> createRemoteAction() {
        return pubnub.getMessageActions(channel, new PNBoundedPage(start, end, limit));
    }
}
