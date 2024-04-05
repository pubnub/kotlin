package com.pubnub.internal.endpoints;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.DeleteMessages;
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
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

    public DeleteMessagesImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected @NotNull EndpointInterface<PNDeleteMessagesResult> createAction() {
        return pubnub.deleteMessages(channels, start, end);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channels == null || channels.isEmpty()) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}
