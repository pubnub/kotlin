package com.pubnub.internal.v2.callbacks

import com.google.gson.JsonPrimitive
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.utils.PatchValue
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteMembershipEvent
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.internal.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DelegatingEventListenerTest {
    @Test
    fun testEquals() {
        val eventListener = object : EventListener {}
        val otherEventListener = object : EventListener {}
        val delegating1 = DelegatingEventListener(eventListener)
        val delegating2 = DelegatingEventListener(eventListener)
        val otherDelegating = DelegatingEventListener(otherEventListener)

        Assertions.assertEquals(delegating1, delegating2)
        Assertions.assertEquals(delegating2, delegating1)
        Assertions.assertNotEquals(delegating1, otherDelegating)
        Assertions.assertNotEquals(delegating2, otherDelegating)
        Assertions.assertNotEquals(otherDelegating, delegating1)
        Assertions.assertNotEquals(otherDelegating, delegating2)
    }

    @Test
    fun objects() {
        var uuidCalled = false
        var channelCalled = false
        var membershipCalled = false

        val eventListener =
            object : EventListener {
                override fun uuid(
                    pubnub: PubNub,
                    pnUUIDMetadataResult: PNUUIDMetadataResult,
                ) {
                    uuidCalled = true
                }

                override fun channel(
                    pubnub: PubNub,
                    pnChannelMetadataResult: PNChannelMetadataResult,
                ) {
                    channelCalled = true
                }

                override fun membership(
                    pubnub: PubNub,
                    pnMembershipResult: PNMembershipResult,
                ) {
                    membershipCalled = true
                }
            }
        val delegating = DelegatingEventListener(eventListener)
        val pn = PubNub.create(PNConfiguration(UserId("a")))
        delegating.objects(
            pn,
            PNObjectEventResult(
                BasePubSubResult("a", "b", 0L, null, null),
                PNSetMembershipEventMessage("a", "b", "c", "d", PNSetMembershipEvent("a", "b", null, "c", "d", null)),
            ),
        )
        delegating.objects(
            pn,
            PNObjectEventResult(
                BasePubSubResult("a", "b", 0L, null, null),
                PNSetUUIDMetadataEventMessage(
                    "a",
                    "b",
                    "c",
                    "d",
                    PNUUIDMetadata("a", PatchValue.of("b"), null, PatchValue.of("c"), PatchValue.of("d"), null, null, null, null, null)
                ),
            ),
        )
        delegating.objects(
            pn,
            PNObjectEventResult(
                BasePubSubResult("a", "b", 0L, null, null),
                PNSetChannelMetadataEventMessage(
                    "a",
                    "b",
                    "c",
                    "d",
                    com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata(
                        "a",
                        PatchValue.of("b"),
                        null,
                        PatchValue.of(mapOf("c" to "c")),
                        PatchValue.of("d"),
                        null,
                        null,
                        null
                    )
                ),
            ),
        )

        Assertions.assertTrue(uuidCalled)
        Assertions.assertTrue(channelCalled)
        Assertions.assertTrue(membershipCalled)
    }

    val channel = "myChannel"
    val subscription = "mySub"
    val timetoken = 100L
    val userMetadata = JsonPrimitive(100)
    val publisher = "myPublish"

    val source = "mySource"
    val version = "myVersion"
    val event = "myEvent"
    val type = "myType"

    val id = "myId"
    val name = "myName"
    val externalId = "myExternalId"
    val profileUrl = "myProfileUrl"
    val email = "myEmail"
    val custom = mapOf("a" to "b")
    val updated = "myUpdated"
    val eTag = "myEtag"
    val status = "myStatus"
    val uuid = "myUuid"
    val description = "myDescription"

    @Test
    fun getSetUuidMetadataResult() {
        val metadata = PNUUIDMetadata(
            id,
            PatchValue.of(name),
            PatchValue.of(externalId),
            PatchValue.of(profileUrl),
            PatchValue.of(email),
            PatchValue.of(custom),
            PatchValue.of(updated),
            PatchValue.of(eTag),
            PatchValue.of(type),
            PatchValue.of(status)
        )
        val message = PNSetUUIDMetadataEventMessage(source, version, event, type, metadata)
        val objectEvent = PNObjectEventResult(BasePubSubResult(channel, subscription, timetoken, userMetadata, publisher), message)

        val result: PNUUIDMetadataResult = DelegatingEventListener.getSetUuidMetadataResult(objectEvent, message)

        Assertions.assertEquals(event, result.event)
        result.data.let { data ->
            Assertions.assertEquals(id, data.id)
            Assertions.assertEquals(name, data.name)
            Assertions.assertEquals(externalId, data.externalId)
            Assertions.assertEquals(profileUrl, data.profileUrl)
            Assertions.assertEquals(email, data.email)
            Assertions.assertEquals(custom, data.custom)
            Assertions.assertEquals(updated, data.updated)
            Assertions.assertEquals(eTag, data.eTag)
            Assertions.assertEquals(type, data.type)
            Assertions.assertEquals(status, data.status)
        }
        Assertions.assertEquals(channel, result.channel)
        Assertions.assertEquals(subscription, result.subscription)
        Assertions.assertEquals(timetoken, result.timetoken)
        Assertions.assertEquals(userMetadata, result.userMetadata)
        Assertions.assertEquals(publisher, result.publisher)
    }

    @Test
    fun getDeleteUuidMetadataResult() {
        val message = PNDeleteUUIDMetadataEventMessage(source, version, event, type, uuid)
        val objectEvent = PNObjectEventResult(BasePubSubResult(channel, subscription, timetoken, userMetadata, publisher), message)
        val result: PNUUIDMetadataResult = DelegatingEventListener.getDeleteUuidMetadataResult(objectEvent, message)

        Assertions.assertEquals(event, result.event)
        result.data.let { data ->
            Assertions.assertEquals(uuid, data.id)
            Assertions.assertEquals(null, data.name)
            Assertions.assertEquals(null, data.externalId)
            Assertions.assertEquals(null, data.profileUrl)
            Assertions.assertEquals(null, data.email)
            Assertions.assertEquals(null, data.custom)
            Assertions.assertEquals(null, data.updated)
            Assertions.assertEquals(null, data.eTag)
            Assertions.assertEquals(null, data.type)
            Assertions.assertEquals(null, data.status)
        }
        Assertions.assertEquals(channel, result.channel)
        Assertions.assertEquals(subscription, result.subscription)
        Assertions.assertEquals(timetoken, result.timetoken)
        Assertions.assertEquals(userMetadata, result.userMetadata)
        Assertions.assertEquals(publisher, result.publisher)
    }

    @Test
    fun getSetChannelMetadataResult() {
        val metadata =
            com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata(
                id,
                PatchValue.of(name),
                PatchValue.of(description),
                PatchValue.of(custom),
                PatchValue.of(updated),
                PatchValue.of(eTag),
                PatchValue.of(type),
                PatchValue.of(status)
            )
        val message = PNSetChannelMetadataEventMessage(source, version, event, type, metadata)
        val objectEvent = PNObjectEventResult(BasePubSubResult(channel, subscription, timetoken, userMetadata, publisher), message)

        val result: PNChannelMetadataResult = DelegatingEventListener.getSetChannelMetadataResult(objectEvent, message)

        Assertions.assertEquals(event, result.event)
        result.data.let { data ->
            Assertions.assertEquals(id, data.id)
            Assertions.assertEquals(name, data.name)
            Assertions.assertEquals(description, data.description)
            Assertions.assertEquals(custom, data.custom)
            Assertions.assertEquals(updated, data.updated)
            Assertions.assertEquals(eTag, data.eTag)
            Assertions.assertEquals(type, data.type)
            Assertions.assertEquals(status, data.status)
        }
        Assertions.assertEquals(channel, result.channel)
        Assertions.assertEquals(subscription, result.subscription)
        Assertions.assertEquals(timetoken, result.timetoken)
        Assertions.assertEquals(userMetadata, result.userMetadata)
        Assertions.assertEquals(publisher, result.publisher)
    }

    @Test
    fun getDeleteChannelMetadataResult() {
        val message = PNDeleteChannelMetadataEventMessage(source, version, event, type, channel)
        val objectEvent = PNObjectEventResult(BasePubSubResult(channel, subscription, timetoken, userMetadata, publisher), message)

        val result: PNChannelMetadataResult = DelegatingEventListener.getDeleteChannelMetadataResult(objectEvent, message)

        Assertions.assertEquals(event, result.event)
        result.data.let { data ->
            Assertions.assertEquals(channel, data.id)
            Assertions.assertEquals(null, data.name)
            Assertions.assertEquals(null, data.description)
            Assertions.assertEquals(null, data.custom)
            Assertions.assertEquals(null, data.updated)
            Assertions.assertEquals(null, data.eTag)
            Assertions.assertEquals(null, data.type)
            Assertions.assertEquals(null, data.status)
        }
        Assertions.assertEquals(channel, result.channel)
        Assertions.assertEquals(subscription, result.subscription)
        Assertions.assertEquals(timetoken, result.timetoken)
        Assertions.assertEquals(userMetadata, result.userMetadata)
        Assertions.assertEquals(publisher, result.publisher)
    }

    @Test
    fun getDeleteMembershipResult() {
        val membershipEvent = PNDeleteMembershipEvent(channel, uuid)
        val message = PNDeleteMembershipEventMessage(source, version, event, type, membershipEvent)
        val objectEvent = PNObjectEventResult(BasePubSubResult(channel, subscription, timetoken, userMetadata, publisher), message)

        val result: PNMembershipResult = DelegatingEventListener.getDeleteMembershipResult(objectEvent, message)

        Assertions.assertEquals(event, result.event)
        result.data.let { data ->
            Assertions.assertEquals(channel, data.channel.id)
            Assertions.assertEquals(uuid, data.uuid)
            Assertions.assertEquals(null, data.custom)
            Assertions.assertEquals(null, data.updated)
            Assertions.assertEquals(null, data.eTag)
            Assertions.assertEquals(null, data.status)
        }
        Assertions.assertEquals(channel, result.channel)
        Assertions.assertEquals(subscription, result.subscription)
        Assertions.assertEquals(timetoken, result.timetoken)
        Assertions.assertEquals(userMetadata, result.userMetadata)
        Assertions.assertEquals(publisher, result.publisher)
    }

    @Test
    fun getSetMembershipResult() {
        val metadata = PNSetMembershipEvent(channel, uuid, PatchValue.of(custom), eTag, updated, PatchValue.of(status))
        val message = PNSetMembershipEventMessage(source, version, event, type, metadata)
        val objectEvent = PNObjectEventResult(BasePubSubResult(channel, subscription, timetoken, userMetadata, publisher), message)

        val result: PNMembershipResult = DelegatingEventListener.getSetMembershipResult(objectEvent, message)

        Assertions.assertEquals(event, result.event)
        result.data.let { data ->
            Assertions.assertEquals(channel, data.channel.id)
            Assertions.assertEquals(uuid, data.uuid)
            Assertions.assertEquals(custom, data.custom)
            Assertions.assertEquals(updated, data.updated)
            Assertions.assertEquals(eTag, data.eTag)
            Assertions.assertEquals(status, data.status)
        }
        Assertions.assertEquals(channel, result.channel)
        Assertions.assertEquals(subscription, result.subscription)
        Assertions.assertEquals(timetoken, result.timetoken)
        Assertions.assertEquals(userMetadata, result.userMetadata)
        Assertions.assertEquals(publisher, result.publisher)
    }
}
