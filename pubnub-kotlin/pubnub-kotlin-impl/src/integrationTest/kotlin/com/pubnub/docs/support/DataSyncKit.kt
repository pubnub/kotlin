package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage
import com.pubnub.api.v2.callbacks.EventListener
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.function.Executable
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.Callable
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/**
 * Arrange and assert helpers of the DataSync tests (Kotlin and Java, the Java tests call the `@JvmStatic` members with plain
 * arguments). Nothing here is part of a sample. The DataSync objects the samples use have literal ids that persist on the
 * keyset, so a page resets them before and after its run ([resetPage], [finishPage]) and waits for the service to show them gone,
 * because the lists of a replica can lag a write.
 */
object DataSyncKit {
    const val ROLE = "datasync"

    const val ALICE = "user-alice"
    const val BOB = "seller-bob"
    const val SUMMER = "channel-summer-sale"
    const val WINTER = "channel-winter-sale"
    const val SUMMER_MEMBERSHIP = "membership-alice-summer-sale"
    const val WINTER_MEMBERSHIP = "membership-alice-winter-sale"
    const val SNEAKER = "product-sneaker-42"
    const val BOOT = "product-boot-7"
    const val LINK = "rel-bob-owns-sneaker-42"

    // ---------------------------------------------------------------------------------------------------------------
    // Reset and cleanup
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Removes every listed object by id, in dependency order, and ignores a missing one. Removing a user, a channel or an entity also
     * removes the memberships and links that point at it.
     */
    @JvmStatic
    fun removeListed(client: PubNub) {
        val names = Resources.dataSync()
        val api = client.dataSync
        names.relationships.forEach { quietly { api.removeRelationship(it).sync() } }
        names.memberships.forEach { quietly { api.removeMembership(it).sync() } }
        names.entities.forEach { quietly { api.removeEntity(it).sync() } }
        names.users.forEach { quietly { api.removeUser(it).sync() } }
        names.channels.forEach { quietly { api.removeChannel(it).sync() } }
    }

    private fun quietly(call: () -> Unit) {
        try {
            call()
        } catch (error: Exception) {
            if (Retry.statusCode(error) != 404) {
                println(Redact.scrub("datasync reset: a removal failed (${Retry.statusCode(error)})"))
            }
        }
    }

    /**
     * Waits until the lists that the exact-list assertions read show nothing of the page's objects. The removals cascade, and a
     * replica can still list a leftover for a moment, so the reads must be empty three times in a row.
     */
    @JvmStatic
    fun untilGone(client: PubNub) {
        val api = client.dataSync
        Wait.settled("the page objects are gone from the lists", timeoutMs = 60_000) {
            val left =
                api.getMemberships(userId = ALICE, limit = 20).sync().data.map { it.id } +
                    api.getMemberships(channelId = SUMMER, limit = 20).sync().data.map { it.id } +
                    api.getMemberships(channelId = WINTER, limit = 20).sync().data.map { it.id } +
                    api.getRelationships(className = "ProductOwner", entityAId = BOB, limit = 20).sync().data.map { it.id } +
                    api.getRelationships(className = "ProductOwner", entityBId = SNEAKER, limit = 20).sync().data.map { it.id } +
                    api.getUsers(filterFast = "id == \"$ALICE\" || id == \"$BOB\"").sync().data.map { it.id } +
                    api.getChannels(filterFast = "id == \"$SUMMER\" || id == \"$WINTER\"").sync().data.map { it.id } +
                    api.getEntities(className = "product", filterFast = "id == \"$SNEAKER\" || id == \"$BOOT\"").sync().data.map { it.id }
            assertTrue(left.isEmpty(), "still listed after the reset: ${left.distinct()}")
        }
    }

    /** Before the first test of a page: remove the listed objects and wait until they are gone. */
    @JvmStatic
    fun resetPage() {
        LiveClients().use { clients ->
            val server = clients.server(ROLE, "docs-reset")
            Retry.retryTransient(label = "reset the datasync objects") { removeListed(server) }
            untilGone(server)
        }
    }

