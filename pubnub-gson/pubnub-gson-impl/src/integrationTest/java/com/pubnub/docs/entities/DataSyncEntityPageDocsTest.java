package com.pubnub.docs.entities;

import static com.pubnub.docs.support.DataSyncKit.ALICE;
import static com.pubnub.docs.support.DataSyncKit.SNEAKER;
import static com.pubnub.docs.support.DataSyncKit.SUMMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType;
import com.pubnub.docs.support.DataSyncJavaKit;
import com.pubnub.docs.support.DataSyncKit;
import com.pubnub.docs.support.JavaWait;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import java.util.Collections;
import java.util.HashSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java DataSync entity pages data-sync-user, data-sync-channel and data-sync-entity
 * (docs/sdks/java/entities/data-sync-*.md). The samples only create a subscription and subscribe, so each test arms a listener
 * on the same client, changes the object through a second client and waits for the event on the channel of the subscription.
 */
@ExtendWith(LiveExtension.class)
class DataSyncEntityPageDocsTest {
    private static final String ROLE = DataSyncKit.ROLE;

    @BeforeAll
    static void prepare() {
        DataSyncKit.resetPage();
        DataSyncKit.seedObserved();
    }

    @AfterAll
    static void finish() {
        DataSyncKit.finishPage();
    }

    @Test
    void dataSyncUserSubscription(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        DataSyncJavaKit.Events events = new DataSyncJavaKit.Events(client);
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(ALICE), Collections.<String>emptyList());

        // snippet.dataSyncUserSubscription
        Subscription subscription = pubnub.dataSyncUser("user-alice").subscription();
        subscription.subscribe();
        // snippet.end

        ready.await();
        assertEquals(Collections.singleton(ALICE), new HashSet<>(client.getSubscribedChannels()));
        DataSyncKit.setUser(server, ALICE, "Alice D.", "shopper");
        PNDataSyncEventResult event = events.await("the update of Alice on her channel", 30_000,
                e -> e.getChannel().equals(ALICE) && DataSyncKit.isSet(e, "user", ALICE, PNDataSyncSetEventType.UPDATE));
        assertEquals("user", event.getExtractedMessage().getType());
        events.close();
    }

    @Test
    void dataSyncChannelSubscription(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        DataSyncJavaKit.Events events = new DataSyncJavaKit.Events(client);
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(SUMMER), Collections.<String>emptyList());

        // snippet.dataSyncChannelSubscription
        Subscription subscription = pubnub.dataSyncChannel("channel-summer-sale").subscription();
        subscription.subscribe();
        // snippet.end

        ready.await();
        assertEquals(Collections.singleton(SUMMER), new HashSet<>(client.getSubscribedChannels()));
        DataSyncKit.setChannel(server, SUMMER, "Summer Sale 2027", "promotion");
        PNDataSyncEventResult event = events.await("the update of the channel on its own channel", 30_000,
                e -> e.getChannel().equals(SUMMER) && DataSyncKit.isSet(e, "channel", SUMMER, PNDataSyncSetEventType.UPDATE));
        assertEquals("channel", event.getExtractedMessage().getType());
        events.close();
    }

    @Test
    void dataSyncEntitySubscription(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        DataSyncJavaKit.Events events = new DataSyncJavaKit.Events(client);
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(SNEAKER), Collections.<String>emptyList());

        // snippet.dataSyncEntitySubscription
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription();
        subscription.subscribe();
        // snippet.end

        ready.await();
        assertEquals(Collections.singleton(SNEAKER), new HashSet<>(client.getSubscribedChannels()));
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 66.0, 4);
        PNDataSyncEventResult event = events.await("the update of the product on its own channel", 30_000,
                e -> e.getChannel().equals(SNEAKER) && DataSyncKit.isSet(e, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE));
        assertEquals("entity", event.getExtractedMessage().getType());
        events.close();
    }

    /** Not a sample: the page says {@code default} and {@code __default__} observe the entity itself and that a blank projection throws. */
    @Test
    void defaultProjectionNamesObserveTheEntityItself(LiveClients clients) throws Exception {
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        for (String name : new String[] {"default", "__default__"}) {
            JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(SNEAKER), Collections.<String>emptyList());
            Subscription subscription = client.dataSyncEntity(SNEAKER).subscription(name);
            subscription.subscribe();
            ready.await();
            assertEquals(Collections.singleton(SNEAKER), new HashSet<>(client.getSubscribedChannels()), "projection " + name);
            subscription.unsubscribe();
        }
        assertThrows(IllegalArgumentException.class, () -> client.dataSyncEntity(SNEAKER).subscription(""));
    }

    @Test
    void dataSyncProjectionSubscription(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        String projection = "__admin__" + SNEAKER;
        DataSyncJavaKit.Events events = new DataSyncJavaKit.Events(client);
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(projection), Collections.<String>emptyList());

        // snippet.dataSyncProjectionSubscription
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription("admin");
        subscription.subscribe();
        // snippet.end

        ready.await();
        // the projection has its own channel, and the subscription is on that channel only
        assertEquals(Collections.singleton(projection), new HashSet<>(client.getSubscribedChannels()));
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 67.0, 4);
        PNDataSyncEventResult event = events.await("the update of the product on the admin projection channel", 30_000,
                e -> e.getChannel().equals(projection) && DataSyncKit.isSet(e, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE));
        assertEquals("product", event.getExtractedMessage().getClassName());
        events.close();
    }
}
