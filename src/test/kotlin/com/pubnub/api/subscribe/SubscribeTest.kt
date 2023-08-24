package com.pubnub.api.subscribe

import com.pubnub.api.managers.SubscribeEventEngineManager
import com.pubnub.api.subscribe.eventengine.data.SubscriptionData
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val CHANNEL_01 = "channel01"
private const val CHANNEL_02 = "channel02"
private const val CHANNEL_03 = "channel03"

private const val CHANNEL_GROUPS_01 = "channelGroups01"
private const val CHANNEL_GROUPS_02 = "channelGroups02"
private const val CHANNEL_GROUPS_03 = "channelGroups03"

internal class SubscribeTest {
    private val channelsInSubscriptionData = mutableSetOf(CHANNEL_01, CHANNEL_02)
    private val channelGroupsInSubscriptionData = mutableSetOf(CHANNEL_GROUPS_01, CHANNEL_GROUPS_02)
    private lateinit var objectUnderTest: Subscribe
    private val subscribeEventEngineManager: SubscribeEventEngineManager = mockk()
    private var subscriptionData: SubscriptionData = createSubscriptionStateContainingValues()
    private val withPresence = false
    private val withTimetoken = 12345345452L
    private val subscribeEvent: CapturingSlot<SubscribeEvent> = slot()

    @BeforeEach
    internal fun setUp() {
        every { subscribeEventEngineManager.addEventToQueue(capture(subscribeEvent)) } returns Unit
        objectUnderTest = Subscribe(subscribeEventEngineManager, subscriptionData)
    }

    @Test
    fun `should store channels and channelGroups in local storage and pass SubscriptionChange event for handling when timeToken is zero`() {
        val channelToSubscribe = CHANNEL_03
        val channelGroupsToSubscribe = CHANNEL_GROUPS_03

        objectUnderTest.subscribe(setOf(channelToSubscribe), setOf(channelGroupsToSubscribe), withPresence)

        verify { subscribeEventEngineManager.addEventToQueue(any()) }

        assertEquals(
            SubscribeEvent.SubscriptionChanged(
                (channelsInSubscriptionData + channelToSubscribe),
                (channelGroupsInSubscriptionData + channelGroupsToSubscribe)
            ),
            subscribeEvent.captured
        )
    }

    @Test
    fun `should store channels and channelGroups in local storage and pass SubscriptionRestored event for handling when timeToken is non zero`() {
        val channelToSubscribe = CHANNEL_03
        val channelGroupsToSubscribe = CHANNEL_GROUPS_03

        objectUnderTest.subscribe(
            setOf(channelToSubscribe),
            setOf(channelGroupsToSubscribe),
            withPresence,
            withTimetoken
        )

        verify { subscribeEventEngineManager.addEventToQueue(any()) }
        assertEquals(
            SubscribeEvent.SubscriptionRestored(
                (channelsInSubscriptionData + channelToSubscribe),
                (channelGroupsInSubscriptionData + channelGroupsToSubscribe),
                SubscriptionCursor(withTimetoken, "42") // todo magic number
            ),
            subscribeEvent.captured
        )
    }

    @Test
    fun `should remove channels and channelGroups from local storage and pass SubscriptionChange event for handling when at least one channel or channelGroup left in storage`() {
        val channelsToUnsubscribe: Set<String> = setOf(CHANNEL_01)
        val channelGroupsToUnsubscribe: Set<String> = setOf(CHANNEL_GROUPS_01)

        objectUnderTest.unsubscribe(channelsToUnsubscribe, channelGroupsToUnsubscribe)

        verify { subscribeEventEngineManager.addEventToQueue(any()) }
        assertEquals(
            SubscribeEvent.SubscriptionChanged(setOf(CHANNEL_02), setOf(CHANNEL_GROUPS_02)),
            subscribeEvent.captured
        )
        assertEquals(setOf(CHANNEL_02), subscriptionData.channels)
        assertEquals(setOf(CHANNEL_GROUPS_02), subscriptionData.channelGroups)
    }

    @Test
    fun `should remove channels and channelGroups from local storage and pass UnsubscribeAll event for handling when no channel nor channelGroup left in storage`() {
        val channelsToUnsubscribe: Set<String> = setOf(CHANNEL_01, CHANNEL_02)
        val channelGroupsToUnsubscribe: Set<String> = setOf(CHANNEL_GROUPS_01, CHANNEL_GROUPS_02)

        objectUnderTest.unsubscribe(channelsToUnsubscribe, channelGroupsToUnsubscribe)

        verify { subscribeEventEngineManager.addEventToQueue(any()) }
        assertEquals(SubscribeEvent.UnsubscribeAll, subscribeEvent.captured)
        assertTrue(subscriptionData.channels.size == 0)
        assertTrue(subscriptionData.channelGroups.size == 0)
    }

    @Test
    fun `should remove all channels and channelGroups from local storage when unsubscribeAll`() {

        objectUnderTest.unsubscribeAll()

        verify { subscribeEventEngineManager.addEventToQueue(any()) }
        assertEquals(SubscribeEvent.UnsubscribeAll, subscribeEvent.captured)
        assertTrue(subscriptionData.channels.size == 0)
        assertTrue(subscriptionData.channelGroups.size == 0)
    }

    @Test
    fun `should return subscribed channels from local storage when getSubscribedChannels`() {

        val subscribedChannels = objectUnderTest.getSubscribedChannels()

        assertEquals(channelsInSubscriptionData.toList(), subscribedChannels)
    }

    @Test
    fun `should return subscribed channelGroups from local storage when getSubscribedChannelGroups`() {

        val subscribedChannelGroups = objectUnderTest.getSubscribedChannelGroups()

        assertEquals(channelGroupsInSubscriptionData.toList(), subscribedChannelGroups)
    }

    @Test
    fun `should pass disconnect event for handling when disconnect`() {

        objectUnderTest.disconnect()

        assertEquals(SubscribeEvent.Disconnect, subscribeEvent.captured)
    }

    @Test
    fun `should pass reconnect event for handling when disconnect`() {
        objectUnderTest.reconnect()

        assertEquals(SubscribeEvent.Reconnect, subscribeEvent.captured)
    }

    private fun createSubscriptionStateContainingValues(): SubscriptionData {
        subscriptionData = SubscriptionData()
        subscriptionData.channels.addAll(channelsInSubscriptionData)
        subscriptionData.channelGroups.addAll(channelGroupsInSubscriptionData)
        return subscriptionData
    }
}
