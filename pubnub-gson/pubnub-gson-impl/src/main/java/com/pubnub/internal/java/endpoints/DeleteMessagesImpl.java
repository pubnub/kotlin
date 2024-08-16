package com.pubnub.internal.java.endpoints;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.DeleteMessages;
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class DeleteMessagesImpl extends IdentityMappingEndpoint<PNDeleteMessagesResult> implements DeleteMessages {
    private List<String> channels = new ArrayList<>();
    private Long start;
    private Long end;

    public DeleteMessagesImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected @NotNull Endpoint<PNDeleteMessagesResult> createAction() {
        return pubnub.deleteMessages(channels, start, end);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channels == null || channels.isEmpty()) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}
