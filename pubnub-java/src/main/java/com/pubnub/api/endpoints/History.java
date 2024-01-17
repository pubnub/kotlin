package com.pubnub.api.endpoints;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
@Accessors(chain = true, fluent = true)
public class History extends ValidatingEndpoint<PNHistoryResult> {
    private String channel;
    private Long start;
    private Long end;
    private boolean reverse;
    private int count = com.pubnub.internal.endpoints.History.MAX_COUNT;
    private boolean includeTimetoken;
    private boolean includeMeta;

    public History(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNHistoryResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.history(
                channel,
                start,
                end,
                count,
                reverse,
                includeTimetoken,
                includeMeta
        ));
    }
}