    /** After the last test of a page: best effort removal. The janitor removes the same ids again after the run. */
    @JvmStatic
    fun finishPage() {
        try {
            LiveClients().use { clients -> removeListed(clients.server(ROLE, "docs-reset")) }
        } catch (error: Exception) {
            println(Redact.scrub("datasync cleanup after the page failed: ${error.javaClass.simpleName}"))
        }
    }

    /** The companions of the DataSync page: Bob (a user of another type) and a second product, so that a filter has something to leave out. */
    @JvmStatic
    fun seedCompanions() {
        LiveClients().use { clients ->
            val server = clients.server(ROLE, "docs-reset")
            createUser(server, BOB, "Bob", "seller")
            createProduct(server, BOOT, "Leather Boot", 150.0, 0)
        }
    }

    /** What the DataSync listener samples observe: Alice, the summer channel and the sneaker. */
    @JvmStatic
    fun seedObserved() {
        LiveClients().use { clients ->
            val server = clients.server(ROLE, "docs-reset")
            createUser(server, ALICE, "Alice", "shopper")
            createChannel(server, SUMMER, "Summer Sale", "promotion")
            createProduct(server, SNEAKER, "Retro Sneaker", 89.99, 12)
        }
    }

    /**
     * The `status` of the result of every kind of call (create 201, the others 200 on the page) on throwaway objects of the five
     * kinds, keyed by the call name. The objects use the probe ids of [Resources] and are removed again.
     */
    @JvmStatic
    fun resultStatuses(server: PubNub): Map<String, Int> {
        val api = server.dataSync
        val statuses = linkedMapOf<String, Int>()
        val user = "docs-probe-user"
        val channel = "docs-probe-channel"
        val product = "docs-probe-product"
        val membership = "docs-probe-membership"
        val link = "docs-probe-rel"
        val patch = listOf(PNJsonPatchOperation(op = "replace", path = "/payload/name", value = "Patched"))
        try {
            statuses["createUser"] = api.createUser(classVersion = 1, userId = user, payload = mapOf("name" to "Probe")).sync().status
            statuses["getUser"] = awaitRead("the probe user", { api.getUser(user).sync() }).status
            statuses["getUsers"] = api.getUsers(filterFast = "id == \"$user\"").sync().status
            statuses["setUser"] = api.setUser(user, 1, payload = mapOf("name" to "Probe set")).sync().status
            statuses["updateUser"] = api.updateUser(user, patch).sync().status
            statuses["createChannel"] = api.createChannel(classVersion = 1, channelId = channel, payload = mapOf("name" to "Probe")).sync().status
            statuses["getChannel"] = awaitRead("the probe channel", { api.getChannel(channel).sync() }).status
            statuses["getChannels"] = api.getChannels(filterFast = "id == \"$channel\"").sync().status
            statuses["setChannel"] = api.setChannel(channel, 1, payload = mapOf("name" to "Probe set")).sync().status
            statuses["updateChannel"] = api.updateChannel(channel, patch).sync().status
            statuses["createEntity"] =
                api.createEntity("product", 1, entityId = product, payload = mapOf("name" to "Probe", "price" to 1.0, "stock" to 1)).sync().status
            statuses["getEntity"] = awaitRead("the probe product", { api.getEntity(product).sync() }).status
            statuses["getEntities"] = api.getEntities("product", filterFast = "id == \"$product\"").sync().status
            statuses["setEntity"] =
                api.setEntity(product, 1, payload = mapOf("name" to "Probe set", "price" to 1.0, "stock" to 1)).sync().status
            statuses["updateEntity"] = api.updateEntity(product, patch).sync().status
            statuses["createMembership"] = api.createMembership(channel, user, 1, membershipId = membership).sync().status
            statuses["getMembership"] = awaitRead("the probe membership", { api.getMembership(membership).sync() }).status
            statuses["getMemberships"] = api.getMemberships(userId = user).sync().status
            statuses["setMembership"] = api.setMembership(membership, 1, payload = mapOf("role" to "set")).sync().status
            statuses["updateMembership"] =
                api.updateMembership(membership, listOf(PNJsonPatchOperation(op = "replace", path = "/payload/role", value = "patched"))).sync().status
            statuses["createRelationship"] = api.createRelationship(user, product, "ProductOwner", 1, relationshipId = link).sync().status
            statuses["getRelationship"] = awaitRead("the probe link", { api.getRelationship(link).sync() }).status
            statuses["getRelationships"] = api.getRelationships("ProductOwner", entityAId = user).sync().status
            statuses["setRelationship"] = api.setRelationship(link, 1, payload = mapOf("tier" to "set")).sync().status
            statuses["updateRelationship"] =
                api.updateRelationship(link, listOf(PNJsonPatchOperation(op = "replace", path = "/payload/tier", value = "patched"))).sync().status
        } finally {
            listOf(
                { api.removeRelationship(link).sync() },
                { api.removeMembership(membership).sync() },
                { api.removeEntity(product).sync() },
                { api.removeUser(user).sync() },
                { api.removeChannel(channel).sync() },
            ).forEach { quietly { it() } }
        }
        return statuses
    }

