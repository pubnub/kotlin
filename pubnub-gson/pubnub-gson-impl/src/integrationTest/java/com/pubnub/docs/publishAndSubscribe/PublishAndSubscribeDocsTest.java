package com.pubnub.docs.publishAndSubscribe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.files.PNDownloadableFile;
import com.pubnub.api.models.consumer.history.PNFetchMessageItem;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.v2.subscriptions.SubscriptionCursor;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.JavaWait;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page publish-and-subscribe (docs/sdks/java/api-reference/publish-and-subscribe.md). One test
 * per snippet. The text between a start marker and {@code snippet.end} is the published sample, the arrangement is above it
 * and the assertions below it. The DataSync listener samples of the page live with the DataSync tests.
 *
 * <p>The sample's own client is a Java client ({@code com.pubnub.api.java.PubNub}), the second clients of a test are Kotlin
 * clients ({@code com.pubnub.api.PubNub}), so the Kotlin waits of {@code Kit} work on them and {@code JavaWait} on the
 * sample's client.
 */
@ExtendWith(LiveExtension.class)
class PublishAndSubscribeDocsTest {
    private static final String OPEN = "open";

    // The announce max of the presence rule of the open keyset: above it presence switches to interval events.
    private static final int ANNOUNCE_MAX = 20;

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    private static void assertNothingPrinted(Stdout out, String part) {
        assertTrue(out.lines(part).isEmpty(), "a line with the text was printed that must not appear");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Publish
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void publishMain(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "lat");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.publishAndSubscribe.samples.PublishApp.main(new String[0]);

        PNMessageResult message = (PNMessageResult) arrived.await();
        JsonObject body = message.getMessage().getAsJsonObject();
        assertEquals(32L, body.get("lat").getAsLong());
        assertEquals(32L, body.get("lng").getAsLong());
        assertEquals("demoUserId", message.getPublisher());
        assertEquals("text-message", message.getCustomMessageType());
        Kit.assertTimetoken(message.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("Publish timetoken: " + message.getTimetoken()));
        Kit.storedItem(receiver, "myChannel", message.getTimetoken());
    }

    @Test
    void publishWithMetadata(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "hello");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");

