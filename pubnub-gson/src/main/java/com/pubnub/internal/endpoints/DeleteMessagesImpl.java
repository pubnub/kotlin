package com.pubnub.internal.endpoints;

import com.pubnub.api.endpoints.DeleteMessages;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;
import com.pubnub.internal.PubNubCore;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class DeleteMessagesImpl extends DelegatingEndpoint<PNDeleteMessagesResult> implements DeleteMessages {
    private List<String> channels;
    private Long start;
    private Long end;

    public DeleteMessagesImpl(PubNubCore pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected ExtendedRemoteAction<PNDeleteMessagesResult> createAction() {
        return pubnub.deleteMessages(
                channels,
                start,
                end
        );
    }
}
