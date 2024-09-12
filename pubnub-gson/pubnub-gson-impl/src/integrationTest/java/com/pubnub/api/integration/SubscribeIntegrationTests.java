package com.pubnub.api.integration;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.RandomGenerator;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.v2.callbacks.handlers.OnMessageHandler;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.entities.ChannelMetadata;
import com.pubnub.api.java.v2.entities.UserMetadata;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import org.awaitility.Durations;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubscribeIntegrationTests extends BaseIntegrationTest {

    private PubNub mGuestClient;

    @Override
    protected void onBefore() {
        mGuestClient = getPubNub();
    }

    @Override
    protected void onAfter() {

    }

    @Test
    public void testSubscribeToMultipleChannels() {
        final String channel1 = randomChannel();
        final String channel2 = randomChannel();
        final String channel3 = randomChannel();

        pubNub.subscribe()
                .channels(Arrays.asList(channel1, channel2, channel3))
                .withPresence()
                .execute();

        pause(2);

        assertEquals(3, pubNub.getSubscribedChannels().size());
        assertTrue(pubNub.getSubscribedChannels().contains(channel1));
        assertTrue(pubNub.getSubscribedChannels().contains(channel2));
        assertTrue(pubNub.getSubscribedChannels().contains(channel3));
    }

    @Test
    public void testSubscribeToChannel() {
        final String channel = randomChannel();

        pubNub.subscribe()
                .channels(Collections.singletonList(channel))
                .withPresence()
                .execute();

        pause(2);

        assertEquals(1, pubNub.getSubscribedChannels().size());
        assertTrue(pubNub.getSubscribedChannels().contains(channel));
    }

    // If test does not work make sure you've enabled wildcard
    @Test
    public void testWildcardSubscribe() {
        final AtomicBoolean success = new AtomicBoolean();

        subscribeToChannel(pubNub, "my.*");
        subscribeToChannel(mGuestClient, "my.test");

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                assertTrue(message.getMessage().toString().contains("Cool message"));
                success.set(true);
            }
            
        });

        publishMessage(mGuestClient, "my.test", "Cool message!");

        listen(success);
    }

    @Test
    public void testUnsubscribeFromChannel() {
        final AtomicBoolean success = new AtomicBoolean();

        final String expectedChannel = randomChannel();

        subscribeToChannel(pubNub, expectedChannel);

        pubNub.addListener(new SubscribeCallback() {


            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
                boolean channelSubscribed = false;
                for (int i = 0; i < pubnub.getSubscribedChannels().size(); i++) {
                    if (pubnub.getSubscribedChannels().get(i).contains(expectedChannel)) {
                        channelSubscribed = true;
                    }
                }
                assertFalse(channelSubscribed);

                pubNub.removeListener(this);
                success.set(true);
            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                success.set(true);
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {
                success.set(true);
            }
        });

        unsubscribeFromChannel(pubNub, expectedChannel);

        listen(success, Durations.TEN_SECONDS);
    }

    @Test
    public void testUnsubscribeFromAllChannels() {
        final AtomicBoolean success = new AtomicBoolean();
        subscribeToChannel(pubNub, randomChannel());

        pubNub.addListener(new SubscribeCallback.BaseSubscribeCallback() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
                assertEquals(0, pubNub.getSubscribedChannels().size());
                success.set(true);
            }
        });

        unsubscribeFromAllChannels(pubNub);
        listen(success);
    }

    @Test
    public void testAssignBehaviourForChannelGroup() throws InterruptedException, PubNubException {
        AtomicInteger numberOfReceivedMessages = new AtomicInteger(0);
        String expectedMessage = random();
        String channelGroupName = "cg_" + randomChannel();
        String channel01 = randomChannel();
        String channel02 = randomChannel();
        pubNub.addChannelsToChannelGroup().channelGroup(channelGroupName).channels(Arrays.asList(channel01, channel02)).sync();

        Subscription channelGroupSubscription = pubNub.channelGroup(channelGroupName).subscription();
        channelGroupSubscription.setOnMessage(pnMessageResult -> numberOfReceivedMessages.incrementAndGet());

        channelGroupSubscription.subscribe();
        Thread.sleep(2000);

        pubNub.publish().message(expectedMessage).channel(channel01).sync();
        Thread.sleep(1000);
        assertEquals(1, numberOfReceivedMessages.get());

        channelGroupSubscription.setOnMessage(null);

        pubNub.publish().message(expectedMessage).channel(channel01).sync();
        Thread.sleep(1000);
        assertEquals(1, numberOfReceivedMessages.get());
    }

    @Test
    public void testAssignBehaviourForUuidMetadataUsingChannelMetadataObject() throws InterruptedException, PubNubException {
        AtomicInteger numberOfChannelMetadataEvents = new AtomicInteger();
        String channelName = randomChannel();
        ChannelMetadata channelMetadata = pubNub.channelMetadata(channelName);
        Subscription channelMetadataSubscription = channelMetadata.subscription();

        channelMetadataSubscription.setOnChannelMetadata(pnChannelMetadataResult -> {
            System.out.println("received: " + pnChannelMetadataResult.getEvent());
            numberOfChannelMetadataEvents.incrementAndGet();
        });
        channelMetadataSubscription.subscribe();
        Thread.sleep(2000);

        pubNub.setChannelMetadata().channel(channelName).name("MyChannel").description("This is test description").sync();
        Thread.sleep(1000);

        assertEquals(1, numberOfChannelMetadataEvents.get());

        channelMetadataSubscription.setOnChannelMetadata(null);
        pubNub.setChannelMetadata().channel(channelName).name("different channel name").description("This is test description").sync();
        Thread.sleep(1000);

        assertEquals(1, numberOfChannelMetadataEvents.get());
    }

    @Test
    public void testAssignBehaviourForUuidMetadataUsingChannelObject() throws PubNubException, InterruptedException {
        AtomicInteger numberOfSetMetaDataEvent = new AtomicInteger(0);
        Channel channel = pubNub.channel(randomChannel());
        Subscription subscription = channel.subscription();
        subscription.setOnUuidMetadata(pnUUIDMetadataResult -> {
            System.out.println("-=super: " + pnUUIDMetadataResult.getData());
            numberOfSetMetaDataEvent.incrementAndGet();
        });
        subscription.subscribe();
        Thread.sleep(2000);

        // to get event related to uuidMetadata changes we need to add this uuid to channel that we subscribed to.
        pubNub.setMemberships().channelMemberships(Collections.singletonList(PNChannelMembership.channel(channel.getName()))).sync();
        pubNub.setUUIDMetadata().name("uuid name").sync();
        Thread.sleep(1000);

        assertEquals(1, numberOfSetMetaDataEvent.get());

        subscription.setOnUuidMetadata(null);
        pubNub.setUUIDMetadata().name("different uuid name").sync();
        Thread.sleep(1000);

        assertEquals(1, numberOfSetMetaDataEvent.get());
    }

    @Test
    public void testAssignBehaviourForUuidMetadataUsingUserMetadataObject() throws PubNubException, InterruptedException {
        AtomicInteger numberOfUuidMetadataEvents = new AtomicInteger(0);
        String channelName = randomChannel();
        UserMetadata userMetadata = pubNub.userMetadata(channelName);
        Subscription userMetadataSubscription = userMetadata.subscription();

        userMetadataSubscription.setOnUuidMetadata(pnUUIDMetadataResult -> {
            System.out.println("pnUUIDMetadataResult.getData(): " + pnUUIDMetadataResult.getData());
            numberOfUuidMetadataEvents.incrementAndGet();
        });
        userMetadataSubscription.subscribe();
        Thread.sleep(2000);

        // to get event related to uuidMetadata changes we need to add this uuid to channel that we subscribed to.
        pubNub.setMemberships().channelMemberships(Collections.singletonList(PNChannelMembership.channel(channelName))).sync();
        pubNub.setUUIDMetadata().name("uuid name").sync();
        Thread.sleep(1000);

        assertEquals(1, numberOfUuidMetadataEvents.get());

        userMetadataSubscription.setOnUuidMetadata(null);
        pubNub.setUUIDMetadata().name("different uuid name").sync();
        Thread.sleep(1000);

        assertEquals(1, numberOfUuidMetadataEvents.get());
    }

    @Test
    public void testAssigningEventBehaviourToSubscription() throws InterruptedException, PubNubException, IOException {
        String expectedMessage = random();
        String fileContent = "This is content";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        final JsonObject expectedStatePayload = generatePayload();
        AtomicInteger numberOfReceivedMessage = new AtomicInteger(0);
        AtomicInteger numberOfReceivedPresentEvent = new AtomicInteger(0);
        AtomicInteger numberOfReceivedSignal = new AtomicInteger(0);
        AtomicInteger numberOfReceivedMessageAction = new AtomicInteger(0);
        AtomicInteger numberOfReceivedChannelMetadataEvents = new AtomicInteger(0);
        AtomicInteger numberOfReceivedMembershipEvent = new AtomicInteger(0);
        AtomicInteger numberOfReceivedFileMessages = new AtomicInteger(0);
        Channel chan01 = pubNub.channel(randomChannel());


        Subscription subscription = chan01.subscription(SubscriptionOptions.receivePresenceEvents());
        OnMessageHandler onMessageHandler = pnMessageResult -> {
            numberOfReceivedMessage.incrementAndGet();
            System.out.println("-=pnMessageResult: " + pnMessageResult.getMessage());
        };

        subscription.setOnMessage(pnMessageResult -> System.out.println("Received message: " + pnMessageResult.getMessage()));

        subscription.setOnMessage(onMessageHandler);
        subscription.setOnPresence(pnPresenceEventResult -> {
            numberOfReceivedPresentEvent.incrementAndGet();
            System.out.println("-=pnPresenceEventResult: " + pnPresenceEventResult.getEvent());

        });
        subscription.setOnSignal(pnSignalResult -> numberOfReceivedSignal.incrementAndGet());
        subscription.setOnMessageAction(pnMessageActionResult -> numberOfReceivedMessageAction.incrementAndGet());
        subscription.setOnChannelMetadata(pnChannelMetadataResult -> numberOfReceivedChannelMetadataEvents.incrementAndGet());
        subscription.setOnMembership(pnMembershipResult -> numberOfReceivedMembershipEvent.incrementAndGet());
        subscription.setOnFile(pnFileEventResult -> numberOfReceivedFileMessages.incrementAndGet());
        subscription.subscribe();
        Thread.sleep(2000);

        PNPublishResult pnPublishResult01 = pubNub.publish().message(expectedMessage).channel(chan01.getName()).sync();
        pubNub.signal().message(expectedMessage).channel(chan01.getName()).sync();
        PNMessageAction pnMessageAction = new PNMessageAction().setType("reaction").setValue(RandomGenerator.emoji()).setMessageTimetoken(pnPublishResult01.getTimetoken());
        pubNub.addMessageAction().messageAction(pnMessageAction).channel(chan01.getName()).sync();
        pubNub.setChannelMetadata().channel(chan01.getName()).name("Channel name").description("-=desc").status("active").type("Chat").sync();
        pubNub.setMemberships().channelMemberships(Collections.singletonList(PNChannelMembership.channel(chan01.getName()))).sync();
        pubNub.sendFile().channel(chan01.getName()).fileName(random()).inputStream(inputStream).message("message").sync();

        Thread.sleep(1000);

        assertEquals(1, numberOfReceivedMessage.get());
        assertEquals(1, numberOfReceivedSignal.get());
        assertEquals(1, numberOfReceivedPresentEvent.get()); // first presence event is join generated automatically
        assertEquals(1, numberOfReceivedMessageAction.get());
        assertEquals(1, numberOfReceivedChannelMetadataEvents.get());
        assertEquals(1, numberOfReceivedMembershipEvent.get());
        assertEquals(1, numberOfReceivedFileMessages.get());

        subscription.setOnMessage(null);
        subscription.setOnPresence(null);
        subscription.setOnSignal(null);
        subscription.setOnMessageAction(null);
        subscription.setOnChannelMetadata(null);
        subscription.setOnMembership(null);
        subscription.setOnFile(null);

        PNPublishResult pnPublishResult02 = pubNub.publish().message(expectedMessage).channel(chan01.getName()).sync();
        pubNub.setPresenceState().state(expectedStatePayload).channels(Collections.singletonList(chan01.getName())).sync();
        pubNub.signal().message(expectedMessage).channel(chan01.getName()).sync();
        pnMessageAction = new PNMessageAction().setType("reaction02").setValue(RandomGenerator.emoji()).setMessageTimetoken(pnPublishResult02.getTimetoken());
        pubNub.addMessageAction().messageAction(pnMessageAction).channel(chan01.getName()).sync();
        pubNub.setChannelMetadata().channel(chan01.getName()).name("Channel name").description("desc").status("active").type("Chat").sync();
        pubNub.setMemberships().channelMemberships(Collections.singletonList(PNChannelMembership.channel(chan01.getName()))).uuid("differentUUID").sync();
        pubNub.sendFile().channel(chan01.getName()).fileName(random()).inputStream(inputStream).message("message").sync();
        Thread.sleep(1000);

        // number of events received should remain 1
        assertEquals(1, numberOfReceivedMessage.get());
        assertEquals(1, numberOfReceivedPresentEvent.get());
        assertEquals(1, numberOfReceivedSignal.get());
        assertEquals(1, numberOfReceivedMessageAction.get());
        assertEquals(1, numberOfReceivedChannelMetadataEvents.get());
        assertEquals(1, numberOfReceivedMembershipEvent.get());
        assertEquals(1, numberOfReceivedFileMessages.get());
    }

    @Test
    public void testAssigningEventBehaviourToSubscriptionSet() throws InterruptedException, PubNubException {
        AtomicInteger numberOfMessagesReceived = new AtomicInteger(0);
        AtomicInteger numberOfSignalsReceived = new AtomicInteger(0);
        AtomicInteger numberOfPresenceEventsReceived = new AtomicInteger(0);
        AtomicInteger numberOfMessageActionsReceived = new AtomicInteger(0);
        final JsonObject expectedStatePayload = generatePayload();
        Channel channel01 = pubNub.channel("ch1_" + randomChannel());
        Channel channel02 = pubNub.channel("ch2_" + randomChannel());
        // we want to get only presence events for channel01
        Subscription subscription01 = channel01.subscription(SubscriptionOptions.receivePresenceEvents());
        Subscription subscription02 = channel02.subscription();

        SubscriptionSet subscriptionSet = subscription01.plus(subscription02);
        subscriptionSet.setOnMessage(pnMessageResult -> numberOfMessagesReceived.incrementAndGet());
        subscriptionSet.setOnSignal(pnSignalResult -> numberOfSignalsReceived.incrementAndGet());
        subscriptionSet.setOnPresence(pnPresenceEventResult -> numberOfPresenceEventsReceived.incrementAndGet());
        subscriptionSet.setOnMessageAction(pnMessageActionResult -> numberOfMessageActionsReceived.incrementAndGet());

        subscriptionSet.subscribe();
        Thread.sleep(2000);

        PNPublishResult pnPublishResult01 = pubNub.publish().channel(channel01.getName()).message("anything").sync();
        PNPublishResult pnPublishResult02 = pubNub.publish().channel(channel02.getName()).message("anything").sync();
        pubNub.signal().channel(channel01.getName()).message("anything").sync();
        pubNub.signal().channel(channel02.getName()).message("anything").sync();
        PNMessageAction pnMessageAction01 = new PNMessageAction().setType("reaction").setValue(RandomGenerator.emoji()).setMessageTimetoken(pnPublishResult01.getTimetoken());
        PNMessageAction pnMessageAction02 = new PNMessageAction().setType("reaction").setValue(RandomGenerator.emoji()).setMessageTimetoken(pnPublishResult02.getTimetoken());
        pubNub.addMessageAction().messageAction(pnMessageAction01).channel(channel01.getName()).sync();
        pubNub.addMessageAction().messageAction(pnMessageAction02).channel(channel02.getName()).sync();
        Thread.sleep(1000);

        assertEquals(2, numberOfMessagesReceived.get());
        assertEquals(2, numberOfSignalsReceived.get());
        assertEquals(1, numberOfPresenceEventsReceived.get()); // first presence event is join generated automatically
        assertEquals(2, numberOfMessageActionsReceived.get());

        subscriptionSet.setOnMessage(null);
        subscriptionSet.setOnSignal(null);
        subscriptionSet.setOnPresence(null);
        subscriptionSet.setOnMessageAction(null);

        pubNub.publish().channel(channel01.getName()).message("anything").sync();
        pubNub.publish().channel(channel02.getName()).message("anything").sync();
        pubNub.signal().channel(channel01.getName()).message("anything").sync();
        pubNub.signal().channel(channel02.getName()).message("anything").sync();
        pubNub.setPresenceState().state(expectedStatePayload).channels(Collections.singletonList(channel01.getName())).sync();
        pubNub.setPresenceState().state(expectedStatePayload).channels(Collections.singletonList(channel02.getName())).sync();
        pnMessageAction01 = new PNMessageAction().setType("reaction02").setValue(RandomGenerator.emoji()).setMessageTimetoken(pnPublishResult01.getTimetoken());
        pnMessageAction02 = new PNMessageAction().setType("reaction02").setValue(RandomGenerator.emoji()).setMessageTimetoken(pnPublishResult02.getTimetoken());
        pubNub.addMessageAction().messageAction(pnMessageAction01).channel(channel01.getName()).sync();
        pubNub.addMessageAction().messageAction(pnMessageAction02).channel(channel02.getName()).sync();

        Thread.sleep(1000);

        assertEquals(2, numberOfMessagesReceived.get());
        assertEquals(2, numberOfSignalsReceived.get());
        assertEquals(1, numberOfPresenceEventsReceived.get());
        assertEquals(2, numberOfMessageActionsReceived.get());
    }

    @Test
    public void canAddAndRemoveSubscriptionFromSubscriptionSet() {
        Subscription subscription01 = pubNub.channel(randomChannel()).subscription();
        Subscription subscription02 = pubNub.channel(randomChannel()).subscription();
        Subscription subscription03 = pubNub.channel(randomChannel()).subscription();

        SubscriptionSet subscriptionSet = subscription01.plus(subscription02);
        assertEquals(2, subscriptionSet.getSubscriptions().size());

        subscriptionSet.add(subscription03);
        assertEquals(3, subscriptionSet.getSubscriptions().size());

        subscriptionSet.remove(subscription01);
        assertEquals(2, subscriptionSet.getSubscriptions().size());
    }
}
