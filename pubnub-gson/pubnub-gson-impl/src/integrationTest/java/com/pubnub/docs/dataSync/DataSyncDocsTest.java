package com.pubnub.docs.dataSync;

import static com.pubnub.docs.support.DataSyncKit.ALICE;
import static com.pubnub.docs.support.DataSyncKit.BOB;
import static com.pubnub.docs.support.DataSyncKit.BOOT;
import static com.pubnub.docs.support.DataSyncKit.LINK;
import static com.pubnub.docs.support.DataSyncKit.SNEAKER;
import static com.pubnub.docs.support.DataSyncKit.SUMMER;
import static com.pubnub.docs.support.DataSyncKit.SUMMER_MEMBERSHIP;
import static com.pubnub.docs.support.DataSyncKit.WINTER;
import static com.pubnub.docs.support.DataSyncKit.WINTER_MEMBERSHIP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncCreateChannelResult;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncGetChannelResult;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncGetChannelsResult;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncRemoveChannelResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncCreateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncGetEntitiesResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncRemoveEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncGetMembershipsResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncRemoveMembershipResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncCreateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncGetUsersResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncRemoveUserResult;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.entities.DataSyncEntity;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.datasync.DataSyncErrors;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncChannelEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncEntityEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncMembershipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncRelationshipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage;
import com.pubnub.docs.support.DataSyncJavaKit;
import com.pubnub.docs.support.DataSyncKit;
import com.pubnub.docs.support.JavaWait;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page data-sync (docs/sdks/java/api-reference/data-sync.md). One test per snippet. The text between
 * a start marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions below it.
 *
 * <p>The samples work on objects with literal ids that persist on the keyset (a user, a channel, a membership, a product and a
 * link), and the service answers a create of an existing id with 409. So the page removes every listed object before and after
 * its run, and the tests run in the order of {@code @Order}: each one builds on the objects of the tests before it (create, read,
 * list, update, patch), the removals come last. The sample's client is a Java client wrapped by the recorder, a second Kotlin
 * client ({@code server}) arranges and reads back.
 */
