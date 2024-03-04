package com.pubnub.internal.v2.callbacks

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.v2.callbacks.EventListener
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

        val eventListener = object : EventListener {
            override fun uuid(pubnub: PubNub, pnUUIDMetadataResult: PNUUIDMetadataResult) {
                uuidCalled = true
            }

            override fun channel(pubnub: PubNub, pnChannelMetadataResult: PNChannelMetadataResult) {
                channelCalled = true
            }

            override fun membership(pubnub: PubNub, pnMembershipResult: PNMembershipResult) {
                membershipCalled = true
            }
        }
        val delegating = DelegatingEventListener(eventListener)
        val pn = PubNub.create(PNConfiguration(UserId("a")))
        delegating.objects(pn, PNObjectEventResult(BasePubSubResult("a", "b", 0L, null, null), PNSetMembershipEventMessage("a", "b", "c", "d", PNSetMembershipEvent("a", "b", null, "c", "d", null))))
        delegating.objects(pn, PNObjectEventResult(BasePubSubResult("a", "b", 0L, null, null), PNSetUUIDMetadataEventMessage("a", "b", "c", "d", PNUUIDMetadata("a", "b", null, "c", "d", null, null, null, null, null))))
        delegating.objects(pn, PNObjectEventResult(BasePubSubResult("a", "b", 0L, null, null), PNSetChannelMetadataEventMessage("a", "b", "c", "d", PNChannelMetadata("a", "b", null, "c", "d", null, null, null))))

        Assertions.assertTrue(uuidCalled)
        Assertions.assertTrue(channelCalled)
        Assertions.assertTrue(membershipCalled)

    }
}