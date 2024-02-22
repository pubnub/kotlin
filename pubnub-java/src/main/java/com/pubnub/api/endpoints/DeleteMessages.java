package com.pubnub.api.endpoints;

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;
import com.pubnub.internal.InternalPubNubClient;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class DeleteMessages extends Endpoint<PNDeleteMessagesResult> {
    private List<String> channels;
    private Long start;
    private Long end;

    public DeleteMessages(InternalPubNubClient pubnub) {
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
