package com.pubnub.api.builder;

import com.pubnub.internal.PubNubImpl;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class PresenceBuilder extends PubSubBuilder {

    @Setter(AccessLevel.PUBLIC)
    private boolean connected;

    public PresenceBuilder(PubNubImpl pubnub) {
        super(pubnub);
    }

    public void execute() {
        this.getPubnub().presence(
                getChannelSubscriptions(),
                getChannelGroupSubscriptions(),
                connected
        );
    }

    public PresenceBuilder channels(List<String> channels) {
        return (PresenceBuilder) super.channels(channels);
    }

    public PresenceBuilder channelGroups(List<String> channelGroups) {
        return (PresenceBuilder) super.channelGroups(channelGroups);
    }

}
