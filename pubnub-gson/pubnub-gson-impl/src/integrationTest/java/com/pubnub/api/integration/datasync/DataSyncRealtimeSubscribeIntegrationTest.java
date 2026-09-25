package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.callbacks.StatusListener;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.PNStatus;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * End-to-end DataSync realtime subscribe ({@code e=5}) through the Java/GSON SDK, across every element type
 * (User, Channel, Entity, Membership, Relationship). Each element is covered by two tests — one that reads
 * events through {@code addListener(EventListener)}, and one through the {@code setOnDataSync(...)}
 * lambda-setter — so both delivery paths a customer can use are exercised end to end.
 *
 * <p>There is no {@code dataSyncMembership}/{@code dataSyncRelationship} handle: a Membership/Relationship
 * event is published to <b>both endpoint ref-channels</b>, so a customer hears it by subscribing to an
 * <i>endpoint</i> — a Channel ref for a Membership, an Entity ref for a Relationship.
 *
 * <p>Each element's write lifecycle maps onto the realtime leaves:
 * <ul>
 *   <li>create &rarr; {@code PNSet…EventMessage} with {@code event == PNDataSyncSetEventType.CREATE}</li>
 *   <li>patch / full replace &rarr; {@code PNSet…EventMessage} with {@code event == PNDataSyncSetEventType.UPDATE}</li>
 *   <li>remove &rarr; {@code PNDelete…EventMessage}</li>
 * </ul>
 *
 * <p>Uses {@code server} (secretKey) for both subscribe and CRUD. Each milestone is awaited by its own latch —
 * never by an exact event count (the backend may fan out cascade/duplicate deliveries).
 *
 * <p>Membership/Relationship tests require the keyset classes provisioned by
 * {@code scripts/datasync/create-classes.sh} (Channel/User built-ins for memberships; TestNode + TestFriendship
 * for relationships).
 */
public class DataSyncRealtimeSubscribeIntegrationTest extends BaseIntegrationTest {
    private static final int CLASS_VERSION = 1;

    @Override
    protected void onBefore() {
        server = getServer();
    }

    private static Map<String, Object> payload(String key, String value) {
        final Map<String, Object> payload = new HashMap<>();
        payload.put(key, value);
        return payload;
    }

    private static Map<String, Object> userPayload(String username, String email) {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("email", email);
        return payload;
    }

    /**
     * Mints a token (scoped to plain PubSub {@code read} on the given DataSync ref-channels) and hands back a
     * PAM-only client authenticated with it. A realtime subscribe on a DataSync ref-channel is authorized by
     * the ordinary channel-{@code read} PAM check — the backend only <i>publishes</i> the events; no
     * DataSync-specific grant is consulted on the subscribe/receive path — so channel {@code read} on the exact
     * ref-channel string is all the subscriber needs. {@code server} (secretKey) still performs every CRUD write.
     */
    private PubNub authorizedSubscriber(String... refChannels) throws PubNubException {
        final PubNub client = getAuthorizedClient();
        final List<TokenGrant> grants = new ArrayList<>();
        for (String refChannel : refChannels) {
            grants.add(ChannelGrant.name(refChannel).read());
        }
        final String token = server.grantToken(60)
                .authorizedUserId(client.getConfiguration().getUserId())
                .grants(grants)
                .sync()
                .getToken();
        client.setToken(token);
        return client;
    }

