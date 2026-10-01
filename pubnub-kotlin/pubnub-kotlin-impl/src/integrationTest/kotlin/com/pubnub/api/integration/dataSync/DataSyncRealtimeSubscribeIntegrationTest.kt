package com.pubnub.api.integration.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.TokenGrant
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.test.CommonUtils
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * End-to-end DataSync realtime subscribe (`e=5`) across every element type (User, Channel, Entity,
 * Membership, Relationship). Each element is covered by two tests — one that reads events through
 * `addListener(EventListener)`, and one through the `onDataSync = { … }` lambda-setter — so both delivery
 * paths a customer can use are exercised end to end.
 *
 * There is no `dataSyncMembership`/`dataSyncRelationship` handle: a Membership/Relationship event is
 * published to **both endpoint ref-channels**, so a customer hears it by subscribing to an *endpoint* — a
 * Channel ref for a Membership, an Entity ref for a Relationship.
 *
 * Each element's write lifecycle maps onto the realtime leaves:
 *  - create -> `PNSet…EventMessage` with `event == PNDataSyncSetEventType.CREATE`
 *  - patch / full replace -> `PNSet…EventMessage` with `event == PNDataSyncSetEventType.UPDATE`
 *  - remove -> `PNDelete…EventMessage`
 *
 * Uses `server` (secretKey) for both subscribe and CRUD. Each milestone is awaited by its own latch —
 * never by an exact event count (the backend may fan out cascade/duplicate deliveries).
 *
 * Membership/Relationship tests require the keyset classes provisioned by
 * `scripts/datasync/create-classes.sh` (Channel/User built-ins for memberships; TestNode + TestFriendship
 * for relationships).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataSyncRealtimeSubscribeIntegrationTest : BaseIntegrationTest() {
    private val classVersion = 1

    /**
     * Mints a token carrying [grants] and hands back a PAM-only client authenticated with it. A realtime
     * subscribe on a DataSync ref-channel is authorized by the ordinary channel-`read` PAM check — the backend
     * only *publishes* the events; no DataSync-specific grant is consulted on the subscribe/receive path — so
     * [com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant.subscribe] / [com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant.subscribePattern] (channel `read` on the resolved ref-channel)
     * is all the subscriber needs. `server` (secretKey) still performs every CRUD write.
     */
    private fun authorizedSubscriber(vararg grants: TokenGrant): PubNub {
        val client = createAuthorizedClient()
        val token =
            server.grantToken(
                ttl = 60,
                authorizedUserId = client.configuration.userId,
                grants = grants.toList(),
            ).sync().token
        client.setToken(token)
        return client
    }

    /**
     * Subscribes and blocks until [client]'s subscribe loop is actually connected, instead of sleeping on a
     * fixed guess. Publishing before the receive loop is up would drop the realtime CREATE (e=5 events are not
     * replayed from history on connect), so the writes must wait for [com.pubnub.api.enums.PNStatusCategory.PNConnectedCategory].
     */
    private fun subscribeAndAwaitConnect(client: PubNub, subscription: Subscription) {
        val connected = CountDownLatch(1)
        client.addListener(
            object : StatusListener {
                override fun status(pubnub: PubNub, status: PNStatus) {
                    if (status.category == PNStatusCategory.PNConnectedCategory) {
                        connected.countDown()
                    }
                }
            },
        )
        subscription.subscribe()
        Assert.assertTrue("subscribe loop did not connect", connected.await(15, TimeUnit.SECONDS))
    }

    @Test
    fun receivesUserEventsViaAddListener() {
        val userId = "user-rt-" + CommonUtils.randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2) // patch + full replace both fire UPDATE
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncUserEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncUserEventMessage? = null

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        val client = authorizedSubscriber(DataSyncGrant.subscribe(userId))

        val subscription = client.dataSyncUser(userId).subscription()
        subscription.addListener(
            object : EventListener {
                override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                    when (val msg = result.extractedMessage) {
                        is PNSetDataSyncUserEventMessage ->
                            when (msg.event) {
                                PNDataSyncSetEventType.CREATE -> {
                                    createLeaf = msg
                                    sawCreate.countDown()
                                }
                                PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                            }
                        is PNDeleteDataSyncUserEventMessage -> {
                            deleteLeaf = msg
                            sawDelete.countDown()
                        }
                        else -> {}
                    }
                }
            },
        )
        subscribeAndAwaitConnect(client, subscription)

        try {
            server.dataSync.createUser(
                classVersion = classVersion,
                userId = userId,
                status = "active",
                payload = mapOf("username" to "Alice"),
            ).sync()
            server.dataSync.updateUser(
                userId = userId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ).sync()
            server.dataSync.setUser(
                userId = userId,
                classVersion = classVersion,
                status = "archived",
                payload = mapOf("username" to "Alice Cooper"),
            ).sync()
            server.dataSync.removeUser(userId).sync()

            Assert.assertTrue("Expected a create user event", sawCreate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue(
                "Expected two update user events (patch + full replace)",
                sawUpdate.await(15, TimeUnit.SECONDS)
            )
            Assert.assertTrue("Expected a delete user event", sawDelete.await(15, TimeUnit.SECONDS))

            Assert.assertEquals(userId, createLeaf!!.data.id)
            Assert.assertEquals("active", createLeaf!!.data.status)
            Assert.assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            Assert.assertEquals(userId, deleteLeaf!!.id)
            Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
        }
    }

    @Test
    fun receivesUserEventsViaOnDataSync() {
        val userId = "user-rt-" + CommonUtils.randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncUserEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncUserEventMessage? = null

        val subscription = server.dataSyncUser(userId).subscription()
        subscription.onDataSync = { result ->
            when (val msg = result.extractedMessage) {
                is PNSetDataSyncUserEventMessage ->
                    when (msg.event) {
                        PNDataSyncSetEventType.CREATE -> {
                            createLeaf = msg
                            sawCreate.countDown()
                        }
                        PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                    }
                is PNDeleteDataSyncUserEventMessage -> {
                    deleteLeaf = msg
                    sawDelete.countDown()
                }
                else -> {}
            }
        }
        subscribeAndAwaitConnect(server, subscription)

        server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = mapOf("username" to "Alice"),
        ).sync()
        server.dataSync.updateUser(
            userId = userId,
            operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
        ).sync()
        server.dataSync.setUser(
            userId = userId,
            classVersion = classVersion,
            status = "archived",
            payload = mapOf("username" to "Alice Cooper"),
        ).sync()
        server.dataSync.removeUser(userId).sync()

        Assert.assertTrue("Expected a create user event", sawCreate.await(15, TimeUnit.SECONDS))
        Assert.assertTrue(
            "Expected two update user events (patch + full replace)",
            sawUpdate.await(15, TimeUnit.SECONDS)
        )
        Assert.assertTrue("Expected a delete user event", sawDelete.await(15, TimeUnit.SECONDS))

        Assert.assertEquals(userId, createLeaf!!.data.id)
        Assert.assertEquals("active", createLeaf!!.data.status)
        Assert.assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
        Assert.assertEquals(userId, deleteLeaf!!.id)
        Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
    }

    @Test
    fun receivesChannelEventsViaAddListener() {
        val channelId = "channel-rt-" + CommonUtils.randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncChannelEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncChannelEventMessage? = null

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        val client = authorizedSubscriber(DataSyncGrant.subscribe(channelId))
        val subscription = client.dataSyncChannel(channelId).subscription()
        subscription.addListener(
            object : EventListener {
                override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                    when (val msg = result.extractedMessage) {
                        is PNSetDataSyncChannelEventMessage ->
                            when (msg.event) {
                                PNDataSyncSetEventType.CREATE -> {
                                    createLeaf = msg
                                    sawCreate.countDown()
                                }
                                PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                            }
                        is PNDeleteDataSyncChannelEventMessage -> {
                            deleteLeaf = msg
                            sawDelete.countDown()
                        }
                        else -> {}
                    }
                }
            },
        )
        subscribeAndAwaitConnect(client, subscription)

        try {
            server.dataSync.createChannel(
                classVersion = classVersion,
                channelId = channelId,
                status = "active",
                payload = mapOf("name" to "Chan-A"),
            ).sync()
            server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ).sync()
            server.dataSync.setChannel(
                channelId = channelId,
                classVersion = classVersion,
                status = "archived",
                payload = mapOf("name" to "Chan-B"),
            ).sync()
            server.dataSync.removeChannel(channelId).sync()

            Assert.assertTrue("Expected a create channel event", sawCreate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected two update channel events", sawUpdate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected a delete channel event", sawDelete.await(15, TimeUnit.SECONDS))

            Assert.assertEquals(channelId, createLeaf!!.data.id)
            Assert.assertEquals("active", createLeaf!!.data.status)
            Assert.assertEquals(channelId, deleteLeaf!!.id)
            Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
        }
    }

    @Test
    fun receivesChannelEventsViaOnDataSync() {
        val channelId = "channel-rt-" + CommonUtils.randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncChannelEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncChannelEventMessage? = null

        val subscription = server.dataSyncChannel(channelId).subscription()
        subscription.onDataSync = { result ->
            when (val msg = result.extractedMessage) {
                is PNSetDataSyncChannelEventMessage ->
                    when (msg.event) {
                        PNDataSyncSetEventType.CREATE -> {
                            createLeaf = msg
                            sawCreate.countDown()
                        }
                        PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                    }
                is PNDeleteDataSyncChannelEventMessage -> {
                    deleteLeaf = msg
                    sawDelete.countDown()
                }
                else -> {}
            }
        }
        subscribeAndAwaitConnect(server, subscription)

        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = mapOf("name" to "Chan-A"),
        ).sync()
        server.dataSync.updateChannel(
            channelId = channelId,
            operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
        ).sync()
        server.dataSync.setChannel(
            channelId = channelId,
            classVersion = classVersion,
            status = "archived",
            payload = mapOf("name" to "Chan-B"),
        ).sync()
        server.dataSync.removeChannel(channelId).sync()

        Assert.assertTrue("Expected a create channel event", sawCreate.await(15, TimeUnit.SECONDS))
        Assert.assertTrue("Expected two update channel events", sawUpdate.await(15, TimeUnit.SECONDS))
        Assert.assertTrue("Expected a delete channel event", sawDelete.await(15, TimeUnit.SECONDS))

        Assert.assertEquals(channelId, createLeaf!!.data.id)
        Assert.assertEquals("active", createLeaf!!.data.status)
        Assert.assertEquals(channelId, deleteLeaf!!.id)
        Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
    }

    @Test
    fun receivesEntityEventsViaAddListener() {
        val entityId = "entity-rt-" + CommonUtils.randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncEntityEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncEntityEventMessage? = null

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        val client = authorizedSubscriber(DataSyncGrant.subscribe(entityId))
        val subscription = client.dataSyncEntity(entityId).subscription()
        subscription.addListener(
            object : EventListener {
                override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                    when (val msg = result.extractedMessage) {
                        is PNSetDataSyncEntityEventMessage ->
                            when (msg.event) {
                                PNDataSyncSetEventType.CREATE -> {
                                    createLeaf = msg
                                    sawCreate.countDown()
                                }
                                PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                            }
                        is PNDeleteDataSyncEntityEventMessage -> {
                            deleteLeaf = msg
                            sawDelete.countDown()
                        }
                        else -> {}
                    }
                }
            },
        )
        subscribeAndAwaitConnect(client, subscription)

        try {
            // `TestUser` declares `email` in the `admin` projection only; `username` is in `__default__`. This
            // subscription is on the bare ref (the `__default__` channel), so the realtime snapshot must carry
            // `username` but NOT `email`. `server` holds the secretKey, so it bypasses the projection write-guard
            // and can write the admin-only `email` directly (a __default__-projection token would be rejected
            // with DS-0202).
            server.dataSync.createEntity(
                className = "TestUser",
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = mapOf("username" to "Alice", "email" to "alice@example.com"),
            ).sync()
            server.dataSync.updateEntity(
                entityId = entityId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ).sync()
            server.dataSync.setEntity(
                entityId = entityId,
                classVersion = classVersion,
                status = "archived",
                payload = mapOf("username" to "Alice Johnson", "email" to "alicejohnson@example.com"),
            ).sync()
            server.dataSync.removeEntity(entityId).sync()

            Assert.assertTrue("Expected a create entity event", sawCreate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected two update entity events", sawUpdate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected a delete entity event", sawDelete.await(15, TimeUnit.SECONDS))

            Assert.assertEquals(entityId, createLeaf!!.data.id)
            // default-projection ref carries `username` but hides the admin-only `email`.
            Assert.assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            Assert.assertNull(
                "default projection must omit the admin-only email field",
                createLeaf!!.data.payload?.get("email")
            )
            Assert.assertEquals(entityId, deleteLeaf!!.id)
            Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
        }
    }

    @Test
    fun receivesEntityEventsViaOnDataSync() {
        val entityId = "entity-rt-" + CommonUtils.randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncEntityEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncEntityEventMessage? = null

        val subscription = server.dataSyncEntity(entityId).subscription()
        subscription.onDataSync = { result ->
            when (val msg = result.extractedMessage) {
                is PNSetDataSyncEntityEventMessage ->
                    when (msg.event) {
                        PNDataSyncSetEventType.CREATE -> {
                            createLeaf = msg
                            sawCreate.countDown()
                        }
                        PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                    }
                is PNDeleteDataSyncEntityEventMessage -> {
                    deleteLeaf = msg
                    sawDelete.countDown()
                }
                else -> {}
            }
        }
        subscribeAndAwaitConnect(server, subscription)

        // `TestUser` declares `email` in the `admin` projection only; `username` is in `__default__`. This
        // subscription is on the bare ref (the `__default__` channel), so the realtime snapshot must carry
        // `username` but NOT `email`. `server` holds the secretKey, so it bypasses the projection write-guard
        // and can write the admin-only `email` directly (a __default__-projection token would be rejected
        // with DS-0202).
        server.dataSync.createEntity(
            className = "TestUser",
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = mapOf("username" to "Alice", "email" to "alice@example.com"),
        ).sync()
        server.dataSync.updateEntity(
            entityId = entityId,
            operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
        ).sync()
        server.dataSync.setEntity(
            entityId = entityId,
            classVersion = classVersion,
            status = "archived",
            payload = mapOf("username" to "Alice Johnson", "email" to "AJohnson@example.com"),
        ).sync()
        server.dataSync.removeEntity(entityId).sync()

        Assert.assertTrue("Expected a create entity event", sawCreate.await(15, TimeUnit.SECONDS))
        Assert.assertTrue("Expected two update entity events", sawUpdate.await(15, TimeUnit.SECONDS))
        Assert.assertTrue("Expected a delete entity event", sawDelete.await(15, TimeUnit.SECONDS))

        Assert.assertEquals(entityId, createLeaf!!.data.id)
        // default-projection ref carries `username` but hides the admin-only `email`.
        Assert.assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
        Assert.assertNull(
            "default projection must omit the admin-only email field",
            createLeaf!!.data.payload?.get("email")
        )
        Assert.assertEquals(entityId, deleteLeaf!!.id)
        Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
    }

    @Test
    fun receivesMembershipEventsViaAddListener() {
        val run = CommonUtils.randomValue()
        val channelId = "channel-m-$run"
        val userId = "user-m-$run"
        val membershipId = "membership-$run"
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(1)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncMembershipEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncMembershipEventMessage? = null

        server.dataSync.createChannel(classVersion = classVersion, channelId = channelId, payload = mapOf("name" to "Chan-$run")).sync()
        server.dataSync.createUser(classVersion = classVersion, userId = userId, payload = mapOf("name" to "User-$run")).sync()
        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        // A membership is published to both endpoint refs; subscribe to the Channel endpoint to hear it.
        val client = authorizedSubscriber(DataSyncGrant.subscribe(channelId))
        try {
            val subscription = client.dataSyncChannel(channelId).subscription()
            subscription.addListener(
                object : EventListener {
                    override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                        when (val msg = result.extractedMessage) {
                            is PNSetDataSyncMembershipEventMessage ->
                                when (msg.event) {
                                    PNDataSyncSetEventType.CREATE -> {
                                        createLeaf = msg
                                        sawCreate.countDown()
                                    }
                                    PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                                }
                            is PNDeleteDataSyncMembershipEventMessage -> {
                                deleteLeaf = msg
                                sawDelete.countDown()
                            }
                            else -> {}
                        }
                    }
                },
            )
            subscribeAndAwaitConnect(client, subscription)

            server.dataSync.createMembership(
                channelId = channelId,
                userId = userId,
                classVersion = classVersion,
                membershipId = membershipId,
                status = "active",
            ).sync()
            server.dataSync.setMembership(
                membershipId = membershipId,
                classVersion = classVersion,
                status = "archived",
            ).sync()
            server.dataSync.removeMembership(membershipId).sync()

            Assert.assertTrue("Expected a create membership event", sawCreate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected an update membership event", sawUpdate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected a delete membership event", sawDelete.await(15, TimeUnit.SECONDS))

            Assert.assertEquals(membershipId, createLeaf!!.data.id)
            Assert.assertEquals(channelId, createLeaf!!.data.channelId)
            Assert.assertEquals(userId, createLeaf!!.data.userId)
            Assert.assertEquals(membershipId, deleteLeaf!!.id)
            Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: Exception) {
            }
            try {
                server.dataSync.removeUser(userId).sync()
            } catch (ignored: Exception) {
            }
        }
    }

    @Test
    fun receivesMembershipEventsViaOnDataSync() {
        val run = CommonUtils.randomValue()
        val channelId = "channel-m-$run"
        val userId = "user-m-$run"
        val membershipId = "membership-$run"
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(1)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncMembershipEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncMembershipEventMessage? = null

        server.dataSync.createChannel(classVersion = classVersion, channelId = channelId, payload = mapOf("name" to "Chan-$run")).sync()
        server.dataSync.createUser(classVersion = classVersion, userId = userId, payload = mapOf("name" to "User-$run")).sync()
        try {
            val subscription = server.dataSyncChannel(channelId).subscription()
            subscription.onDataSync = { result ->
                when (val msg = result.extractedMessage) {
                    is PNSetDataSyncMembershipEventMessage ->
                        when (msg.event) {
                            PNDataSyncSetEventType.CREATE -> {
                                createLeaf = msg
                                sawCreate.countDown()
                            }
                            PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                        }
                    is PNDeleteDataSyncMembershipEventMessage -> {
                        deleteLeaf = msg
                        sawDelete.countDown()
                    }
                    else -> {}
                }
            }
            subscribeAndAwaitConnect(server, subscription)

            server.dataSync.createMembership(
                channelId = channelId,
                userId = userId,
                classVersion = classVersion,
                membershipId = membershipId,
                status = "active",
            ).sync()
            server.dataSync.setMembership(
                membershipId = membershipId,
                classVersion = classVersion,
                status = "archived",
            ).sync()
            server.dataSync.removeMembership(membershipId).sync()

            Assert.assertTrue("Expected a create membership event", sawCreate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected an update membership event", sawUpdate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected a delete membership event", sawDelete.await(15, TimeUnit.SECONDS))

            Assert.assertEquals(membershipId, createLeaf!!.data.id)
            Assert.assertEquals(channelId, createLeaf!!.data.channelId)
            Assert.assertEquals(userId, createLeaf!!.data.userId)
            Assert.assertEquals(membershipId, deleteLeaf!!.id)
            Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: Exception) {
            }
            try {
                server.dataSync.removeUser(userId).sync()
            } catch (ignored: Exception) {
            }
        }
    }

    @Test
    fun receivesRelationshipEventsViaAddListener() {
        val run = CommonUtils.randomValue()
        val entityAId = "node-a-$run"
        val entityBId = "node-b-$run"
        val relationshipId = "relationship-$run"
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(1)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncRelationshipEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncRelationshipEventMessage? = null

        server.dataSync.createEntity(
            className = "TestNode",
            classVersion = classVersion,
            entityId = entityAId,
            payload = mapOf("name" to "NodeA-$run")
        ).sync()
        server.dataSync.createEntity(
            className = "TestNode",
            classVersion = classVersion,
            entityId = entityBId,
            payload = mapOf("name" to "NodeB-$run")
        ).sync()
        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        // A relationship is published to both endpoint refs; subscribe to entity A's ref to hear it.
        val client = authorizedSubscriber(DataSyncGrant.subscribe(entityAId))
        try {
            val subscription = client.dataSyncEntity(entityAId).subscription()
            subscription.addListener(
                object : EventListener {
                    override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                        when (val msg = result.extractedMessage) {
                            is PNSetDataSyncRelationshipEventMessage ->
                                when (msg.event) {
                                    PNDataSyncSetEventType.CREATE -> {
                                        createLeaf = msg
                                        sawCreate.countDown()
                                    }
                                    PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                                }
                            is PNDeleteDataSyncRelationshipEventMessage -> {
                                deleteLeaf = msg
                                sawDelete.countDown()
                            }
                            else -> {}
                        }
                    }
                },
            )
            subscribeAndAwaitConnect(client, subscription)

            server.dataSync.createRelationship(
                entityAId = entityAId,
                entityBId = entityBId,
                className = "TestFriendship",
                classVersion = classVersion,
                relationshipId = relationshipId,
                status = "active",
            ).sync()
            server.dataSync.setRelationship(
                relationshipId = relationshipId,
                classVersion = classVersion,
                status = "archived",
            ).sync()
            server.dataSync.removeRelationship(relationshipId).sync()

            Assert.assertTrue("Expected a create relationship event", sawCreate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected an update relationship event", sawUpdate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected a delete relationship event", sawDelete.await(15, TimeUnit.SECONDS))

            Assert.assertEquals(relationshipId, createLeaf!!.data.id)
            Assert.assertEquals(entityAId, createLeaf!!.data.entityAId)
            Assert.assertEquals(entityBId, createLeaf!!.data.entityBId)
            Assert.assertEquals(relationshipId, deleteLeaf!!.id)
            Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
            try {
                server.dataSync.removeEntity(entityAId).sync()
            } catch (ignored: Exception) {
            }
            try {
                server.dataSync.removeEntity(entityBId).sync()
            } catch (ignored: Exception) {
            }
        }
    }

    @Test
    fun receivesRelationshipEventsViaOnDataSync() {
        val run = CommonUtils.randomValue()
        val entityAId = "node-a-$run"
        val entityBId = "node-b-$run"
        val relationshipId = "relationship-$run"
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(1)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncRelationshipEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncRelationshipEventMessage? = null

        server.dataSync.createEntity(
            className = "TestNode",
            classVersion = classVersion,
            entityId = entityAId,
            payload = mapOf("name" to "NodeA-$run")
        ).sync()
        server.dataSync.createEntity(
            className = "TestNode",
            classVersion = classVersion,
            entityId = entityBId,
            payload = mapOf("name" to "NodeB-$run")
        ).sync()
        try {
            val subscription = server.dataSyncEntity(entityAId).subscription()
            subscription.onDataSync = { result ->
                when (val msg = result.extractedMessage) {
                    is PNSetDataSyncRelationshipEventMessage ->
                        when (msg.event) {
                            PNDataSyncSetEventType.CREATE -> {
                                createLeaf = msg
                                sawCreate.countDown()
                            }
                            PNDataSyncSetEventType.UPDATE -> sawUpdate.countDown()
                        }
                    is PNDeleteDataSyncRelationshipEventMessage -> {
                        deleteLeaf = msg
                        sawDelete.countDown()
                    }
                    else -> {}
                }
            }
            subscribeAndAwaitConnect(server, subscription)

            server.dataSync.createRelationship(
                entityAId = entityAId,
                entityBId = entityBId,
                className = "TestFriendship",
                classVersion = classVersion,
                relationshipId = relationshipId,
                status = "active",
            ).sync()
            server.dataSync.setRelationship(
                relationshipId = relationshipId,
                classVersion = classVersion,
                status = "archived",
            ).sync()
            server.dataSync.removeRelationship(relationshipId).sync()

            Assert.assertTrue("Expected a create relationship event", sawCreate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected an update relationship event", sawUpdate.await(15, TimeUnit.SECONDS))
            Assert.assertTrue("Expected a delete relationship event", sawDelete.await(15, TimeUnit.SECONDS))

            Assert.assertEquals(relationshipId, createLeaf!!.data.id)
            Assert.assertEquals(entityAId, createLeaf!!.data.entityAId)
            Assert.assertEquals(entityBId, createLeaf!!.data.entityBId)
            Assert.assertEquals(relationshipId, deleteLeaf!!.id)
            Assert.assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            try {
                server.dataSync.removeEntity(entityAId).sync()
            } catch (ignored: Exception) {
            }
            try {
                server.dataSync.removeEntity(entityBId).sync()
            } catch (ignored: Exception) {
            }
        }
    }

    @Test
    fun adminProjectionSubscriptionCarriesAdminOnlyField() {
        // Counterpart to the default-projection check in the `receivesEntityEvents*` tests (which subscribe
        // to the bare ref and see `username` but NOT the admin-only `email`). Subscribing with
        // `subscription("admin")` resolves to the `__admin__{id}` channel, which carries the admin-projection
        // fields — so the realtime snapshot DOES include `email`. `TestUser` declares `email` in the `admin`
        // projection only.
        val entityId = "entity-proj-" + CommonUtils.randomValue()
        val sawCreate = CountDownLatch(1)
        var createLeaf: PNSetDataSyncEntityEventMessage? = null

        val subscription = server.dataSyncEntity(entityId).subscription("admin")
        subscription.onDataSync = { result ->
            val msg = result.extractedMessage
            if (msg is PNSetDataSyncEntityEventMessage && msg.event == PNDataSyncSetEventType.CREATE) {
                createLeaf = msg
                sawCreate.countDown()
            }
        }
        subscribeAndAwaitConnect(server, subscription)

        try {
            server.dataSync.createEntity(
                className = "TestUser",
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = mapOf("username" to "Alice", "email" to "alice@example.com"),
            ).sync()

            Assert.assertTrue(
                "Expected a create entity event on the admin projection",
                sawCreate.await(15, TimeUnit.SECONDS)
            )
            Assert.assertEquals(entityId, createLeaf!!.data.id)
            Assert.assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            Assert.assertEquals("alice@example.com", createLeaf!!.data.payload?.get("email"))
        } finally {
            try {
                server.dataSync.removeEntity(entityId).sync()
            } catch (ignored: Exception) {
            }
        }
    }

    @Test
    fun adminProjectionSubscriptionUnderTokenCarriesAdminOnlyField() {
        // Same admin-projection guarantee as `adminProjectionSubscriptionCarriesAdminOnlyField`, but the
        // subscriber is a PAM-only client that has NO secretKey — it authenticates purely with a token minted
        // by `server`. `DataSyncGrant.subscribe(id, "admin")` grants the pub/sub `read` on the resolved
        // `__admin__{id}` ref-channel, which is what authorizes the realtime subscribe. The paired
        // `DataSyncGrant.entity(..., projection = "admin")` is the documented "read through and subscribe to the
        // same projection" setup: it sets the REST read lens and is NOT needed to receive events (see
        // `adminProjectionSubscribeGrantAloneReceivesAdminOnlyField`), so the REST read below is what exercises
        // it. `server` (secretKey) does the CRUD write, since a `__default__` token cannot write the admin-only
        // `email`.
        val entityId = "entity-proj-token-" + CommonUtils.randomValue()
        val client =
            authorizedSubscriber(
                DataSyncGrant.subscribe(entityId, projection = "admin"),
                DataSyncGrant.entity(entityId, get = true, projection = "admin"),
            )

        assertAdminCreateReceived(client, entityId) {
            val fetched = client.dataSync.getEntity(entityId).sync()
            Assert.assertEquals("alice@example.com", fetched.data.payload?.get("email")) // REST read through "admin"
        }
    }

    @Test
    fun adminProjectionSubscribeGrantAloneReceivesAdminOnlyField() {
        // The token carries ONLY `DataSyncGrant.subscribe(id, "admin")` — no DataSync entity grant at all. The
        // realtime subscribe is a plain pub/sub read of `__admin__{id}`, and the admin projection is baked into
        // what the backend publishes on that channel, so the event still carries the admin-only `email`.
        val entityId = "entity-proj-sub-only-" + CommonUtils.randomValue()
        val client = authorizedSubscriber(DataSyncGrant.subscribe(entityId, projection = "admin"))

        assertAdminCreateReceived(client, entityId)
    }

    @Test
    fun subscribePatternGrantReceivesDefaultProjectionEvents() {
        // Verifies that PAM honours the `^` anchor in channel patterns: `subscribePattern("<prefix>-.*")` is
        // granted as `^(?:<prefix>-.*)`. If this test fails (no event under a token that should match), PAM does
        // not accept the anchored form and the anchoring in `DataSyncNamespace.refChannelPattern` must be
        // revisited. Counterpart: `subscribePatternGrantDoesNotCoverProjectionMirror`.
        val prefix = "entity-pat-" + CommonUtils.randomValue()
        val entityId = "$prefix-1"
        val client = authorizedSubscriber(DataSyncGrant.subscribePattern("$prefix-.*"))

        val sawCreate = CountDownLatch(1)
        var createLeaf: PNSetDataSyncEntityEventMessage? = null
        val subscription = client.dataSyncEntity(entityId).subscription()
        subscription.onDataSync = { result ->
            val msg = result.extractedMessage
            if (msg is PNSetDataSyncEntityEventMessage && msg.event == PNDataSyncSetEventType.CREATE) {
                createLeaf = msg
                sawCreate.countDown()
            }
        }
        subscribeAndAwaitConnect(client, subscription)

        try {
            server.dataSync.createEntity(
                className = "TestUser",
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = mapOf("username" to "Alice", "email" to "alice@example.com"),
            ).sync()

            Assert.assertTrue(
                "Expected a create entity event under a subscribePattern token",
                sawCreate.await(15, TimeUnit.SECONDS),
            )
            Assert.assertEquals(entityId, createLeaf!!.data.id)
            Assert.assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            Assert.assertNull(
                "default projection must not expose the admin-only email",
                createLeaf!!.data.payload?.get("email")
            )
        } finally {
            client.unsubscribeAll()
            client.destroy()
            try {
                server.dataSync.removeEntity(entityId).sync()
            } catch (ignored: Exception) {
            }
        }
    }

    @Test
    fun subscribePatternGrantDoesNotCoverProjectionMirror() {
        // Verifies that PAM honours the `^` anchor in channel patterns: the default-projection grant
        // `^(?:<prefix>-.*)` must NOT match the admin mirror `__admin__<prefix>-1`. An unanchored
        // `<prefix>-.*` would match it, leaking admin-only fields to a default-projection token. Together with
        // `subscribePatternGrantReceivesDefaultProjectionEvents` this pins the anchoring behaviour;
        val prefix = "entity-pat-deny-" + CommonUtils.randomValue()
        val entityId = "$prefix-1"
        val client = authorizedSubscriber(DataSyncGrant.subscribePattern("$prefix-.*"))

        val denied = CountDownLatch(1)
        var deniedStatus: PNStatus? = null
        client.addListener(
            object : StatusListener {
                override fun status(pubnub: PubNub, status: PNStatus) {
                    if (status.category == PNStatusCategory.PNConnectionError) {
                        deniedStatus = status
                        denied.countDown()
                    }
                }
            },
        )

        try {
            client.dataSyncEntity(entityId).subscription("admin").subscribe()

            Assert.assertTrue(
                "Expected the admin-mirror subscribe to be rejected under a default-projection pattern token",
                denied.await(15, TimeUnit.SECONDS),
            )
            Assert.assertEquals(403, deniedStatus!!.exception?.statusCode)
        } finally {
            client.unsubscribeAll()
            client.destroy()
        }
    }

    @Test
    fun subscribePatternWithProjectionReceivesAdminOnlyField() {
        // `subscribePattern("<prefix>-.*", "admin")` is granted as `^__admin__(?:<prefix>-.*)`, which covers the
        // admin mirror `__admin__<prefix>-1`. The `entityPattern(..., projection = "admin")` grant sets the REST
        // read lens for the same ids (the usual pairing); the realtime event carries the admin-only `email`.
        val prefix = "entity-pat-proj-" + CommonUtils.randomValue()
        val entityId = "$prefix-1"
        val client =
            authorizedSubscriber(
                DataSyncGrant.subscribePattern("$prefix-.*", projection = "admin"),
                DataSyncGrant.entityPattern("$prefix-.*", get = true, projection = "admin"),
            )

        assertAdminCreateReceived(client, entityId)
    }

    /**
     * Subscribes [client] to the `admin` projection of [entityId], has `server` create it as a `TestUser`
     * carrying the admin-only `email`, and asserts the realtime CREATE carries it. Runs [afterEvent] (while the
     * client and entity still exist), then tears down the client and the entity.
     */
    private fun assertAdminCreateReceived(client: PubNub, entityId: String, afterEvent: () -> Unit = {}) {
        val sawCreate = CountDownLatch(1)
        var createLeaf: PNSetDataSyncEntityEventMessage? = null

        val subscription = client.dataSyncEntity(entityId).subscription("admin")
        subscription.onDataSync = { result ->
            val msg = result.extractedMessage
            if (msg is PNSetDataSyncEntityEventMessage && msg.event == PNDataSyncSetEventType.CREATE) {
                createLeaf = msg
                sawCreate.countDown()
            }
        }

        try {
            subscribeAndAwaitConnect(client, subscription)

            server.dataSync.createEntity(
                className = "TestUser",
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = mapOf("username" to "Alice", "email" to "alice@example.com"),
            ).sync()

            Assert.assertTrue(
                "Expected a create entity event on the admin projection under a PAM token",
                sawCreate.await(15, TimeUnit.SECONDS),
            )
            Assert.assertEquals(entityId, createLeaf!!.data.id)
            Assert.assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            Assert.assertEquals("alice@example.com", createLeaf!!.data.payload?.get("email"))

            afterEvent()
        } finally {
            client.unsubscribeAll()
            client.destroy()
            try {
                server.dataSync.removeEntity(entityId).sync()
            } catch (ignored: Exception) {
            }
        }
    }
}