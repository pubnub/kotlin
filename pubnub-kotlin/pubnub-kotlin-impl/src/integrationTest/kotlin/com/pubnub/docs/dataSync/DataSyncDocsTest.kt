@file:Suppress("ktlint")

package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncGetChannelsResult
import com.pubnub.api.models.consumer.datasync.dataSyncErrorCode
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntitiesResult
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncGetMembershipsResult
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncGetUsersResult
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
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.DataSyncKit
import com.pubnub.docs.support.DataSyncKit.ALICE
import com.pubnub.docs.support.DataSyncKit.BOB
import com.pubnub.docs.support.DataSyncKit.BOOT
import com.pubnub.docs.support.DataSyncKit.LINK
import com.pubnub.docs.support.DataSyncKit.ROLE
import com.pubnub.docs.support.DataSyncKit.SNEAKER
import com.pubnub.docs.support.DataSyncKit.SUMMER
import com.pubnub.docs.support.DataSyncKit.SUMMER_MEMBERSHIP
import com.pubnub.docs.support.DataSyncKit.WINTER
import com.pubnub.docs.support.DataSyncKit.WINTER_MEMBERSHIP
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page data-sync (docs/sdks/kotlin/api-reference/data-sync.md). One test per snippet. The text
 * between a start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions below it.
 *
 * The samples work on objects with literal ids that persist on the keyset (a user, a channel, a membership, a product and a
 * link), and the service answers a create of an existing id with 409. So the page removes every listed object before and after
 * its run, and the tests run in the order of `@Order`: each one builds on the objects of the tests before it (create, read, list,
 * update, patch), the removals come last. The "Main" samples build their own client, so what they did is proven through what
 * they printed and through a read of a second client. The "Other" samples get the recording client.
 */
