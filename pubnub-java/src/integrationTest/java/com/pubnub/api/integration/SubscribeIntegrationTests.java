package com.pubnub.api.integration;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

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
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                assertTrue(message.getMessage().toString().contains("Cool message"));
                success.set(true);
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {

            }

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull final PubNub pubnub, @NotNull final PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

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
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
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

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull final PubNub pubnub, @NotNull final PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

            }
        });

        unsubscribeFromChannel(pubNub, expectedChannel);

        listen(success);
    }

    @Test
    public void testUnsubscribeFromAllChannels() {
        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
                assertEquals(0, pubNub.getSubscribedChannels().size());
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

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull PubNub pubNub, @NotNull PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

            }
        });

        unsubscribeFromAllChannels(pubNub);
        listen(success);
    }

}