    /** What [resultStatuses] must return according to the page: 201 for a create and 200 for every other call. */
    @JvmStatic
    fun documentedStatuses(): Map<String, Int> {
        val lists =
            mapOf(
                "User" to "Users",
                "Channel" to "Channels",
                "Entity" to "Entities",
                "Membership" to "Memberships",
                "Relationship" to "Relationships",
            )
        val statuses = linkedMapOf<String, Int>()
        for ((kind, plural) in lists) {
            statuses["create$kind"] = 201
            statuses["get$kind"] = 200
            statuses["get$plural"] = 200
            statuses["set$kind"] = 200
            statuses["update$kind"] = 200
        }
        return statuses
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Waits for Java lambdas, and reads of one object
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Reads until the service answers and [check] holds three times in a row. A replica that has not seen a write yet answers 404,
     * which counts as a pass that did not hold.
     */
    fun <T> awaitRead(
        what: String,
        read: () -> T,
        check: (T) -> Unit = {},
    ): T =
        Wait.settled(what, timeoutMs = 45_000) {
            val value =
                try {
                    read()
                } catch (error: PubNubException) {
                    throw AssertionError("$what: the read answered HTTP ${error.statusCode}")
                }
            check(value)
            value
        }

    /** [awaitRead] for a Java lambda. */
    @JvmStatic
    fun <T> awaitReadJava(
        what: String,
        read: Callable<T>,
        check: java.util.function.Consumer<T>,
    ): T = awaitRead(what, { read.call() }, { check.accept(it) })

    /** [Wait.settled] for a Java lambda that can throw. */
    @JvmStatic
    fun settled(
        what: String,
        run: Executable,
    ) = Wait.settled(what, timeoutMs = 45_000) { run.execute() }

    /** Waits until [read] answers 404, the way a read after a remove ends. A replica that has not seen the remove still answers. */
    @JvmStatic
    fun awaitMissing(
        what: String,
        read: Callable<*>,
    ) {
        Wait.eventually("$what reads 404 after the removal", timeoutMs = 30_000) {
            try {
                read.call()
            } catch (error: PubNubException) {
                assertEquals(404, error.statusCode, "$what: the read failed with something other than 404")
                return@eventually
            }
            throw AssertionError("$what can still be read")
        }
    }

    /**
     * The page says `expiresAt` is the creation time plus the class TTL, rounded up to the start of the next whole UTC day, and
     * that an update never refreshes it. The timestamps are ISO 8601 text.
     */
    @JvmStatic
    fun assertExpiry(
        createdAt: String,
        expiresAt: String,
        ttlDays: Long,
    ) {
        val target = Instant.parse(createdAt).plus(Duration.ofDays(ttlDays))
        val nextDay = target.truncatedTo(ChronoUnit.DAYS).plus(Duration.ofDays(1))
        val expires = Instant.parse(expiresAt)
        val onTheDot = target == target.truncatedTo(ChronoUnit.DAYS)
        assertTrue(
            expires == nextDay || (onTheDot && expires == target),
            "expiresAt $expiresAt is not the creation time $createdAt plus $ttlDays days rounded up to the next day $nextDay",
        )
    }

    /** The HTTP status of the failure of [call], or fails when [call] succeeds. */
    @JvmStatic
    fun failureStatus(call: Callable<*>): Int {
        try {
            call.call()
        } catch (error: PubNubException) {
            return error.statusCode
        }
        throw AssertionError("the call was expected to fail, but it succeeded")
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Writes outside the samples (the second client of a test). They return the eTag the service sent.
    // ---------------------------------------------------------------------------------------------------------------

    @JvmStatic
    fun createUser(
        server: PubNub,
        id: String,
        name: String,
        type: String,
    ): String =
        Retry.retryTransient(label = "create user $id") {
            server.dataSync.createUser(classVersion = 1, userId = id, payload = mapOf("name" to name, "type" to type)).sync().data.eTag
        }

    @JvmStatic
    fun setUser(
        server: PubNub,
        id: String,
        name: String,
        type: String,
    ): String =
        Retry.retryTransient(label = "set user $id") {
            server.dataSync.setUser(id, 1, payload = mapOf("name" to name, "type" to type)).sync().data.eTag
        }

    @JvmStatic
    fun createChannel(
        server: PubNub,
        id: String,
        name: String,
        type: String,
    ): String =
        Retry.retryTransient(label = "create channel $id") {
            server.dataSync.createChannel(
                classVersion = 1,
                channelId = id,
                payload = mapOf("name" to name, "type" to type),
            ).sync().data.eTag
        }

    @JvmStatic
    fun setChannel(
        server: PubNub,
        id: String,
        name: String,
        type: String,
    ): String =
        Retry.retryTransient(label = "set channel $id") {
            server.dataSync.setChannel(id, 1, payload = mapOf("name" to name, "type" to type)).sync().data.eTag
        }

    @JvmStatic
    fun createProduct(
        server: PubNub,
        id: String,
        name: String,
        price: Double,
        stock: Int,
    ): String =
        Retry.retryTransient(label = "create product $id") {
            server.dataSync.createEntity(
                className = "product",
                classVersion = 1,
                entityId = id,
                payload = mapOf("name" to name, "price" to price, "stock" to stock),
            ).sync().data.eTag
        }

    /** Replaces the payload of a product. [extra] adds free-form payload fields next to name, price and stock. */
    @JvmStatic
    @JvmOverloads
    fun setProduct(
        server: PubNub,
        id: String,
        name: String,
        price: Double,
        stock: Int,
        extra: Map<String, Any?> = emptyMap(),
    ): String =
        Retry.retryTransient(label = "set product $id") {
            server.dataSync.setEntity(
                id,
                1,
                payload = mapOf("name" to name, "price" to price, "stock" to stock) + extra,
            ).sync().data.eTag
        }

    @JvmStatic
    fun createMembership(
        server: PubNub,
        id: String,
        channelId: String,
        userId: String,
        status: String,
        role: String,
    ): String =
        Retry.retryTransient(label = "create membership $id") {
            server.dataSync.createMembership(
                channelId = channelId,
                userId = userId,
                classVersion = 1,
                membershipId = id,
                status = status,
                payload = mapOf("role" to role),
            ).sync().data.eTag
        }

    @JvmStatic
    fun setMembership(
        server: PubNub,
        id: String,
        status: String?,
        role: String,
    ): String =
        Retry.retryTransient(label = "set membership $id") {
            server.dataSync.setMembership(id, 1, status = status, payload = mapOf("role" to role)).sync().data.eTag
        }

    @JvmStatic
    fun createLink(
        server: PubNub,
        id: String,
        sellerId: String,
        productId: String,
        payload: Map<String, Any?>,
    ): String =
        Retry.retryTransient(label = "create relationship $id") {
            server.dataSync.createRelationship(
                entityAId = sellerId,
                entityBId = productId,
                className = "ProductOwner",
                classVersion = 1,
                relationshipId = id,
                payload = payload,
            ).sync().data.eTag
        }

    @JvmStatic
    fun setLink(
        server: PubNub,
        id: String,
        payload: Map<String, Any?>,
    ): String =
        Retry.retryTransient(label = "set relationship $id") {
            server.dataSync.setRelationship(id, 1, payload = payload).sync().data.eTag
        }

    // ---------------------------------------------------------------------------------------------------------------
    // Reads of a list: the ids it holds. Null means the parameter is not sent.
    // ---------------------------------------------------------------------------------------------------------------

    @JvmStatic
    @JvmOverloads
    fun userIds(
        server: PubNub,
        filterFast: String? = null,
        filter: String? = null,
        limit: Int? = null,
    ): List<String> = server.dataSync.getUsers(filterFast = filterFast, filter = filter, limit = limit).sync().data.map { it.id }

    @JvmStatic
    @JvmOverloads
    fun channelIds(
        server: PubNub,
        filterFast: String? = null,
        filter: String? = null,
        limit: Int? = null,
    ): List<String> = server.dataSync.getChannels(filterFast = filterFast, filter = filter, limit = limit).sync().data.map { it.id }

    /** The products, optionally sorted by [sortBy] ascending. */
    @JvmStatic
    @JvmOverloads
    fun productIds(
        server: PubNub,
        filterFast: String? = null,
        filter: String? = null,
        sortBy: String? = null,
        limit: Int? = null,
    ): List<String> =
        server.dataSync.getEntities(
            className = "product",
            filterFast = filterFast,
            filter = filter,
            sort = sortBy?.let { listOf(PNDataSyncSortField(property = it)) } ?: emptyList(),
            limit = limit,
        ).sync().data.map { it.id }

    @JvmStatic
    @JvmOverloads
    fun membershipIds(
        server: PubNub,
        userId: String? = null,
        channelId: String? = null,
        filterFast: String? = null,
        filter: String? = null,
    ): List<String> =
        server.dataSync.getMemberships(userId = userId, channelId = channelId, filterFast = filterFast, filter = filter, limit = 20)
            .sync().data.map { it.id }

    @JvmStatic
    @JvmOverloads
    fun linkIds(
        server: PubNub,
        entityAId: String? = null,
        entityBId: String? = null,
        filterFast: String? = null,
        filter: String? = null,
    ): List<String> =
        server.dataSync.getRelationships(
            className = "ProductOwner",
            entityAId = entityAId,
            entityBId = entityBId,
            filterFast = filterFast,
            filter = filter,
            limit = 20,
        ).sync().data.map { it.id }

    // ---------------------------------------------------------------------------------------------------------------
    // Events
    // ---------------------------------------------------------------------------------------------------------------

    /** Collects the DataSync events a Kotlin client receives. Add it before the action, [await] after it. */
    class Events(private val client: PubNub) : EventListener, AutoCloseable {
        val received = CopyOnWriteArrayList<PNDataSyncEventResult>()
        private val lock = Object()

        init {
            client.addListener(this)
        }

        override fun dataSync(
            pubnub: PubNub,
            result: PNDataSyncEventResult,
        ) {
            received.add(result)
            synchronized(lock) { lock.notifyAll() }
        }

        /** The first event that matches [test], or a failure that lists what arrived. */
        fun await(
            what: String,
            timeoutMs: Long = 30_000,
            test: (PNDataSyncEventResult) -> Boolean,
        ): PNDataSyncEventResult {
            val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs)
            while (true) {
                received.firstOrNull(test)?.let { return it }
                val left = TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime())
                if (left <= 0) {
                    val seen = received.joinToString("; ") { "${it.channel}: ${it.extractedMessage.type}" }.ifEmpty { "none" }
                    throw AssertionError("$what did not arrive within ${timeoutMs / 1000.0}s. Events seen: $seen")
                }
                synchronized(lock) { lock.wait(minOf(left, 500)) }
            }
        }

        override fun close() {
            client.removeListener(this)
        }
    }

