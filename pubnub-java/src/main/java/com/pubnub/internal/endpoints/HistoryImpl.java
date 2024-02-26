package com.pubnub.internal.endpoints;

import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
@Accessors(chain = true, fluent = true)
public class HistoryImpl extends DelegatingEndpoint<PNHistoryResult> implements com.pubnub.api.endpoints.History {
    private String channel;
    private Long start;
    private Long end;
    private boolean reverse;
    private int count = History.MAX_COUNT;
    private boolean includeTimetoken;
    private boolean includeMeta;

    public HistoryImpl(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNHistoryResult> createAction() {
        return pubnub.history(
                channel,
                start,
                end,
                count,
                reverse,
                includeTimetoken,
                includeMeta
        );
    }
}