        // origin: pubnub-gson/pubnub-gson-docs/src/main/java/com/pubnub/docs/publishAndSubscribe/publish/PublishOthers.java#publishWithMetadata
        // snippet.publishWithMetadata
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        channel.publish(Arrays.asList("hello", "there"))
                .customMessageType("text-message")
                .shouldStore(true)
                .meta("meta") // optional meta data object which can be used with the filtering ability.
                .usePOST(true)
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("Message published with timetoken: " + publishResult.getTimetoken());
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("Error publishing message: " + exception.getMessage());
                    });
                });
        // snippet.end
        clients.track(pubnub);

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(Arrays.asList("hello", "there"), Arrays.asList(
                message.getMessage().getAsJsonArray().get(0).getAsString(), message.getMessage().getAsJsonArray().get(1).getAsString()));
        assertEquals("yourUserId", message.getPublisher());
        assertEquals("text-message", message.getCustomMessageType());
        assertEquals("\"meta\"", message.getUserMetadata().toString());
        Kit.assertTimetoken(message.getTimetoken());
        PNFetchMessageItem stored = Kit.storedItem(receiver, "myChannel", message.getTimetoken());
        assertEquals("\"meta\"", stored.getMeta().toString());
        assertEquals("text-message", stored.getCustomMessageType());
    }

    @Test
    void publishJsonObject(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "lat");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        // snippet.publishJsonObject
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JsonObject position = new JsonObject();
        position.addProperty("lat", 32L);
        position.addProperty("lng", 32L);

        System.out.println("position: " + position);

        PNPublishResult publishResult = channel.publish(position)
                .customMessageType("text-message")
                .sync();

        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
        // snippet.end
        clients.track(pubnub);

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(32L, message.getMessage().getAsJsonObject().get("lat").getAsLong());
        assertEquals(32L, message.getMessage().getAsJsonObject().get("lng").getAsLong());
        assertEquals(publishResult.getTimetoken(), message.getTimetoken());
        assertEquals("yourUserId", message.getPublisher());
        assertEquals("text-message", message.getCustomMessageType());
        Kit.assertTimetoken(publishResult.getTimetoken());
        out.assertPrinted("position: {\"lat\":32,\"lng\":32}");
        out.assertPrinted("PubNub timetoken: " + publishResult.getTimetoken());
        Kit.storedItem(receiver, "myChannel", publishResult.getTimetoken());
    }

    @Test
    void publishJsonArray(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "[32]");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        // snippet.publishJsonArray
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JsonArray position = new JsonArray();
        position.add(32L);

        System.out.println("before pub: " + position);

        channel.publish(position)
                .customMessageType("text-message")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
        clients.track(pubnub);

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(1, message.getMessage().getAsJsonArray().size());
        assertEquals(32L, message.getMessage().getAsJsonArray().get(0).getAsLong());
        assertEquals("text-message", message.getCustomMessageType());
        assertEquals("yourUserId", message.getPublisher());
        Kit.assertTimetoken(message.getTimetoken());
        out.assertPrinted("before pub: [32]");
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("PubNub timetoken: " + message.getTimetoken()));
        Kit.storedItem(receiver, "myChannel", message.getTimetoken());
    }

    @Test
    void publishJsonObjectOrgJson(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "lat");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        // origin: pubnub-gson/pubnub-gson-docs/src/main/java/com/pubnub/docs/publishAndSubscribe/publish/PublishOthers.java#publishJsonObjectOrgJson
        // snippet.publishJsonObjectOrgJson
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JSONObject position = new JSONObject();
        position.put("lat", 32L);
        position.put("lng", 32L);

        System.out.println("before pub: " + position);
        PNPublishResult publishResult = channel.publish(position.toMap())
                .customMessageType("text-message")
                .sync();

        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
        // snippet.end
        clients.track(pubnub);

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(32L, message.getMessage().getAsJsonObject().get("lat").getAsLong());
        assertEquals(32L, message.getMessage().getAsJsonObject().get("lng").getAsLong());
        assertEquals(publishResult.getTimetoken(), message.getTimetoken());
        assertEquals("text-message", message.getCustomMessageType());
        Kit.assertTimetoken(publishResult.getTimetoken());
        out.assertPrinted("PubNub timetoken: " + publishResult.getTimetoken());
        Kit.storedItem(receiver, "myChannel", publishResult.getTimetoken());
    }

    @Test
    void publishJsonArrayOrgJson(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "[32]");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        // origin: pubnub-gson/pubnub-gson-docs/src/main/java/com/pubnub/docs/publishAndSubscribe/publish/PublishOthers.java#publishJsonArrayOrgJson
        // snippet.publishJsonArrayOrgJson
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JSONArray position = new JSONArray();
        position.put(32L);

        System.out.println("before pub: " + position);

        channel.publish(position.toList())
                .customMessageType("text-message")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
        clients.track(pubnub);

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(1, message.getMessage().getAsJsonArray().size());
        assertEquals(32L, message.getMessage().getAsJsonArray().get(0).getAsLong());
        assertEquals("text-message", message.getCustomMessageType());
        Kit.assertTimetoken(message.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("PubNub timetoken: " + message.getTimetoken()));
        Kit.storedItem(receiver, "myChannel", message.getTimetoken());
    }

    @Test
    void publishWithTTL(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "test-message");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        // snippet.publishWithTTL
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        PNPublishResult publishResult = channel.publish("test-message")
                .customMessageType("text-message")
                .shouldStore(true)
                .ttl(10)
                .sync();

        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
        // snippet.end
        clients.track(pubnub);

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals("test-message", message.getMessage().getAsString());
        assertEquals(publishResult.getTimetoken(), message.getTimetoken());
        assertEquals("text-message", message.getCustomMessageType());
        Kit.assertTimetoken(publishResult.getTimetoken());
        out.assertPrinted("PubNub timetoken: " + publishResult.getTimetoken());
        PNFetchMessageItem stored = Kit.storedItem(receiver, "myChannel", publishResult.getTimetoken());
        assertEquals("test-message", stored.getMessage().getAsString());
    }

    @Test
    void publishOld(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "my_channel", "lat");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Cleanup.record("history", "my_channel");
        Stdout out = Stdout.captureForTest();

        // snippet.publishOld
        JsonObject position = new JsonObject();
        position.addProperty("lat", 32L);
        position.addProperty("lng", 32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position)
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end

        PNPublishResult published = (PNPublishResult) recorder.awaitOk("publish");
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(32L, message.getMessage().getAsJsonObject().get("lat").getAsLong());
        assertEquals(published.getTimetoken(), message.getTimetoken());
        assertEquals("yourUserId", message.getPublisher());
        Kit.assertTimetoken(published.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("pub timetoken: " + published.getTimetoken()));
        Kit.storedItem(receiver, "my_channel", published.getTimetoken());
    }

    @Test
    void publishOldWithMetadata(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "suchChannel", "hello");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("suchChannel"));
        Cleanup.record("history", "suchChannel");

        // snippet.publishOldWithMetadata
        PNPublishResult pnPublishResult = pubNub.publish()
                .message(Arrays.asList("hello", "there"))
                .channel("suchChannel")
                .shouldStore(true)
                .meta("meta") // optional meta data object which can be used with the filtering ability.
                .usePOST(true)
                .sync();
        // snippet.end

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals("there", message.getMessage().getAsJsonArray().get(1).getAsString());
        assertEquals(pnPublishResult.getTimetoken(), message.getTimetoken());
        assertEquals("\"meta\"", message.getUserMetadata().toString());
        Kit.assertTimetoken(pnPublishResult.getTimetoken());
        assertEquals(pnPublishResult.getTimetoken(), ((PNPublishResult) recorder.awaitOk("publish")).getTimetoken());
        PNFetchMessageItem stored = Kit.storedItem(receiver, "suchChannel", pnPublishResult.getTimetoken());
        assertEquals("\"meta\"", stored.getMeta().toString());
    }

    @Test
    void publishJsonObjectGson(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "my_channel", "lat");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Cleanup.record("history", "my_channel");
        Stdout out = Stdout.captureForTest();

        // snippet.publishJsonObjectGson
        JsonObject position = new JsonObject();
        position.addProperty("lat", 32L);
        position.addProperty("lng", 32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position)
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end

        PNPublishResult published = (PNPublishResult) recorder.awaitOk("publish");
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(32L, message.getMessage().getAsJsonObject().get("lng").getAsLong());
        assertEquals(published.getTimetoken(), message.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("pub timetoken: " + published.getTimetoken()));
        Kit.storedItem(receiver, "my_channel", published.getTimetoken());
    }

    @Test
    void publishJsonArrayGson(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "my_channel", "[32]");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Cleanup.record("history", "my_channel");
        Stdout out = Stdout.captureForTest();

        // snippet.publishJsonArrayGson
        JsonArray position = new JsonArray();
        position.add(32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position)
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end

        PNPublishResult published = (PNPublishResult) recorder.awaitOk("publish");
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(32L, message.getMessage().getAsJsonArray().get(0).getAsLong());
        assertEquals(published.getTimetoken(), message.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("pub timetoken: " + published.getTimetoken()));
        Kit.storedItem(receiver, "my_channel", published.getTimetoken());
    }

    @Test
    void publishJsonObjectOrgJsonOld(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "my_channel", "lat");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Cleanup.record("history", "my_channel");
        Stdout out = Stdout.captureForTest();

        // origin: pubnub-gson/pubnub-gson-docs/src/main/java/com/pubnub/docs/publishAndSubscribe/publish/PublishOld.java#publishJsonObjectOrgJson
        // snippet.publishJsonObjectOrgJsonOld
        JSONObject position = new JSONObject();
        position.put("lat", 32L);
        position.put("lng", 32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position.toMap())
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end

        PNPublishResult published = (PNPublishResult) recorder.awaitOk("publish");
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(32L, message.getMessage().getAsJsonObject().get("lat").getAsLong());
        assertEquals(published.getTimetoken(), message.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("pub timetoken: " + published.getTimetoken()));
        Kit.storedItem(receiver, "my_channel", published.getTimetoken());
    }

    @Test
    void publishJsonArrayOrgJsonOld(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "my_channel", "[32]");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Cleanup.record("history", "my_channel");
        Stdout out = Stdout.captureForTest();

        // origin: pubnub-gson/pubnub-gson-docs/src/main/java/com/pubnub/docs/publishAndSubscribe/publish/PublishOld.java#publishJsonArrayOrgJson
        // snippet.publishJsonArrayOrgJsonOld
        org.json.JSONArray position = new org.json.JSONArray();
        position.put(32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position.toList())
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end

        PNPublishResult published = (PNPublishResult) recorder.awaitOk("publish");
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(32L, message.getMessage().getAsJsonArray().get(0).getAsLong());
        assertEquals(published.getTimetoken(), message.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("pub timetoken: " + published.getTimetoken()));
        Kit.storedItem(receiver, "my_channel", published.getTimetoken());
    }

    @Test
    void publishMessagesFor10Hours(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "coolChannel", "test");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("coolChannel"));
        Cleanup.record("history", "coolChannel");

        // snippet.publishMessagesFor10Hours
        PNPublishResult result = pubNub.publish()
                .channel("coolChannel")
                .message("test")
                .shouldStore(true)
                .ttl(10)
                .sync();
        // snippet.end

        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals("test", message.getMessage().getAsString());
        assertEquals(result.getTimetoken(), message.getTimetoken());
        Kit.assertTimetoken(result.getTimetoken());
        Kit.storedItem(receiver, "coolChannel", result.getTimetoken());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Fire and signal
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void fireMessage(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", "hello");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");
        Stdout out = Stdout.captureForTest();

        // snippet.fireMessage
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        channel.fire(Arrays.asList("hello", "there"))
                .usePOST(true)
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("publish worked! timetoken: " + publishResult.getTimetoken());
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("error happened while publishing: " + exception.toString());
                    });
                });
        // snippet.end
        clients.track(pubnub);

        // A fired message goes to Functions only: no subscriber gets it and history does not keep it. A normal publish
        // that arrives afterwards proves that the channel delivers.
        Wait.eventuallyRun("the sample reported the fire", () -> out.assertPrinted("publish worked! timetoken: "));
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String controlText = Kit.probe("fire-control");
        Wait.Expectation control = Kit.armMessage(receiver, "myChannel", controlText);
        Kit.publishFrom(friend, "myChannel", controlText, false);
        control.await();
        Kit.assertNothingArrives(arrived, "a fired message reached a subscriber", 3000);
        Kit.assertNoStoredText(receiver, "myChannel", "there");
    }

    @Test
    void fireMessageToChannel(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, "my_channel", "hello");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Cleanup.record("history", "my_channel");
        Stdout out = Stdout.captureForTest();

        // snippet.fireMessageToChannel
        pubNub.fire()
                .message(Arrays.asList("hello", "there"))
                .channel("my_channel")
                .usePOST(true)
                .async(result -> {
                    result.onSuccess((PNPublishResult fireResult) -> {
                        System.out.println("publish worked! timetoken: " + fireResult.getTimetoken());
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("error happened while calling fire: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNPublishResult fired = (PNPublishResult) recorder.awaitOk("fire");
        Kit.assertTimetoken(fired.getTimetoken());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("publish worked! timetoken: " + fired.getTimetoken()));
        // A fired message goes to Functions only: no subscriber gets it and history does not keep it. A normal publish
        // that arrives afterwards proves that the channel delivers.
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String controlText = Kit.probe("fire-control");
        Wait.Expectation control = Kit.armMessage(receiver, "my_channel", controlText);
        Kit.publishFrom(friend, "my_channel", controlText, false);
        control.await();
        Kit.assertNothingArrives(arrived, "a fired message reached a subscriber", 3000);
        Kit.assertNotStored(receiver, "my_channel", fired.getTimetoken());
    }

    @Test
    void signalMessage(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armSignal(receiver, "myChannel", "Hello everyone!");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");

        // snippet.signalMessage
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        channel.signal("Hello everyone!")
                .customMessageType("text-message")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        Long timetoken = publishResult.getTimetoken(); // signal message timetoken
                    }).onFailure( (PubNubException exception) -> {
                        exception.printStackTrace();
                    });
                });
        // snippet.end
        clients.track(pubnub);

        PNSignalResult signal = (PNSignalResult) arrived.await();
        assertEquals("Hello everyone!", signal.getMessage().getAsString());
        assertEquals("yourUserId", signal.getPublisher());
        assertEquals("text-message", signal.getCustomMessageType());
        Kit.assertTimetoken(signal.getTimetoken());
        Kit.assertNotStored(receiver, "myChannel", signal.getTimetoken());
    }

    @Test
    void signalMessageToChannel(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-receiver");
        Wait.Expectation arrived = Kit.armSignal(receiver, "bar", "Hello everyone!");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("bar"));
        Cleanup.record("history", "bar");

        // snippet.signalMessageToChannel
        pubNub.signal()
                .message("Hello everyone!")
                .channel("bar")
                .async(result -> {
                    result.onSuccess((PNPublishResult signalResult) -> {
                        Long timetoken = signalResult.getTimetoken(); // signal message timetoken
                    }).onFailure((PubNubException exception) -> {
                        System.out.println("error happened while calling signal: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNPublishResult sent = (PNPublishResult) recorder.awaitOk("signal");
        PNSignalResult signal = (PNSignalResult) arrived.await();
        assertEquals("Hello everyone!", signal.getMessage().getAsString());
        assertEquals("yourUserId", signal.getPublisher());
        assertEquals(sent.getTimetoken(), signal.getTimetoken());
        Kit.assertNotStored(receiver, "bar", sent.getTimetoken());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Subscribe
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void subscribeToChannelOld(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String text = Kit.probe("subscribe");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("my_channel"), Collections.emptyList());
        JavaWait.Expectation arrived = JavaWait.message(client, "my_channel", text);

        // origin: pubnub-gson/pubnub-gson-docs/src/main/java/com/pubnub/docs/publishAndSubscribe/subscribe/SubscribeOld.java#subscribeToChannel
        // snippet.subscribeToChannelOld
        pubNub.subscribe()
                .channels(Arrays.asList("my_channel"))
                .execute();
        // snippet.end

        PNStatus status = (PNStatus) connected.await();
        assertEquals(PNStatusCategory.PNConnectedCategory, status.getCategory());
        assertEquals(Collections.singletonList("my_channel"), client.getSubscribedChannels());
        PNPublishResult sent = Kit.publishFrom(friend, "my_channel", text, false);
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(text, message.getMessage().getAsString());
        assertEquals("docs-joiner", message.getPublisher());
        assertEquals(sent.getTimetoken(), message.getTimetoken());
        Kit.assertTimetoken(message.getTimetoken());
    }

    @Test
    void subscribeWithLogging(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub watcher = kotlinClient(clients, "docs-receiver");
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String text = Kit.probe("subscribe-with-logging");
        Kit.awaitNoOccupants(watcher, "my_channel");
        Kit.subscribeAndConnect(watcher, Collections.singletonList("my_channel"), Collections.emptyList(), true);
        Wait.Expectation joined = Kit.armPresence(watcher, "join", "yourUserId", "my_channel");

        // snippet.subscribeWithLogging
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);

        pubnub.subscribe()
                .channels(Arrays.asList("my_channel")) // subscribe to channels information
                .execute();
        // snippet.end
        clients.track(pubnub);

        PNPresenceEventResult join = (PNPresenceEventResult) joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        assertEquals("yourUserId", join.getUuid());
        assertEquals(Collections.singletonList("my_channel"), pubnub.getSubscribedChannels());
        JavaWait.Expectation arrived = JavaWait.message(pubnub, "my_channel", text);
        PNPublishResult sent = Kit.publishFrom(friend, "my_channel", text, false);
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(sent.getTimetoken(), message.getTimetoken());
        assertEquals("docs-joiner", message.getPublisher());
    }

    @Test
    void subscribeToPresenceChannel(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.awaitNoOccupants(friend, "my_channel");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("my_channel"), Collections.emptyList());
        JavaWait.Expectation joined = JavaWait.presence(client, "join", "docs-joiner", "my_channel");

        // snippet.subscribeToPresenceChannel
        pubNub.subscribe()
                .channels(Arrays.asList("my_channel")) // subscribe to channels
                .withPresence() // also subscribe to related presence information
                .execute();
        // snippet.end

        connected.await();
        Kit.subscribeAndConnect(friend, Collections.singletonList("my_channel"));
        PNPresenceEventResult join = (PNPresenceEventResult) joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        assertEquals("my_channel", join.getChannel());
        assertEquals(2, join.getOccupancy());
        assertNotNull(join.getTimestamp());
    }

    @Test
    void presenceEventsJoin(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.awaitNoOccupants(friend, "my_channel");
        subscribeWithPresence(client, "my_channel");
        JavaWait.Expectation joined = JavaWait.presence(client, "join", "docs-joiner", "my_channel");

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

        Kit.subscribeAndConnect(friend, Collections.singletonList("my_channel"));
        PNPresenceEventResult join = (PNPresenceEventResult) joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        assertEquals("docs-joiner", join.getUuid());
        assertEquals(2, join.getOccupancy());
        assertNotNull(join.getTimestamp());
        Kit.assertTimetoken(join.getTimetoken());
    }

    @Test
    void presenceEventsLeave(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.awaitNoOccupants(friend, "my_channel");
        subscribeWithPresence(client, "my_channel");
        JavaWait.Expectation joined = JavaWait.presence(client, "join", "docs-joiner", "my_channel");
        JavaWait.Expectation left = JavaWait.presence(client, "leave", "docs-joiner", "my_channel");

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

        Kit.subscribeAndConnect(friend, Collections.singletonList("my_channel"));
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        friend.unsubscribe(Collections.singletonList("my_channel"), Collections.emptyList());
        PNPresenceEventResult leave = (PNPresenceEventResult) left.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        assertEquals("docs-joiner", leave.getUuid());
        assertEquals(1, leave.getOccupancy());
        assertNotNull(leave.getTimestamp());
    }

    @Test
    void presenceEventsTimeout(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub lost = clients.kotlinClient(OPEN, "docs-joiner", (Consumer<com.pubnub.api.v2.PNConfiguration.Builder>) builder -> {
            builder.setPresenceTimeout(20);
            builder.setSuppressLeaveEvents(true);
        });
        Kit.awaitNoOccupants(lost, "my_channel");
        subscribeWithPresence(client, "my_channel");
        JavaWait.Expectation joined = JavaWait.presence(client, "join", "docs-joiner", "my_channel");
        JavaWait.Expectation timedOut = JavaWait.presence(client, "timeout", "docs-joiner", "my_channel");

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

        Kit.subscribeAndConnect(lost, Collections.singletonList("my_channel"));
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        // The joiner stops without a leave, so only the presence timeout of 20 seconds removes it.
        lost.destroy();
        PNPresenceEventResult timeoutEvent = (PNPresenceEventResult) timedOut.await(Wait.PRESENCE_TIMEOUT_MS);
        assertEquals("docs-joiner", timeoutEvent.getUuid());
        assertEquals(1, timeoutEvent.getOccupancy());
        assertNotNull(timeoutEvent.getTimestamp());
    }

    @Test
    void presenceEventsStateChange(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.awaitNoOccupants(friend, "my_channel");
        subscribeWithPresence(client, "my_channel");
        JavaWait.Expectation joined = JavaWait.presence(client, "join", "docs-joiner", "my_channel");
        JavaWait.Expectation changed = JavaWait.presence(client, "state-change", "docs-joiner", "my_channel");

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

        Kit.subscribeAndConnect(friend, Collections.singletonList("my_channel"));
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        friend.setPresenceState(Collections.singletonList("my_channel"), Collections.emptyList(), Collections.singletonMap("isTyping", true)).sync();
        PNPresenceEventResult change = (PNPresenceEventResult) changed.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        assertEquals("docs-joiner", change.getUuid());
        assertEquals("{\"isTyping\":true}", change.getState().toString());
        assertNotNull(change.getTimestamp());
    }

    @Test
    void presenceEventsInterval(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.awaitNoOccupants(friend, "my_channel");
        subscribeWithPresence(client, "my_channel");
        JavaWait.Expectation interval = JavaWait.presenceEvent(client, "interval", "my_channel");

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

        Kit.joinCrowd(clients, "my_channel", ANNOUNCE_MAX);
        PNPresenceEventResult event = (PNPresenceEventResult) interval.await(Wait.PRESENCE_TIMEOUT_MS);
        assertNotNull(event.getTimestamp());
        assertTrue(event.getOccupancy() > ANNOUNCE_MAX, "the interval event does not report the crowd");
        assertNotNull(event.getHereNowRefresh(), "the interval event has no hereNowRefresh");
    }

    @Test
    void subscribeWithState(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.createGroup(friend, "awesomeChannelGroup", "awesomeChannel");
        Kit.awaitNoOccupants(friend, "awesomeChannel");

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

        // DOCS FINDING: the sample sends the state of a local class and Gson skips local classes, so the state that reaches
        // the service is an empty object and fieldA and fieldB are lost. The test states what the service holds.
        Kit.eventually("the state of yourUserId is set on awesomeChannel", () -> {
            JsonElement state = friend.getPresenceState(Collections.singletonList("awesomeChannel"), Collections.emptyList(), "yourUserId")
                    .sync().getStateByUUID().get("awesomeChannel");
            assertEquals("{}", String.valueOf(state));
        });
    }

    @Test
    void subscribeToChannelGroup(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.createGroup(friend, "cg1", "ch3");
        Kit.createGroup(friend, "cg2", "bar");
        String text = Kit.probe("group");
        JavaWait.Expectation connected = JavaWait.connected(client, Arrays.asList("ch1", "ch2"), Arrays.asList("cg1", "cg2"));
        JavaWait.Expectation arrived = JavaWait.message(client, "ch3", text);

        // snippet.subscribeToChannelGroup
        pubNub.subscribe()
                .channels(Arrays.asList("ch1", "ch2")) // subscribe to channels
                .channelGroups(Arrays.asList("cg1", "cg2")) // subscribe to channel groups
                .withTimetoken(1337L) // optional, pass a timetoken
                .withPresence() // also subscribe to related presence information
                .execute();
        // snippet.end

        connected.await();
        assertEquals(new HashSet<>(Arrays.asList("ch1", "ch2")), new HashSet<>(client.getSubscribedChannels()));
        assertEquals(new HashSet<>(Arrays.asList("cg1", "cg2")), new HashSet<>(client.getSubscribedChannelGroups()));
        Kit.publishFrom(friend, "ch3", text, false);
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals("ch3", message.getChannel());
        assertEquals("cg1", message.getSubscription());
    }

    @Test
    void subscribeToPresenceChannelOfChannelGroup(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.createGroup(friend, "cg1", "ch3");
        Kit.createGroup(friend, "cg2", "bar");
        Kit.awaitNoOccupants(friend, "ch3", "bar");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.emptyList(), Arrays.asList("cg1", "cg2"));
        JavaWait.Expectation joined = JavaWait.presence(client, "join", "docs-joiner", "ch3");

        // snippet.subscribeToPresenceChannelOfChannelGroup
        pubNub.subscribe()
                .channelGroups(Arrays.asList("cg1", "cg2")) // subscribe to channel groups
                .withTimetoken(1337L) // optional, pass a timetoken
                .withPresence() // also subscribe to related presence information
                .execute();
        // snippet.end

        connected.await();
        assertEquals(new HashSet<>(Arrays.asList("cg1", "cg2")), new HashSet<>(client.getSubscribedChannelGroups()));
        Kit.subscribeAndConnect(friend, Collections.singletonList("ch3"));
        PNPresenceEventResult join = (PNPresenceEventResult) joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS);
        assertEquals("ch3", join.getChannel());
        assertEquals("cg1", join.getSubscription());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Subscriptions and subscription sets
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void createSubscription(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.awaitNoOccupants(friend, "channelName");
        String text = Kit.probe("create-subscription");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("channelName"), Collections.emptyList());
        JavaWait.Expectation joined = JavaWait.presence(client, "join", "docs-joiner", "channelName");
        JavaWait.Expectation arrived = JavaWait.message(client, "channelName", text);

        // snippet.createSubscription
        // Entity-based, local-scoped

        // Specify the channel for subscription
        Channel myChannel = pubnub.channel("channelName");

        // Create subscription options, if any
        SubscriptionOptions options = SubscriptionOptions.receivePresenceEvents();

        // Return a Subscription object that is used to establish the subscription
        Subscription subscription = myChannel.subscription(options);

        // Activate the subscription to start receiving events
        subscription.subscribe();
        // snippet.end

        connected.await();
        assertEquals(Collections.singletonList("channelName"), client.getSubscribedChannels());
        Kit.subscribeAndConnect(friend, Collections.singletonList("channelName"));
        assertEquals("docs-joiner", ((PNPresenceEventResult) joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)).getUuid());
        Kit.publishFrom(friend, "channelName", text, false);
        assertEquals(text, ((PNMessageResult) arrived.await()).getMessage().getAsString());
    }

    @Test
    void subscribe(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String text = Kit.probe("subscribe-method");
        Channel myChannel = pubNub.channel("channelName");
        Subscription subscription = myChannel.subscription();
        Subscription mySubscription01 = pubNub.channel("my_channel01").subscription();
        Subscription mySubscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(mySubscription01);
        subscriptions.add(mySubscription02);
        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(subscriptions);
        JavaWait.Expectation connected =
                JavaWait.connected(client, Arrays.asList("channelName", "my_channel01", "my_channel02"), Collections.emptyList());
        JavaWait.Expectation arrivedSingle = JavaWait.message(client, "channelName", text);
        JavaWait.Expectation arrivedSet = JavaWait.message(client, "my_channel02", text);

        // snippet.subscribe
        // For subscription
        subscription.subscribe();
        // For subscription set
        subscriptionSet.subscribe();
        // snippet.end

        connected.await();
        assertEquals(new HashSet<>(Arrays.asList("channelName", "my_channel01", "my_channel02")), new HashSet<>(client.getSubscribedChannels()));
        Kit.publishFrom(friend, "channelName", text, false);
        Kit.publishFrom(friend, "my_channel02", text, false);
        assertEquals("channelName", ((PNMessageResult) arrivedSingle.await()).getChannel());
        assertEquals("my_channel02", ((PNMessageResult) arrivedSet.await()).getChannel());
    }

    @Test
    void subscribeSubscriptionSet(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String textOne = Kit.probe("set-one");
        String textTwo = Kit.probe("set-two");
        JavaWait.Expectation connected = JavaWait.connected(client, Arrays.asList("my_channel01", "my_channel02"), Collections.emptyList());
        JavaWait.Expectation arrivedOne = JavaWait.message(client, "my_channel01", textOne);
        JavaWait.Expectation arrivedTwo = JavaWait.message(client, "my_channel02", textTwo);

        // snippet.subscribeSubscriptionSet
        // Step 1: Create a subscription set
        Subscription mySubscription01 = pubNub.channel("my_channel01").subscription();
        Subscription mySubscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(mySubscription01);
        subscriptions.add(mySubscription02);

        SubscriptionSet mySubscriptionSet = pubNub.subscriptionSetOf(subscriptions);

        // Step 2: Subscribe using the subscription set
        mySubscriptionSet.subscribe();
        // snippet.end

        connected.await();
        assertEquals(new HashSet<>(Arrays.asList("my_channel01", "my_channel02")), new HashSet<>(client.getSubscribedChannels()));
        Kit.publishFrom(friend, "my_channel01", textOne, false);
        Kit.publishFrom(friend, "my_channel02", textTwo, false);
        assertEquals("my_channel01", ((PNMessageResult) arrivedOne.await()).getChannel());
        assertEquals("my_channel02", ((PNMessageResult) arrivedTwo.await()).getChannel());
    }

    @Test
    void createSubscriptionSetFrom2subscriptions(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        Kit.createGroup(friend, "channelGroup", "ch3");
        String kept = Kit.probe("set-kept");
        String removed = Kit.probe("set-removed");
        String viaGroup = Kit.probe("set-group");

        // snippet.createSubscriptionSetFrom2subscriptions
        // Create subscriptions
        Subscription subscription1 = pubNub.channel("channelName").subscription();
        Subscription subscription2 = pubNub.channelGroup("channelGroup").subscription();
        Subscription subscription3 = pubNub.channel("channelName03").subscription();

        // Combine into a subscription set
        SubscriptionSet subscriptionSet = subscription1.plus(subscription2);

        // Add another subscription to the set
        subscriptionSet.add(subscription3);

        // Remove a subscription from the set
        subscriptionSet.remove(subscription3);
        // snippet.end

        assertEquals(2, subscriptionSet.getSubscriptions().size());
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("channelName"), Collections.singletonList("channelGroup"));
        JavaWait.Expectation arrivedKept = JavaWait.message(client, "channelName", kept);
        JavaWait.Expectation arrivedGroup = JavaWait.message(client, "ch3", viaGroup);
        JavaWait.Expectation arrivedRemoved = JavaWait.message(client, "channelName03", removed);
        subscriptionSet.subscribe();
        connected.await();
        Kit.publishFrom(friend, "channelName", kept, false);
        Kit.publishFrom(friend, "ch3", viaGroup, false);
        Kit.publishFrom(friend, "channelName03", removed, false);
        assertEquals(kept, ((PNMessageResult) arrivedKept.await()).getMessage().getAsString());
        assertEquals("channelGroup", ((PNMessageResult) arrivedGroup.await()).getSubscription());
        JavaWait.assertNothingArrives(arrivedRemoved, "a message of the removed subscription arrived", 3000);
    }

    @Test
    void subscribeWithTimetoken(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String text = Kit.probe("with-timetoken");
        JavaWait.Expectation connected = JavaWait.connected(client, Arrays.asList("my_channel01", "my_channel02"), Collections.emptyList());
        JavaWait.Expectation arrived = JavaWait.message(client, "my_channel02", text);

        // snippet.subscribeWithTimetoken
        // Create subscription options, if any
        SubscriptionOptions options = SubscriptionOptions.receivePresenceEvents();

        // Define the channels to subscribe to
        Subscription subscription01 = pubNub.channel("my_channel01").subscription(options);
        Subscription subscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(subscription01);
        subscriptions.add(subscription02);

        // Create a subscription set with specified channels and subscription options
        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(subscriptions);

        // Define the timetoken for where the subscription should start
        Long yourTimeToken = 100000000000L; // Directly using Long type

        // Subscribe to the created SubscriptionSet with the desired timetoken
        subscriptionSet.subscribe(new SubscriptionCursor(yourTimeToken));
        // snippet.end

        connected.await();
        assertEquals(new HashSet<>(Arrays.asList("my_channel01", "my_channel02")), new HashSet<>(client.getSubscribedChannels()));
        Kit.publishFrom(friend, "my_channel02", text, false);
        assertEquals(text, ((PNMessageResult) arrived.await()).getMessage().getAsString());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Event listeners
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void addEventListeners(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String text = Kit.probe("add-event-listeners");
        Cleanup.record("channel_metadata", "my_channel");
        Cleanup.record("uuid_metadata", "my_channel");
        Kit.awaitNoOccupants(friend, "my_channel");
        Stdout out = Stdout.captureForTest();
        JavaWait.Expectation connected =
                JavaWait.connected(client, Arrays.asList("my_channel", "my_channel02"), Collections.emptyList());

        // snippet.addEventListeners
        // Create a subscription to a specific channel
        Subscription subscription = pubNub.channel("my_channel").subscription(SubscriptionOptions.receivePresenceEvents());

        subscription.addListener(new EventListener() {
            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult result) {
                // Log or process message
                System.out.println("Message: " + result.getMessage());
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult result) {
                // Handle presence updates
                // requires a subscription with presence
                System.out.println("Presence userId: " + result.getUuid() + ", Event: " + result.getEvent());
            }

            @Override
            public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult result) {
                // Handle signals
                System.out.println("Signal: " + result.getMessage());
            }

            @Override
            public void messageAction(@NotNull PubNub pubNub, @NotNull PNMessageActionResult result) {
                // Handle message reactions
                System.out.println("Message Reaction: " + result.getData());
            }

            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult result) {
                // Handle file events
                System.out.println("File: " + result.getFile().getName());
            }

            @Override
            public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {
                // Handle uuid metadata events
                System.out.println("UUID: " + pnUUIDMetadataResult.getData().getName());
            }

            @Override
            public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) {
                // Handle channel metadata events
                System.out.println("Channel: " + pnChannelMetadataResult.getData().getName());
            }

            @Override
            public void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) {
                // Handle membership metadata events
                System.out.println("Channel: " + pnMembershipResult.getData().getChannel().getName() + " UUID: "
                        + pnMembershipResult.getData().getUuid());
            }
        });

        // Activate the subscription to start receiving events
        subscription.subscribe();

        // Print a status when successfully subscribed
        System.out.println("Subscribed to channel 'my_channel'");


        Subscription subscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptionsSet = new HashSet<>();
        subscriptionsSet.add(subscription);
        subscriptionsSet.add(subscription02);

        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(subscriptionsSet);

        // add global listener that listen on all subscribed channels
        pubNub.addListener(new EventListener() {
            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                System.out.println("Message: " + message.getMessage());
            }
        });

        // Activate the subscriptionSet to start receiving events
        subscriptionSet.subscribe();
        // snippet.end

        connected.await();
        out.assertPrinted("Subscribed to channel 'my_channel'");

        Kit.subscribeAndConnect(friend, Collections.singletonList("my_channel"));
        Wait.eventuallyRun("the subscription printed the join of the friend", () -> out.assertPrinted("Presence userId: docs-joiner, Event: join"));

        // The subscription, the set and the global listener all see my_channel, so a message prints once per listener.
        PNPublishResult sent = Kit.publishFrom(friend, "my_channel", text, true);
        Cleanup.record("history", "my_channel");
        Wait.eventuallyRun("the listeners printed the message", () ->
                assertEquals(2, out.lines("Message: \"" + text + "\"").size(), "the message was not printed once per listener"));

        // A signal payload is limited to 64 bytes, so it gets a short text.
        String signalText = "sig-" + UUID.randomUUID().toString().substring(0, 8);
        Kit.signalFrom(friend, "my_channel", signalText);
        Wait.eventuallyRun("the subscription printed the signal", () -> out.assertPrinted("Signal: \"" + signalText + "\""));

        Kit.retry("message action", () ->
                friend.addMessageAction("my_channel", new PNMessageAction("reaction", "smiley_face", sent.getTimetoken())).sync());
        Wait.eventuallyRun("the subscription printed the message action", () -> {
            // The sample prints the action object, which has no toString, so only its class name shows.
            out.assertPrinted("Message Reaction: com.pubnub.api.models.consumer.message_actions.PNMessageAction@");
        });

        Kit.retry("channel metadata", () ->
                friend.setChannelMetadata("my_channel", "Docs channel", null, null, false, null, null, null).sync());
        Wait.eventuallyRun("the subscription printed the channel metadata event", () -> out.assertPrinted("Channel: PatchValue(value=Docs channel)"));

        // User metadata events travel on the channel that has the name of the user, here a user called my_channel.
        Kit.retry("user metadata", () ->
                friend.setUUIDMetadata("my_channel", "Docs user", null, null, null, null, false, null, null, null).sync());
        Wait.eventuallyRun("the subscription printed the user metadata event", () -> out.assertPrinted("UUID: PatchValue(value=Docs user)"));

        String fileName = "docs-listener-" + UUID.randomUUID() + ".txt";
        com.pubnub.api.models.consumer.files.PNFileUploadResult upload = Kit.retry("send file", () ->
                friend.sendFile("my_channel", fileName, new ByteArrayInputStream("docs listener".getBytes()), null, null, null, null, null, null).sync());
        Kit.cleanup("delete the uploaded file", () -> friend.deleteFile("my_channel", fileName, upload.getFile().getId()).sync());
        Wait.eventuallyRun("the subscription printed the file event", () -> out.assertPrinted("File: " + fileName));
    }

    @Test
    void setEventListeners(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        String text = Kit.probe("set-event-listeners");
        Cleanup.record("channel_metadata", "my_channel");
        Cleanup.record("uuid_metadata", "my_channel");
        Subscription subscription = pubNub.channel("my_channel").subscription(SubscriptionOptions.receivePresenceEvents());

        // Handlers that the sample replaces. Each counts the event it is for, so that a handler of the sample that did not
        // replace it would leave the counter growing.
        AtomicInteger replaced = new AtomicInteger();
        subscription.setOnMessage(r -> replaced.incrementAndGet());
        subscription.setOnSignal(r -> replaced.incrementAndGet());
        subscription.setOnMessageAction(r -> replaced.incrementAndGet());
        subscription.setOnFile(r -> replaced.incrementAndGet());
        subscription.setOnUuidMetadata(r -> replaced.incrementAndGet());
        subscription.setOnChannelMetadata(r -> replaced.incrementAndGet());
        subscription.setOnMembership(r -> replaced.incrementAndGet());
        subscription.setOnPresence(r -> replaced.incrementAndGet());
        Kit.awaitNoOccupants(friend, "my_channel");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("my_channel"), Collections.emptyList());
        subscription.subscribe();
        connected.await();

        // snippet.setEventListeners
        subscription.setOnMessage((PNMessageResult pnMessageResult) -> {
            JsonElement message = pnMessageResult.getMessage();
            String channel1 = pnMessageResult.getChannel();
            String publisher = pnMessageResult.getPublisher();
            String subscription1 = pnMessageResult.getSubscription();
            String customMessageType = pnMessageResult.getCustomMessageType();
            Long timetoken = pnMessageResult.getTimetoken();
        });

        subscription.setOnSignal((PNSignalResult pnSignalResult) -> {
            JsonElement message = pnSignalResult.getMessage();
            String channel = pnSignalResult.getChannel();
            String publisher = pnSignalResult.getPublisher();
            String subscription1 = pnSignalResult.getSubscription();
            String customMessageType = pnSignalResult.getCustomMessageType();
            Long timetoken = pnSignalResult.getTimetoken();
        });

        subscription.setOnMessageAction((PNMessageActionResult pnMessageActionResult) -> {
            PNMessageAction messageAction = pnMessageActionResult.getMessageAction();
            String channel = pnMessageActionResult.getChannel();
            String event = pnMessageActionResult.getEvent();
            String publisher = pnMessageActionResult.getPublisher();
            String subscription1 = pnMessageActionResult.getSubscription();
            Long timetoken = pnMessageActionResult.getTimetoken();
        });

        subscription.setOnFile((PNFileEventResult pnFileEventResult) -> {
            JsonElement message = (JsonElement) pnFileEventResult.getMessage();
            PNDownloadableFile file = pnFileEventResult.getFile();
            String channel = pnFileEventResult.getChannel();
            String publisher = pnFileEventResult.getPublisher();
            String subscription1 = pnFileEventResult.getSubscription();
            String customMessageType = pnFileEventResult.getCustomMessageType();
            Long timetoken = pnFileEventResult.getTimetoken();
        });

        subscription.setOnUuidMetadata((PNUUIDMetadataResult pnUUIDMetadataResult) -> {
            String event = pnUUIDMetadataResult.getEvent();
            PNUUIDMetadata data = pnUUIDMetadataResult.getData();
            String channel = pnUUIDMetadataResult.getChannel();
            String publisher = pnUUIDMetadataResult.getPublisher();
            String subscription1 = pnUUIDMetadataResult.getSubscription();
            Long timetoken = pnUUIDMetadataResult.getTimetoken();
        });

        subscription.setOnChannelMetadata((PNChannelMetadataResult pnChannelMetadataResult) -> {
            String event = pnChannelMetadataResult.getEvent();
            PNChannelMetadata data = pnChannelMetadataResult.getData();
            String channel = pnChannelMetadataResult.getChannel();
            String publisher = pnChannelMetadataResult.getPublisher();
            String subscription1 = pnChannelMetadataResult.getSubscription();
            Long timetoken = pnChannelMetadataResult.getTimetoken();
        });

        subscription.setOnMembership((PNMembershipResult pnMembershipResult) -> {
            String event = pnMembershipResult.getEvent();
            PNMembership data = pnMembershipResult.getData();
            String channel = pnMembershipResult.getChannel();
            String publisher = pnMembershipResult.getPublisher();
            String subscription1 = pnMembershipResult.getSubscription();
            Long timetoken = pnMembershipResult.getTimetoken();
        });

        subscription.setOnPresence((PNPresenceEventResult pnPresenceEventResult) -> {
            String event = pnPresenceEventResult.getEvent();
            Integer occupancy = pnPresenceEventResult.getOccupancy();
            String channel = pnPresenceEventResult.getChannel();
            List<String> join = pnPresenceEventResult.getJoin();
            List<String> leave = pnPresenceEventResult.getLeave();
            JsonElement state = pnPresenceEventResult.getState();
            List<String> timeout = pnPresenceEventResult.getTimeout();
            String subscription1 = pnPresenceEventResult.getSubscription();
            Long timetoken = pnPresenceEventResult.getTimetoken();
        });
        // snippet.end

        // Every kind of event now arrives while the replaced handlers must stay silent. A listener added with addListener
        // is a control: it sees each event, so the silence is not a missing event.
        AtomicInteger seen = new AtomicInteger();
        subscription.addListener(new EventListener() {
            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult result) {
                seen.incrementAndGet();
            }

            @Override
            public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult result) {
                seen.incrementAndGet();
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult result) {
                seen.incrementAndGet();
            }

            @Override
            public void messageAction(@NotNull PubNub pubNub, @NotNull PNMessageActionResult result) {
                seen.incrementAndGet();
            }

            @Override
            public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult result) {
                seen.incrementAndGet();
            }

            @Override
            public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult result) {
                seen.incrementAndGet();
            }
        });
        Kit.subscribeAndConnect(friend, Collections.singletonList("my_channel"));
        PNPublishResult sent = Kit.publishFrom(friend, "my_channel", text, true);
        Cleanup.record("history", "my_channel");
        Kit.signalFrom(friend, "my_channel", "sig-" + UUID.randomUUID().toString().substring(0, 8));
        Kit.retry("message action", () ->
                friend.addMessageAction("my_channel", new PNMessageAction("reaction", "smiley_face", sent.getTimetoken())).sync());
        Kit.retry("channel metadata", () ->
                friend.setChannelMetadata("my_channel", "Docs channel", null, null, false, null, null, null).sync());
        Kit.retry("user metadata", () ->
                friend.setUUIDMetadata("my_channel", "Docs user", null, null, null, null, false, null, null, null).sync());
        Wait.eventuallyRun("the control listener saw presence, message, signal, message action, channel and user events", () ->
                assertTrue(seen.get() >= 6, "the control listener did not see every kind of event"));
        assertEquals(0, replaced.get(), "a handler that the sample replaced still ran");
    }

    @Test
    void addConnectionStatusListener(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        Stdout out = Stdout.captureForTest();
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("my_channel"), Collections.emptyList());

        // snippet.addConnectionStatusListener
        pubNub.addListener((pubnub, status) -> {
            // Handle connection status updates
            System.out.println("Connection Status: " + status.getCategory());
        });
        // snippet.end

        client.subscribe().channels(Collections.singletonList("my_channel")).execute();
        connected.await();
        Wait.eventuallyRun("the status listener printed the connected status", () ->
                out.assertPrinted("Connection Status: PNConnectedCategory"));
        client.unsubscribeAll();
        Wait.eventuallyRun("the status listener printed the disconnected status", () ->
                out.assertPrinted("Connection Status: PNDisconnectedCategory"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Unsubscribe
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void unsubscribe(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-receiver");
        String text = Kit.probe("unsubscribe");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("my_channel"), Collections.emptyList());
        client.subscribe().channels(Collections.singletonList("my_channel")).execute();
        connected.await();
        Kit.subscribeAndConnect(control, Collections.singletonList("my_channel"));
        assertEquals(Collections.singletonList("my_channel"), client.getSubscribedChannels());

        // snippet.unsubscribe
        pubNub.unsubscribe()
                .channels(Arrays.asList("my_channel"))
                .execute();
        // snippet.end

        assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to my_channel");
        Wait.Expectation controlArrived = Kit.armMessage(control, "my_channel", text);
        JavaWait.Expectation afterArrived = JavaWait.message(client, "my_channel", text);
        Kit.publishFrom(friend, "my_channel", text, false);
        controlArrived.await();
        JavaWait.assertNothingArrives(afterArrived, "a message arrived after the unsubscribe", 3000);
    }

    @Test
    void unsubscribeFromMultipleChannels(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(friend, "cg1", "ch3");
        Kit.createGroup(friend, "cg2", "bar");
        Kit.createGroup(friend, "cg3", "coolChannel");
        String textChannel = Kit.probe("unsubscribe-channel");
        String textGroup = Kit.probe("unsubscribe-group");
        JavaWait.Expectation connected =
                JavaWait.connected(client, Arrays.asList("ch1", "ch2", "ch3"), Arrays.asList("cg1", "cg2", "cg3"));
        client.subscribe().channels(Arrays.asList("ch1", "ch2", "ch3")).channelGroups(Arrays.asList("cg1", "cg2", "cg3")).execute();
        connected.await();
        Kit.subscribeAndConnect(control, Collections.singletonList("ch1"), Collections.singletonList("cg1"));

        // snippet.unsubscribeFromMultipleChannels
        pubNub.unsubscribe()
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3"))
                .execute();
        // snippet.end

        assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to channels");
        assertTrue(client.getSubscribedChannelGroups().isEmpty(), "the client is still subscribed to channel groups");
        Wait.Expectation controlChannel = Kit.armMessage(control, "ch1", textChannel);
        Wait.Expectation controlGroup = Kit.armMessage(control, "ch3", textGroup);
        JavaWait.Expectation afterChannel = JavaWait.message(client, "ch1", textChannel);
        JavaWait.Expectation afterGroup = JavaWait.message(client, "ch3", textGroup);
        Kit.publishFrom(friend, "ch1", textChannel, false);
        Kit.publishFrom(friend, "ch3", textGroup, false);
        controlChannel.await();
        controlGroup.await();
        JavaWait.assertNothingArrives(afterChannel, "a channel message arrived after the unsubscribe", 3000);
        JavaWait.assertNothingArrives(afterGroup, "a channel group message arrived after the unsubscribe", 3000);
    }

    @Test
    void unsubscribeFromChannelGroup(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(friend, "cg1", "ch3");
        Kit.createGroup(friend, "cg2", "bar");
        Kit.createGroup(friend, "cg3", "coolChannel");
        String text = Kit.probe("unsubscribe-group");
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.emptyList(), Arrays.asList("cg1", "cg2", "cg3"));
        client.subscribe().channelGroups(Arrays.asList("cg1", "cg2", "cg3")).execute();
        connected.await();
        Kit.subscribeAndConnect(control, Collections.emptyList(), Collections.singletonList("cg1"));

        // snippet.unsubscribeFromChannelGroup
        pubNub.unsubscribe()
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3"))
                .execute();
        // snippet.end

        assertTrue(client.getSubscribedChannelGroups().isEmpty(), "the client is still subscribed to channel groups");
        Wait.Expectation controlArrived = Kit.armMessage(control, "ch3", text);
        JavaWait.Expectation afterArrived = JavaWait.message(client, "ch3", text);
        Kit.publishFrom(friend, "ch3", text, false);
        controlArrived.await();
        JavaWait.assertNothingArrives(afterArrived, "a channel group message arrived after the unsubscribe", 3000);
    }

    @Test
    void unsubscribeMethod(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-receiver");
        String text = Kit.probe("unsubscribe-method");
        Subscription subscription = pubNub.channel("my_channel").subscription();
        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(new HashSet<>());
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList("my_channel"), Collections.emptyList());
        subscription.subscribe();
        connected.await();
        Kit.subscribeAndConnect(control, Collections.singletonList("my_channel"));

        // snippet.unsubscribeMethod
        // For subscription
        subscription.unsubscribe();
        // For subscription set
        subscriptionSet.unsubscribe();

        // snippet.end

        Wait.eventuallyRun("the client dropped the channel", () ->
                assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to my_channel"));
        Wait.Expectation controlArrived = Kit.armMessage(control, "my_channel", text);
        JavaWait.Expectation afterArrived = JavaWait.message(client, "my_channel", text);
        Kit.publishFrom(friend, "my_channel", text, false);
        controlArrived.await();
        JavaWait.assertNothingArrives(afterArrived, "a message arrived after the unsubscribe", 3000);
    }

    @Test
    void unsubscribeBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-receiver");
        String text = Kit.probe("unsubscribe-basic");
        Subscription subscription = pubNub.channel("my_channel").subscription();
        Kit.subscribeAndConnect(control, Collections.singletonList("my_channel"));

        // snippet.unsubscribeBasic
        // Subscribe to a channel
        subscription.subscribe();

        // Unsubscribe from that channel
        subscription.unsubscribe();
        // snippet.end

        Wait.eventuallyRun("the client dropped the channel", () ->
                assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to my_channel"));
        Wait.Expectation controlArrived = Kit.armMessage(control, "my_channel", text);
        JavaWait.Expectation afterArrived = JavaWait.message(client, "my_channel", text);
        Kit.publishFrom(friend, "my_channel", text, false);
        controlArrived.await();
        JavaWait.assertNothingArrives(afterArrived, "a message arrived after the unsubscribe", 3000);
        // The sample subscribes and unsubscribes at once, so a join of the handshake can outlive the leave.
        Kit.leaveAs(clients, "yourUserId", Collections.singletonList("my_channel"));
    }

    @Test
    void unsubscribeAllMethod(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-receiver");
        String text = Kit.probe("unsubscribe-all-method");
        JavaWait.Expectation connected = JavaWait.connected(client, Arrays.asList("my_channel", "other_channel"), Collections.emptyList());
        client.subscribe().channels(Arrays.asList("my_channel", "other_channel")).execute();
        connected.await();
        Kit.subscribeAndConnect(control, Collections.singletonList("my_channel"));

        // snippet.unsubscribeAllMethod
        pubNub.unsubscribeAll();
        // snippet.end

        assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to channels");
        Wait.Expectation controlArrived = Kit.armMessage(control, "my_channel", text);
        JavaWait.Expectation afterArrived = JavaWait.message(client, "my_channel", text);
        Kit.publishFrom(friend, "my_channel", text, false);
        controlArrived.await();
        JavaWait.assertNothingArrives(afterArrived, "a message arrived after unsubscribeAll", 3000);
    }

    @Test
    void unsubscribeAllBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub friend = kotlinClient(clients, "docs-joiner");
        com.pubnub.api.PubNub control = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(friend, "my_channel_group", "ch3");
        String textChannel = Kit.probe("unsubscribe-all-channel");
        String textGroup = Kit.probe("unsubscribe-all-group");
        Kit.subscribeAndConnect(control, Collections.singletonList("my_channel"), Collections.singletonList("my_channel_group"));
        // The sample only creates its subscriptions, so the client is subscribed the plain way for unsubscribeAll to have work.
        JavaWait.Expectation connected =
                JavaWait.connected(client, Collections.singletonList("my_channel"), Collections.singletonList("my_channel_group"));
        client.subscribe().channels(Collections.singletonList("my_channel")).channelGroups(Collections.singletonList("my_channel_group")).execute();
        connected.await();
        assertEquals(Collections.singletonList("my_channel"), client.getSubscribedChannels());

        // snippet.unsubscribeAllBasic
        // Subscribe to channels
        Subscription mySubscription01 = pubNub.channel("my_channel").subscription(SubscriptionOptions.receivePresenceEvents());

        // Subscribe to a channel group
        Subscription mySubscription02 = pubNub.channelGroup("my_channel_group").subscription();

        // Later, when you want to unsubscribe from all subscriptions
        pubNub.unsubscribeAll();
        // snippet.end

        assertTrue(client.getSubscribedChannels().isEmpty(), "the client is still subscribed to channels");
        assertTrue(client.getSubscribedChannelGroups().isEmpty(), "the client is still subscribed to channel groups");
        Wait.Expectation controlChannel = Kit.armMessage(control, "my_channel", textChannel);
        Wait.Expectation controlGroup = Kit.armMessage(control, "ch3", textGroup);
        JavaWait.Expectation afterChannel = JavaWait.message(client, "my_channel", textChannel);
        JavaWait.Expectation afterGroup = JavaWait.message(client, "ch3", textGroup);
        Kit.publishFrom(friend, "my_channel", textChannel, false);
        Kit.publishFrom(friend, "ch3", textGroup, false);
        controlChannel.await();
        controlGroup.await();
        JavaWait.assertNothingArrives(afterChannel, "a channel message arrived after unsubscribeAll", 3000);
        JavaWait.assertNothingArrives(afterGroup, "a channel group message arrived after unsubscribeAll", 3000);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------------------------------------------------

    /** Subscribes a Java client with presence and waits for the connected status. */
    private static void subscribeWithPresence(PubNub client, String channel) {
        JavaWait.Expectation connected = JavaWait.connected(client, Collections.singletonList(channel), Collections.emptyList());
        client.subscribe().channels(Collections.singletonList(channel)).withPresence().execute();
        connected.await();
    }
}
