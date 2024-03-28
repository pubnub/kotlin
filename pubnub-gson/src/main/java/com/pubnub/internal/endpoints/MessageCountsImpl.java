package com.pubnub.internal.endpoints;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.MessageCounts;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.history.PNMessageCountResult;
import com.pubnub.internal.PubNubCore;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class MessageCountsImpl extends DelegatingEndpoint<PNMessageCountResult> implements MessageCounts {

    /**
     * The channel name you wish to pull history from. May be a single channel, or multiple channels, separated by
     * comma.
     */
    private List<String> channels;

    /**
     * Comma-delimited list of timetokens, in order of the channels list, in the request path. If list of timetokens
     * is not same length as list of channels, a 400 bad request will result.
     */
    private List<Long> channelsTimetoken;

    public MessageCountsImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNMessageCountResult> createAction() {
        return pubnub.messageCounts(channels, channelsTimetoken);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channels == null || channels.isEmpty()) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
        if (channelsTimetoken == null || channelsTimetoken.contains(null)) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_TIMETOKEN_MISSING);
        }
    }
}
