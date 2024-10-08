package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.utils.PatchValue
import com.pubnub.test.CommonUtils.randomValue
import com.pubnub.test.subscribeToBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ObjectsIntegrationTest : BaseIntegrationTest() {
    private val testUuid = "ThisIsMyId" + randomValue()
    private val otherTestUuid = "OtherTestId" + randomValue()
    val channel = "ThisIsMyChannel" + randomValue()
    val otherChannel = "OtherChannel" + randomValue()
    val status = "Status" + randomValue()
    val type = "Type" + randomValue()
    val noUpdated = ""
    val noEtag = ""

    @Test
    fun setGetAndRemoveChannelMetadata() {
        val setResult =
            pubnub.setChannelMetadata(
                channel = channel,
                name = randomValue(15),
                status = status,
                type = type,
            ).sync()

        val getAllResult = pubnub.getAllChannelMetadata(filter = "id == \"$channel\"").sync()
        val getSingleResult = pubnub.getChannelMetadata(channel = channel).sync()
        pubnub.removeChannelMetadata(channel = channel).sync()

        assertTrue(getAllResult.data.any { it.id == channel })
        assertEquals(setResult, getSingleResult)

        val getAllAfterRemovalResult = pubnub.getAllChannelMetadata(filter = "id == \"$channel\"").sync()

        assertTrue(getAllAfterRemovalResult.data.none { it.id == channel })
    }

    @Test
    fun setGetAndRemoveUUIDMetadata() {
        // maintenance task: remove all UserMetadata
        // removeAllUserMetadataWithPaging() // this is not finished yet

        val setResult =
            pubnub.setUUIDMetadata(
                uuid = testUuid,
                name = randomValue(15),
                status = status,
                type = type,
            ).sync()

        assertEquals(status, setResult.data?.status?.value)
        assertEquals(type, setResult.data?.type?.value)

        val getAllResult = pubnub.getAllUUIDMetadata(filter = "id == \"$testUuid\"").sync()
        val getSingleResult = pubnub.getUUIDMetadata(uuid = testUuid).sync()
        pubnub.removeUUIDMetadata(uuid = testUuid).sync()

        assertTrue(getAllResult.data.any { it.id == testUuid })
        assertEquals(setResult, getSingleResult)

        val getAllAfterRemovalResult = pubnub.getAllUUIDMetadata(filter = "id == \"$testUuid\"").sync()

        assertTrue(getAllAfterRemovalResult.data.none { it.id == testUuid })
    }

    @Test
    fun addGetAndRemoveMembership() {
        val channels = listOf(PNChannelMembership.Partial(channelId = channel))
        val setResult =
            pubnub.setMemberships(
                channels = channels,
                uuid = testUuid,
                includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
            ).sync()

        val getAllResult =
            pubnub.getMemberships(
                uuid = testUuid,
                sort = listOf(PNSortKey.PNDesc(PNMembershipKey.CHANNEL_ID)),
                includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
            ).sync()

        assertEquals(setResult, getAllResult)

        pubnub.removeMemberships(
            channels = channels.map { it.channel },
            uuid = testUuid,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
        ).sync()

        val getAllAfterRemovalResult =
            pubnub.getMemberships(
                uuid = testUuid,
                includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
            ).sync()

        assertTrue(getAllAfterRemovalResult.data.filter { it.channel != null }.none { it.channel!!.id == channel })
    }

    @Test
    fun addGetAndRemoveMember() {
        val uuids = listOf(PNMember.Partial(uuidId = testUuid, custom = null, null))

        val setResult =
            pubnub.setChannelMembers(
                channel = channel,
                uuids = uuids,
                includeUUIDDetails = PNUUIDDetailsLevel.UUID,
            ).sync()

        val getAllResult =
            pubnub.getChannelMembers(
                channel = channel,
                includeUUIDDetails = PNUUIDDetailsLevel.UUID,
            ).sync()

        assertEquals(setResult, getAllResult)

        pubnub.removeChannelMembers(
            channel = channel,
            uuids = uuids.map { it.uuid },
            includeUUIDDetails = PNUUIDDetailsLevel.UUID,
        ).sync()

        val getAllAfterRemovalResult =
            pubnub.getChannelMembers(
                channel = channel,
                includeUUIDDetails = PNUUIDDetailsLevel.UUID,
            ).sync()

        assertTrue(getAllAfterRemovalResult.data.filter { it.uuid != null }.none { it.uuid!!.id == testUuid })
    }

    @Test
    fun testListeningToAllObjectsEvents() {
        val countDownLatch = CountDownLatch(8)
        val uuids = listOf(PNMember.Partial(uuidId = testUuid, custom = null, null))
        val channels =
            listOf(
                PNChannelMembership.Partial(channelId = channel),
            )

        pubnub.addListener(
            listener =
                object : SubscribeCallback() {
                    override fun status(
                        pubnub: PubNub,
                        pnStatus: PNStatus,
                    ) {}

                    override fun objects(
                        pubnub: PubNub,
                        event: PNObjectEventResult,
                    ) {
                        println(event)
                        countDownLatch.countDown()
                    }
                },
        )

        pubnub.subscribeToBlocking(channel)

        pubnub.setMemberships(
            channels = channels,
        ).sync()
        pubnub.setChannelMembers(channel = channel, uuids = uuids).sync()
        pubnub.setChannelMetadata(channel = channel, name = randomValue(15)).sync()
        pubnub.setUUIDMetadata(name = randomValue(15)).sync()

        pubnub.setChannelMetadata(channel = channel, description = "aaa").sync()
        pubnub.removeChannelMetadata(channel = channel).sync()
        pubnub.removeUUIDMetadata().sync()
        pubnub.removeChannelMembers(channel = channel, uuids = uuids.map { it.uuid }).sync()
        pubnub.removeMemberships(channels = channels.map { it.channel }).sync()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
    }

    @Test
    fun testListeningToAllObjectsEventsV2() {
        val countDownLatch = CountDownLatch(16)
        val uuids = listOf(PNMember.Partial(uuidId = testUuid, custom = null, null))
        val channels =
            listOf(
                PNChannelMembership.Partial(channelId = channel),
            )

        val metadataSub = pubnub.channelMetadata(channel).subscription()
        val userMetadataSub = pubnub.userMetadata(channel).subscription()

        metadataSub.addListener(
            listener =
                object : com.pubnub.api.v2.callbacks.EventListener {
                    override fun objects(
                        pubnub: PubNub,
                        result: PNObjectEventResult,
                    ) {
                        countDownLatch.countDown()
                    }
                },
        )
        userMetadataSub.addListener(
            listener =
                object : com.pubnub.api.v2.callbacks.EventListener {
                    override fun objects(
                        pubnub: PubNub,
                        result: PNObjectEventResult,
                    ) {
                        countDownLatch.countDown()
                    }
                },
        )
        metadataSub.subscribe()
        userMetadataSub.subscribe()

        Thread.sleep(2000)

        pubnub.setMemberships(
            channels = channels,
        ).sync()
        pubnub.setChannelMembers(channel = channel, uuids = uuids).sync()
        pubnub.setChannelMetadata(channel = channel, name = randomValue(15)).sync()
        pubnub.setUUIDMetadata(name = randomValue(15)).sync()

        pubnub.setChannelMetadata(channel = channel, description = "aaa").sync()
        pubnub.removeChannelMetadata(channel = channel).sync()
        pubnub.removeUUIDMetadata().sync()
        pubnub.removeChannelMembers(channel = channel, uuids = uuids.map { it.uuid }).sync()
        pubnub.removeMemberships(channels = channels.map { it.channel }).sync()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
    }

    // TODO: separate it
    @Test
    fun customDataIsPassedCorrectly() {
        val expectedUUID = "my_main_uud"
        val expectedName = "my_main_uuid_name"
        val expectedEmail = "my_main_uuid_email"
        val expectedExternalId = "my_main_uuid_ext_id"
        val expectedProfileUrl = "http://my_main_uuid_profile_url"
        val expectedCustom = hashMapOf("color" to "red", "foo" to "bar")

        pubnub.setUUIDMetadata(
            uuid = expectedUUID,
            name = expectedName,
            email = expectedEmail,
            externalId = expectedExternalId,
            profileUrl = expectedProfileUrl,
            custom = expectedCustom,
            includeCustom = true,
        ).sync().apply {
            assertEquals(expectedUUID, this.data?.id)
            assertEquals(expectedName, this.data?.name?.value)
            assertEquals(expectedEmail, this.data?.email?.value)
            assertEquals(expectedExternalId, this.data?.externalId?.value)
            assertEquals(expectedProfileUrl, this.data?.profileUrl?.value)
            assertEquals(expectedCustom, this.data?.custom?.value)
        }
    }

    @Test
    fun testManageMember() {
        pubnub.setChannelMembers(
            channel = channel,
            uuids = listOf(PNMember.Partial(uuidId = otherTestUuid, status = status)),
        ).sync()

        pubnub.manageChannelMembers(
            channel = channel,
            uuidsToSet = listOf(PNMember.Partial(uuidId = testUuid, status = status)),
            uuidsToRemove = listOf(),
        ).sync()

        val getAllResult =
            pubnub.getChannelMembers(channel = channel, includeUUIDDetails = PNUUIDDetailsLevel.UUID).sync().data

        val otherTestUuidMatcher =
            PNMember(
                uuidMetadata(id = otherTestUuid),
                custom = null,
                status = PatchValue.of(status),
                eTag = noEtag,
                updated = noUpdated,
            )
        val testUuidMatcher =
            PNMember(
                uuidMetadata(id = testUuid),
                custom = null,
                status = PatchValue.of(status),
                eTag = noEtag,
                updated = noUpdated,
            )

        assertThat(
            getAllResult.map { it.copy(updated = noUpdated, eTag = noEtag) },
            containsInAnyOrder(testUuidMatcher, otherTestUuidMatcher),
        )

        val removeResult =
            pubnub.manageChannelMembers(
                channel = channel,
                uuidsToSet = listOf(),
                uuidsToRemove = listOf(testUuid, otherTestUuid),
            ).sync().data

        assertThat(removeResult, not(containsInAnyOrder(testUuidMatcher, otherTestUuidMatcher)))
    }

    @Test
    fun testManageMembership() {
        pubnub.setMemberships(
            uuid = testUuid,
            channels = listOf(PNChannelMembership.Partial(channelId = channel, status = status)),
        ).sync()

        pubnub.manageMemberships(
            channelsToSet = listOf(PNChannelMembership.Partial(channelId = otherChannel, status = status)),
            channelsToRemove = listOf(),
            uuid = testUuid,
        ).sync()

        val getAllResult =
            pubnub.getMemberships(uuid = testUuid, includeChannelDetails = PNChannelDetailsLevel.CHANNEL).sync().data

        val channelMatcher =
            PNChannelMembership(
                channelMetadata(id = channel),
                custom = null,
                status = PatchValue.of(status),
                eTag = noEtag,
                updated = noUpdated,
            )
        val otherChannelMatcher =
            PNChannelMembership(
                channelMetadata(id = otherChannel),
                custom = null,
                status = PatchValue.of(status),
                eTag = noEtag,
                updated = noUpdated,
            )

        assertThat(
            getAllResult.map { it.copy(updated = noUpdated, eTag = noEtag) },
            containsInAnyOrder(channelMatcher, otherChannelMatcher),
        )

        val removeResult =
            pubnub.manageMemberships(
                channelsToSet = listOf(),
                channelsToRemove = listOf(channel, otherChannel),
                uuid = testUuid,
            ).sync().data

        assertThat(
            removeResult,
            not(containsInAnyOrder(channelMatcher, otherChannelMatcher)),
        )
    }

    private fun uuidMetadata(id: String): PNUUIDMetadata {
        return PNUUIDMetadata(
            id = id,
            name = null,
            externalId = null,
            profileUrl = null,
            email = null,
            custom = null,
            updated = null,
            eTag = null,
            type = null,
            status = null,
        )
    }

    private fun channelMetadata(id: String): PNChannelMetadata {
        return PNChannelMetadata(
            id = id,
            name = null,
            description = null,
            custom = null,
            updated = null,
            eTag = null,
            type = null,
            status = null,
        )
    }

    fun removeAllUserMetadataWithPaging() { // should be fixed
        var hasMore = true
        var page: PNPage? = null
        while (hasMore) {
            println("-=page")
            val allMetadataPage: PNUUIDMetadataArrayResult = pubnub.getAllUUIDMetadata(page = page).sync()

            allMetadataPage.data.forEach { pnUUIDMetadata: PNUUIDMetadata ->
                pubnub.removeUUIDMetadata(uuid = pnUUIDMetadata.id).sync()
            }
            page = allMetadataPage.next
            hasMore = page != null
        }
    }
}
