package com.pubnub.docs.presence;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class UserStateOther extends SnippetBase {

    private void setState() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#set-state-1

        PubNub pubNub = createPubNub();

        // snippet.setState
        String channel = "my_channel";
        JsonObject state = new JsonObject();
        state.addProperty("is_typing", true);

        pubNub.setPresenceState()
                .channels(Arrays.asList(channel))
                .state(state)
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void getState() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#get-state-1

        PubNub pubNub = createPubNub();

        // snippet.getState
        pubNub.getPresenceState()
                .channels(Arrays.asList("ch1", "ch2", "ch3")) // channels to fetch state for
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3")) // channel groups to fetch state for
                .uuid("suchUUID") // uuid of user to fetch, or for own uuid
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void setStateForChannelsInChannelGroup() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#set-state-for-channels-in-channel-group

        PubNub pubNub = createPubNub();

        // snippet.setStateForChannelsInChannelGroup
        JsonObject state = new JsonObject();
        state.addProperty("is_typing", true);

        pubNub.setPresenceState()
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3")) // apply on those channel groups
                .channels(Arrays.asList("ch1", "ch2", "ch3")) // apply on those channels
                .state(state) // the new state
                // snippet.hide
                // IntelliJ show error in .async(result -> { /* check result */ });
                // but it compiles fine. In pure project with Java SDK as dependency error is not present
                // snippet.show
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void setStateResponseOnListener() throws PubNubException{
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#set-state-for-channels-in-channel-group

        PubNub pubNub = createPubNub();

        // snippet.setStateResponseOnListener
        Channel channel = pubNub.channel("ch1");
        Subscription subscription = channel.subscription(SubscriptionOptions.receivePresenceEvents());

        subscription.setOnPresence(pnPresenceEventResult -> {
            if (pnPresenceEventResult.getEvent().equals("state-change")) {
                boolean is_typing = pnPresenceEventResult.getState()
                        // snippet.hide
                            // getAsJsonObject is displayed as having issue but it compiles fine. In pure project with Java SDK as dependency error is not present
                        // snippet.show
                        .getAsJsonObject()
                        .get("is_typing")
                        .getAsBoolean();
            }
        });
        // snippet.end
    }
}
