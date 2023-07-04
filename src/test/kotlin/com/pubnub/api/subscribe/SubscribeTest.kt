package com.pubnub.api.subscribe

import com.pubnub.api.managers.EventEngineManager
import com.pubnub.api.managers.SubscriptionState
import com.pubnub.api.models.SubscriptionItem
import com.pubnub.api.subscribe.eventengine.event.Event
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
    private val channelsInLocalStorage = listOf(CHANNEL_01, CHANNEL_02)
    private val channelGroupsInLocalStorage = listOf(CHANNEL_GROUPS_01, CHANNEL_GROUPS_02)
    private lateinit var objectUnderTest: Subscribe
    private val eventEngineManager: EventEngineManager = mockk()
    private var subscriptionState: SubscriptionState = createSubscriptionStateContainingValues()
    private val withPresence = false
    private val withTimetoken = 12345345452L
    private val event: CapturingSlot<Event> = slot()

    @BeforeEach
    internal fun setUp() {
        every { eventEngineManager.addEventToQueue(capture(event)) } returns Unit
        objectUnderTest = Subscribe(eventEngineManager, subscriptionState)
    }

    @Test
    internal fun `should store channels and channelGroups in local storage and pass SubscriptionChange event for handling when timeToken is zero`() {
        val channelToSubscribe = CHANNEL_03
        val channelGroupsToSubscribe = CHANNEL_GROUPS_03

        objectUnderTest.subscribe(listOf(channelToSubscribe), listOf(channelGroupsToSubscribe), withPresence)

        verify { eventEngineManager.addEventToQueue(any()) }
        assertEquals(
            Event.SubscriptionChanged(
                channelsInLocalStorage + listOf(channelToSubscribe),
                channelGroupsInLocalStorage + listOf(channelGroupsToSubscribe)
            ),
            event.captured
        )
        assertTrue(subscriptionState.channels.contains(CHANNEL_01))
        assertTrue(subscriptionState.channelGroups.contains(CHANNEL_GROUPS_01))
    }

    @Test
    internal fun `should store channels and channelGroups in local storage and pass SubscriptionRestored event for handling when timeToken is non zero`() {
        val channelToSubscribe = CHANNEL_03
        val channelGroupsToSubscribe = CHANNEL_GROUPS_03

        objectUnderTest.subscribe(
            listOf(channelToSubscribe),
            listOf(channelGroupsToSubscribe),
            withPresence,
            withTimetoken
        )

        verify { eventEngineManager.addEventToQueue(any()) }
        assertEquals(
            Event.SubscriptionRestored(
                channelsInLocalStorage + listOf(channelToSubscribe),
                channelGroupsInLocalStorage + listOf(channelGroupsToSubscribe),
                SubscriptionCursor(withTimetoken, "42") // todo magic number
            ),
            event.captured
        )
        assertTrue(subscriptionState.channels.contains(CHANNEL_01))
        assertTrue(subscriptionState.channelGroups.contains(CHANNEL_GROUPS_01))
    }

    @Test
    internal fun `should remove channels and channelGroups from local storage and pass SubscriptionChange event for handling when at least one channel or channelGroup left in storage`() {
        val channelsToUnsubscribe: List<String> = listOf(CHANNEL_01)
        val channelGroupsToUnsubscribe: List<String> = listOf(CHANNEL_GROUPS_01)

        objectUnderTest.unsubscribe(channelsToUnsubscribe, channelGroupsToUnsubscribe)

        verify { eventEngineManager.addEventToQueue(any()) }
        assertEquals(Event.SubscriptionChanged(listOf(CHANNEL_02), listOf(CHANNEL_GROUPS_02)), event.captured)
        assertTrue(subscriptionState.channels.size == 1 && subscriptionState.channels.contains(CHANNEL_02))
        assertTrue(
            subscriptionState.channelGroups.size == 1 && subscriptionState.channelGroups.contains(
                CHANNEL_GROUPS_02
            )
        )
    }

    @Test
    internal fun `should remove channels and channelGroups from local storage and pass UnsubscribeAll event for handling when no channel nor channelGroup left in storage`() {
        val channelsToUnsubscribe: List<String> = listOf(CHANNEL_01, CHANNEL_02)
        val channelGroupsToUnsubscribe: List<String> = listOf(CHANNEL_GROUPS_01, CHANNEL_GROUPS_02)

        objectUnderTest.unsubscribe(channelsToUnsubscribe, channelGroupsToUnsubscribe)

        verify { eventEngineManager.addEventToQueue(any()) }
        assertEquals(Event.UnsubscribeAll, event.captured)
        assertTrue(subscriptionState.channels.size == 0)
        assertTrue(subscriptionState.channelGroups.size == 0)
    }

    @Test
    internal fun `should remove all channels and channelGroups from local storage when unsubscribeAll`() {

        objectUnderTest.unsubscribeAll()

        verify { eventEngineManager.addEventToQueue(any()) }
        assertEquals(Event.UnsubscribeAll, event.captured)
        assertTrue(subscriptionState.channels.size == 0)
        assertTrue(subscriptionState.channelGroups.size == 0)
    }

    @Test
    internal fun `should return subscribed channels from local storage when getSubscribedChannels`() {

        val subscribedChannels = objectUnderTest.getSubscribedChannels()

        assertEquals(channelsInLocalStorage, subscribedChannels)
    }

    @Test
    internal fun `should return subscribed channelGroups from local storage when getSubscribedChannelGroups`() {

        val subscribedChannelGroups = objectUnderTest.getSubscribedChannelGroups()

        assertEquals(channelGroupsInLocalStorage, subscribedChannelGroups)
    }

    private fun createSubscriptionStateContainingValues(): SubscriptionState {
        val channelsInLocalStorage = HashMap(
            channelsInLocalStorage.map { channelName -> channelName to SubscriptionItem(channelName) }.toMap()
        )
        val channelGroupsInLocalStorage = HashMap(
            channelGroupsInLocalStorage.map { channelGroupName -> channelGroupName to SubscriptionItem(channelGroupName) }
                .toMap()
        )

        subscriptionState = SubscriptionState()
        subscriptionState.channels = channelsInLocalStorage
        subscriptionState.channelGroups = channelGroupsInLocalStorage
        return subscriptionState
    }
}