@ExtendWith(LiveExtension::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DataSyncDocsTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun prepare() {
            DataSyncKit.resetPage()
            DataSyncKit.seedCompanions()
        }

        @AfterAll
        @JvmStatic
        fun finish() = DataSyncKit.finishPage()
    }

    private fun assertPrinted(out: Stdout, text: String) =
        Wait.eventually("the sample printed \"$text\"", timeoutMs = 20_000) { out.assertPrinted(text) }

    private fun assertPrintedLine(out: Stdout, line: String) =
        Wait.eventually("the sample printed the line \"$line\"", timeoutMs = 20_000) {
            assertTrue(out.text().lines().contains(line), "the sample did not print the line \"$line\". Printed: ${out.text().take(800)}")
        }

    private fun assertNotPrintedLine(out: Stdout, line: String) =
        assertTrue(!out.text().lines().contains(line), "the sample printed the line \"$line\" that the filter must leave out")

    private fun assertNoFailure(out: Stdout) =
        assertTrue(out.lines("Failed:").isEmpty(), "the sample printed a failure: ${out.lines("Failed:")}")

    // -----------------------------------------------------------------------------------------------------------------
    // Users
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(10)
    fun createUserBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val out = Stdout.captureForTest()

        // snippet.createUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.createUser(
            userId = "user-alice",
            classVersion = 1,
            payload = mapOf("name" to "Alice", "type" to "shopper")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created user ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val user = DataSyncKit.awaitRead("Alice can be read", { server.dataSync.getUser(ALICE).sync().data }) {
            assertEquals(mapOf("name" to "Alice", "type" to "shopper"), it.payload)
        }
        assertPrinted(out, "Created user user-alice with eTag ${user.eTag}")
        assertNoFailure(out)
        assertEquals("User", user.className)
        assertEquals(1, user.classVersion)
        assertEquals("Global", user.classLevel)
        assertTrue(user.createdAt.isNotBlank() && user.updatedAt.isNotBlank() && user.expiresAt.isNotBlank(), "timestamps are set")
    }

    @Test
    @Order(11)
    fun getUserBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("Alice can be read", { server.dataSync.getUser(ALICE).sync().data })
        val out = Stdout.captureForTest()

        // snippet.getUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getUser("user-alice").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched user ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Fetched user user-alice with payload {name=Alice, type=shopper}")
        assertNoFailure(out)
    }

    @Test
    @Order(12)
    fun getUsersBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of users shows Alice and Bob") {
            val ids = DataSyncKit.userIds(server, limit = 10)
            assertTrue(ids.containsAll(listOf(ALICE, BOB)), "the list holds $ids")
        }
        val out = Stdout.captureForTest()

        // snippet.getUsersBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getUsers(
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrintedLine(out, ALICE)
        assertPrintedLine(out, BOB)
        assertPrintedLine(out, "More pages available: false")
        assertNoFailure(out)
    }

    @Test
    @Order(13)
    fun getUsersFilterFast(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getUsersFilterFast
        pubnub.dataSync.getUsers(
            filterFast = "type == \"shopper\"",
            sort = listOf(PNDataSyncSortField(property = "name")),
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getUsers") as PNDataSyncGetUsersResult
        assertTrue(result.data.any { it.id == ALICE }, "the filter finds Alice right after her creation")
        assertTrue(result.data.none { it.id == BOB }, "Bob is a seller, the filter leaves him out")
        assertPrintedLine(out, ALICE)
        assertNotPrintedLine(out, BOB)
        assertNoFailure(out)
    }

    @Test
    @Order(14)
    fun getUsersFilter(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        // `filter` is eventually consistent: wait until it holds on consecutive reads, then run the sample once.
        DataSyncKit.settled("the filter index lists Alice") {
            assertEquals(listOf(ALICE), DataSyncKit.userIds(server, filter = "name LIKE \"Alice*\""))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getUsersFilter
        pubnub.dataSync.getUsers(
            filter = "name LIKE \"Alice*\"",
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getUsers") as PNDataSyncGetUsersResult
        assertEquals(listOf(ALICE), result.data.map { it.id })
        assertPrintedLine(out, ALICE)
        assertNotPrintedLine(out, BOB)
        assertNoFailure(out)
    }

    @Test
    @Order(15)
    fun getUsersPagination(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of users shows Alice and Bob") {
            assertTrue(DataSyncKit.userIds(server, limit = 20).containsAll(listOf(ALICE, BOB)))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getUsersPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getUsers(
                limit = 20,
                cursor = cursor
            ).async { result ->
                result.onFailure { exception ->
                    println("Failed: ${exception.message}")
                }.onSuccess { value ->
                    value.data.forEach { println(it.id) }
                    if (value.next.hasNext) {
                        fetchPage(value.next.cursor)
                    }
                }
            }
        }

        fetchPage(cursor = null)
        // snippet.end

        val first = recorder.awaitOk("getUsers") as PNDataSyncGetUsersResult
        assertTrue(first.data.size <= 20)
        assertPrintedLine(out, ALICE)
        assertPrintedLine(out, BOB)
        assertNoFailure(out)
    }

    @Test
    @Order(16)
    fun setUserBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val before = DataSyncKit.awaitRead("Alice can be read", { server.dataSync.getUser(ALICE).sync().data })
        val out = Stdout.captureForTest()

        // snippet.setUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.setUser(
            userId = "user-alice",
            classVersion = 1,
            payload = mapOf("name" to "Alice B.", "type" to "shopper")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced user ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("Alice shows the new name", { server.dataSync.getUser(ALICE).sync().data }) {
            assertEquals("Alice B.", it.payload?.get("name"))
        }
        assertPrinted(out, "Replaced user user-alice, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag, "the write produced a new eTag")
    }

    @Test
    @Order(17)
    fun updateUserBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        // The patch must change something: put the old name back first.
        DataSyncKit.setUser(server, ALICE, "Alice", "shopper")
        val before = DataSyncKit.awaitRead("Alice shows the old name", { server.dataSync.getUser(ALICE).sync().data }) {
            assertEquals("Alice", it.payload?.get("name"))
        }
        val out = Stdout.captureForTest()

        // snippet.updateUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.updateUser(
            userId = "user-alice",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/name", value = "Alice B.")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated user ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("Alice shows the patched name", { server.dataSync.getUser(ALICE).sync().data }) {
            assertEquals("Alice B.", it.payload?.get("name"))
            assertEquals("shopper", it.payload?.get("type"))
        }
        assertPrinted(out, "Updated user user-alice, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
    }

    /** Not a sample: the page says a stale `ifMatch` fails with a 412 and that the category is a DATASYNC_* one chosen from the status. */
    @Test
    @Order(18)
    fun staleIfMatchFailsAsDocumented(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val stale = DataSyncKit.awaitRead("Alice can be read", { server.dataSync.getUser(ALICE).sync().data }).eTag
        DataSyncKit.setUser(server, ALICE, "Alice B.", "shopper")
        val error = assertThrows<PubNubException> {
            server.dataSync.setUser(ALICE, 1, payload = mapOf("name" to "stale", "type" to "shopper"), ifMatch = stale).sync()
        }
        assertEquals(412, error.statusCode, "the page says a stale eTag fails with 412")
        assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, error.pubnubError)
        assertTrue(error.dataSyncErrorCode()?.startsWith("DS-") == true, "the error carries a DS- code")
    }

    /**
     * Not a sample: the page says "a successful call yields a result object carrying the HTTP `status`" and its response blocks
     * show 201 for a create and 200 for every other call.
     */
    @Test
    @Order(19)
    fun successfulResultsCarryTheHttpStatus(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        assertEquals(DataSyncKit.documentedStatuses(), DataSyncKit.resultStatuses(server))
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Channels
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(20)
    fun createChannelBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val out = Stdout.captureForTest()

        // snippet.createChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.createChannel(
            channelId = "channel-summer-sale",
            classVersion = 1,
            payload = mapOf("name" to "Summer Sale", "type" to "promotion")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created channel ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val channel = DataSyncKit.awaitRead("the channel can be read", { server.dataSync.getChannel(SUMMER).sync().data }) {
            assertEquals(mapOf("name" to "Summer Sale", "type" to "promotion"), it.payload)
        }
        assertPrinted(out, "Created channel channel-summer-sale with eTag ${channel.eTag}")
        assertNoFailure(out)
        assertEquals("Channel", channel.className)
        assertEquals(1, channel.classVersion)
        assertEquals("Global", channel.classLevel)
    }

    @Test
    @Order(21)
    fun getChannelBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the channel can be read", { server.dataSync.getChannel(SUMMER).sync().data })
        val out = Stdout.captureForTest()

        // snippet.getChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getChannel("channel-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched channel ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Fetched channel channel-summer-sale with payload {name=Summer Sale, type=promotion}")
        assertNoFailure(out)
    }

    @Test
    @Order(22)
    fun getChannelsBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of channels shows the summer channel") {
            assertTrue(DataSyncKit.channelIds(server, limit = 10).contains(SUMMER))
        }
        val out = Stdout.captureForTest()

        // snippet.getChannelsBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getChannels(
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrintedLine(out, SUMMER)
        assertPrintedLine(out, "More pages available: false")
        assertNoFailure(out)
    }

    @Test
    @Order(23)
    fun getChannelsFilterFast(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getChannelsFilterFast
        pubnub.dataSync.getChannels(
            filterFast = "type == \"promotion\"",
            sort = listOf(PNDataSyncSortField(property = "name")),
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getChannels") as PNDataSyncGetChannelsResult
        assertTrue(result.data.any { it.id == SUMMER }, "the filter finds the channel right after its creation")
        assertPrintedLine(out, SUMMER)
        assertNoFailure(out)
    }

    @Test
    @Order(24)
    fun getChannelsFilter(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the filter index lists the summer channel") {
            assertEquals(listOf(SUMMER), DataSyncKit.channelIds(server, filter = "name LIKE \"Summer*\""))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getChannelsFilter
        pubnub.dataSync.getChannels(
            filter = "name LIKE \"Summer*\"",
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getChannels") as PNDataSyncGetChannelsResult
        assertEquals(listOf(SUMMER), result.data.map { it.id })
        assertPrintedLine(out, SUMMER)
        assertNoFailure(out)
    }

    @Test
    @Order(25)
    fun getChannelsPagination(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of channels shows the summer channel") {
            assertTrue(DataSyncKit.channelIds(server, limit = 20).contains(SUMMER))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getChannelsPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getChannels(
                limit = 20,
                cursor = cursor
            ).async { result ->
                result.onFailure { exception ->
                    println("Failed: ${exception.message}")
                }.onSuccess { value ->
                    value.data.forEach { println(it.id) }
                    if (value.next.hasNext) {
                        fetchPage(value.next.cursor)
                    }
                }
            }
        }

        fetchPage(cursor = null)
        // snippet.end

        recorder.awaitOk("getChannels")
        assertPrintedLine(out, SUMMER)
        assertNoFailure(out)
    }

    @Test
    @Order(26)
    fun setChannelBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val before = DataSyncKit.awaitRead("the channel can be read", { server.dataSync.getChannel(SUMMER).sync().data })
        val out = Stdout.captureForTest()

        // snippet.setChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.setChannel(
            channelId = "channel-summer-sale",
            classVersion = 1,
            payload = mapOf("name" to "Summer Sale 2026", "type" to "promotion")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced channel ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the channel shows the new name", { server.dataSync.getChannel(SUMMER).sync().data }) {
            assertEquals("Summer Sale 2026", it.payload?.get("name"))
        }
        assertPrinted(out, "Replaced channel channel-summer-sale, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
    }

    @Test
    @Order(27)
    fun updateChannelBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        // The patch must change something: put the old name back first.
        DataSyncKit.setChannel(server, SUMMER, "Summer Sale", "promotion")
        val before = DataSyncKit.awaitRead("the channel shows the old name", { server.dataSync.getChannel(SUMMER).sync().data }) {
            assertEquals("Summer Sale", it.payload?.get("name"))
        }
        val out = Stdout.captureForTest()

        // snippet.updateChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.updateChannel(
            channelId = "channel-summer-sale",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/name", value = "Summer Sale 2026")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated channel ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the channel shows the patched name", { server.dataSync.getChannel(SUMMER).sync().data }) {
            assertEquals("Summer Sale 2026", it.payload?.get("name"))
            assertEquals("promotion", it.payload?.get("type"))
        }
        assertPrinted(out, "Updated channel channel-summer-sale, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Memberships
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(30)
    fun createMembershipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val out = Stdout.captureForTest()

        // snippet.createMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.createMembership(
            membershipId = "membership-alice-summer-sale",
            channelId = "channel-summer-sale",
            userId = "user-alice",
            classVersion = 1,
            status = "active",
            payload = mapOf("role" to "viewer")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created membership ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val membership = DataSyncKit.awaitRead("the membership can be read", { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync().data }) {
            assertEquals(mapOf("role" to "viewer"), it.payload)
        }
        assertPrinted(out, "Created membership membership-alice-summer-sale with eTag ${membership.eTag}")
        assertNoFailure(out)
        assertEquals(ALICE, membership.userId)
        assertEquals(SUMMER, membership.channelId)
        assertEquals("active", membership.status)
        assertEquals("Membership", membership.className)
        assertEquals(1, membership.classVersion)
    }

    @Test
    @Order(31)
    fun getMembershipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the membership can be read", { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync().data })
        val out = Stdout.captureForTest()

        // snippet.getMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getMembership("membership-alice-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched membership ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Fetched membership membership-alice-summer-sale with payload {role=viewer}")
        assertNoFailure(out)
    }

    @Test
    @Order(32)
    fun getMembershipsBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("Alice lists exactly her membership") {
            assertEquals(listOf(SUMMER_MEMBERSHIP), DataSyncKit.membershipIds(server, userId = ALICE))
        }
        val out = Stdout.captureForTest()

        // snippet.getMembershipsBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getMemberships(
            userId = "user-alice",
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrintedLine(out, SUMMER_MEMBERSHIP)
        assertPrintedLine(out, "More pages available: false")
        assertNoFailure(out)
    }

    @Test
    @Order(33)
    fun getMembershipsByChannelId(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the summer channel lists exactly Alice") {
            assertEquals(listOf(SUMMER_MEMBERSHIP), DataSyncKit.membershipIds(server, channelId = SUMMER))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getMembershipsByChannelId
        pubnub.dataSync.getMemberships(
            channelId = "channel-summer-sale",
            limit = 50
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println("${it.userId} is a member of ${it.channelId}") }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getMemberships") as PNDataSyncGetMembershipsResult
        assertEquals(listOf(ALICE), result.data.map { it.userId })
        assertPrintedLine(out, "user-alice is a member of channel-summer-sale")
        assertPrintedLine(out, "More pages available: false")
        assertNoFailure(out)
    }

    @Test
    @Order(34)
    fun getMembershipsFilterFast(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        // A second membership of Alice, archived, that the filter must leave out. The next test removes its channel again.
        DataSyncKit.createChannel(server, WINTER, "Winter Sale", "promotion")
        DataSyncKit.createMembership(server, WINTER_MEMBERSHIP, WINTER, ALICE, "archived", "viewer")
        DataSyncKit.settled("Alice lists both memberships") {
            assertEquals(setOf(SUMMER_MEMBERSHIP, WINTER_MEMBERSHIP), DataSyncKit.membershipIds(server, userId = ALICE).toSet())
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getMembershipsFilterFast
        // Membership payload fields such as `role` are not filterable; filter on `status` instead
        pubnub.dataSync.getMemberships(
            userId = "user-alice",
            filterFast = "status == \"active\"",
            sort = listOf(PNDataSyncSortField(property = "createdAt", ascending = false)),
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getMemberships") as PNDataSyncGetMembershipsResult
        assertEquals(listOf(SUMMER_MEMBERSHIP), result.data.map { it.id }, "only the active membership matches")
        assertPrintedLine(out, SUMMER_MEMBERSHIP)
        assertNotPrintedLine(out, WINTER_MEMBERSHIP)
        assertNoFailure(out)
    }

    @Test
    @Order(35)
    fun getMembershipsFilter(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        Cleanup.cleanupAfterTest("remove the winter channel and its membership") { server.dataSync.removeChannel(WINTER).sync() }
        // `filter` is eventually consistent: wait until the index shows the active membership and not the archived one.
        DataSyncKit.settled("the filter index lists the active membership only") {
            assertEquals(listOf(SUMMER_MEMBERSHIP), DataSyncKit.membershipIds(server, userId = ALICE, filter = "status LIKE \"act*\""))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getMembershipsFilter
        // Membership payload fields such as `role` are not filterable; filter on `status` instead
        pubnub.dataSync.getMemberships(
            userId = "user-alice",
            filter = "status LIKE \"act*\"",
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getMemberships") as PNDataSyncGetMembershipsResult
        assertEquals(listOf(SUMMER_MEMBERSHIP), result.data.map { it.id }, "only the active membership matches")
        assertPrintedLine(out, SUMMER_MEMBERSHIP)
        assertNotPrintedLine(out, WINTER_MEMBERSHIP)
        assertNoFailure(out)
    }

    @Test
    @Order(36)
    fun getMembershipsPagination(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("Alice lists her membership") {
            assertTrue(DataSyncKit.membershipIds(server, userId = ALICE).contains(SUMMER_MEMBERSHIP))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getMembershipsPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getMemberships(
                userId = "user-alice",
                limit = 20,
                cursor = cursor
            ).async { result ->
                result.onFailure { exception ->
                    println("Failed: ${exception.message}")
                }.onSuccess { value ->
                    value.data.forEach { println(it.id) }
                    if (value.next.hasNext) {
                        fetchPage(value.next.cursor)
                    }
                }
            }
        }

        fetchPage(cursor = null)
        // snippet.end

        recorder.awaitOk("getMemberships")
        assertPrintedLine(out, SUMMER_MEMBERSHIP)
        assertNoFailure(out)
    }

    @Test
    @Order(37)
    fun setMembershipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val before = DataSyncKit.awaitRead("the membership can be read", { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync().data })
        val out = Stdout.captureForTest()

        // snippet.setMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.setMembership(
            membershipId = "membership-alice-summer-sale",
            classVersion = 1,
            payload = mapOf("role" to "moderator")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced membership ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the membership shows the new role", { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync().data }) {
            assertEquals(mapOf("role" to "moderator"), it.payload)
        }
        assertPrinted(out, "Replaced membership membership-alice-summer-sale, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
        assertEquals(ALICE, after.userId)
        assertEquals(SUMMER, after.channelId)
    }

    @Test
    @Order(38)
    fun updateMembershipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        // The patch must change something: put the old role back first.
        DataSyncKit.setMembership(server, SUMMER_MEMBERSHIP, "active", "viewer")
        val before = DataSyncKit.awaitRead("the membership shows the old role", { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync().data }) {
            assertEquals(mapOf("role" to "viewer"), it.payload)
        }
        val out = Stdout.captureForTest()

        // snippet.updateMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.updateMembership(
            membershipId = "membership-alice-summer-sale",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/role", value = "moderator")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated membership ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the membership shows the patched role", { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync().data }) {
            assertEquals(mapOf("role" to "moderator"), it.payload)
        }
        assertPrinted(out, "Updated membership membership-alice-summer-sale, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
    }

    /** Not a sample: the page says a membership that duplicates an existing user and channel pair is rejected with a 409. */
    @Test
    @Order(39)
    fun duplicateMembershipFailsAsDocumented(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val error = assertThrows<PubNubException> {
            server.dataSync.createMembership(channelId = SUMMER, userId = ALICE, classVersion = 1, membershipId = WINTER_MEMBERSHIP).sync()
        }
        assertEquals(409, error.statusCode, "the page says a duplicate pair fails with 409")
        assertEquals(PubNubError.DATASYNC_CONFLICT, error.pubnubError)
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Entities
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(40)
    fun createEntityBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val out = Stdout.captureForTest()

        // snippet.createEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.createEntity(
            entityId = "product-sneaker-42",
            className = "product",
            classVersion = 1,
            payload = mapOf("name" to "Retro Sneaker", "price" to 89.99, "stock" to 12)
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created entity ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val entity = DataSyncKit.awaitRead("the product can be read", { server.dataSync.getEntity(SNEAKER).sync().data }) {
            assertEquals("Retro Sneaker", it.payload?.get("name"))
        }
        assertPrinted(out, "Created entity product-sneaker-42 with eTag ${entity.eTag}")
        assertNoFailure(out)
        assertEquals("product", entity.className)
        assertEquals("SubKey", entity.classLevel)
        assertEquals(1, entity.classVersion)
        assertEquals(89.99, (entity.payload?.get("price") as Number).toDouble())
        assertEquals(12, (entity.payload?.get("stock") as Number).toInt())
    }

    @Test
    @Order(41)
    fun getEntityBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the product can be read", { server.dataSync.getEntity(SNEAKER).sync().data })
        val out = Stdout.captureForTest()

        // snippet.getEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getEntity("product-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched entity ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Fetched entity product-sneaker-42 with payload {name=Retro Sneaker, price=89.99, stock=12")
        assertNoFailure(out)
    }

    @Test
    @Order(42)
    fun getEntitiesBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of products shows both products") {
            assertTrue(DataSyncKit.productIds(server, limit = 10).containsAll(listOf(SNEAKER, BOOT)))
        }
        val out = Stdout.captureForTest()

        // snippet.getEntitiesBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getEntities(
            className = "product",
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrintedLine(out, SNEAKER)
        assertPrintedLine(out, BOOT)
        assertPrintedLine(out, "More pages available: false")
        assertNoFailure(out)
    }

    @Test
    @Order(43)
    fun getEntitiesFilterFast(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        // The sort is served from the same index as `filter`, so wait until the sorted list shows the sneaker before the sample runs.
        DataSyncKit.settled("the sorted fast filter lists the sneaker and not the boot") {
            assertEquals(listOf(SNEAKER), DataSyncKit.productIds(server, filterFast = "price < 100 && stock > 0", sortBy = "price"))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getEntitiesFilterFast
        // `price` and `stock` must be declared filterable (`simple` or `full`) on the `product` class
        pubnub.dataSync.getEntities(
            className = "product",
            filterFast = "price < 100 && stock > 0",
            sort = listOf(PNDataSyncSortField(property = "price")),
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getEntities") as PNDataSyncGetEntitiesResult
        assertEquals(listOf(SNEAKER), result.data.map { it.id }, "only the cheap product in stock matches")
        assertPrintedLine(out, SNEAKER)
        assertNotPrintedLine(out, BOOT)
        assertNoFailure(out)
    }

    @Test
    @Order(44)
    fun getEntitiesFilter(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the filter index lists the sneaker and not the boot") {
            assertEquals(listOf(SNEAKER), DataSyncKit.productIds(server, filter = "name LIKE \"*Sneaker*\""))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getEntitiesFilter
        // `filter` only accepts properties declared with `full` filtering on the `product` class
        pubnub.dataSync.getEntities(
            className = "product",
            filter = "name LIKE \"*Sneaker*\"",
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getEntities") as PNDataSyncGetEntitiesResult
        assertEquals(listOf(SNEAKER), result.data.map { it.id })
        assertPrintedLine(out, SNEAKER)
        assertNotPrintedLine(out, BOOT)
        assertNoFailure(out)
    }

    @Test
    @Order(45)
    fun getEntitiesPagination(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of products shows both products") {
            assertTrue(DataSyncKit.productIds(server, limit = 20).containsAll(listOf(SNEAKER, BOOT)))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getEntitiesPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getEntities(
                className = "product",
                limit = 20,
                cursor = cursor
            ).async { result ->
                result.onFailure { exception ->
                    println("Failed: ${exception.message}")
                }.onSuccess { value ->
                    value.data.forEach { println(it.id) }
                    if (value.next.hasNext) {
                        fetchPage(value.next.cursor)
                    }
                }
            }
        }

        fetchPage(cursor = null)
        // snippet.end

        recorder.awaitOk("getEntities")
        assertPrintedLine(out, SNEAKER)
        assertPrintedLine(out, BOOT)
        assertNoFailure(out)
    }

    @Test
    @Order(46)
    fun setEntityBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val before = DataSyncKit.awaitRead("the product can be read", { server.dataSync.getEntity(SNEAKER).sync().data })
        val out = Stdout.captureForTest()

        // snippet.setEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.setEntity(
            entityId = "product-sneaker-42",
            classVersion = 1,
            payload = mapOf("name" to "Retro Sneaker", "price" to 79.99, "stock" to 8)
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced entity ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the product shows the new price", { server.dataSync.getEntity(SNEAKER).sync().data }) {
            assertEquals(79.99, (it.payload?.get("price") as Number).toDouble())
            assertEquals(8, (it.payload?.get("stock") as Number).toInt())
        }
        assertPrinted(out, "Replaced entity product-sneaker-42, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
    }

    @Test
    @Order(47)
    fun updateEntityBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        // The patch must change something: put the old price and stock back first.
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 89.99, 12)
        val before = DataSyncKit.awaitRead("the product shows the old price", { server.dataSync.getEntity(SNEAKER).sync().data }) {
            assertEquals(89.99, (it.payload?.get("price") as Number).toDouble())
        }
        val out = Stdout.captureForTest()

        // snippet.updateEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.updateEntity(
            entityId = "product-sneaker-42",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/price", value = 79.99),
                PNJsonPatchOperation(op = "replace", path = "/payload/stock", value = 8)
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated entity ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the product shows the patched price", { server.dataSync.getEntity(SNEAKER).sync().data }) {
            assertEquals(79.99, (it.payload?.get("price") as Number).toDouble())
            assertEquals(8, (it.payload?.get("stock") as Number).toInt())
            assertEquals("Retro Sneaker", it.payload?.get("name"))
        }
        assertPrinted(out, "Updated entity product-sneaker-42, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
    }

    @Test
    @Order(48)
    fun updateEntityMultipleOperations(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        // The sample needs a product with a name to test, a price to copy and replace, and the `legacySku` field to remove.
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 89.99, 12, mapOf("legacySku" to "SKU-1"))
        val before = DataSyncKit.awaitRead("the product shows the seeded fields", { server.dataSync.getEntity(SNEAKER).sync().data }) {
            assertEquals("SKU-1", it.payload?.get("legacySku"))
        }
        // The sample says to use the eTag of an earlier read, not a literal value. The literal of the text is swapped for the real eTag.
        recorder.placeholder("a1b2c3d4e5f6", before.eTag)
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.updateEntityMultipleOperations
        pubnub.dataSync.updateEntity(
            entityId = "product-sneaker-42",
            operations = listOf(
                // Fail the whole patch unless the name is still "Retro Sneaker"
                PNJsonPatchOperation(op = "test", path = "/payload/name", value = "Retro Sneaker"),
                // Keep the old price before changing it
                PNJsonPatchOperation(op = "copy", from = "/payload/price", path = "/payload/previousPrice"),
                PNJsonPatchOperation(op = "replace", path = "/payload/price", value = 79.99),
                PNJsonPatchOperation(op = "replace", path = "/payload/stock", value = 8),
                PNJsonPatchOperation(op = "add", path = "/payload/sale", value = true),
                // Rename a field
                PNJsonPatchOperation(op = "move", from = "/payload/sale", path = "/payload/onSale"),
                // The removed field must exist on the entity, otherwise the patch is rejected
                PNJsonPatchOperation(op = "remove", path = "/payload/legacySku")
            ),
            // Use the eTag from an earlier read of the entity, not a literal value
            ifMatch = "a1b2c3d4e5f6"
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Patched ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end

        recorder.awaitOk("updateEntity")
        val after = DataSyncKit.awaitRead("the product shows the patched fields", { server.dataSync.getEntity(SNEAKER).sync().data }) {
            assertEquals(79.99, (it.payload?.get("price") as Number).toDouble())
        }
        assertPrinted(out, "Patched product-sneaker-42, new eTag ${after.eTag}")
        assertNoFailure(out)
        val payload = after.payload!!
        assertEquals(89.99, (payload["previousPrice"] as Number).toDouble(), "copy kept the old price")
        assertEquals(8, (payload["stock"] as Number).toInt())
        assertEquals(true, payload["onSale"], "add then move left the field under its new name")
        assertTrue("sale" !in payload, "move removed the source")
        assertTrue("legacySku" !in payload, "remove deleted the field")
        assertNotEquals(before.eTag, after.eTag)
        // The page says that the guard rejects the patch with a 412 once the entity changed since the read.
        val error = assertThrows<PubNubException> {
            server.dataSync.updateEntity(
                SNEAKER,
                listOf(PNJsonPatchOperation(op = "replace", path = "/payload/price", value = 70.0)),
                ifMatch = before.eTag,
            ).sync()
        }
        assertEquals(412, error.statusCode, "the page says a stale eTag fails with 412")
    }

    /** Not a sample: the page says `expiresAt` is the creation time plus the class TTL (31 days for the product class) rounded up to a day. */
    @Test
    @Order(49)
    fun expiresAtOfAProductFollowsTheClassTtl(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val product = server.dataSync.getEntity(SNEAKER).sync().data
        DataSyncKit.assertExpiry(product.createdAt, product.expiresAt, 31)
    }

    /** Not a sample: the page says the built in User class keeps an object for 30 days, with the same rounding. */
    @Test
    @Order(59)
    fun expiresAtOfAUserFollowsTheBuiltInTtl(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val user = server.dataSync.getUser(ALICE).sync().data
        DataSyncKit.assertExpiry(user.createdAt, user.expiresAt, 30)
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Relationships
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(50)
    fun createRelationshipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val out = Stdout.captureForTest()

        // snippet.createRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.createRelationship(
            relationshipId = "rel-bob-owns-sneaker-42",
            entityAId = "seller-bob",
            entityBId = "product-sneaker-42",
            className = "ProductOwner",
            classVersion = 1,
            payload = mapOf("since" to "2026-07-13")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created relationship ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val link = DataSyncKit.awaitRead("the link can be read", { server.dataSync.getRelationship(LINK).sync().data }) {
            assertEquals(mapOf("since" to "2026-07-13"), it.payload)
        }
        assertPrinted(out, "Created relationship rel-bob-owns-sneaker-42 with eTag ${link.eTag}")
        assertNoFailure(out)
        assertEquals(BOB, link.entityAId)
        assertEquals(SNEAKER, link.entityBId)
        assertEquals("ProductOwner", link.className)
        assertEquals(1, link.classVersion)
    }

    @Test
    @Order(51)
    fun getRelationshipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the link can be read", { server.dataSync.getRelationship(LINK).sync().data })
        val out = Stdout.captureForTest()

        // snippet.getRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getRelationship("rel-bob-owns-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched relationship ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Fetched relationship rel-bob-owns-sneaker-42 with payload {since=2026-07-13}")
        assertNoFailure(out)
    }

    @Test
    @Order(52)
    fun getRelationshipsBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of links shows the link") {
            assertEquals(listOf(LINK), DataSyncKit.linkIds(server))
        }
        val out = Stdout.captureForTest()

        // snippet.getRelationshipsBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrintedLine(out, LINK)
        assertPrintedLine(out, "More pages available: false")
        assertNoFailure(out)
    }

    @Test
    @Order(53)
    fun getRelationshipsByEntityBId(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the sneaker lists exactly the link") {
            assertEquals(listOf(LINK), DataSyncKit.linkIds(server, entityBId = SNEAKER))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getRelationshipsByEntityBId
        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
            entityBId = "product-sneaker-42",
            limit = 50
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println("${it.entityAId} owns ${it.entityBId}") }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getRelationships") as PNDataSyncGetRelationshipsResult
        assertEquals(listOf(LINK), result.data.map { it.id })
        assertPrintedLine(out, "seller-bob owns product-sneaker-42")
        assertPrintedLine(out, "More pages available: false")
        assertNoFailure(out)
    }

    @Test
    @Order(54)
    fun setRelationshipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val before = DataSyncKit.awaitRead("the link can be read", { server.dataSync.getRelationship(LINK).sync().data })
        val out = Stdout.captureForTest()

        // snippet.setRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.setRelationship(
            relationshipId = "rel-bob-owns-sneaker-42",
            classVersion = 1,
            payload = mapOf("since" to "2026-07-13", "tier" to "gold")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced relationship ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the link shows the tier", { server.dataSync.getRelationship(LINK).sync().data }) {
            assertEquals(mapOf("since" to "2026-07-13", "tier" to "gold"), it.payload)
        }
        assertPrinted(out, "Replaced relationship rel-bob-owns-sneaker-42, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
        assertEquals(BOB, after.entityAId)
        assertEquals(SNEAKER, after.entityBId)
    }

    @Test
    @Order(55)
    fun getRelationshipsFilterFast(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the sorted fast filter lists the link") {
            assertEquals(listOf(LINK), DataSyncKit.linkIds(server, filterFast = "tier == \"gold\""))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getRelationshipsFilterFast
        // `tier` and `since` must be declared filterable (`simple` or `full`) on the `ProductOwner` class
        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
            filterFast = "tier == \"gold\"",
            sort = listOf(PNDataSyncSortField(property = "since", ascending = false)),
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getRelationships") as PNDataSyncGetRelationshipsResult
        assertEquals(listOf(LINK), result.data.map { it.id })
        assertPrintedLine(out, LINK)
        assertNoFailure(out)
    }

    @Test
    @Order(56)
    fun getRelationshipsFilter(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the filter index lists the link") {
            assertEquals(listOf(LINK), DataSyncKit.linkIds(server, filter = "tier LIKE \"gold*\""))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getRelationshipsFilter
        // `filter` only accepts properties declared with `full` filtering on the `ProductOwner` class
        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
            filter = "tier LIKE \"gold*\"",
            limit = 20
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getRelationships") as PNDataSyncGetRelationshipsResult
        assertEquals(listOf(LINK), result.data.map { it.id })
        assertPrintedLine(out, LINK)
        assertNoFailure(out)
    }

    @Test
    @Order(57)
    fun getRelationshipsPagination(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.settled("the list of links shows the link") {
            assertEquals(listOf(LINK), DataSyncKit.linkIds(server))
        }
        val pubnub = recorder.wrap(clients.server(ROLE, "docs-sample"))
        val out = Stdout.captureForTest()

        // snippet.getRelationshipsPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getRelationships(
                className = "ProductOwner",
                limit = 20,
                cursor = cursor
            ).async { result ->
                result.onFailure { exception ->
                    println("Failed: ${exception.message}")
                }.onSuccess { value ->
                    value.data.forEach { println(it.id) }
                    if (value.next.hasNext) {
                        fetchPage(value.next.cursor)
                    }
                }
            }
        }

        fetchPage(cursor = null)
        // snippet.end

        recorder.awaitOk("getRelationships")
        assertPrintedLine(out, LINK)
        assertNoFailure(out)
    }

    @Test
    @Order(58)
    fun updateRelationshipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        val before = DataSyncKit.awaitRead("the link shows the gold tier", { server.dataSync.getRelationship(LINK).sync().data }) {
            assertEquals("gold", it.payload?.get("tier"))
        }
        val out = Stdout.captureForTest()

        // snippet.updateRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.updateRelationship(
            relationshipId = "rel-bob-owns-sneaker-42",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/tier", value = "platinum")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated relationship ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val after = DataSyncKit.awaitRead("the link shows the patched tier", { server.dataSync.getRelationship(LINK).sync().data }) {
            assertEquals("platinum", it.payload?.get("tier"))
            assertEquals("2026-07-13", it.payload?.get("since"))
        }
        assertPrinted(out, "Updated relationship rel-bob-owns-sneaker-42, new eTag ${after.eTag}")
        assertNoFailure(out)
        assertNotEquals(before.eTag, after.eTag)
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Removal of a link and a membership, then the events of the product, then the removal of the rest
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    @Order(80)
    fun removeRelationshipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the link can be read", { server.dataSync.getRelationship(LINK).sync().data })
        val out = Stdout.captureForTest()

        // snippet.removeRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.removeRelationship("rel-bob-owns-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed relationship, HTTP status ${value.status}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Removed relationship, HTTP status 200")
        assertNoFailure(out)
        DataSyncKit.awaitMissing("the link") { server.dataSync.getRelationship(LINK).sync() }
    }

    @Test
    @Order(81)
    fun removeMembershipBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the membership can be read", { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync().data })
        val out = Stdout.captureForTest()

        // snippet.removeMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.removeMembership("membership-alice-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed membership, HTTP status ${value.status}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Removed membership, HTTP status 200")
        assertNoFailure(out)
        DataSyncKit.awaitMissing("the membership") { server.dataSync.getMembership(SUMMER_MEMBERSHIP).sync() }
    }

    @Test
    @Order(82)
    fun dataSyncEventListener(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val out = Stdout.captureForTest()
        val ready = Wait.connected(client, listOf(SNEAKER))

        // snippet.dataSyncEventListener
        val subscription = pubnub.channel("product-sneaker-42").subscription()

        subscription.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                when (val message = result.extractedMessage) {
                    is PNSetDataSyncUserEventMessage ->
                        println("User ${message.data.id} ${message.event}")
                    is PNSetDataSyncChannelEventMessage ->
                        println("Channel ${message.data.id} ${message.event}")
                    is PNSetDataSyncMembershipEventMessage ->
                        println("Membership ${message.data.id} ${message.event}")
                    is PNSetDataSyncEntityEventMessage ->
                        println("Entity ${message.data.id} ${message.event}")
                    is PNSetDataSyncRelationshipEventMessage ->
                        println("Relationship ${message.data.id} ${message.event}")
                    is PNDeleteDataSyncUserEventMessage ->
                        println("User ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncChannelEventMessage ->
                        println("Channel ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncMembershipEventMessage ->
                        println("Membership ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncEntityEventMessage ->
                        println("Entity ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncRelationshipEventMessage ->
                        println("Relationship ${message.id} deleted at ${message.deletedAt}")
                    else ->
                        println("Unknown DataSync event of type ${message.type}")
                }
            }
        })

        subscription.subscribe()
        // snippet.end

        ready.await()
        // update, delete and create of the product, each one reaches the listener of the sample
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 74.99, 7)
        assertPrinted(out, "Entity product-sneaker-42 ${PNDataSyncSetEventType.UPDATE}")
        server.dataSync.removeEntity(SNEAKER).sync()
        assertPrinted(out, "Entity product-sneaker-42 deleted at ")
        DataSyncKit.createProduct(server, SNEAKER, "Retro Sneaker", 74.99, 7)
        assertPrinted(out, "Entity product-sneaker-42 ${PNDataSyncSetEventType.CREATE}")
        assertTrue(out.lines("Unknown DataSync event").isEmpty(), "no event fell into the else branch")
    }

    @Test
    @Order(83)
    fun subscribeToProjectionChannels(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val projection = "__admin__$SNEAKER"
        val events = DataSyncKit.Events(client)
        val ready = Wait.connected(client, listOf(SNEAKER, projection))

        // snippet.subscribeToProjectionChannels
        val entity = pubnub.dataSyncEntity("product-sneaker-42")

        val defaultSubscription = entity.subscription()
        val adminSubscription = entity.subscription("admin")

        defaultSubscription.subscribe()
        adminSubscription.subscribe()
        // snippet.end

        ready.await()
        assertEquals(setOf(SNEAKER, projection), client.getSubscribedChannels().filterNot { it.endsWith("-pnpres") }.toSet())
        // One change of the product publishes two events, one on the channel of the object and one on the channel of the projection.
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 69.5, 6)
        val onDefault = events.await("the update on the default projection channel") { it.channel == SNEAKER && DataSyncKit.isSet(it, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE) }
        val onAdmin = events.await("the update on the admin projection channel") { it.channel == projection && DataSyncKit.isSet(it, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE) }
        assertEquals("entity", onDefault.extractedMessage.type)
        assertEquals("product", onAdmin.extractedMessage.className)
        events.close()
    }

    @Test
    @Order(84)
    fun removeEntityBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the product can be read", { server.dataSync.getEntity(SNEAKER).sync().data })
        val out = Stdout.captureForTest()

        // snippet.removeEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.removeEntity("product-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed entity, HTTP status ${value.status}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Removed entity, HTTP status 200")
        assertNoFailure(out)
        DataSyncKit.awaitMissing("the product") { server.dataSync.getEntity(SNEAKER).sync() }
    }

    @Test
    @Order(85)
    fun removeChannelBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("the channel can be read", { server.dataSync.getChannel(SUMMER).sync().data })
        val out = Stdout.captureForTest()

        // snippet.removeChannelBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.removeChannel("channel-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed channel, HTTP status ${value.status}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Removed channel, HTTP status 200")
        assertNoFailure(out)
        DataSyncKit.awaitMissing("the channel") { server.dataSync.getChannel(SUMMER).sync() }
    }

    @Test
    @Order(86)
    fun removeUserBasicUsage(clients: LiveClients) {
        val server = clients.server(ROLE, "docs-server")
        DataSyncKit.awaitRead("Alice can be read", { server.dataSync.getUser(ALICE).sync().data })
        val out = Stdout.captureForTest()

        // snippet.removeUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.dataSync.removeUser("user-alice").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed user, HTTP status ${value.status}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        assertPrinted(out, "Removed user, HTTP status 200")
        assertNoFailure(out)
        DataSyncKit.awaitMissing("Alice") { server.dataSync.getUser(ALICE).sync() }
    }
}