    /**
     * Subscribes and blocks until {@code client}'s subscribe loop is actually connected, instead of sleeping on
     * a fixed guess. Publishing before the receive loop is up would drop the realtime CREATE (e=5 events are not
     * replayed from history on connect), so the writes must wait for
     * {@link PNStatusCategory#PNConnectedCategory}.
     */
    private void subscribeAndAwaitConnect(PubNub client, Subscription subscription) throws InterruptedException {
        final CountDownLatch connected = new CountDownLatch(1);
        client.addListener(new StatusListener() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    connected.countDown();
                }
            }
        });
        subscription.subscribe();
        assertTrue("subscribe loop did not connect", connected.await(15, TimeUnit.SECONDS));
    }

    @Test
    public void receivesUserEventsViaAddListener() throws PubNubException, InterruptedException {
        final String userId = "user-rt-" + RandomStringUtils.random(8, "abcdefgh");
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(2); // patch + full replace both fire UPDATE
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncUserEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncUserEventMessage> deleteLeaf = new AtomicReference<>();

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        final PubNub client = authorizedSubscriber(userId);
        final Subscription subscription = client.dataSyncUser(userId).subscription();
        subscription.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                if (result.getExtractedMessage() instanceof PNSetDataSyncUserEventMessage) {
                    final PNSetDataSyncUserEventMessage set = (PNSetDataSyncUserEventMessage) result.getExtractedMessage();
                    if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                        createLeaf.set(set);
                        sawCreate.countDown();
                    } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                        sawUpdate.countDown();
                    }
                } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncUserEventMessage) {
                    deleteLeaf.set((PNDeleteDataSyncUserEventMessage) result.getExtractedMessage());
                    sawDelete.countDown();
                }
            }
        });
        subscribeAndAwaitConnect(client, subscription);

        try {
            server.dataSync().createUser(CLASS_VERSION).userId(userId).status("active").payload(payload("username", "Alice")).sync();
            server.dataSync().updateUser(userId, Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build())).sync();
            server.dataSync().setUser(userId, CLASS_VERSION).status("archived").payload(payload("username", "Alice Cooper")).sync();
            server.dataSync().removeUser(userId).sync();

            assertTrue("Expected a create user event", sawCreate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected two update user events (patch + full replace)", sawUpdate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected a delete user event", sawDelete.await(15, TimeUnit.SECONDS));

            assertEquals(userId, createLeaf.get().getData().getId());
            assertEquals("active", createLeaf.get().getData().getStatus());
            assertEquals("Alice", createLeaf.get().getData().getPayload().get("username"));
            assertEquals(userId, deleteLeaf.get().getId());
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
        } finally {
            destroyClient(client);
        }
    }

    @Test
    public void receivesUserEventsViaOnDataSync() throws PubNubException, InterruptedException {
        final String userId = "user-rt-" + RandomStringUtils.random(8, "abcdefgh");
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(2);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncUserEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncUserEventMessage> deleteLeaf = new AtomicReference<>();

        final Subscription subscription = server.dataSyncUser(userId).subscription();
        subscription.setOnDataSync(result -> {
            if (result.getExtractedMessage() instanceof PNSetDataSyncUserEventMessage) {
                final PNSetDataSyncUserEventMessage set = (PNSetDataSyncUserEventMessage) result.getExtractedMessage();
                if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                    createLeaf.set(set);
                    sawCreate.countDown();
                } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                    sawUpdate.countDown();
                }
            } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncUserEventMessage) {
                deleteLeaf.set((PNDeleteDataSyncUserEventMessage) result.getExtractedMessage());
                sawDelete.countDown();
            }
        });
        subscribeAndAwaitConnect(server, subscription);

        server.dataSync().createUser(CLASS_VERSION).userId(userId).status("active").payload(payload("username", "Alice")).sync();
        server.dataSync().updateUser(userId, Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build())).sync();
        server.dataSync().setUser(userId, CLASS_VERSION).status("archived").payload(payload("username", "Alice Cooper")).sync();
        server.dataSync().removeUser(userId).sync();

        assertTrue("Expected a create user event", sawCreate.await(15, TimeUnit.SECONDS));
        assertTrue("Expected two update user events (patch + full replace)", sawUpdate.await(15, TimeUnit.SECONDS));
        assertTrue("Expected a delete user event", sawDelete.await(15, TimeUnit.SECONDS));

        assertEquals(userId, createLeaf.get().getData().getId());
        assertEquals("active", createLeaf.get().getData().getStatus());
        assertEquals("Alice", createLeaf.get().getData().getPayload().get("username"));
        assertEquals(userId, deleteLeaf.get().getId());
        assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
    }

    @Test
    public void receivesChannelEventsViaAddListener() throws PubNubException, InterruptedException {
        final String channelId = "channel-rt-" + RandomStringUtils.random(8, "abcdefgh");
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(2);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncChannelEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncChannelEventMessage> deleteLeaf = new AtomicReference<>();

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        final PubNub client = authorizedSubscriber(channelId);
        final Subscription subscription = client.dataSyncChannel(channelId).subscription();
        subscription.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                if (result.getExtractedMessage() instanceof PNSetDataSyncChannelEventMessage) {
                    final PNSetDataSyncChannelEventMessage set = (PNSetDataSyncChannelEventMessage) result.getExtractedMessage();
                    if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                        createLeaf.set(set);
                        sawCreate.countDown();
                    } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                        sawUpdate.countDown();
                    }
                } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncChannelEventMessage) {
                    deleteLeaf.set((PNDeleteDataSyncChannelEventMessage) result.getExtractedMessage());
                    sawDelete.countDown();
                }
            }
        });
        subscribeAndAwaitConnect(client, subscription);

        try {
            server.dataSync().createChannel(CLASS_VERSION).channelId(channelId).status("active").payload(payload("name", "Chan-A")).sync();
            server.dataSync().updateChannel(channelId, Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build())).sync();
            server.dataSync().setChannel(channelId, CLASS_VERSION).status("archived").payload(payload("name", "Chan-B")).sync();
            server.dataSync().removeChannel(channelId).sync();

            assertTrue("Expected a create channel event", sawCreate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected two update channel events", sawUpdate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected a delete channel event", sawDelete.await(15, TimeUnit.SECONDS));

            assertEquals(channelId, createLeaf.get().getData().getId());
            assertEquals("active", createLeaf.get().getData().getStatus());
            assertEquals(channelId, deleteLeaf.get().getId());
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
        } finally {
            destroyClient(client);
        }
    }

    @Test
    public void receivesChannelEventsViaOnDataSync() throws PubNubException, InterruptedException {
        final String channelId = "channel-rt-" + RandomStringUtils.random(8, "abcdefgh");
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(2);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncChannelEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncChannelEventMessage> deleteLeaf = new AtomicReference<>();

        final Subscription subscription = server.dataSyncChannel(channelId).subscription();
        subscription.setOnDataSync(result -> {
            if (result.getExtractedMessage() instanceof PNSetDataSyncChannelEventMessage) {
                final PNSetDataSyncChannelEventMessage set = (PNSetDataSyncChannelEventMessage) result.getExtractedMessage();
                if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                    createLeaf.set(set);
                    sawCreate.countDown();
                } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                    sawUpdate.countDown();
                }
            } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncChannelEventMessage) {
                deleteLeaf.set((PNDeleteDataSyncChannelEventMessage) result.getExtractedMessage());
                sawDelete.countDown();
            }
        });
        subscribeAndAwaitConnect(server, subscription);

        server.dataSync().createChannel(CLASS_VERSION).channelId(channelId).status("active").payload(payload("name", "Chan-A")).sync();
        server.dataSync().updateChannel(channelId, Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build())).sync();
        server.dataSync().setChannel(channelId, CLASS_VERSION).status("archived").payload(payload("name", "Chan-B")).sync();
        server.dataSync().removeChannel(channelId).sync();

        assertTrue("Expected a create channel event", sawCreate.await(15, TimeUnit.SECONDS));
        assertTrue("Expected two update channel events", sawUpdate.await(15, TimeUnit.SECONDS));
        assertTrue("Expected a delete channel event", sawDelete.await(15, TimeUnit.SECONDS));

        assertEquals(channelId, createLeaf.get().getData().getId());
        assertEquals("active", createLeaf.get().getData().getStatus());
        assertEquals(channelId, deleteLeaf.get().getId());
        assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
    }

    @Test
    public void receivesEntityEventsViaAddListener() throws PubNubException, InterruptedException {
        final String entityId = "entity-rt-" + RandomStringUtils.random(8, "abcdefgh");
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(2);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncEntityEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncEntityEventMessage> deleteLeaf = new AtomicReference<>();

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        final PubNub client = authorizedSubscriber(entityId);
        final Subscription subscription = client.dataSyncEntity(entityId).subscription();
        subscription.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                if (result.getExtractedMessage() instanceof PNSetDataSyncEntityEventMessage) {
                    final PNSetDataSyncEntityEventMessage set = (PNSetDataSyncEntityEventMessage) result.getExtractedMessage();
                    if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                        createLeaf.set(set);
                        sawCreate.countDown();
                    } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                        sawUpdate.countDown();
                    }
                } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncEntityEventMessage) {
                    deleteLeaf.set((PNDeleteDataSyncEntityEventMessage) result.getExtractedMessage());
                    sawDelete.countDown();
                }
            }
        });
        subscribeAndAwaitConnect(client, subscription);

        try {
            // `TestUser` declares `email` in the `admin` projection only; `username` is in `__default__`. This
            // subscription is on the bare ref (the `__default__` channel), so the realtime snapshot must carry
            // `username` but NOT `email`. `server` holds the secretKey, so it bypasses the projection write-guard
            // and can write the admin-only `email` directly (a __default__-projection token would be rejected
            // with DS-0202).
            server.dataSync().createEntity("TestUser", CLASS_VERSION).entityId(entityId).status("active").payload(userPayload("Alice", "alice@example.com")).sync();
            server.dataSync().updateEntity(entityId, Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build())).sync();
            server.dataSync().setEntity(entityId, CLASS_VERSION).status("archived").payload(userPayload("Alice Cooper", "cooper@example.com")).sync();
            server.dataSync().removeEntity(entityId).sync();

            assertTrue("Expected a create entity event", sawCreate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected two update entity events", sawUpdate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected a delete entity event", sawDelete.await(15, TimeUnit.SECONDS));

            assertEquals(entityId, createLeaf.get().getData().getId());
            // default-projection ref carries `username` but hides the admin-only `email`.
            assertEquals("Alice", createLeaf.get().getData().getPayload().get("username"));
            assertNull("default projection must omit the admin-only email field", createLeaf.get().getData().getPayload().get("email"));
            assertEquals(entityId, deleteLeaf.get().getId());
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
        } finally {
            destroyClient(client);
        }
    }

    @Test
    public void receivesEntityEventsViaOnDataSync() throws PubNubException, InterruptedException {
        final String entityId = "entity-rt-" + RandomStringUtils.random(8, "abcdefgh");
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(2);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncEntityEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncEntityEventMessage> deleteLeaf = new AtomicReference<>();

        final Subscription subscription = server.dataSyncEntity(entityId).subscription();
        subscription.setOnDataSync(result -> {
            if (result.getExtractedMessage() instanceof PNSetDataSyncEntityEventMessage) {
                final PNSetDataSyncEntityEventMessage set = (PNSetDataSyncEntityEventMessage) result.getExtractedMessage();
                if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                    createLeaf.set(set);
                    sawCreate.countDown();
                } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                    sawUpdate.countDown();
                }
            } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncEntityEventMessage) {
                deleteLeaf.set((PNDeleteDataSyncEntityEventMessage) result.getExtractedMessage());
                sawDelete.countDown();
            }
        });
        subscribeAndAwaitConnect(server, subscription);

        // `TestUser` declares `email` in the `admin` projection only; `username` is in `__default__`. This
        // subscription is on the bare ref (the `__default__` channel), so the realtime snapshot must carry
        // `username` but NOT `email`. `server` holds the secretKey, so it bypasses the projection write-guard
        // and can write the admin-only `email` directly (a __default__-projection token would be rejected
        // with DS-0202).
        server.dataSync().createEntity("TestUser", CLASS_VERSION).entityId(entityId).status("active").payload(userPayload("Alice", "alice@example.com")).sync();
        server.dataSync().updateEntity(entityId, Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build())).sync();
        server.dataSync().setEntity(entityId, CLASS_VERSION).status("archived").payload(userPayload("Alice Cooper", "cooper@example.com")).sync();
        server.dataSync().removeEntity(entityId).sync();

        assertTrue("Expected a create entity event", sawCreate.await(15, TimeUnit.SECONDS));
        assertTrue("Expected two update entity events", sawUpdate.await(15, TimeUnit.SECONDS));
        assertTrue("Expected a delete entity event", sawDelete.await(15, TimeUnit.SECONDS));

        assertEquals(entityId, createLeaf.get().getData().getId());
        // default-projection ref carries `username` but hides the admin-only `email`.
        assertEquals("Alice", createLeaf.get().getData().getPayload().get("username"));
        assertNull("default projection must omit the admin-only email field", createLeaf.get().getData().getPayload().get("email"));
        assertEquals(entityId, deleteLeaf.get().getId());
        assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
    }

    @Test
    public void receivesMembershipEventsViaAddListener() throws PubNubException, InterruptedException {
        final String run = RandomStringUtils.random(8, "abcdefgh");
        final String channelId = "channel-m-" + run;
        final String userId = "user-m-" + run;
        final String membershipId = "membership-" + run;
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(1);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncMembershipEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncMembershipEventMessage> deleteLeaf = new AtomicReference<>();

        server.dataSync().createChannel(CLASS_VERSION).channelId(channelId).payload(payload("name", "Chan-" + run)).sync();
        server.dataSync().createUser(CLASS_VERSION).userId(userId).payload(payload("name", "User-" + run)).sync();
        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        // A membership is published to both endpoint refs; subscribe to the Channel endpoint to hear it.
        final PubNub client = authorizedSubscriber(channelId);
        try {
            final Subscription subscription = client.dataSyncChannel(channelId).subscription();
            subscription.addListener(new EventListener() {
                @Override
                public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                    if (result.getExtractedMessage() instanceof PNSetDataSyncMembershipEventMessage) {
                        final PNSetDataSyncMembershipEventMessage set = (PNSetDataSyncMembershipEventMessage) result.getExtractedMessage();
                        if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                            createLeaf.set(set);
                            sawCreate.countDown();
                        } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                            sawUpdate.countDown();
                        }
                    } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncMembershipEventMessage) {
                        deleteLeaf.set((PNDeleteDataSyncMembershipEventMessage) result.getExtractedMessage());
                        sawDelete.countDown();
                    }
                }
            });
            subscribeAndAwaitConnect(client, subscription);

            server.dataSync().createMembership(channelId, userId, CLASS_VERSION).membershipId(membershipId).status("active").sync();
            server.dataSync().setMembership(membershipId, CLASS_VERSION).status("archived").sync();
            server.dataSync().removeMembership(membershipId).sync();

            assertTrue("Expected a create membership event", sawCreate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected an update membership event", sawUpdate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected a delete membership event", sawDelete.await(15, TimeUnit.SECONDS));

            assertEquals(membershipId, createLeaf.get().getData().getId());
            assertEquals(channelId, createLeaf.get().getData().getChannelId());
            assertEquals(userId, createLeaf.get().getData().getUserId());
            assertEquals(membershipId, deleteLeaf.get().getId());
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
        } finally {
            destroyClient(client);
            quietRemoveChannel(channelId);
            quietRemoveUser(userId);
        }
    }

    @Test
    public void receivesMembershipEventsViaOnDataSync() throws PubNubException, InterruptedException {
        final String run = RandomStringUtils.random(8, "abcdefgh");
        final String channelId = "channel-m-" + run;
        final String userId = "user-m-" + run;
        final String membershipId = "membership-" + run;
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(1);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncMembershipEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncMembershipEventMessage> deleteLeaf = new AtomicReference<>();

        server.dataSync().createChannel(CLASS_VERSION).channelId(channelId).payload(payload("name", "Chan-" + run)).sync();
        server.dataSync().createUser(CLASS_VERSION).userId(userId).payload(payload("name", "User-" + run)).sync();
        try {
            final Subscription subscription = server.dataSyncChannel(channelId).subscription();
            subscription.setOnDataSync(result -> {
                if (result.getExtractedMessage() instanceof PNSetDataSyncMembershipEventMessage) {
                    final PNSetDataSyncMembershipEventMessage set = (PNSetDataSyncMembershipEventMessage) result.getExtractedMessage();
                    if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                        createLeaf.set(set);
                        sawCreate.countDown();
                    } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                        sawUpdate.countDown();
                    }
                } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncMembershipEventMessage) {
                    deleteLeaf.set((PNDeleteDataSyncMembershipEventMessage) result.getExtractedMessage());
                    sawDelete.countDown();
                }
            });
            subscribeAndAwaitConnect(server, subscription);

            server.dataSync().createMembership(channelId, userId, CLASS_VERSION).membershipId(membershipId).status("active").sync();
            server.dataSync().setMembership(membershipId, CLASS_VERSION).status("archived").sync();
            server.dataSync().removeMembership(membershipId).sync();

            assertTrue("Expected a create membership event", sawCreate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected an update membership event", sawUpdate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected a delete membership event", sawDelete.await(15, TimeUnit.SECONDS));

            assertEquals(membershipId, createLeaf.get().getData().getId());
            assertEquals(channelId, createLeaf.get().getData().getChannelId());
            assertEquals(userId, createLeaf.get().getData().getUserId());
            assertEquals(membershipId, deleteLeaf.get().getId());
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
        } finally {
            quietRemoveChannel(channelId);
            quietRemoveUser(userId);
        }
    }

    @Test
    public void receivesRelationshipEventsViaAddListener() throws PubNubException, InterruptedException {
        final String run = RandomStringUtils.random(8, "abcdefgh");
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String relationshipId = "relationship-" + run;
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(1);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncRelationshipEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncRelationshipEventMessage> deleteLeaf = new AtomicReference<>();

        server.dataSync().createEntity("TestNode", CLASS_VERSION).entityId(entityAId).payload(payload("name", "NodeA-" + run)).sync();
        server.dataSync().createEntity("TestNode", CLASS_VERSION).entityId(entityBId).payload(payload("name", "NodeB-" + run)).sync();
        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        // A relationship is published to both endpoint refs; subscribe to entity A's ref to hear it.
        final PubNub client = authorizedSubscriber(entityAId);
        try {
            final Subscription subscription = client.dataSyncEntity(entityAId).subscription();
            subscription.addListener(new EventListener() {
                @Override
                public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                    if (result.getExtractedMessage() instanceof PNSetDataSyncRelationshipEventMessage) {
                        final PNSetDataSyncRelationshipEventMessage set = (PNSetDataSyncRelationshipEventMessage) result.getExtractedMessage();
                        if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                            createLeaf.set(set);
                            sawCreate.countDown();
                        } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                            sawUpdate.countDown();
                        }
                    } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncRelationshipEventMessage) {
                        deleteLeaf.set((PNDeleteDataSyncRelationshipEventMessage) result.getExtractedMessage());
                        sawDelete.countDown();
                    }
                }
            });
            subscribeAndAwaitConnect(client, subscription);

            server.dataSync().createRelationship(entityAId, entityBId, "TestFriendship", CLASS_VERSION).relationshipId(relationshipId).status("active").sync();
            server.dataSync().setRelationship(relationshipId, CLASS_VERSION).status("archived").sync();
            server.dataSync().removeRelationship(relationshipId).sync();

            assertTrue("Expected a create relationship event", sawCreate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected an update relationship event", sawUpdate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected a delete relationship event", sawDelete.await(15, TimeUnit.SECONDS));

            assertEquals(relationshipId, createLeaf.get().getData().getId());
            assertEquals(entityAId, createLeaf.get().getData().getEntityAId());
            assertEquals(entityBId, createLeaf.get().getData().getEntityBId());
            assertEquals(relationshipId, deleteLeaf.get().getId());
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
        } finally {
            destroyClient(client);
            quietRemoveEntity(entityAId);
            quietRemoveEntity(entityBId);
        }
    }

    @Test
    public void receivesRelationshipEventsViaOnDataSync() throws PubNubException, InterruptedException {
        final String run = RandomStringUtils.random(8, "abcdefgh");
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String relationshipId = "relationship-" + run;
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final CountDownLatch sawUpdate = new CountDownLatch(1);
        final CountDownLatch sawDelete = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncRelationshipEventMessage> createLeaf = new AtomicReference<>();
        final AtomicReference<PNDeleteDataSyncRelationshipEventMessage> deleteLeaf = new AtomicReference<>();

        server.dataSync().createEntity("TestNode", CLASS_VERSION).entityId(entityAId).payload(payload("name", "NodeA-" + run)).sync();
        server.dataSync().createEntity("TestNode", CLASS_VERSION).entityId(entityBId).payload(payload("name", "NodeB-" + run)).sync();
        try {
            final Subscription subscription = server.dataSyncEntity(entityAId).subscription();
            subscription.setOnDataSync(result -> {
                if (result.getExtractedMessage() instanceof PNSetDataSyncRelationshipEventMessage) {
                    final PNSetDataSyncRelationshipEventMessage set = (PNSetDataSyncRelationshipEventMessage) result.getExtractedMessage();
                    if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                        createLeaf.set(set);
                        sawCreate.countDown();
                    } else if (set.getEvent() == PNDataSyncSetEventType.UPDATE) {
                        sawUpdate.countDown();
                    }
                } else if (result.getExtractedMessage() instanceof PNDeleteDataSyncRelationshipEventMessage) {
                    deleteLeaf.set((PNDeleteDataSyncRelationshipEventMessage) result.getExtractedMessage());
                    sawDelete.countDown();
                }
            });
            subscribeAndAwaitConnect(server, subscription);

            server.dataSync().createRelationship(entityAId, entityBId, "TestFriendship", CLASS_VERSION).relationshipId(relationshipId).status("active").sync();
            server.dataSync().setRelationship(relationshipId, CLASS_VERSION).status("archived").sync();
            server.dataSync().removeRelationship(relationshipId).sync();

            assertTrue("Expected a create relationship event", sawCreate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected an update relationship event", sawUpdate.await(15, TimeUnit.SECONDS));
            assertTrue("Expected a delete relationship event", sawDelete.await(15, TimeUnit.SECONDS));

            assertEquals(relationshipId, createLeaf.get().getData().getId());
            assertEquals(entityAId, createLeaf.get().getData().getEntityAId());
            assertEquals(entityBId, createLeaf.get().getData().getEntityBId());
            assertEquals(relationshipId, deleteLeaf.get().getId());
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf.get().getDeletedAt());
        } finally {
            quietRemoveEntity(entityAId);
            quietRemoveEntity(entityBId);
        }
    }

    @Test
    public void adminProjectionSubscriptionCarriesAdminOnlyField() throws PubNubException, InterruptedException {
        // Counterpart to the default-projection check in the `receivesEntityEvents*` tests (which subscribe
        // to the bare ref and see `username` but NOT the admin-only `email`). Subscribing with
        // `subscription("admin")` resolves to the `__admin__{id}` channel, which carries the admin-projection
        // fields — so the realtime snapshot DOES include `email`. `TestUser` declares `email` in the `admin`
        // projection only.
        final String entityId = "entity-proj-" + RandomStringUtils.random(8, "abcdefgh");
        final CountDownLatch sawCreate = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncEntityEventMessage> createLeaf = new AtomicReference<>();

        final Subscription subscription = server.dataSyncEntity(entityId).subscription("admin");
        subscription.setOnDataSync(result -> {
            if (result.getExtractedMessage() instanceof PNSetDataSyncEntityEventMessage) {
                final PNSetDataSyncEntityEventMessage set = (PNSetDataSyncEntityEventMessage) result.getExtractedMessage();
                if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                    createLeaf.set(set);
                    sawCreate.countDown();
                }
            }
        });
        subscribeAndAwaitConnect(server, subscription);

        try {
            // `server` holds the secretKey, so it bypasses the projection write-guard and can write the
            // admin-only `email` directly (a __default__-projection token would be rejected with DS-0202).
            server.dataSync().createEntity("TestUser", CLASS_VERSION).entityId(entityId).status("active").payload(userPayload("Alice", "alice@example.com")).sync();

            assertTrue("Expected a create entity event on the admin projection", sawCreate.await(15, TimeUnit.SECONDS));
            assertEquals(entityId, createLeaf.get().getData().getId());
            assertEquals("Alice", createLeaf.get().getData().getPayload().get("username"));
            assertEquals("alice@example.com", createLeaf.get().getData().getPayload().get("email"));
        } finally {
            quietRemoveEntity(entityId);
        }
    }

    @Test
    public void adminProjectionSubscriptionUnderTokenCarriesAdminOnlyField() throws PubNubException, InterruptedException {
        // Same admin-projection guarantee as `adminProjectionSubscriptionCarriesAdminOnlyField`, but the
        // subscriber is a PAM-only client that has NO secretKey — it authenticates purely with a token minted
        // by `server`. A realtime subscribe over PubSub still needs a plain channel `read` grant on the
        // resolved `__admin__{id}` ref-channel; the DataSync `entity(...).projection("admin")` grant only
        // carries the projection lens, not the pub/sub read bit (they are separate concerns). So the token
        // must carry both. `server` (secretKey) does the CRUD write, since a `__default__` token cannot write
        // the admin-only `email`.
        final String entityId = "entity-proj-token-" + RandomStringUtils.random(8, "abcdefgh");
        final String adminChannel = "__admin__" + entityId;
        final PubNub client = getAuthorizedClient();
        final UserId authorizedUserId = client.getConfiguration().getUserId();

        final String token = server.grantToken(60)
                .authorizedUserId(authorizedUserId)
                .grants(Arrays.asList(
                        ChannelGrant.name(adminChannel).read(),
                        DataSyncGrant.entity(entityId).get().projection("admin")
                ))
                .sync()
                .getToken();
        client.setToken(token);

        final CountDownLatch sawCreate = new CountDownLatch(1);
        final AtomicReference<PNSetDataSyncEntityEventMessage> createLeaf = new AtomicReference<>();

        final Subscription subscription = client.dataSyncEntity(entityId).subscription("admin");
        subscription.setOnDataSync(result -> {
            if (result.getExtractedMessage() instanceof PNSetDataSyncEntityEventMessage) {
                final PNSetDataSyncEntityEventMessage set = (PNSetDataSyncEntityEventMessage) result.getExtractedMessage();
                if (set.getEvent() == PNDataSyncSetEventType.CREATE) {
                    createLeaf.set(set);
                    sawCreate.countDown();
                }
            }
        });
        subscribeAndAwaitConnect(client, subscription);

        try {
            server.dataSync().createEntity("TestUser", CLASS_VERSION).entityId(entityId).status("active").payload(userPayload("Alice", "alice@example.com")).sync();

            assertTrue("Expected a create entity event on the admin projection under a PAM token", sawCreate.await(15, TimeUnit.SECONDS));
            assertEquals(entityId, createLeaf.get().getData().getId());
            assertEquals("Alice", createLeaf.get().getData().getPayload().get("username"));
            assertEquals("alice@example.com", createLeaf.get().getData().getPayload().get("email"));
        } finally {
            destroyClient(client);
            quietRemoveEntity(entityId);
        }
    }

    private void quietRemoveChannel(String channelId) {
        try {
            server.dataSync().removeChannel(channelId).sync();
        } catch (Exception ignored) {
        }
    }

    private void quietRemoveUser(String userId) {
        try {
            server.dataSync().removeUser(userId).sync();
        } catch (Exception ignored) {
        }
    }

    private void quietRemoveEntity(String entityId) {
        try {
            server.dataSync().removeEntity(entityId).sync();
        } catch (Exception ignored) {
        }
    }
}