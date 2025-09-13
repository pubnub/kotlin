package com.pubnub.docs.publishAndSubscribe.subscribe;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.docs.SnippetBase;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SubscribeOld extends SnippetBase {

    private void subscribeToChannel() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-13

        PubNub pubNub = createPubNub();

        // snippet.subscribeToChannel
        pubNub.subscribe()
                .channels(Arrays.asList("my_channel"))
                .execute();
        // snippet.end
    }

    private void subscribeWithLogging() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-subscribe-with-logging

        // snippet.subscribeWithLogging
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);

        pubnub.subscribe()
                .channels(Arrays.asList("my_channel")) // subscribe to channels information
                .execute();
        // snippet.end
    }

    private void subscribeToPresenceChannel() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#subscribing-to-a-presence-channel

        PubNub pubNub = createPubNub();

        // snippet.subscribeToPresenceChannel
        pubNub.subscribe()
                .channels(Arrays.asList("my_channel")) // subscribe to channels
                .withPresence() // also subscribe to related presence information
                .execute();
        // snippet.end
    }

    private void presenceEvents() throws PubNubException {
        PubNub pubNub = createPubNub();

        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#join-event
        // snippet.presenceEventsJoin
        pubNub.addListener(new EventListener() {
            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presenceEvent) {
                if (presenceEvent.getEvent().equals("join")) {
                    presenceEvent.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    presenceEvent.getTimestamp(); // 1345546797
                    presenceEvent.getOccupancy(); // 2
                }
            }
        });
        // snippet.end

        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#leave-event
        // snippet.presenceEventsLeave
        pubNub.addListener(new EventListener() {
            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presenceEvent) {
                if (presenceEvent.getEvent().equals("leave")) {
                    presenceEvent.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    presenceEvent.getTimestamp(); // 1345546797
                    presenceEvent.getOccupancy(); // 2
                }
            }
        });
        // snippet.end

        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#timeout-event
        // snippet.presenceEventsTimeout
        pubNub.addListener(new EventListener() {
            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presenceEvent) {
                if (presenceEvent.getEvent().equals("timeout")) {
                    presenceEvent.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    presenceEvent.getTimestamp(); // 1345546797
                    presenceEvent.getOccupancy(); // 2
                }
            }
        });
        // snippet.end

        //https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#interval-event
        // snippet.presenceEventsInterval
        pubNub.addListener(new EventListener() {
            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presenceEvent) {
                if (presenceEvent.getEvent().equals("interval")) {
                    presenceEvent.getTimestamp(); // <unix timestamp>
                    presenceEvent.getOccupancy(); // <# users in channel>
                    presenceEvent.getHereNowRefresh(); // true
                }
            }
        });
        // snippet.end


        // snippet.presenceEventsStateChange
        pubNub.addListener(new EventListener() {
            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presenceEvent) {
                if (presenceEvent.getEvent().equals("state-change")) {
                    presenceEvent.getState(); // new state
                }
            }
        });
        // snippet.end
    }

    private void subscribeWithState() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#subscribing-with-state

        PubNub pubNub = createPubNub();

        // snippet.subscribeWithState

        class complexData {
            String fieldA;
            int fieldB;
        }

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                    complexData data = new complexData();
                    data.fieldA = "Awesome";
                    data.fieldB = 10;
                    pubnub.setPresenceState()
                            .channels(Arrays.asList("awesomeChannel"))
                            .channelGroups(Arrays.asList("awesomeChannelGroup"))
                            .state(data)
                            .async(result -> { /* check result */ });
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubNub.subscribe().channels(Arrays.asList("awesomeChannel")).execute();
        // snippet.end
    }

    private void subscribeToChannelGroup() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#subscribe-to-a-channel-group

        PubNub pubNub = createPubNub();

        // snippet.subscribeToChannelGroup
        pubNub.subscribe()
                .channels(Arrays.asList("ch1", "ch2")) // subscribe to channels
                .channelGroups(Arrays.asList("cg1", "cg2")) // subscribe to channel groups
                .withTimetoken(1337L) // optional, pass a timetoken
                .withPresence() // also subscribe to related presence information
                .execute();
        // snippet.end
    }

    private void subscribeToPresenceChannelOfChannelGroup() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#subscribe-to-the-presence-channel-of-a-channel-group

        PubNub pubNub = createPubNub();

        // snippet.subscribeToPresenceChannelOfChannelGroup
        pubNub.subscribe()
                .channelGroups(Arrays.asList("cg1", "cg2")) // subscribe to channel groups
                .withTimetoken(1337L) // optional, pass a timetoken
                .withPresence() // also subscribe to related presence information
                .execute();
        // snippet.end
    }
}
