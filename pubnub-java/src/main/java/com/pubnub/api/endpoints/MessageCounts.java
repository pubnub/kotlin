package com.pubnub.api.endpoints;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.history.PNMessageCountResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class MessageCounts extends ValidatingEndpoint<PNMessageCountResult> {

    /**
     * The channel name you wish to pull history from. May be a single channel, or multiple channels, separated by
     * comma.
     */
    private List<String> channels; // TODO merge with timetokens into a map?

    /**
     * Comma-delimited list of timetokens, in order of the channels list, in the request path. If list of timetokens
     * is not same length as list of channels, a 400 bad request will result.
     */
    private List<Long> channelsTimetoken;

    public MessageCounts(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNMessageCountResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.messageCounts(channels, channelsTimetoken));
    }
}
