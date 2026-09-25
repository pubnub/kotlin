package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
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
import com.pubnub.test.CommonUtils.randomValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
     * Mints a token (scoped to plain PubSub `read` on the given DataSync ref-channels) and hands back a
     * PAM-only client authenticated with it. A realtime subscribe on a DataSync ref-channel is authorized by
     * the ordinary channel-`read` PAM check — the backend only *publishes* the events; no DataSync-specific
     * grant is consulted on the subscribe/receive path — so channel `read` on the exact ref-channel string is
     * all the subscriber needs. `server` (secretKey) still performs every CRUD write.
     */
    private fun authorizedSubscriber(vararg refChannels: String): PubNub {
        val client = createAuthorizedClient()
        val token =
            server.grantToken(
                ttl = 60,
                authorizedUserId = client.configuration.userId,
                grants = refChannels.map { ChannelGrant.name(it, read = true) },
            ).sync().token
        client.setToken(token)
        return client
    }

    @Test
    fun receivesUserEventsViaAddListener() {
        val userId = "user-rt-" + randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2) // patch + full replace both fire UPDATE
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncUserEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncUserEventMessage? = null

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        val client = authorizedSubscriber(userId)

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
        subscription.subscribe()
        Thread.sleep(2000) // let the subscribe loop connect before publishing changes

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

            assertTrue("Expected a create user event", sawCreate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected two update user events (patch + full replace)", sawUpdate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected a delete user event", sawDelete.await(15, TimeUnit.SECONDS))

            assertEquals(userId, createLeaf!!.data.id)
            assertEquals("active", createLeaf!!.data.status)
            assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            assertEquals(userId, deleteLeaf!!.id)
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
        }
    }

    @Test
    fun receivesUserEventsViaOnDataSync() {
        val userId = "user-rt-" + randomValue()
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
        subscription.subscribe()
        Thread.sleep(2000)

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

        assertTrue("Expected a create user event", sawCreate.await(15, TimeUnit.SECONDS))
        assertTrue("Expected two update user events (patch + full replace)", sawUpdate.await(15, TimeUnit.SECONDS))
        assertTrue("Expected a delete user event", sawDelete.await(15, TimeUnit.SECONDS))

        assertEquals(userId, createLeaf!!.data.id)
        assertEquals("active", createLeaf!!.data.status)
        assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
        assertEquals(userId, deleteLeaf!!.id)
        assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
    }

    @Test
    fun receivesChannelEventsViaAddListener() {
        val channelId = "channel-rt-" + randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncChannelEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncChannelEventMessage? = null

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        val client = authorizedSubscriber(channelId)
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
        subscription.subscribe()
        Thread.sleep(2000)

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

            assertTrue("Expected a create channel event", sawCreate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected two update channel events", sawUpdate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected a delete channel event", sawDelete.await(15, TimeUnit.SECONDS))

            assertEquals(channelId, createLeaf!!.data.id)
            assertEquals("active", createLeaf!!.data.status)
            assertEquals(channelId, deleteLeaf!!.id)
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
        }
    }

    @Test
    fun receivesChannelEventsViaOnDataSync() {
        val channelId = "channel-rt-" + randomValue()
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
        subscription.subscribe()
        Thread.sleep(2000)

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

        assertTrue("Expected a create channel event", sawCreate.await(15, TimeUnit.SECONDS))
        assertTrue("Expected two update channel events", sawUpdate.await(15, TimeUnit.SECONDS))
        assertTrue("Expected a delete channel event", sawDelete.await(15, TimeUnit.SECONDS))

        assertEquals(channelId, createLeaf!!.data.id)
        assertEquals("active", createLeaf!!.data.status)
        assertEquals(channelId, deleteLeaf!!.id)
        assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
    }

    @Test
    fun receivesEntityEventsViaAddListener() {
        val entityId = "entity-rt-" + randomValue()
        val sawCreate = CountDownLatch(1)
        val sawUpdate = CountDownLatch(2)
        val sawDelete = CountDownLatch(1)
        var createLeaf: PNSetDataSyncEntityEventMessage? = null
        var deleteLeaf: PNDeleteDataSyncEntityEventMessage? = null

        // A PAM-only client (no secretKey) subscribes under a server-minted token; `server` does the writes.
        val client = authorizedSubscriber(entityId)
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
        subscription.subscribe()
        Thread.sleep(2000)

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

            assertTrue("Expected a create entity event", sawCreate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected two update entity events", sawUpdate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected a delete entity event", sawDelete.await(15, TimeUnit.SECONDS))

            assertEquals(entityId, createLeaf!!.data.id)
            // default-projection ref carries `username` but hides the admin-only `email`.
            assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            assertNull("default projection must omit the admin-only email field", createLeaf!!.data.payload?.get("email"))
            assertEquals(entityId, deleteLeaf!!.id)
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
        } finally {
            client.unsubscribeAll()
            client.destroy()
        }
    }

    @Test
    fun receivesEntityEventsViaOnDataSync() {
        val entityId = "entity-rt-" + randomValue()
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
        subscription.subscribe()
        Thread.sleep(2000)

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

        assertTrue("Expected a create entity event", sawCreate.await(15, TimeUnit.SECONDS))
        assertTrue("Expected two update entity events", sawUpdate.await(15, TimeUnit.SECONDS))
        assertTrue("Expected a delete entity event", sawDelete.await(15, TimeUnit.SECONDS))

        assertEquals(entityId, createLeaf!!.data.id)
        // default-projection ref carries `username` but hides the admin-only `email`.
        assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
        assertNull("default projection must omit the admin-only email field", createLeaf!!.data.payload?.get("email"))
        assertEquals(entityId, deleteLeaf!!.id)
        assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
    }

    @Test
    fun receivesMembershipEventsViaAddListener() {
        val run = randomValue()
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
        val client = authorizedSubscriber(channelId)
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
            subscription.subscribe()
            Thread.sleep(2000)

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

            assertTrue("Expected a create membership event", sawCreate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected an update membership event", sawUpdate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected a delete membership event", sawDelete.await(15, TimeUnit.SECONDS))

            assertEquals(membershipId, createLeaf!!.data.id)
            assertEquals(channelId, createLeaf!!.data.channelId)
            assertEquals(userId, createLeaf!!.data.userId)
            assertEquals(membershipId, deleteLeaf!!.id)
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
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
        val run = randomValue()
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
            subscription.subscribe()
            Thread.sleep(2000)

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

            assertTrue("Expected a create membership event", sawCreate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected an update membership event", sawUpdate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected a delete membership event", sawDelete.await(15, TimeUnit.SECONDS))

            assertEquals(membershipId, createLeaf!!.data.id)
            assertEquals(channelId, createLeaf!!.data.channelId)
            assertEquals(userId, createLeaf!!.data.userId)
            assertEquals(membershipId, deleteLeaf!!.id)
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
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
        val run = randomValue()
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
        val client = authorizedSubscriber(entityAId)
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
            subscription.subscribe()
            Thread.sleep(2000)

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

            assertTrue("Expected a create relationship event", sawCreate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected an update relationship event", sawUpdate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected a delete relationship event", sawDelete.await(15, TimeUnit.SECONDS))

            assertEquals(relationshipId, createLeaf!!.data.id)
            assertEquals(entityAId, createLeaf!!.data.entityAId)
            assertEquals(entityBId, createLeaf!!.data.entityBId)
            assertEquals(relationshipId, deleteLeaf!!.id)
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
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
        val run = randomValue()
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
            subscription.subscribe()
            Thread.sleep(2000)

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

            assertTrue("Expected a create relationship event", sawCreate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected an update relationship event", sawUpdate.await(15, TimeUnit.SECONDS))
            assertTrue("Expected a delete relationship event", sawDelete.await(15, TimeUnit.SECONDS))

            assertEquals(relationshipId, createLeaf!!.data.id)
            assertEquals(entityAId, createLeaf!!.data.entityAId)
            assertEquals(entityBId, createLeaf!!.data.entityBId)
            assertEquals(relationshipId, deleteLeaf!!.id)
            assertNotNull("delete leaf must carry deletedAt", deleteLeaf!!.deletedAt)
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
        val entityId = "entity-proj-" + randomValue()
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
        subscription.subscribe()
        Thread.sleep(2000)

        try {
            server.dataSync.createEntity(
                className = "TestUser",
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = mapOf("username" to "Alice", "email" to "alice@example.com"),
            ).sync()

            assertTrue("Expected a create entity event on the admin projection", sawCreate.await(15, TimeUnit.SECONDS))
            assertEquals(entityId, createLeaf!!.data.id)
            assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            assertEquals("alice@example.com", createLeaf!!.data.payload?.get("email"))
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
        // by `server`. A realtime subscribe over PubSub still needs a plain channel `read` grant on the
        // resolved `__admin__{id}` ref-channel; the DataSync `entity(..., projection = "admin")` grant only
        // carries the projection lens, not the pub/sub read bit (they are separate concerns). So the token
        // must carry both. `server` (secretKey) does the CRUD write, since a `__default__` token cannot write
        // the admin-only `email`.
        val entityId = "entity-proj-token-" + randomValue()
        val adminChannel = "__admin__$entityId"
        val client = createAuthorizedClient()
        val authorizedUserId = client.configuration.userId

        val token =
            server.grantToken(
                ttl = 60,
                authorizedUserId = authorizedUserId,
                grants =
                    listOf(
                        ChannelGrant.name(adminChannel, read = true),
                        DataSyncGrant.entity(entityId, get = true, projection = "admin"),
                    ),
            ).sync().token
        client.setToken(token)

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
        subscription.subscribe()
        Thread.sleep(2000)

        try {
            server.dataSync.createEntity(
                className = "TestUser",
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = mapOf("username" to "Alice", "email" to "alice@example.com"),
            ).sync()

            assertTrue(
                "Expected a create entity event on the admin projection under a PAM token",
                sawCreate.await(15, TimeUnit.SECONDS),
            )
            assertEquals(entityId, createLeaf!!.data.id)
            assertEquals("Alice", createLeaf!!.data.payload?.get("username"))
            assertEquals("alice@example.com", createLeaf!!.data.payload?.get("email"))
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
