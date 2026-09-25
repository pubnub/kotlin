package com.pubnub.internal.v2.subscription

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.v2.entities.ChannelName
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Regression guard for the "timetoken trap": [SubscriptionImpl.accepts] drops any event whose
 * [com.pubnub.api.models.consumer.pubsub.PubSubResult.timetoken] is null or non-monotonic. Because
 * [PNDataSyncEventResult] delegates `PubSubResult by result`, it must expose the underlying
 * [BasePubSubResult.timetoken] so DataSync events survive `accepts()` the same way objects events do.
 */
class DataSyncSubscriptionAcceptsTest {
    private val pubNubImpl: PubNubImpl = mockk(relaxed = true)
    private val channelName = "user-1"
    private val channels = setOf(ChannelName(channelName))

    private fun activeSubscription(): SubscriptionImpl {
        val subscription =
            SubscriptionImpl(
                pubNubImpl,
                channels,
                emptySet(),
                SubscriptionOptions.filter { result -> channels.any { it.id == result.channel } },
            )
        subscription.onSubscriptionActive(SubscriptionCursor(0L))
        return subscription
    }

    private fun dataSyncEvent(timetoken: Long?) =
        PNDataSyncEventResult(
            BasePubSubResult(
                channel = channelName,
                subscription = null,
                timetoken = timetoken,
                userMetadata = null,
                publisher = null,
            ),
            PNDeleteDataSyncUserEventMessage(
                source = "data-sync",
                version = "1.0",
                type = "user",
                className = null,
                classLevel = null,
                classVersion = null,
                id = "user-1",
                deletedAt = "2026-03-01T00:00:00Z",
            ),
        )

    @Test
    fun `datasync event with a monotonic timetoken is accepted`() {
        val subscription = activeSubscription()
        assertTrue(subscription.accepts(AnnouncementEnvelope(dataSyncEvent(17000000000000000L))))
    }

    @Test
    fun `datasync event with a null timetoken is dropped`() {
        val subscription = activeSubscription()
        assertFalse(subscription.accepts(AnnouncementEnvelope(dataSyncEvent(null))))
    }

    @Test
    fun `datasync event with a non-monotonic timetoken is dropped`() {
        val subscription = activeSubscription()
        val tt = 17000000000000000L
        assertTrue(subscription.accepts(AnnouncementEnvelope(dataSyncEvent(tt))))
        // same timetoken again -> not strictly greater -> dropped
        assertFalse(subscription.accepts(AnnouncementEnvelope(dataSyncEvent(tt))))
    }
}