    private const val PROBE_WAIT_MS = 20_000L

    private data class ProbeStep(val label: String, val type: String, val id: String, val create: () -> Unit)

    /**
     * The preflight probe of the event rules. Event publishing is a per class switch in the Admin Portal that the Admin API cannot
     * read, so each of the five classes the samples use gets a throwaway object, and the probe waits for the create event. Returns
     * one line per class whose events are off ("DataSync events are off for product v1"). The objects use ids of [Resources] and
     * are removed again, whatever happens.
     */
    @JvmStatic
    fun probeEvents(): List<String> {
        val problems = mutableListOf<String>()
        try {
            LiveClients().use { clients ->
                val server = clients.server(ROLE, "docs-probe")
                removeListed(server)
                val user = "docs-probe-user"
                val channel = "docs-probe-channel"
                val product = "docs-probe-product"
                val channels = listOf(user, channel, product)
                val events = Events(server)
                try {
                    val ready = Wait.connected(server, channels)
                    server.subscribe(channels = channels)
                    ready.await()
                    probeSteps(server, events, user, channel, product, problems)
                } finally {
                    events.close()
                    removeListed(server)
                }
            }
        } catch (error: Exception) {
            problems.add(Redact.scrub("DataSync event probe failed: ${error.javaClass.simpleName}: ${error.message}"))
        } catch (error: AssertionError) {
            problems.add(Redact.scrub("DataSync event probe could not subscribe: ${error.message}"))
        }
        return problems
    }

