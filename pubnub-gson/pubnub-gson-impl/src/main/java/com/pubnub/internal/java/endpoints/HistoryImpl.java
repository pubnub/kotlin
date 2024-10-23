package com.pubnub.internal.java.endpoints;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Setter
@Slf4j
@Accessors(chain = true, fluent = true)
public class HistoryImpl extends PassthroughEndpoint<PNHistoryResult> implements com.pubnub.api.java.endpoints.History {
    private String channel;
    private Long start;
    private Long end;
    private boolean reverse;
    private int count = PNHistoryResult.MAX_COUNT;
    private boolean includeTimetoken;
    private boolean includeMeta;

    public HistoryImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNHistoryResult> createRemoteAction() {
        return pubnub.history(channel, start, end, count, reverse, includeTimetoken, includeMeta, false);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channel == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
    }
}