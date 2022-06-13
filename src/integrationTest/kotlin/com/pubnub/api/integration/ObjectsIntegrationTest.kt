package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.member.PNUUIDWithCustom
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelWithCustom
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.subscribeToBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ObjectsIntegrationTest : BaseIntegrationTest() {
    private val testUuid = "ThisIsMyId" + randomValue()
    val channel = "ThisIsMyChannel" + randomValue()

    @Test
    fun setGetAndRemoveChannelMetadata() {
        val setResult = pubnub.setChannelMetadata(
            channel = channel,
            name = randomValue(15)
        ).sync()!!

        val getAllResult = pubnub.getAllChannelMetadata().sync()!!
        val getSingleResult = pubnub.getChannelMetadata(channel = channel).sync()!!
        pubnub.removeChannelMetadata(channel = channel).sync()!!

        assertTrue(getAllResult.data.any { it.id == channel })
        assertEquals(setResult, getSingleResult)

        val getAllAfterRemovalResult = pubnub.getAllChannelMetadata().sync()!!

        assertTrue(getAllAfterRemovalResult.data.none { it.id == channel })
    }

    @Test
    fun setGetAndRemoveUUIDMetadata() {
        val setResult = pubnub.setUUIDMetadata(
            uuid = testUuid,
            name = randomValue(15)
        ).sync()!!

        val getAllResult = pubnub.getAllUUIDMetadata().sync()!!
        val getSingleResult = pubnub.getUUIDMetadata(uuid = testUuid).sync()!!
        pubnub.removeUUIDMetadata(uuid = testUuid).sync()!!

        assertTrue(getAllResult.data.any { it.id == testUuid })
        assertEquals(setResult, getSingleResult)

        val getAllAfterRemovalResult = pubnub.getAllUUIDMetadata().sync()!!

        assertTrue(getAllAfterRemovalResult.data.none { it.id == testUuid })
    }

    @Test
    fun addGetAndRemoveMembership() {
        val channels = listOf(PNChannelWithCustom(channel = channel))
        val setResult = pubnub.setMemberships(
            channels = channels,
            uuid = testUuid,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL
        ).sync()!!

        val getAllResult = pubnub.getMemberships(
            uuid = testUuid,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL
        ).sync()!!

        assertEquals(setResult, getAllResult)

        pubnub.removeMemberships(
            channels = channels.map { it.channel },
            uuid = testUuid,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL
        ).sync()!!

        val getAllAfterRemovalResult = pubnub.getMemberships(
            uuid = testUuid,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL
        ).sync()!!

        assertTrue(
            getAllAfterRemovalResult.data.filter { it.channel != null }
                .none { it.channel!!.id == channel }
        )
    }

    @Test
    fun addGetAndRemoveMember() {
        val uuids = listOf(PNMember.Partial(uuidId = testUuid, custom = null, null))

        val setResult = pubnub.setChannelMembers(
            channel = channel,
            uuids = uuids,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID
        ).sync()!!

        val getAllResult = pubnub.getChannelMembers(
            channel = channel,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID
        ).sync()!!

        assertEquals(setResult, getAllResult)

        pubnub.removeChannelMembers(
            channel = channel,
            uuids = uuids.map { it.uuid },
            includeUUIDDetails = PNUUIDDetailsLevel.UUID
        ).sync()!!

        val getAllAfterRemovalResult = pubnub.getChannelMembers(
            channel = channel,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID
        ).sync()!!

        assertTrue(
            getAllAfterRemovalResult.data.filter { it.uuid != null }
                .none { it.uuid!!.id == testUuid }
        )
    }

    @Test
    fun testListeningToAllObjectsEvents() {
        val countDownLatch = CountDownLatch(8)
        val uuids = listOf(PNMember.Partial(uuidId = testUuid, custom = null, null))
        val channels = listOf(
            PNChannelMembership.Partial(channelId = channel)
        )

        pubnub.addListener(
            listener = object : SubscribeCallback() {
                override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

                override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
                    println(objectEvent)
                    countDownLatch.countDown()
                }
            }
        )

        pubnub.subscribeToBlocking(channel)

        pubnub.setMemberships(
            channels = channels
        ).sync()!!
        pubnub.setChannelMembers(channel = channel, uuids = uuids).sync()!!
        pubnub.setChannelMetadata(channel = channel, name = randomValue(15)).sync()!!
        pubnub.setUUIDMetadata(name = randomValue(15)).sync()!!

        pubnub.setChannelMetadata(channel = channel, description = "aaa").sync()!!
        pubnub.removeChannelMetadata(channel = channel).sync()!!
        pubnub.removeUUIDMetadata().sync()!!
        pubnub.removeChannelMembers(channel = channel, uuids = uuids.map { it.uuid }).sync()!!
        pubnub.removeMemberships(channels = channels.map { it.channel }).sync()!!

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
            includeCustom = true
        ).apply {
            queryParam += "mydata" to "myval"
        }
            .sync().apply {
                assertEquals(this?.data?.id, expectedUUID)
                assertEquals(this?.data?.name, expectedName)
                assertEquals(this?.data?.email, expectedEmail)
                assertEquals(this?.data?.externalId, expectedExternalId)
                assertEquals(this?.data?.profileUrl, expectedProfileUrl)
                assertEquals(this?.data?.custom, expectedCustom)
            }
    }
}
