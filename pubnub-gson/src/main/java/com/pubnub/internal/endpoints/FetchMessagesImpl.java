package com.pubnub.internal.endpoints;

import com.pubnub.api.models.consumer.PNBoundedPage;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.internal.PubNubCore;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Slf4j
@Accessors(chain = true, fluent = true)
public class FetchMessagesImpl extends DelegatingEndpoint<PNFetchMessagesResult> implements com.pubnub.api.endpoints.FetchMessages {
    private List<String> channels = new ArrayList<>();
    private int maximumPerChannel = 0;
    private Long start;
    private Long end;

    private boolean includeMeta;
    private boolean includeMessageActions;
    private boolean includeMessageType = true;
    private boolean includeUUID = true;

    public FetchMessagesImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @NotNull
    @Override
    protected FetchMessagesEndpoint createAction() {
        return pubnub.fetchMessages(
                channels,
                new PNBoundedPage(start, end, maximumPerChannel),
                includeUUID,
                includeMeta,
                includeMessageActions,
                includeMessageType
        );
    }

}
