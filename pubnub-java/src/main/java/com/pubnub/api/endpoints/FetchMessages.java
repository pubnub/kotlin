package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.internal.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Accessors(chain = true, fluent = true)
public class FetchMessages extends Endpoint<PNFetchMessagesResult> {
    @Setter
    private List<String> channels = new ArrayList<>();
    @Setter
    private int maximumPerChannel = 0;
    @Setter
    private Long start;
    @Setter
    private Long end;

    @Setter
    private boolean includeMeta;
    @Setter
    private boolean includeMessageActions;
    @Setter
    private boolean includeMessageType = true;
    @Setter
    private boolean includeUUID = true;

    public FetchMessages(PubNubImpl pubnub) {
        super(pubnub);
    }

    @NotNull
    @Override
    protected com.pubnub.internal.endpoints.FetchMessages createAction() {
        return pubnub.fetchMessages(
                channels,
                maximumPerChannel,
                start,
                end,
                includeMeta,
                includeMessageActions,
                includeMessageType
        );
    }

}