    private fun probeSteps(
        server: PubNub,
        events: Events,
        user: String,
        channel: String,
        product: String,
        problems: MutableList<String>,
    ) {
        val steps =
            listOf(
                ProbeStep("User v1", "user", user) { createUser(server, user, "Probe", "probe") },
                ProbeStep("Channel v1", "channel", channel) { createChannel(server, channel, "Probe", "probe") },
                ProbeStep("product v1", "entity", product) { createProduct(server, product, "Probe", 1.0, 1) },
                ProbeStep("Membership v1", "membership", "docs-probe-membership") {
                    createMembership(server, "docs-probe-membership", channel, user, "active", "probe")
                },
                ProbeStep("ProductOwner v1", "relationship", "docs-probe-rel") {
                    createLink(server, "docs-probe-rel", user, product, mapOf("since" to "2026-01-01"))
                },
            )
        for (step in steps) {
            try {
                step.create()
                events.await("the create event of ${step.label}", PROBE_WAIT_MS) {
                    it.extractedMessage.type == step.type && idOf(it.extractedMessage) == step.id
                }
            } catch (error: AssertionError) {
                val seconds = PROBE_WAIT_MS / 1000
                problems.add("DataSync events are off for ${step.label} (no create event of the probe object within $seconds s)")
            } catch (error: PubNubException) {
                problems.add("DataSync probe for ${step.label} could not write: HTTP ${error.statusCode}")
            }
        }
    }

