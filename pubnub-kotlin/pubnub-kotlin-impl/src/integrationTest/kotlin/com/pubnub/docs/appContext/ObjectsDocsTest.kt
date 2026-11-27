@file:Suppress("ktlint")

package com.pubnub.docs.appContext

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.docs.support.AppContextKit
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Redact
import com.pubnub.docs.support.Stdout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page objects (docs/sdks/kotlin/api-reference/objects.md): App Context user metadata, channel
 * metadata, memberships and members. One test per snippet. The text between a start marker and `snippet.end` is the published
 * sample, the arrangement is above it and the assertions below it. Every object is reset to a known state before the sample
 * writes it, reads that must be exact wait for the service to settle (App Context reads lag behind writes), and `custom`
 * appears in a result only when the sample asks for it.
 */
@ExtendWith(LiveExtension::class)
class ObjectsDocsTest {
    // The user id of the sample's own client: the samples without a user id act on it.
    private val me = "docs-appctx-kotlin"

    private fun customOf(value: Map<String, Any?>?, key: String): Any? = value?.get(key)

    // ---------------------------------------------------------------------------------------------------------------
    // User metadata
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun getAllUUIDMetadataMain(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        val users = listOf("user-1", "user-2", "user-3")
        AppContextKit.cleanupAfterTest(reader, users, emptyList())
        users.forEach { AppContextKit.clearUser(reader, it) }
        val out = Stdout.captureForTest()

        com.pubnub.docs.appContext.samples.getAllUUIDMetadataMain.main()

        assertTrue(out.lines("ERROR:").isEmpty(), "the sample printed an error: ${out.lines("ERROR:")}")
        out.assertPrinted("SUCCESS: Created/updated user metadata for user-1")
        out.assertPrinted("SUCCESS: Created/updated user metadata for user-2")
        out.assertPrinted("SUCCESS: Created/updated user metadata for user-3")
        out.assertPrinted("SUCCESS: Retrieved UUID metadata objects")
        out.assertPrinted("SUCCESS: Retrieved filtered UUID metadata objects")
        out.assertPrinted("""Filter: "custom.role == 'Developer'"""")
        out.assertPrinted("SUCCESS: Retrieved metadata for UUID: user-2")
        users.forEach { out.assertPrinted("SUCCESS: Removed user metadata for $it") }
        val text = out.text()
        val all = text.substringAfter("# Getting All UUID Metadata (Basic)").substringBefore("# Getting All UUID Metadata (with Filter)")
        users.forEach { assertTrue(all.contains("UUID: $it"), "the list of all users does not show $it") }
        val filtered = text.substringAfter("# Getting All UUID Metadata (with Filter)").substringBefore("# Getting Metadata for a Specific UUID")
        assertTrue(filtered.contains("UUID: user-1"), "the filtered list does not show the developer user-1")
        assertTrue(!filtered.contains("UUID: user-2") && !filtered.contains("UUID: user-3"), "the filtered list shows a user that is not a developer")
        val specific = text.substringAfter("# Getting Metadata for a Specific UUID").substringBefore("# Cleaning")
        assertTrue(specific.contains("UUID: user-2") && specific.contains("Bob"), "the specific read does not show Bob")
        users.forEach { AppContextKit.awaitUserGone(reader, it) }
    }

    @Test
    fun getUUIDMetadataBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf(me), emptyList())
        AppContextKit.clearUser(reader, me)
        AppContextKit.seedUser(reader, me, "Foo", mapOf("role" to "tester"))

