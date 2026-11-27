package com.pubnub.docs.dataSync;

import static com.pubnub.docs.support.DataSyncKit.ALICE;
import static com.pubnub.docs.support.DataSyncKit.SNEAKER;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult;
import com.pubnub.docs.support.DataSyncKit;
import com.pubnub.docs.support.JavaWait;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the DataSync listener samples of the Java docs page publish-and-subscribe
 * (docs/sdks/java/api-reference/publish-and-subscribe.md, "Add DataSync listeners"). The samples subscribe to Alice and to the
 * sneaker and print every event they get. The test changes the object through a second client and waits for the printed line,
 * so the line proves that the listener of the sample, and not only the subscription, works.
 */
@ExtendWith(LiveExtension.class)
class DataSyncSubscribeDocsTest {
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

    private static void assertPrinted(Stdout out, String text) {
        Wait.eventuallyRun("the sample printed \"" + text + "\"", () -> out.assertPrinted(text));
    }

    @Test
    void addDataSyncListener(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        Stdout out = Stdout.captureForTest();
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(SNEAKER), Collections.<String>emptyList());

        // snippet.addDataSyncListener
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription();

        subscription.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType());
            }
        });

        subscription.subscribe();
        // snippet.end

        ready.await();
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 71.0, 5);
        assertPrinted(out, "DataSync event on product-sneaker-42: entity");
    }

    @Test
    void addDataSyncListenerLambda(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        Stdout out = Stdout.captureForTest();
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(SNEAKER), Collections.<String>emptyList());

        // snippet.addDataSyncListenerLambda
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription();

        subscription.setOnDataSync(result ->
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType()));

        subscription.subscribe();
        // snippet.end

        ready.await();
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 72.0, 5);
        assertPrinted(out, "DataSync event on product-sneaker-42: entity");
    }

    @Test
    void addDataSyncListenerClient(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        Stdout out = Stdout.captureForTest();
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(SNEAKER), Collections.<String>emptyList());

        // snippet.addDataSyncListenerClient
        pubnub.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType());
            }
        });

        pubnub.dataSyncEntity("product-sneaker-42").subscription().subscribe();
        // snippet.end

        ready.await();
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 73.0, 5);
        assertPrinted(out, "DataSync event on product-sneaker-42: entity");
    }

    @Test
    void addDataSyncListenerSubscriptionSet(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = clients.server(ROLE, "docs-server");
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        Stdout out = Stdout.captureForTest();
        JavaWait.Expectation ready = JavaWait.connected(client, Arrays.asList(ALICE, SNEAKER), Collections.<String>emptyList());

        // snippet.addDataSyncListenerSubscriptionSet
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(pubnub.dataSyncUser("user-alice").subscription());
        subscriptions.add(pubnub.dataSyncEntity("product-sneaker-42").subscription());

        SubscriptionSet subscriptionSet = pubnub.subscriptionSetOf(subscriptions);

        subscriptionSet.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType());
            }
        });

        subscriptionSet.subscribe();
        // snippet.end

        ready.await();
        // one set, two kinds of object: a change of the user and a change of the product both reach the one listener
        DataSyncKit.setUser(server, ALICE, "Alice C.", "shopper");
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 74.0, 5);
        assertPrinted(out, "DataSync event on user-alice: user");
        assertPrinted(out, "DataSync event on product-sneaker-42: entity");
        assertTrue(out.lines("Failed").isEmpty());
    }
}
