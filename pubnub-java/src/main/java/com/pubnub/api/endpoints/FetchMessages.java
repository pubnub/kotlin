package com.pubnub.api.endpoints;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Setter
@Slf4j
@Accessors(chain = true, fluent = true)
public class FetchMessages extends ValidatingEndpoint<PNFetchMessagesResult> {
    private List<String> channels = new ArrayList<>();
    private int maximumPerChannel = 0;
    private Long start;
    private Long end;

    private boolean includeMeta;
    private boolean includeMessageActions;
    private boolean includeMessageType = true;
    private boolean includeUUID = true;

    public FetchMessages(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNFetchMessagesResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.fetchMessages(
                channels,
                maximumPerChannel,
                start,
                end,
                includeMeta,
                includeMessageActions,
                includeMessageType
        ));
    }

}