        // snippet.getUUIDMetadataBasic
        pubnub.getUUIDMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("getUUIDMetadata") as PNUUIDMetadataResult
        assertEquals(200, result.status)
        assertEquals(me, result.data.id)
        assertEquals("Foo", result.data.name?.value)
        assertEquals("foo@example.com", result.data.email?.value)
        assertNull(result.data.custom, "custom is returned although the sample did not ask for it")
    }

    @Test
    fun setUUIDMetadataBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf(me), emptyList())
        AppContextKit.clearUser(reader, me)
        AppContextKit.seedUser(reader, me, "Foo", mapOf("role" to "tester"))

        // snippet.setUUIDMetadataBasic
        pubnub.setUUIDMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("setUUIDMetadata") as PNUUIDMetadataResult
        assertEquals(200, result.status)
        assertEquals(me, result.data.id)
        // The call sets no field, so the stored fields stay as they were.
        assertEquals("Foo", result.data.name?.value)
        val stored = AppContextKit.awaitUser(reader, me, "Foo")
        assertEquals("tester", customOf(stored.custom?.value, "role"))
    }

    @Test
    fun removeUUIDMetadataBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf(me), emptyList())
        AppContextKit.clearUser(reader, me)
        AppContextKit.seedUser(reader, me, "Foo")

        // snippet.removeUUIDMetadataBasic
        pubnub.removeUUIDMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("removeUUIDMetadata") as PNRemoveMetadataResult
        assertEquals(200, result.status)
        AppContextKit.awaitUserGone(reader, me)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Channel metadata
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun getAllChannelMetadataBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, emptyList(), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.seedChannel(reader, "myChannel", "Some Name")
        AppContextKit.awaitChannelsListed(reader, listOf("myChannel"))

        // snippet.getAllChannelMetadataBasic
        pubnub.getAllChannelMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("getAllChannelMetadata") as PNChannelMetadataArrayResult
        assertEquals(200, result.status)
        val mine = result.data.firstOrNull { it.id == "myChannel" }
        assertNotNull(mine, "the list of all channel metadata does not show myChannel")
        assertEquals("Some Name", mine!!.name?.value)
        assertNull(mine.custom, "custom is returned although the sample did not ask for it")
        assertNull(result.totalCount, "a total count is returned although the sample did not ask for it")
    }

    @Test
    fun getChannelMetadataBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, emptyList(), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.seedChannel(reader, "myChannel", "Some Name", mapOf("topic" to "docs"))

        // snippet.getChannelMetadataBasic
        pubnub.getChannelMetadata(channel = "myChannel")
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("getChannelMetadata") as PNChannelMetadataResult
        assertEquals(200, result.status)
        assertEquals("myChannel", result.data.id)
        assertEquals("Some Name", result.data.name?.value)
        assertEquals("Channel of the docs tests", result.data.description?.value)
        assertNull(result.data.custom, "custom is returned although the sample did not ask for it")
    }

    @Test
    fun setChannelMetadataBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, emptyList(), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.seedChannel(reader, "myChannel", "Some Name", mapOf("topic" to "docs"))

        // snippet.setChannelMetadataBasic
        pubnub.setChannelMetadata(channel = "myChannel")
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("setChannelMetadata") as PNChannelMetadataResult
        assertEquals(200, result.status)
        assertEquals("myChannel", result.data.id)
        // The call sets no field, so the stored fields stay as they were.
        assertEquals("Some Name", result.data.name?.value)
        val stored = AppContextKit.awaitChannel(reader, "myChannel", "Some Name")
        assertEquals("docs", customOf(stored.custom?.value, "topic"))
    }

    // The Main sample iterativelyUpdateExistingMetadataMain does not compile here (it imports kotlin.test), see
    // snippet-exceptions.json. samples/GetChannelMetadataMain.kt.pending holds the ready
    // sample and test.

    @Test
    fun removeChannelMetadata(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, emptyList(), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.seedChannel(reader, "myChannel", "Some Name")

        // snippet.removeChannelMetadata
        pubnub.removeChannelMetadata(channel = "myChannel")
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("removeChannelMetadata") as PNRemoveMetadataResult
        assertEquals(200, result.status)
        AppContextKit.awaitChannelGone(reader, "myChannel")
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Memberships
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun getMembershipsBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf(me), listOf("myChannel", "myChannelToRemove"))
        AppContextKit.clearUser(reader, me)
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.clearChannel(reader, "myChannelToRemove")
        AppContextKit.seedChannel(reader, "myChannel", "Some Name", mapOf("topic" to "docs"))
        AppContextKit.seedChannel(reader, "myChannelToRemove", "Other Name")
        AppContextKit.seedMemberships(reader, me, listOf("myChannel", "myChannelToRemove"))

        // snippet.getMembershipsBasic
        pubnub.getMemberships(
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true,
                includeTotalCount = true,
                includeChannel = true,
                includeChannelCustom = true,
                includeChannelType = true,
                includeChannelStatus = true
            ),
            sort = listOf(PNSortKey.PNAsc(PNMembershipKey.CHANNEL_ID))
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val result = recorder.awaitOk("getMemberships") as PNChannelMembershipArrayResult
        assertEquals(200, result.status)
        assertEquals(2, result.totalCount)
        // sorted by channel id, ascending
        assertEquals(listOf("myChannel", "myChannelToRemove"), result.data.map { it.channel.id })
        val first = result.data.first()
        assertEquals("active", first.status?.value)
        assertEquals("docs", first.type?.value)
        assertEquals("Some Name", first.channel.name?.value)
        assertEquals("docs", first.channel.type?.value)
        assertEquals("active", first.channel.status?.value)
        assertEquals("docs", customOf(first.channel.custom?.value, "topic"))
    }

    @Test
    fun setMembershipsBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf(me), listOf("myChannel"))
        AppContextKit.clearUser(reader, me)
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.seedChannel(reader, "myChannel", "Some Name")

        // snippet.setMembershipsBasic
        pubnub.setMemberships(
            channels = listOf(
                PNChannelMembership.Partial(
                    channelId = "myChannel",
                    custom = mapOf("owner" to "PubNub"),
                    status = "active",
                    type = "regular_membership"
                )
            ),
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true,
                includeChannel = true,
                includeChannelCustom = true,
                includeChannelType = true,
                includeChannelStatus = true,
                includeTotalCount = true
            ),
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        // Docs finding: the service refuses the type "regular_membership" of this sample ("Property 'type' cannot contain
        // special characters", the underscore), so the sample as published fails with a 400. The test pins that answer, and
        // proves that nothing was stored. When the docs change the type, this test must assert the stored membership instead.
        val failure = recorder.awaitFailure("setMemberships") as PubNubException
        assertEquals(400, failure.statusCode)
        assertTrue(
            failure.errorMessage.orEmpty().contains("Property 'type' cannot contain special characters"),
            "the service answered with another message than the type refusal",
        )
        AppContextKit.awaitMemberships(reader, me, emptyList())
    }

    @Test
    fun removeMembershipsBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf(me), listOf("myChannel", "myChannelToRemove"))
        AppContextKit.clearUser(reader, me)
        AppContextKit.seedMemberships(reader, me, listOf("myChannel", "myChannelToRemove"))

        // snippet.removeMembershipsBasic
        pubnub.removeMemberships(listOf("myChannel"))
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("removeMemberships") as PNChannelMembershipArrayResult
        assertEquals(200, result.status)
        // only the named membership is removed
        AppContextKit.awaitMemberships(reader, me, listOf("myChannelToRemove"))
    }

    @Test
    fun manageMembershipsBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf(me), listOf("myChannelToSet", "myChannelToRemove"))
        AppContextKit.clearUser(reader, me)
        AppContextKit.clearChannel(reader, "myChannelToSet")
        AppContextKit.clearChannel(reader, "myChannelToRemove")
        AppContextKit.seedMemberships(reader, me, listOf("myChannelToRemove"))

        // snippet.manageMembershipsBasic
        pubnub.manageMemberships(
            channelsToSet = listOf(PNChannelMembership.Partial(channelId = "myChannelToSet")),
            channelsToRemove = listOf("myChannelToRemove"),
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true,
                includeChannel = true,
                includeChannelCustom = true,
                includeChannelType = true,
                includeChannelStatus = true,
                includeTotalCount = true
            )
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val result = recorder.awaitOk("manageMemberships") as PNChannelMembershipArrayResult
        assertEquals(200, result.status)
        assertEquals(listOf("myChannelToSet"), result.data.map { it.channel.id })
        assertEquals(1, result.totalCount)
        AppContextKit.awaitMemberships(reader, me, listOf("myChannelToSet"))
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Members
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun getChannelMembersBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf("myUserId"), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.clearUser(reader, "myUserId")
        AppContextKit.seedMembers(reader, "myChannel", listOf("myUserId"))

        // snippet.getChannelMembersBasic
        pubnub.getChannelMembers("myChannel")
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val result = recorder.awaitOk("getChannelMembers") as PNMemberArrayResult
        assertEquals(200, result.status)
        assertEquals(listOf("myUserId"), result.data.map { it.uuid.id })
        assertNull(result.totalCount, "a total count is returned although the sample did not ask for it")
        // without MemberInclude the status and the type of the membership are not returned
        assertNull(result.data.single().status)
        assertNull(result.data.single().type)
    }

    @Test
    fun setChannelMembersBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf("myUserId"), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.clearUser(reader, "myUserId")
        AppContextKit.seedUser(reader, "myUserId", "Member One")

        // snippet.setChannelMembersBasic
        pubnub.setChannelMembers(
            channel = "myChannel",
            users = listOf(
                PNMember.Partial(
                    uuidId = "myUserId",
                    custom = mapOf("custom" to "customValue"),
                    status = "status",
                    type = "typeABC"
                )
            ),
            include = MemberInclude(includeUser = true, includeStatus = true, includeType = true),
            sort = listOf(PNSortKey.PNAsc(PNMemberKey.STATUS)),
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val result = recorder.awaitOk("setChannelMembers") as PNMemberArrayResult
        assertEquals(200, result.status)
        val member = result.data.single()
        assertEquals("myUserId", member.uuid.id)
        assertEquals("Member One", member.uuid.name?.value)
        assertEquals("status", member.status?.value)
        assertEquals("typeABC", member.type?.value)
        assertNull(member.custom, "custom is returned although the sample did not ask for it")
        val stored = AppContextKit.awaitMembers(reader, "myChannel", listOf("myUserId")).single()
        assertEquals("customValue", customOf(stored.custom?.value, "custom"))
        assertEquals("typeABC", stored.type?.value)
    }

    @Test
    fun removeChannelMembersBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf("myUserId"), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.clearUser(reader, "myUserId")
        AppContextKit.seedMembers(reader, "myChannel", listOf("myUserId", "docs-receiver"))

        // snippet.removeChannelMembersBasic
        pubnub.removeChannelMembers(
            channel = "myChannel",
            userIds = listOf("myUserId"),
            include = MemberInclude(includeUser = true, includeStatus = true, includeType = true),
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val result = recorder.awaitOk("removeChannelMembers") as PNMemberArrayResult
        assertEquals(200, result.status)
        // only the named member is removed, the other stays
        assertEquals(listOf("docs-receiver"), result.data.map { it.uuid.id })
        AppContextKit.awaitMembers(reader, "myChannel", listOf("docs-receiver"))
    }

    @Test
    fun manageChannelMembersBasic(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, me)
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf("myUserId"), listOf("myChannel"))
        AppContextKit.clearChannel(reader, "myChannel")
        AppContextKit.clearUser(reader, "myUserId")
        AppContextKit.seedMembers(reader, "myChannel", listOf("docs-receiver"))

        // snippet.manageChannelMembersBasic
        val pnMemberArrayResult: PNMemberArrayResult = pubnub.manageChannelMembers(
            channel = "myChannel",
            usersToSet = listOf(PNMember.Partial(uuidId = "myUserId", status = "myStatus", type = "distinctType")),
            userIdsToRemove = listOf(),
            include = MemberInclude(includeStatus = true, includeType = true)
        ).sync()
        // snippet.end

        assertEquals(200, pnMemberArrayResult.status)
        val member = pnMemberArrayResult.data.single { it.uuid.id == "myUserId" }
        assertEquals("myStatus", member.status?.value)
        assertEquals("distinctType", member.type?.value)
        // the member that was there before stays, the sample removes nobody
        assertEquals(listOf("docs-receiver", "myUserId"), pnMemberArrayResult.data.map { it.uuid.id }.sorted())
        AppContextKit.awaitMembers(reader, "myChannel", listOf("docs-receiver", "myUserId"))
        val record = recorder.awaitOk("manageChannelMembers")
        assertTrue(record === pnMemberArrayResult || record == pnMemberArrayResult, "the recorded result differs from the sample's result")
    }

    @Test
    fun iterativelyUpdateExistingMetadataMain(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-reader")
        AppContextKit.cleanupAfterTest(reader, emptyList(), listOf("channel01"))
        AppContextKit.clearChannel(reader, "channel01")

        // The sample ends with exitProcess(0), so it runs in a JVM of its own. Its own assertions end it with a failure.
        val result =
            AppContextKit.runInChildJvm("com.pubnub.docs.appContext.samples.iterativelyUpdateExistingMetadataMain.GetChannelMetadataMainKt")

        assertEquals(0, result.exitCode, "the sample failed. Output: ${Redact.scrub(result.output.take(1500))}")
        // The sample removes the channel metadata at its end.
        AppContextKit.awaitChannelGone(reader, "channel01")
    }

    private companion object {
        const val OPEN = "open"
    }
}
