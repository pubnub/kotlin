package com.pubnub.api.endpoints;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class DeleteMessages extends ValidatingEndpoint<PNDeleteMessagesResult> {
    private List<String> channels;
    private Long start;
    private Long end;

    public DeleteMessages(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected Endpoint<PNDeleteMessagesResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.deleteMessages(
                channels,
                start,
                end
        ));
    }
}
