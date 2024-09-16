package com.pubnub.api.integration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.PNConfigurationOverride;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.v2.callbacks.Result;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PublishIntegrationTests extends BaseIntegrationTest {

    @Override
    protected void onBefore() {

    }

    @Override
    protected void onAfter() {

    }

    @Test
    public void testPublishMessage() throws PubNubException {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final JsonObject messagePayload = generateMessage(pubNub);

        pubNub.publish(messagePayload, expectedChannel).shouldStore(true).usePOST(true).async((result) -> {
            assertFalse(result.isFailure());
            success.set(true);
        });

        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilTrue(success);
        success.set(false);

        pubNub.publish()
                .message(messagePayload)
                .channel(expectedChannel)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublishUsingChannelEntity() throws InterruptedException, PubNubException {
        AtomicBoolean messageReceived = new AtomicBoolean(false);
        AtomicBoolean signalReceived = new AtomicBoolean(false);
        String channelName = randomChannel();
        Channel channel = pubNub.channel(channelName);

        Subscription subscription = channel.subscription();
        subscription.setOnMessage(message -> messageReceived.set(true));
        subscription.setOnSignal(pnSignalResult -> signalReceived.set(true));
        subscription.subscribe();
        Thread.sleep(1000);

        channel.publish("My message").meta("meta").sync();
        channel.signal("My signal").sync();
        channel.fire("My fire").sync(); // just to test API

        Awaitility.await()
                .atMost(Durations.FIVE_SECONDS)
                .untilAtomic(messageReceived, Matchers.equalTo(true));
        Awaitility.await()
                .atMost(Durations.FIVE_SECONDS)
                .untilAtomic(signalReceived, Matchers.equalTo(true));

    }

    @Test
    public void testPublishUsingChannelEntityInAsyncMode() throws InterruptedException, PubNubException {
        AtomicBoolean messageSent = new AtomicBoolean(false);
        AtomicBoolean signalSent = new AtomicBoolean(false);
        AtomicBoolean fireSent = new AtomicBoolean(false);
        String channelName = randomChannel();
        Channel channel = pubNub.channel(channelName);

        Subscription subscription = channel.subscription();
        subscription.subscribe();
        Thread.sleep(1000);

        channel.publish("My message").meta("meta").async((Result<PNPublishResult> result) -> {
            result.onSuccess( (PNPublishResult res) -> {
                assertTrue(result.getOrNull().getTimetoken() != 0);
                messageSent.set(true);
            }).onFailure( (PubNubException exception) -> {
                System.out.println("Exception occurred: " + exception.getMessage());
            });
        });

        channel.signal("My signal").async((Result<PNPublishResult> result) ->{
            assertTrue(result.isSuccess());
            assertTrue(result.getOrNull().getTimetoken() != 0);
            signalSent.set(true);
        });

        channel.fire("My fire").async((Result<PNPublishResult> result) -> {
            assertTrue(result.isSuccess());
            assertTrue(result.getOrNull().getTimetoken() != 0);
            fireSent.set(true);
        });

        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAtomic(messageSent, Matchers.equalTo(true));
        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAtomic(signalSent, Matchers.equalTo(true));
        Awaitility.await().atMost(Durations.FIVE_SECONDS).untilAtomic(fireSent, Matchers.equalTo(true));
    }

    @Test
    public void testPublishMessageHistory() throws PubNubException {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final JsonObject messagePayload = new JsonObject();

        try {
            messagePayload.addProperty("name", "joe");
            messagePayload.addProperty("age", 48);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObject whatToExpect = messagePayload;

        pubNub.publish(messagePayload, expectedChannel).sync();

        pause(2);

        pubNub.fetchMessages()
                .channels(Collections.singletonList(expectedChannel))
                .maximumPerChannel(1)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertEquals(1, result.getOrNull().getChannels().size());
                    assertEquals(1, result.getOrNull().getChannels().get(expectedChannel).size());
                    assertEquals(whatToExpect, result.getOrNull().getChannels().get(expectedChannel).get(
                            0).getMessage());

                    success.set(true);
                });

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublishMessageWithOverrideUuid() throws PubNubException {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final JsonObject messagePayload = new JsonObject();
        final String expectedUser = PubNub.generateUUID();

        try {
            messagePayload.addProperty("name", "joe");
            messagePayload.addProperty("age", 48);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObject whatToExpect = messagePayload;

        PNConfiguration overrideConfig = PNConfigurationOverride.from(pubNub.getConfiguration()).setUserId(new UserId(expectedUser)).build();

        pubNub.publish()
                .channel(expectedChannel)
                .message(messagePayload)
                .overrideConfiguration(overrideConfig)
                .sync();

        pause(2);

        pubNub.fetchMessages()
                .channels(Collections.singletonList(expectedChannel))
                .maximumPerChannel(1)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    result.onSuccess(pnFetchMessagesResult -> {
                        assertEquals(1, pnFetchMessagesResult.getChannels().size());
                        assertEquals(1, pnFetchMessagesResult.getChannels().get(expectedChannel).size());
                        assertEquals(whatToExpect, pnFetchMessagesResult.getChannels().get(expectedChannel).get(
                                0).getMessage());
                        assertEquals(expectedUser, pnFetchMessagesResult.getChannels().get(expectedChannel).get(
                                0).getUuid());

                        success.set(true);
                    });
                });

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testPublishMessageNoHistory() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final JsonObject messagePayload = generateMessage(pubNub);

        pubNub.publish()
                .message(messagePayload)
                .channel(expectedChannel)
                .shouldStore(false)
                .async((result) -> {
                    assertFalse(result.isFailure());
                });

        pause(2);

        pubNub.history()
                .count(1)
                .channel(expectedChannel)
                .async((result) -> {
                    assertFalse(result.isFailure());
                    assertEquals(0, result.getOrNull().getMessages().size());
                    success.set(true);
                });

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testReceiveMessage() {
        final AtomicBoolean success = new AtomicBoolean();
        final String expectedChannel = randomChannel();
        final JsonObject messagePayload = generateMessage(pubNub);

        final PubNub observer = getPubNub();

        this.pubNub.addListener(new SubscribeCallback() {

            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    if (status.getAffectedChannels().contains(expectedChannel)) {
                        observer.publish()
                                .message(messagePayload)
                                .channel(expectedChannel)
                                .async((result) -> assertFalse(result.isFailure()));
                    }
                }
            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                assertEquals(expectedChannel, message.getChannel());
                assertEquals(observer.getConfiguration().getUserId().getValue(), message.getPublisher());
                assertEquals(messagePayload, message.getMessage());
                success.set(true);
            }

        });

        subscribeToChannel(pubNub, expectedChannel);

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(success);
    }

    @Test
    public void testReceiveUnencryptedMessageWithCryptoDoesntCrash() {
        final AtomicInteger success = new AtomicInteger(0);
        final String expectedChannel = randomChannel();
        final JsonObject messagePayload = generateMessage(pubNub);

        final PubNub sender = getPubNub();
        final PubNub observer = getPubNub(builder -> builder.cryptoModule(CryptoModule.createAesCbcCryptoModule("test", false)));

        observer.addListener(new SubscribeCallback() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    if (status.getAffectedChannels().contains(expectedChannel)) {
                        // send an unencrypted message first to try to crash the SubscribeMessageProcessor
                        sender.publish()
                                .message(messagePayload)
                                .channel(expectedChannel)
                                .async((result) -> {
                                    assertFalse(result.isFailure());

                                    // then verify if the subscribe loop is still working by sending an encrypted message
                                    observer.publish()
                                            .message(messagePayload)
                                            .channel(expectedChannel)
                                            .async(result2 -> assertFalse(result2.isFailure()));
                                });
                    }
                }
            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                if (success.get() == 0) {
                    assertEquals(expectedChannel, message.getChannel());
                    assertEquals(sender.getConfiguration().getUserId().getValue(), message.getPublisher());
                    assertEquals(PubNubErrorBuilder.PNERROBJ_PNERR_CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED, message.getError());
                    assertEquals(messagePayload, message.getMessage());
                    success.incrementAndGet();
                } else if (success.get() == 1) {
                    assertEquals(expectedChannel, message.getChannel());
                    assertEquals(observer.getConfiguration().getUserId().getValue(), message.getPublisher());
                    assertEquals(messagePayload, message.getMessage());
                    assertNull(message.getError());
                    success.incrementAndGet();
                }
            }

        });

        subscribeToChannel(observer, expectedChannel);

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilAtomic(success, Matchers.greaterThanOrEqualTo(2));
    }

    @Test
    public void testOrgJsonObject_Get_History() throws PubNubException, JSONException {
        final String channel = random();

        final JSONObject payload = new JSONObject();
        payload.put("name", "John Doe");
        payload.put("city", "San Francisco");

        pubNub.publish(payload, channel).usePOST(true).sync();

        pause(3);

        final PNHistoryResult historyResult = pubNub.history()
                .channel(channel)
                .count(1)
                .sync();

        assert historyResult != null;
        final JsonElement receivedMessage = historyResult.getMessages().get(0).getEntry();

        final JSONObject receivedObject = new JSONObject(receivedMessage.toString());

        assertEquals(payload.toString(), receivedObject.toString());
    }

    @Test
    public void testOrgJsonObject_Post_History() throws PubNubException, JSONException {
        final String channel = random();

        final JSONObject payload = generatePayloadJSON();

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .usePOST(true)
                .sync();

        pause(3);

        final PNHistoryResult historyResult = pubNub.history()
                .channel(channel)
                .count(1)
                .sync();
        assert historyResult != null;
        final JsonElement receivedMessage = historyResult.getMessages().get(0).getEntry();

        final JSONObject receivedObject = new JSONObject(receivedMessage.toString());

        assertEquals(payload.toString(), receivedObject.toString());
    }

    @Test
    public void testOrgJsonObject_Get_Receive() throws PubNubException {
        final String channel = random();

        final JSONObject payload = generatePayloadJSON();

        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addListener(new SubscribeCallback() {


            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                final JsonElement receivedMessage = pnMessageResult.getMessage();
                try {
                    final JSONObject receivedObject = new JSONObject(receivedMessage.toString());
                    assertEquals(payload.toString(), receivedObject.toString());
                    success.set(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(channel))
                .execute();

        pause(3);

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .sync();

        listen(success);
    }

    @Test
    public void testOrgJsonObject_Post_Receive() throws PubNubException {
        final String channel = random();
        final JSONObject payload = generatePayloadJSON();
        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addListener(new SubscribeCallback() {


            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                final JsonElement receivedMessage = pnMessageResult.getMessage();
                try {
                    final JSONObject receivedObject = new JSONObject(receivedMessage.toString());
                    assertEquals(payload.toString(), receivedObject.toString());
                    success.set(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(channel))
                .execute();

        pause(3);

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .usePOST(true)
                .sync();

        listen(success);
    }

    @Test
    public void testOrgJsonArray_Get_History() throws PubNubException, JSONException {
        final String channel = random();

        final JSONArray payload = new JSONArray();
        for (int i = 0; i < 3; i++) {
            payload.put(generatePayloadJSON());
        }

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .sync();

        pause(3);

        final PNHistoryResult historyResult = pubNub.history()
                .channel(channel)
                .count(1)
                .sync();

        assert historyResult != null;
        final JsonElement receivedMessage = historyResult.getMessages().get(0).getEntry();

        final JSONArray receivedArray = new JSONArray(receivedMessage.toString());

        assertEquals(payload.toString(), receivedArray.toString());
    }

    @Test
    public void testOrgJsonArray_Post_History() throws PubNubException, JSONException {
        final String channel = random();

        final JSONArray payload = new JSONArray();
        for (int i = 0; i < 3; i++) {
            payload.put(generatePayloadJSON());
        }

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .usePOST(true)
                .sync();

        pause(3);

        final PNHistoryResult historyResult = pubNub.history()
                .channel(channel)
                .count(1)
                .sync();
        assert historyResult != null;
        final JsonElement receivedMessage = historyResult.getMessages().get(0).getEntry();

        final JSONArray receivedArray = new JSONArray(receivedMessage.toString());

        assertEquals(payload.toString(), receivedArray.toString());
    }

    @Test
    public void testOrgJsonArray_Get_Receive() throws PubNubException {
        final String channel = random();

        final JSONArray payload = new JSONArray();
        for (int i = 0; i < 3; i++) {
            payload.put(generatePayloadJSON());
        }

        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addListener(new SubscribeCallback() {


            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                final JsonElement receivedMessage = pnMessageResult.getMessage();
                try {
                    final JSONArray receivedArray = new JSONArray(receivedMessage.toString());
                    assertEquals(payload.toString(), receivedArray.toString());
                    success.set(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(channel))
                .execute();

        pause(3);

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .sync();

        listen(success);
    }

    @Test
    public void testOrgJsonArray_Post_Receive() throws PubNubException {
        final String channel = random();

        final JSONArray payload = new JSONArray();
        for (int i = 0; i < 3; i++) {
            payload.put(generatePayloadJSON());
        }

        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addListener(new SubscribeCallback() {


            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                final JsonElement receivedMessage = pnMessageResult.getMessage();
                try {
                    final JSONArray receivedArray = new JSONArray(receivedMessage.toString());
                    assertEquals(payload.toString(), receivedArray.toString());
                    success.set(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(channel))
                .execute();

        pause(3);

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .usePOST(true)
                .sync();

        listen(success);
    }

    @Test
    public void testOrgJson_Combo() throws PubNubException, JSONException {
        final String channel = random();

        final JSONObject payload = new JSONObject();
        payload.put("key_1", generatePayloadJSON());
        payload.put("key_2", generatePayloadJSON());
        payload.put("z_1", new JSONObject(payload.toString()));
        payload.put("a_2", new JSONObject(payload.toString()));
        payload.put("d_3", new JSONObject(payload.toString()));

        final JSONArray jsonArray = new JSONArray();
        jsonArray.put(generatePayloadJSON());
        jsonArray.put(generatePayloadJSON());
        jsonArray.put(generatePayloadJSON());

        payload.put("z_array", jsonArray);

        final AtomicInteger count = new AtomicInteger();

        pubNub.addListener(new SubscribeCallback() {


            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                final JsonElement receivedMessage = pnMessageResult.getMessage();
                try {
                    final JSONObject receivedObject = new JSONObject(receivedMessage.toString());
                    assertEquals(payload.toString(), receivedObject.toString());
                    count.incrementAndGet();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(channel))
                .execute();

        pause(3);

        pubNub.publish()
                .message(payload)
                .channel(channel)
                .usePOST(true)
                .sync();

        pause(3);

        final PNFetchMessagesResult historyResult = pubNub.fetchMessages()
                .channels(Collections.singletonList(channel))
                .maximumPerChannel(1)
                .sync();

        assert historyResult != null;
        final JsonElement receivedMessage = historyResult.getChannels().get(channel).get(0).getMessage();

        final JSONObject receivedObject = new JSONObject(receivedMessage.toString());
        assertEquals(payload.toString(), receivedObject.toString());
        count.incrementAndGet();

        Awaitility.await()
                .atMost(Durations.FIVE_SECONDS)
                .with()
                .untilAtomic(count, IsEqual.equalTo(2));
    }
}
