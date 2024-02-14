package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.internal.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(chain = true, fluent = true)
public class History extends Endpoint<PNHistoryResult> {
    @Setter
    private String channel;
    @Setter
    private Long start;
    @Setter
    private Long end;
    @Setter
    private boolean reverse;
    @Setter
    private int count = com.pubnub.internal.endpoints.History.MAX_COUNT;
    @Setter
    private boolean includeTimetoken;
    @Setter
    private boolean includeMeta;

    public History(PubNubImpl pubnub) {
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