    /** The id of the object an event carries, or null for a message kind without one. */
    @JvmStatic
    fun idOf(message: PNDataSyncEventMessage): String? =
        when (message) {
            is PNSetDataSyncUserEventMessage -> message.data.id
            is PNSetDataSyncChannelEventMessage -> message.data.id
            is PNSetDataSyncEntityEventMessage -> message.data.id
            is PNSetDataSyncMembershipEventMessage -> message.data.id
            is PNSetDataSyncRelationshipEventMessage -> message.data.id
            else -> null
        }

    /** Whether [result] is a create or update (by [event]) of the object [id] of the kind [type]. */
    @JvmStatic
    fun isSet(
        result: PNDataSyncEventResult,
        type: String,
        id: String,
        event: PNDataSyncSetEventType,
    ): Boolean {
        val message = result.extractedMessage
        val setEvent =
            when (message) {
                is PNSetDataSyncUserEventMessage -> message.event
                is PNSetDataSyncChannelEventMessage -> message.event
                is PNSetDataSyncEntityEventMessage -> message.event
                is PNSetDataSyncMembershipEventMessage -> message.event
                is PNSetDataSyncRelationshipEventMessage -> message.event
                else -> null
            }
        return message.type == type && idOf(message) == id && setEvent == event
    }
}