@ExtendWith(LiveExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataSyncDocsTest {
    private static final String ROLE = DataSyncKit.ROLE;

    @BeforeAll
    static void prepare() {
        DataSyncKit.resetPage();
        DataSyncKit.seedCompanions();
    }

    @AfterAll
    static void finish() {
        DataSyncKit.finishPage();
    }

    private static com.pubnub.api.PubNub server(LiveClients clients) {
        return clients.server(ROLE, "docs-server");
    }

    private static PubNub javaClient(LiveClients clients) {
        return clients.javaClient(ROLE, "myUserId");
    }

    private static void assertPrinted(Stdout out, String text) {
        Wait.eventuallyRun("the sample printed \"" + text + "\"", () -> out.assertPrinted(text));
    }

    private static boolean hasLine(Stdout out, String line) {
        return Arrays.asList(out.text().split("\\R")).contains(line);
    }

    private static void assertPrintedLine(Stdout out, String line) {
        Wait.eventuallyRun("the sample printed the line \"" + line + "\"",
                () -> assertTrue(hasLine(out, line), "the sample did not print the line \"" + line + "\". Printed: " + out.text()));
    }

    private static void assertNotPrintedLine(Stdout out, String line) {
        assertTrue(!hasLine(out, line), "the sample printed the line \"" + line + "\" that the filter must leave out");
    }

    private static void assertNoFailure(Stdout out) {
        assertTrue(out.lines("Failed:").isEmpty(), "the sample printed a failure: " + out.lines("Failed:"));
    }

    private static Map<String, Object> mapOf(Object... keysAndValues) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put((String) keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Users
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(10)
    void createUserBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.createUserBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Alice");
        payload.put("type", "shopper");

        pubnub.dataSync().createUser(1)
                .userId("user-alice")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created user " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncCreateUserResult created = (PNDataSyncCreateUserResult) recorder.awaitOk("createUser");
        assertEquals(ALICE, created.getData().getId());
        assertPrinted(out, "Created user user-alice with eTag " + created.getData().getETag());
        assertNoFailure(out);
        com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser user = DataSyncKit.awaitReadJava("Alice can be read",
                () -> server.getDataSync().getUser(ALICE).sync().getData(),
                u -> assertEquals(mapOf("name", "Alice", "type", "shopper"), u.getPayload()));
        assertEquals(created.getData().getETag(), user.getETag());
        assertEquals("User", user.getClassName());
        assertEquals(1, user.getClassVersion());
        assertEquals("Global", user.getClassLevel());
        assertTrue(!user.getCreatedAt().isEmpty() && !user.getUpdatedAt().isEmpty() && !user.getExpiresAt().isEmpty(), "timestamps are set");
    }

    @Test
    @Order(11)
    void getUserBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("Alice can be read", () -> server.getDataSync().getUser(ALICE).sync().getData(), u -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getUserBasicUsage
        pubnub.dataSync().getUser("user-alice")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched user " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getUser");
        assertPrinted(out, "Fetched user user-alice with payload {name=Alice, type=shopper}");
        assertNoFailure(out);
    }

    @Test
    @Order(12)
    void getUsersBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of users shows Alice and Bob",
                () -> assertTrue(DataSyncKit.userIds(server, null, null, 10).containsAll(Arrays.asList(ALICE, BOB))));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getUsersBasicUsage
        pubnub.dataSync().getUsers()
                .limit(10)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetUsersResult result = (PNDataSyncGetUsersResult) recorder.awaitOk("getUsers");
        assertPrintedLine(out, ALICE);
        assertPrintedLine(out, BOB);
        assertPrintedLine(out, "More pages available: false");
        assertNoFailure(out);
    }

    @Test
    @Order(13)
    void getUsersFilterFast(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getUsersFilterFast
        pubnub.dataSync().getUsers()
                .filterFast("type == \"shopper\"")
                .sort(Collections.singletonList(new PNDataSyncSortField("name")))
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetUsersResult result = (PNDataSyncGetUsersResult) recorder.awaitOk("getUsers");
        assertTrue(result.getData().stream().anyMatch(u -> u.getId().equals(ALICE)), "the filter finds Alice right after her creation");
        assertTrue(result.getData().stream().noneMatch(u -> u.getId().equals(BOB)), "Bob is a seller, the filter leaves him out");
        assertPrintedLine(out, ALICE);
        assertNotPrintedLine(out, BOB);
        assertNoFailure(out);
    }

    @Test
    @Order(14)
    void getUsersFilter(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // `filter` is eventually consistent: wait until it holds on consecutive reads, then run the sample once.
        DataSyncKit.settled("the filter index lists Alice",
                () -> assertEquals(Collections.singletonList(ALICE), DataSyncKit.userIds(server, null, "name LIKE \"Alice*\"")));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getUsersFilter
        pubnub.dataSync().getUsers()
                .filter("name LIKE \"Alice*\"")
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetUsersResult result = (PNDataSyncGetUsersResult) recorder.awaitOk("getUsers");
        assertEquals(1, result.getData().size());
        assertEquals(ALICE, result.getData().get(0).getId());
        assertPrintedLine(out, ALICE);
        assertNotPrintedLine(out, BOB);
        assertNoFailure(out);
    }

    @Test
    @Order(15)
    void getUsersPagination(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of users shows Alice and Bob",
                () -> assertTrue(DataSyncKit.userIds(server, null, null, 20).containsAll(Arrays.asList(ALICE, BOB))));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        fetchUsersPage(pubnub, null);

        PNDataSyncGetUsersResult first = (PNDataSyncGetUsersResult) recorder.awaitOk("getUsers");
        assertTrue(first.getData().size() <= 20);
        assertPrintedLine(out, ALICE);
        assertPrintedLine(out, BOB);
        assertNoFailure(out);
    }

    // snippet.getUsersPagination
    private void fetchUsersPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getUsers()
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchUsersPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchUsersPage(pubnub, null)
    // snippet.end

    @Test
    @Order(16)
    void setUserBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        String before = DataSyncKit.awaitReadJava("Alice can be read", () -> server.getDataSync().getUser(ALICE).sync().getData(), u -> { })
                .getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.setUserBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Alice B.");
        payload.put("type", "shopper");

        pubnub.dataSync().setUser("user-alice", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced user " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncSetUserResult set =
                (com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncSetUserResult) recorder.awaitOk("setUser");
        assertPrinted(out, "Replaced user user-alice, new eTag " + set.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, set.getData().getETag(), "the write produced a new eTag");
        DataSyncKit.awaitReadJava("Alice shows the new name", () -> server.getDataSync().getUser(ALICE).sync().getData(),
                u -> assertEquals("Alice B.", u.getPayload().get("name")));
    }

    @Test
    @Order(17)
    void updateUserBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // The patch must change something: put the old name back first.
        DataSyncKit.setUser(server, ALICE, "Alice", "shopper");
        String before = DataSyncKit.awaitReadJava("Alice shows the old name", () -> server.getDataSync().getUser(ALICE).sync().getData(),
                u -> assertEquals("Alice", u.getPayload().get("name"))).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.updateUserBasicUsage
        pubnub.dataSync().updateUser("user-alice", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/name").value("Alice B.").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated user " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncUpdateUserResult updated =
                (com.pubnub.api.java.models.consumer.datasync.user.PNDataSyncUpdateUserResult) recorder.awaitOk("updateUser");
        assertEquals("Alice B.", updated.getData().getPayload().get("name"));
        assertEquals("shopper", updated.getData().getPayload().get("type"));
        assertPrinted(out, "Updated user user-alice, new eTag " + updated.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, updated.getData().getETag());
    }

    /** Not a sample: the page says a stale {@code ifMatch} fails with a 412 and that the category is a DATASYNC_* one chosen from the status. */
    @Test
    @Order(18)
    void staleIfMatchFailsAsDocumented(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        String stale = DataSyncKit.awaitReadJava("Alice can be read", () -> server.getDataSync().getUser(ALICE).sync().getData(), u -> { })
                .getETag();
        DataSyncKit.setUser(server, ALICE, "Alice B.", "shopper");
        PubNubException error = assertThrows(PubNubException.class, () -> clients.<PubNub>javaClient(ROLE, "docs-stale").dataSync()
                .setUser(ALICE, 1).payload(mapOf("name", "stale", "type", "shopper")).ifMatch(stale).sync());
        assertEquals(412, error.getStatusCode(), "the page says a stale eTag fails with 412");
        assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, error.getPubnubError());
        String code = DataSyncErrors.firstCode(error);
        assertTrue(code != null && code.startsWith("DS-"), "the error carries a DS- code");
    }

    /**
     * Not a sample: the page says "a successful call yields a result object carrying the HTTP status" and its response blocks show
     * 201 for a create and 200 for every other call.
     */
    @Test
    @Order(19)
    void successfulResultsCarryTheHttpStatus(LiveClients clients) throws Exception {
        PubNub client = clients.javaClient(ROLE, "docs-status");
        String id = "docs-probe-user";
        Kit.cleanup("remove the probe user", () -> client.dataSync().removeUser(id).sync());
        Map<String, Object> payload = mapOf("name", "Probe");
        Map<String, Integer> seen = new java.util.LinkedHashMap<>();
        seen.put("createUser", client.dataSync().createUser(1).userId(id).payload(payload).sync().getStatus());
        seen.put("getUser", DataSyncKit.awaitReadJava("the probe user", () -> client.dataSync().getUser(id).sync(), r -> { }).getStatus());
        seen.put("getUsers", client.dataSync().getUsers().filterFast("id == \"" + id + "\"").sync().getStatus());
        seen.put("setUser", client.dataSync().setUser(id, 1).payload(mapOf("name", "Probe set")).sync().getStatus());
        seen.put("updateUser", client.dataSync().updateUser(id, Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/name").value("Patched").build())).sync().getStatus());
        Map<String, Integer> documented = new java.util.LinkedHashMap<>();
        documented.put("createUser", 201);
        documented.put("getUser", 200);
        documented.put("getUsers", 200);
        documented.put("setUser", 200);
        documented.put("updateUser", 200);
        assertEquals(documented, seen);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Channels
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(20)
    void createChannelBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.createChannelBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Summer Sale");
        payload.put("type", "promotion");

        pubnub.dataSync().createChannel(1)
                .channelId("channel-summer-sale")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created channel " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncCreateChannelResult created = (PNDataSyncCreateChannelResult) recorder.awaitOk("createChannel");
        assertPrinted(out, "Created channel channel-summer-sale with eTag " + created.getData().getETag());
        assertNoFailure(out);
        com.pubnub.api.models.consumer.datasync.channel.PNDataSyncChannel channel = DataSyncKit.awaitReadJava("the channel can be read",
                () -> server.getDataSync().getChannel(SUMMER).sync().getData(),
                c -> assertEquals(mapOf("name", "Summer Sale", "type", "promotion"), c.getPayload()));
        assertEquals("Channel", channel.getClassName());
        assertEquals(1, channel.getClassVersion());
        assertEquals("Global", channel.getClassLevel());
    }

    @Test
    @Order(21)
    void getChannelBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the channel can be read", () -> server.getDataSync().getChannel(SUMMER).sync().getData(), c -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getChannelBasicUsage
        pubnub.dataSync().getChannel("channel-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched channel " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getChannel");
        assertPrinted(out, "Fetched channel channel-summer-sale with payload {name=Summer Sale, type=promotion}");
        assertNoFailure(out);
    }

    @Test
    @Order(22)
    void getChannelsBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of channels shows the summer channel",
                () -> assertTrue(DataSyncKit.channelIds(server, null, null, 10).contains(SUMMER)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getChannelsBasicUsage
        pubnub.dataSync().getChannels()
                .limit(10)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getChannels");
        assertPrintedLine(out, SUMMER);
        assertPrintedLine(out, "More pages available: false");
        assertNoFailure(out);
    }

    @Test
    @Order(23)
    void getChannelsFilterFast(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getChannelsFilterFast
        pubnub.dataSync().getChannels()
                .filterFast("type == \"promotion\"")
                .sort(Collections.singletonList(new PNDataSyncSortField("name")))
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetChannelsResult result = (PNDataSyncGetChannelsResult) recorder.awaitOk("getChannels");
        assertTrue(result.getData().stream().anyMatch(c -> c.getId().equals(SUMMER)), "the filter finds the channel right after its creation");
        assertPrintedLine(out, SUMMER);
        assertNoFailure(out);
    }

    @Test
    @Order(24)
    void getChannelsFilter(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the filter index lists the summer channel",
                () -> assertEquals(Collections.singletonList(SUMMER), DataSyncKit.channelIds(server, null, "name LIKE \"Summer*\"", null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getChannelsFilter
        pubnub.dataSync().getChannels()
                .filter("name LIKE \"Summer*\"")
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetChannelsResult result = (PNDataSyncGetChannelsResult) recorder.awaitOk("getChannels");
        assertEquals(1, result.getData().size());
        assertEquals(SUMMER, result.getData().get(0).getId());
        assertPrintedLine(out, SUMMER);
        assertNoFailure(out);
    }

    @Test
    @Order(25)
    void getChannelsPagination(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of channels shows the summer channel",
                () -> assertTrue(DataSyncKit.channelIds(server, null, null, 20).contains(SUMMER)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        fetchChannelsPage(pubnub, null);

        recorder.awaitOk("getChannels");
        assertPrintedLine(out, SUMMER);
        assertNoFailure(out);
    }

    // snippet.getChannelsPagination
    private void fetchChannelsPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getChannels()
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchChannelsPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchChannelsPage(pubnub, null)
    // snippet.end

    @Test
    @Order(26)
    void setChannelBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        String before = DataSyncKit.awaitReadJava("the channel can be read", () -> server.getDataSync().getChannel(SUMMER).sync().getData(),
                c -> { }).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.setChannelBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Summer Sale 2026");
        payload.put("type", "promotion");

        pubnub.dataSync().setChannel("channel-summer-sale", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced channel " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncSetChannelResult set =
                (com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncSetChannelResult) recorder.awaitOk("setChannel");
        assertPrinted(out, "Replaced channel channel-summer-sale, new eTag " + set.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, set.getData().getETag());
        DataSyncKit.awaitReadJava("the channel shows the new name", () -> server.getDataSync().getChannel(SUMMER).sync().getData(),
                c -> assertEquals("Summer Sale 2026", c.getPayload().get("name")));
    }

    @Test
    @Order(27)
    void updateChannelBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // The patch must change something: put the old name back first.
        DataSyncKit.setChannel(server, SUMMER, "Summer Sale", "promotion");
        String before = DataSyncKit.awaitReadJava("the channel shows the old name", () -> server.getDataSync().getChannel(SUMMER).sync().getData(),
                c -> assertEquals("Summer Sale", c.getPayload().get("name"))).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.updateChannelBasicUsage
        pubnub.dataSync().updateChannel("channel-summer-sale", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/name").value("Summer Sale 2026").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated channel " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult updated =
                (com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult) recorder.awaitOk("updateChannel");
        assertEquals("Summer Sale 2026", updated.getData().getPayload().get("name"));
        assertEquals("promotion", updated.getData().getPayload().get("type"));
        assertPrinted(out, "Updated channel channel-summer-sale, new eTag " + updated.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, updated.getData().getETag());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Memberships
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(30)
    void createMembershipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.createMembershipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("role", "viewer");

        pubnub.dataSync().createMembership("channel-summer-sale", "user-alice", 1)
                .membershipId("membership-alice-summer-sale")
                .status("active")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created membership " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncCreateMembershipResult created = (PNDataSyncCreateMembershipResult) recorder.awaitOk("createMembership");
        assertPrinted(out, "Created membership membership-alice-summer-sale with eTag " + created.getData().getETag());
        assertNoFailure(out);
        com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership membership = DataSyncKit.awaitReadJava("the membership can be read",
                () -> server.getDataSync().getMembership(SUMMER_MEMBERSHIP).sync().getData(),
                m -> assertEquals(mapOf("role", "viewer"), m.getPayload()));
        assertEquals(ALICE, membership.getUserId());
        assertEquals(SUMMER, membership.getChannelId());
        assertEquals("active", membership.getStatus());
        assertEquals("Membership", membership.getClassName());
        assertEquals(1, membership.getClassVersion());
    }

    @Test
    @Order(31)
    void getMembershipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the membership can be read", () -> server.getDataSync().getMembership(SUMMER_MEMBERSHIP).sync().getData(),
                m -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getMembershipBasicUsage
        pubnub.dataSync().getMembership("membership-alice-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched membership " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getMembership");
        assertPrinted(out, "Fetched membership membership-alice-summer-sale with payload {role=viewer}");
        assertNoFailure(out);
    }

    @Test
    @Order(32)
    void getMembershipsBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("Alice lists exactly her membership",
                () -> assertEquals(Collections.singletonList(SUMMER_MEMBERSHIP), DataSyncKit.membershipIds(server, ALICE, null, null, null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getMembershipsBasicUsage
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
                .limit(10)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getMemberships");
        assertPrintedLine(out, SUMMER_MEMBERSHIP);
        assertPrintedLine(out, "More pages available: false");
        assertNoFailure(out);
    }

    @Test
    @Order(33)
    void getMembershipsByChannelId(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the summer channel lists exactly Alice",
                () -> assertEquals(Collections.singletonList(SUMMER_MEMBERSHIP), DataSyncKit.membershipIds(server, null, SUMMER, null, null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getMembershipsByChannelId
        pubnub.dataSync().getMemberships()
                .channelId("channel-summer-sale")
                .limit(50)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getUserId() + " is a member of " + item.getChannelId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetMembershipsResult result = (PNDataSyncGetMembershipsResult) recorder.awaitOk("getMemberships");
        assertEquals(1, result.getData().size());
        assertEquals(ALICE, result.getData().get(0).getUserId());
        assertPrintedLine(out, "user-alice is a member of channel-summer-sale");
        assertPrintedLine(out, "More pages available: false");
        assertNoFailure(out);
    }

    @Test
    @Order(34)
    void getMembershipsFilterFast(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // A second membership of Alice, archived, that the filter must leave out. The next test removes its channel again.
        DataSyncKit.createChannel(server, WINTER, "Winter Sale", "promotion");
        DataSyncKit.createMembership(server, WINTER_MEMBERSHIP, WINTER, ALICE, "archived", "viewer");
        DataSyncKit.settled("Alice lists both memberships", () -> assertEquals(
                new HashSet<>(Arrays.asList(SUMMER_MEMBERSHIP, WINTER_MEMBERSHIP)),
                new HashSet<>(DataSyncKit.membershipIds(server, ALICE, null, null, null))));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getMembershipsFilterFast
        // Membership payload fields such as `role` are not filterable, filter on `status` instead
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
                .filterFast("status == \"active\"")
                .sort(Collections.singletonList(new PNDataSyncSortField("createdAt", false)))
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetMembershipsResult result = (PNDataSyncGetMembershipsResult) recorder.awaitOk("getMemberships");
        assertEquals(1, result.getData().size(), "only the active membership matches");
        assertEquals(SUMMER_MEMBERSHIP, result.getData().get(0).getId());
        assertPrintedLine(out, SUMMER_MEMBERSHIP);
        assertNotPrintedLine(out, WINTER_MEMBERSHIP);
        assertNoFailure(out);
    }

    @Test
    @Order(35)
    void getMembershipsFilter(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        Kit.cleanup("remove the winter channel and its membership", () -> server.getDataSync().removeChannel(WINTER, null).sync());
        // `filter` is eventually consistent: wait until the index shows the active membership and not the archived one.
        DataSyncKit.settled("the filter index lists the active membership only", () -> assertEquals(
                Collections.singletonList(SUMMER_MEMBERSHIP), DataSyncKit.membershipIds(server, ALICE, null, null, "status LIKE \"act*\"")));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getMembershipsFilter
        // Membership payload fields such as `role` are not filterable, filter on `status` instead
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
                .filter("status LIKE \"act*\"")
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetMembershipsResult result = (PNDataSyncGetMembershipsResult) recorder.awaitOk("getMemberships");
        assertEquals(1, result.getData().size(), "only the active membership matches");
        assertEquals(SUMMER_MEMBERSHIP, result.getData().get(0).getId());
        assertPrintedLine(out, SUMMER_MEMBERSHIP);
        assertNotPrintedLine(out, WINTER_MEMBERSHIP);
        assertNoFailure(out);
    }

    @Test
    @Order(36)
    void getMembershipsPagination(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("Alice lists her membership",
                () -> assertTrue(DataSyncKit.membershipIds(server, ALICE, null, null, null).contains(SUMMER_MEMBERSHIP)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        fetchMembershipsPage(pubnub, null);

        recorder.awaitOk("getMemberships");
        assertPrintedLine(out, SUMMER_MEMBERSHIP);
        assertNoFailure(out);
    }

    // snippet.getMembershipsPagination
    private void fetchMembershipsPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchMembershipsPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchMembershipsPage(pubnub, null)
    // snippet.end

    @Test
    @Order(37)
    void setMembershipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        String before = DataSyncKit.awaitReadJava("the membership can be read",
                () -> server.getDataSync().getMembership(SUMMER_MEMBERSHIP).sync().getData(), m -> { }).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.setMembershipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("role", "moderator");

        pubnub.dataSync().setMembership("membership-alice-summer-sale", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced membership " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncSetMembershipResult set =
                (com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncSetMembershipResult) recorder.awaitOk("setMembership");
        assertPrinted(out, "Replaced membership membership-alice-summer-sale, new eTag " + set.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, set.getData().getETag());
        assertEquals(ALICE, set.getData().getUserId());
        assertEquals(SUMMER, set.getData().getChannelId());
        DataSyncKit.awaitReadJava("the membership shows the new role", () -> server.getDataSync().getMembership(SUMMER_MEMBERSHIP).sync().getData(),
                m -> assertEquals(mapOf("role", "moderator"), m.getPayload()));
    }

    @Test
    @Order(38)
    void updateMembershipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // The patch must change something: put the old role back first.
        DataSyncKit.setMembership(server, SUMMER_MEMBERSHIP, "active", "viewer");
        String before = DataSyncKit.awaitReadJava("the membership shows the old role",
                () -> server.getDataSync().getMembership(SUMMER_MEMBERSHIP).sync().getData(),
                m -> assertEquals(mapOf("role", "viewer"), m.getPayload())).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.updateMembershipBasicUsage
        pubnub.dataSync().updateMembership("membership-alice-summer-sale", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/role").value("moderator").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated membership " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult updated =
                (com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult) recorder.awaitOk("updateMembership");
        assertEquals("moderator", updated.getData().getPayload().get("role"));
        assertPrinted(out, "Updated membership membership-alice-summer-sale, new eTag " + updated.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, updated.getData().getETag());
    }

    /** Not a sample: the page says a membership that duplicates an existing user and channel pair is rejected with a 409. */
    @Test
    @Order(39)
    void duplicateMembershipFailsAsDocumented(LiveClients clients) throws Exception {
        PubNub client = clients.javaClient(ROLE, "docs-duplicate");
        PubNubException error = assertThrows(PubNubException.class,
                () -> client.dataSync().createMembership(SUMMER, ALICE, 1).membershipId(WINTER_MEMBERSHIP).sync());
        assertEquals(409, error.getStatusCode(), "the page says a duplicate pair fails with 409");
        assertEquals(PubNubError.DATASYNC_CONFLICT, error.getPubnubError());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Entities
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(40)
    void createEntityBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.createEntityBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Retro Sneaker");
        payload.put("price", 89.99);
        payload.put("stock", 12);

        pubnub.dataSync().createEntity("product", 1)
                .entityId("product-sneaker-42")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created entity " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncCreateEntityResult created = (PNDataSyncCreateEntityResult) recorder.awaitOk("createEntity");
        assertPrinted(out, "Created entity product-sneaker-42 with eTag " + created.getData().getETag());
        assertNoFailure(out);
        com.pubnub.api.models.consumer.datasync.entity.PNDataSyncEntity entity = DataSyncKit.awaitReadJava("the product can be read",
                () -> server.getDataSync().getEntity(SNEAKER).sync().getData(),
                e -> assertEquals("Retro Sneaker", e.getPayload().get("name")));
        assertEquals("product", entity.getClassName());
        assertEquals("SubKey", entity.getClassLevel());
        assertEquals(1, entity.getClassVersion());
        assertEquals(89.99, ((Number) entity.getPayload().get("price")).doubleValue());
        assertEquals(12, ((Number) entity.getPayload().get("stock")).intValue());
    }

    @Test
    @Order(41)
    void getEntityBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the product can be read", () -> server.getDataSync().getEntity(SNEAKER).sync().getData(), e -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getEntityBasicUsage
        pubnub.dataSync().getEntity("product-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched entity " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getEntity");
        assertPrinted(out, "Fetched entity product-sneaker-42 with payload {name=Retro Sneaker, price=89.99, stock=12");
        assertNoFailure(out);
    }

    @Test
    @Order(42)
    void getEntitiesBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of products shows both products",
                () -> assertTrue(DataSyncKit.productIds(server, null, null, null, 10).containsAll(Arrays.asList(SNEAKER, BOOT))));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getEntitiesBasicUsage
        pubnub.dataSync().getEntities("product")
                .limit(10)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getEntities");
        assertPrintedLine(out, SNEAKER);
        assertPrintedLine(out, BOOT);
        assertPrintedLine(out, "More pages available: false");
        assertNoFailure(out);
    }

    @Test
    @Order(43)
    void getEntitiesFilterFast(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // The sort is served from the same index as `filter`, so wait until the sorted list shows the sneaker before the sample runs.
        DataSyncKit.settled("the sorted fast filter lists the sneaker and not the boot", () -> assertEquals(Collections.singletonList(SNEAKER),
                DataSyncKit.productIds(server, "price < 100 && stock > 0", null, "price", null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getEntitiesFilterFast
        // `price` and `stock` must be declared filterable (`simple` or `full`) on the `product` class
        pubnub.dataSync().getEntities("product")
                .filterFast("price < 100 && stock > 0")
                .sort(Collections.singletonList(new PNDataSyncSortField("price")))
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetEntitiesResult result = (PNDataSyncGetEntitiesResult) recorder.awaitOk("getEntities");
        assertEquals(1, result.getData().size(), "only the cheap product in stock matches");
        assertEquals(SNEAKER, result.getData().get(0).getId());
        assertPrintedLine(out, SNEAKER);
        assertNotPrintedLine(out, BOOT);
        assertNoFailure(out);
    }

    @Test
    @Order(44)
    void getEntitiesFilter(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the filter index lists the sneaker and not the boot", () -> assertEquals(Collections.singletonList(SNEAKER),
                DataSyncKit.productIds(server, null, "name LIKE \"*Sneaker*\"", null, null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getEntitiesFilter
        // `filter` only accepts properties declared with `full` filtering on the `product` class
        pubnub.dataSync().getEntities("product")
                .filter("name LIKE \"*Sneaker*\"")
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetEntitiesResult result = (PNDataSyncGetEntitiesResult) recorder.awaitOk("getEntities");
        assertEquals(1, result.getData().size());
        assertEquals(SNEAKER, result.getData().get(0).getId());
        assertPrintedLine(out, SNEAKER);
        assertNotPrintedLine(out, BOOT);
        assertNoFailure(out);
    }

    @Test
    @Order(45)
    void getEntitiesPagination(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of products shows both products",
                () -> assertTrue(DataSyncKit.productIds(server, null, null, null, 20).containsAll(Arrays.asList(SNEAKER, BOOT))));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        fetchEntitiesPage(pubnub, null);

        recorder.awaitOk("getEntities");
        assertPrintedLine(out, SNEAKER);
        assertPrintedLine(out, BOOT);
        assertNoFailure(out);
    }

    // snippet.getEntitiesPagination
    private void fetchEntitiesPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getEntities("product")
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchEntitiesPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchEntitiesPage(pubnub, null)
    // snippet.end

    @Test
    @Order(46)
    void setEntityBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        String before = DataSyncKit.awaitReadJava("the product can be read", () -> server.getDataSync().getEntity(SNEAKER).sync().getData(),
                e -> { }).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.setEntityBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Retro Sneaker");
        payload.put("price", 79.99);
        payload.put("stock", 8);

        pubnub.dataSync().setEntity("product-sneaker-42", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced entity " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncSetEntityResult set =
                (com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncSetEntityResult) recorder.awaitOk("setEntity");
        assertPrinted(out, "Replaced entity product-sneaker-42, new eTag " + set.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, set.getData().getETag());
        DataSyncKit.awaitReadJava("the product shows the new price", () -> server.getDataSync().getEntity(SNEAKER).sync().getData(), e -> {
            assertEquals(79.99, ((Number) e.getPayload().get("price")).doubleValue());
            assertEquals(8, ((Number) e.getPayload().get("stock")).intValue());
        });
    }

    @Test
    @Order(47)
    void updateEntityBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // The patch must change something: put the old price and stock back first.
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 89.99, 12);
        String before = DataSyncKit.awaitReadJava("the product shows the old price", () -> server.getDataSync().getEntity(SNEAKER).sync().getData(),
                e -> assertEquals(89.99, ((Number) e.getPayload().get("price")).doubleValue())).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.updateEntityBasicUsage
        pubnub.dataSync().updateEntity("product-sneaker-42", Arrays.asList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/price").value(79.99).build(),
                PNJsonPatchOperation.builder().op("replace").path("/payload/stock").value(8).build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated entity " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncUpdateEntityResult updated =
                (com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncUpdateEntityResult) recorder.awaitOk("updateEntity");
        assertEquals(79.99, ((Number) updated.getData().getPayload().get("price")).doubleValue());
        assertEquals(8, ((Number) updated.getData().getPayload().get("stock")).intValue());
        assertEquals("Retro Sneaker", updated.getData().getPayload().get("name"));
        assertPrinted(out, "Updated entity product-sneaker-42, new eTag " + updated.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, updated.getData().getETag());
    }

    @Test
    @Order(48)
    void updateEntityMultipleOperations(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        // The sample needs a product with a name to test, a price to copy and replace, and the `legacySku` field to remove.
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 89.99, 12, Collections.singletonMap("legacySku", "SKU-1"));
        String before = DataSyncKit.awaitReadJava("the product shows the seeded fields", () -> server.getDataSync().getEntity(SNEAKER).sync().getData(),
                e -> assertEquals("SKU-1", e.getPayload().get("legacySku"))).getETag();
        // The sample says to use the eTag of an earlier read, not a literal value. The literal of the text is swapped for the real eTag.
        recorder.placeholder("a1b2c3d4e5f6", before);
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.updateEntityMultipleOperations
        List<PNJsonPatchOperation> operations = Arrays.asList(
                // Fail the whole patch unless the name is still "Retro Sneaker"
                PNJsonPatchOperation.builder().op("test").path("/payload/name").value("Retro Sneaker").build(),
                // Keep the old price before changing it
                PNJsonPatchOperation.builder().op("copy").path("/payload/previousPrice").from("/payload/price").build(),
                PNJsonPatchOperation.builder().op("replace").path("/payload/price").value(79.99).build(),
                PNJsonPatchOperation.builder().op("replace").path("/payload/stock").value(8).build(),
                PNJsonPatchOperation.builder().op("add").path("/payload/sale").value(true).build(),
                // Rename a field
                PNJsonPatchOperation.builder().op("move").path("/payload/onSale").from("/payload/sale").build(),
                // The removed field must exist on the entity, otherwise the patch is rejected
                PNJsonPatchOperation.builder().op("remove").path("/payload/legacySku").build());

        pubnub.dataSync().updateEntity("product-sneaker-42", operations)
                // Use the eTag from an earlier read of the entity, not a literal value
                .ifMatch("a1b2c3d4e5f6")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Patched " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncUpdateEntityResult patched =
                (com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncUpdateEntityResult) recorder.awaitOk("updateEntity");
        assertPrinted(out, "Patched product-sneaker-42, new eTag " + patched.getData().getETag());
        assertNoFailure(out);
        Map<String, Object> payload = patched.getData().getPayload();
        assertEquals(79.99, ((Number) payload.get("price")).doubleValue());
        assertEquals(89.99, ((Number) payload.get("previousPrice")).doubleValue(), "copy kept the old price");
        assertEquals(8, ((Number) payload.get("stock")).intValue());
        assertEquals(true, payload.get("onSale"), "add then move left the field under its new name");
        assertTrue(!payload.containsKey("sale"), "move removed the source");
        assertTrue(!payload.containsKey("legacySku"), "remove deleted the field");
        assertNotEquals(before, patched.getData().getETag());
        // The page says that the guard rejects the patch with a 412 once the entity changed since the read.
        PubNub guard = clients.javaClient(ROLE, "docs-guard");
        PubNubException error = assertThrows(PubNubException.class, () -> guard.dataSync().updateEntity(SNEAKER, Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/price").value(70.0).build())).ifMatch(before).sync());
        assertEquals(412, error.getStatusCode(), "the page says a stale eTag fails with 412");
    }

    /** Not a sample: the page says {@code expiresAt} is the creation time plus the class TTL (31 days for the product class) rounded up to a day. */
    @Test
    @Order(49)
    void expiresAtOfAProductFollowsTheClassTtl(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        com.pubnub.api.models.consumer.datasync.entity.PNDataSyncEntity product = server.getDataSync().getEntity(SNEAKER).sync().getData();
        DataSyncKit.assertExpiry(product.getCreatedAt(), product.getExpiresAt(), 31);
    }

    /** Not a sample: the page says the built in User class keeps an object for 30 days, with the same rounding. */
    @Test
    @Order(59)
    void expiresAtOfAUserFollowsTheBuiltInTtl(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser user = server.getDataSync().getUser(ALICE).sync().getData();
        DataSyncKit.assertExpiry(user.getCreatedAt(), user.getExpiresAt(), 30);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Relationships
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(50)
    void createRelationshipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.createRelationshipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("since", "2026-07-13");

        pubnub.dataSync().createRelationship("seller-bob", "product-sneaker-42", "ProductOwner", 1)
                .relationshipId("rel-bob-owns-sneaker-42")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created relationship " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncCreateRelationshipResult created = (PNDataSyncCreateRelationshipResult) recorder.awaitOk("createRelationship");
        assertPrinted(out, "Created relationship rel-bob-owns-sneaker-42 with eTag " + created.getData().getETag());
        assertNoFailure(out);
        com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship link = DataSyncKit.awaitReadJava("the link can be read",
                () -> server.getDataSync().getRelationship(LINK).sync().getData(),
                l -> assertEquals(mapOf("since", "2026-07-13"), l.getPayload()));
        assertEquals(BOB, link.getEntityAId());
        assertEquals(SNEAKER, link.getEntityBId());
        assertEquals("ProductOwner", link.getClassName());
        assertEquals(1, link.getClassVersion());
    }

    @Test
    @Order(51)
    void getRelationshipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the link can be read", () -> server.getDataSync().getRelationship(LINK).sync().getData(), l -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getRelationshipBasicUsage
        pubnub.dataSync().getRelationship("rel-bob-owns-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched relationship " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getRelationship");
        assertPrinted(out, "Fetched relationship rel-bob-owns-sneaker-42 with payload {since=2026-07-13}");
        assertNoFailure(out);
    }

    @Test
    @Order(52)
    void getRelationshipsBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of links shows the link",
                () -> assertEquals(Collections.singletonList(LINK), DataSyncKit.linkIds(server, null, null, null, null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getRelationshipsBasicUsage
        pubnub.dataSync().getRelationships("ProductOwner")
                .limit(10)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        recorder.awaitOk("getRelationships");
        assertPrintedLine(out, LINK);
        assertPrintedLine(out, "More pages available: false");
        assertNoFailure(out);
    }

    @Test
    @Order(53)
    void getRelationshipsByEntityBId(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the sneaker lists exactly the link",
                () -> assertEquals(Collections.singletonList(LINK), DataSyncKit.linkIds(server, null, SNEAKER, null, null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getRelationshipsByEntityBId
        pubnub.dataSync().getRelationships("ProductOwner")
                .entityBId("product-sneaker-42")
                .limit(50)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getEntityAId() + " owns " + item.getEntityBId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetRelationshipsResult result = (PNDataSyncGetRelationshipsResult) recorder.awaitOk("getRelationships");
        assertEquals(1, result.getData().size());
        assertEquals(LINK, result.getData().get(0).getId());
        assertPrintedLine(out, "seller-bob owns product-sneaker-42");
        assertPrintedLine(out, "More pages available: false");
        assertNoFailure(out);
    }

    @Test
    @Order(54)
    void setRelationshipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        String before = DataSyncKit.awaitReadJava("the link can be read", () -> server.getDataSync().getRelationship(LINK).sync().getData(),
                l -> { }).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.setRelationshipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("since", "2026-07-13");
        payload.put("tier", "gold");

        pubnub.dataSync().setRelationship("rel-bob-owns-sneaker-42", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced relationship " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult set =
                (com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult) recorder.awaitOk("setRelationship");
        assertPrinted(out, "Replaced relationship rel-bob-owns-sneaker-42, new eTag " + set.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, set.getData().getETag());
        assertEquals(BOB, set.getData().getEntityAId());
        assertEquals(SNEAKER, set.getData().getEntityBId());
        DataSyncKit.awaitReadJava("the link shows the tier", () -> server.getDataSync().getRelationship(LINK).sync().getData(),
                l -> assertEquals(mapOf("since", "2026-07-13", "tier", "gold"), l.getPayload()));
    }

    @Test
    @Order(55)
    void getRelationshipsFilterFast(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the sorted fast filter lists the link",
                () -> assertEquals(Collections.singletonList(LINK), DataSyncKit.linkIds(server, null, null, "tier == \"gold\"", null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getRelationshipsFilterFast
        // `tier` and `since` must be declared filterable (`simple` or `full`) on the `ProductOwner` class
        pubnub.dataSync().getRelationships("ProductOwner")
                .filterFast("tier == \"gold\"")
                .sort(Collections.singletonList(new PNDataSyncSortField("since", false)))
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetRelationshipsResult result = (PNDataSyncGetRelationshipsResult) recorder.awaitOk("getRelationships");
        assertEquals(1, result.getData().size());
        assertEquals(LINK, result.getData().get(0).getId());
        assertPrintedLine(out, LINK);
        assertNoFailure(out);
    }

    @Test
    @Order(56)
    void getRelationshipsFilter(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the filter index lists the link",
                () -> assertEquals(Collections.singletonList(LINK), DataSyncKit.linkIds(server, null, null, null, "tier LIKE \"gold*\"")));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.getRelationshipsFilter
        // `filter` only accepts properties declared with `full` filtering on the `ProductOwner` class
        pubnub.dataSync().getRelationships("ProductOwner")
                .filter("tier LIKE \"gold*\"")
                .limit(20)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        PNDataSyncGetRelationshipsResult result = (PNDataSyncGetRelationshipsResult) recorder.awaitOk("getRelationships");
        assertEquals(1, result.getData().size());
        assertEquals(LINK, result.getData().get(0).getId());
        assertPrintedLine(out, LINK);
        assertNoFailure(out);
    }

    @Test
    @Order(57)
    void getRelationshipsPagination(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.settled("the list of links shows the link",
                () -> assertEquals(Collections.singletonList(LINK), DataSyncKit.linkIds(server, null, null, null, null)));
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        fetchRelationshipsPage(pubnub, null);

        recorder.awaitOk("getRelationships");
        assertPrintedLine(out, LINK);
        assertNoFailure(out);
    }

    // snippet.getRelationshipsPagination
    private void fetchRelationshipsPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getRelationships("ProductOwner")
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchRelationshipsPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchRelationshipsPage(pubnub, null)
    // snippet.end

    @Test
    @Order(58)
    void updateRelationshipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        String before = DataSyncKit.awaitReadJava("the link shows the gold tier", () -> server.getDataSync().getRelationship(LINK).sync().getData(),
                l -> assertEquals("gold", l.getPayload().get("tier"))).getETag();
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.updateRelationshipBasicUsage
        pubnub.dataSync().updateRelationship("rel-bob-owns-sneaker-42", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/tier").value("platinum").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated relationship " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult updated =
                (com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult) recorder.awaitOk("updateRelationship");
        assertEquals("platinum", updated.getData().getPayload().get("tier"));
        assertEquals("2026-07-13", updated.getData().getPayload().get("since"));
        assertPrinted(out, "Updated relationship rel-bob-owns-sneaker-42, new eTag " + updated.getData().getETag());
        assertNoFailure(out);
        assertNotEquals(before, updated.getData().getETag());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Removal of a link and a membership, then the events of the product, then the removal of the rest
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(80)
    void removeRelationshipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the link can be read", () -> server.getDataSync().getRelationship(LINK).sync().getData(), l -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.removeRelationshipBasicUsage
        pubnub.dataSync().removeRelationship("rel-bob-owns-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed relationship, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        assertEquals(200, ((PNDataSyncRemoveRelationshipResult) recorder.awaitOk("removeRelationship")).getStatus());
        assertPrinted(out, "Removed relationship, HTTP status 200");
        assertNoFailure(out);
        DataSyncKit.awaitMissing("the link", () -> server.getDataSync().getRelationship(LINK).sync());
    }

    @Test
    @Order(81)
    void removeMembershipBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the membership can be read", () -> server.getDataSync().getMembership(SUMMER_MEMBERSHIP).sync().getData(),
                m -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.removeMembershipBasicUsage
        pubnub.dataSync().removeMembership("membership-alice-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed membership, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        assertEquals(200, ((PNDataSyncRemoveMembershipResult) recorder.awaitOk("removeMembership")).getStatus());
        assertPrinted(out, "Removed membership, HTTP status 200");
        assertNoFailure(out);
        DataSyncKit.awaitMissing("the membership", () -> server.getDataSync().getMembership(SUMMER_MEMBERSHIP).sync());
    }

    @Test
    @Order(82)
    void dataSyncEventListener(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        Stdout out = Stdout.captureForTest();
        JavaWait.Expectation ready = JavaWait.connected(client, Collections.singletonList(SNEAKER), Collections.<String>emptyList());

        // snippet.dataSyncEventListener
        Subscription subscription = pubnub.channel("product-sneaker-42").subscription();

        subscription.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                PNDataSyncEventMessage message = result.getExtractedMessage();
                if (message instanceof PNSetDataSyncUserEventMessage) {
                    PNSetDataSyncUserEventMessage set = (PNSetDataSyncUserEventMessage) message;
                    System.out.println("User " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncChannelEventMessage) {
                    PNSetDataSyncChannelEventMessage set = (PNSetDataSyncChannelEventMessage) message;
                    System.out.println("Channel " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncMembershipEventMessage) {
                    PNSetDataSyncMembershipEventMessage set = (PNSetDataSyncMembershipEventMessage) message;
                    System.out.println("Membership " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncEntityEventMessage) {
                    PNSetDataSyncEntityEventMessage set = (PNSetDataSyncEntityEventMessage) message;
                    System.out.println("Entity " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncRelationshipEventMessage) {
                    PNSetDataSyncRelationshipEventMessage set = (PNSetDataSyncRelationshipEventMessage) message;
                    System.out.println("Relationship " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNDeleteDataSyncUserEventMessage) {
                    PNDeleteDataSyncUserEventMessage deleted = (PNDeleteDataSyncUserEventMessage) message;
                    System.out.println("User " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncChannelEventMessage) {
                    PNDeleteDataSyncChannelEventMessage deleted = (PNDeleteDataSyncChannelEventMessage) message;
                    System.out.println("Channel " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncMembershipEventMessage) {
                    PNDeleteDataSyncMembershipEventMessage deleted = (PNDeleteDataSyncMembershipEventMessage) message;
                    System.out.println("Membership " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncEntityEventMessage) {
                    PNDeleteDataSyncEntityEventMessage deleted = (PNDeleteDataSyncEntityEventMessage) message;
                    System.out.println("Entity " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncRelationshipEventMessage) {
                    PNDeleteDataSyncRelationshipEventMessage deleted = (PNDeleteDataSyncRelationshipEventMessage) message;
                    System.out.println("Relationship " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else {
                    System.out.println("Unknown DataSync event of type " + message.getType());
                }
            }
        });

        subscription.subscribe();
        // snippet.end

        ready.await();
        // update, delete and create of the product, each one reaches the listener of the sample
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 74.99, 7);
        assertPrinted(out, "Entity product-sneaker-42 " + PNDataSyncSetEventType.UPDATE);
        server.getDataSync().removeEntity(SNEAKER, null).sync();
        assertPrinted(out, "Entity product-sneaker-42 deleted at ");
        DataSyncKit.createProduct(server, SNEAKER, "Retro Sneaker", 74.99, 7);
        assertPrinted(out, "Entity product-sneaker-42 " + PNDataSyncSetEventType.CREATE);
        assertTrue(out.lines("Unknown DataSync event").isEmpty(), "no event fell into the else branch");
    }

    @Test
    @Order(83)
    void subscribeToProjectionChannels(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        PubNub client = clients.javaClient(ROLE, "docs-listener");
        PubNub pubnub = recorder.wrap(client, PubNub.class);
        String projection = "__admin__" + SNEAKER;
        DataSyncJavaKit.Events events = new DataSyncJavaKit.Events(client);
        JavaWait.Expectation ready = JavaWait.connected(client, Arrays.asList(SNEAKER, projection), Collections.<String>emptyList());

        // snippet.subscribeToProjectionChannels
        DataSyncEntity entity = pubnub.dataSyncEntity("product-sneaker-42");

        Subscription defaultSubscription = entity.subscription();
        Subscription adminSubscription = entity.subscription("admin");

        defaultSubscription.subscribe();
        adminSubscription.subscribe();
        // snippet.end

        ready.await();
        assertEquals(new HashSet<>(Arrays.asList(SNEAKER, projection)), new HashSet<>(client.getSubscribedChannels()));
        // One change of the product publishes two events, one on the channel of the object and one on the channel of the projection.
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 69.5, 6);
        PNDataSyncEventResult onDefault = events.await("the update on the default projection channel", 30_000,
                e -> e.getChannel().equals(SNEAKER) && DataSyncKit.isSet(e, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE));
        PNDataSyncEventResult onAdmin = events.await("the update on the admin projection channel", 30_000,
                e -> e.getChannel().equals(projection) && DataSyncKit.isSet(e, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE));
        assertEquals("entity", onDefault.getExtractedMessage().getType());
        assertEquals("product", onAdmin.getExtractedMessage().getClassName());
        events.close();
    }

    @Test
    @Order(84)
    void removeEntityBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the product can be read", () -> server.getDataSync().getEntity(SNEAKER).sync().getData(), e -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.removeEntityBasicUsage
        pubnub.dataSync().removeEntity("product-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed entity, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        assertEquals(200, ((PNDataSyncRemoveEntityResult) recorder.awaitOk("removeEntity")).getStatus());
        assertPrinted(out, "Removed entity, HTTP status 200");
        assertNoFailure(out);
        DataSyncKit.awaitMissing("the product", () -> server.getDataSync().getEntity(SNEAKER).sync());
    }

    @Test
    @Order(85)
    void removeChannelBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("the channel can be read", () -> server.getDataSync().getChannel(SUMMER).sync().getData(), c -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.removeChannelBasicUsage
        pubnub.dataSync().removeChannel("channel-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed channel, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        assertEquals(200, ((PNDataSyncRemoveChannelResult) recorder.awaitOk("removeChannel")).getStatus());
        assertPrinted(out, "Removed channel, HTTP status 200");
        assertNoFailure(out);
        DataSyncKit.awaitMissing("the channel", () -> server.getDataSync().getChannel(SUMMER).sync());
    }

    @Test
    @Order(86)
    void removeUserBasicUsage(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients);
        DataSyncKit.awaitReadJava("Alice can be read", () -> server.getDataSync().getUser(ALICE).sync().getData(), u -> { });
        PubNub pubnub = recorder.wrap(javaClient(clients), PubNub.class);
        Stdout out = Stdout.captureForTest();

        // snippet.removeUserBasicUsage
        pubnub.dataSync().removeUser("user-alice")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed user, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end

        assertEquals(200, ((PNDataSyncRemoveUserResult) recorder.awaitOk("removeUser")).getStatus());
        assertPrinted(out, "Removed user, HTTP status 200");
        assertNoFailure(out);
        DataSyncKit.awaitMissing("Alice", () -> server.getDataSync().getUser(ALICE).sync());
    }
}
