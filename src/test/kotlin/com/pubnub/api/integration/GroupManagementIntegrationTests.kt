package com.pubnub.api.integration

import com.pubnub.api.suite.await
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GroupManagementIntegrationTests : BaseIntegrationTest() {

    lateinit var expectedChannel1: String
    lateinit var expectedChannel2: String
    lateinit var expectedChannel3: String

    lateinit var expectedGroup: String

    override fun onBefore() {
        expectedChannel1 = randomValue()
        expectedChannel2 = randomValue()
        expectedChannel3 = randomValue()
        expectedGroup = randomValue(8)
    }

    @Test
    fun testRemoveChannelsFromGroup() {
        addChannelsToGroup()

        pubnub.removeChannelsFromChannelGroup().apply {
            channelGroup = expectedGroup
            channels = listOf(expectedChannel1, expectedChannel2, expectedChannel3)
        }.await { _, status ->
            assertFalse(status.error)
            assertEquals(3, status.affectedChannels.size)
            assertEquals(0, pubnub.getSubscribedChannels().size)
            assertEquals(1, status.affectedChannelGroups.size)
            assertEquals(0, pubnub.getSubscribedChannelGroups().size)
        }
    }

    @Test
    fun testRemoveChannelFromGroup() {
        addChannelsToGroup()

        pubnub.removeChannelsFromChannelGroup().apply {
            channelGroup = expectedGroup
            channels = listOf(expectedChannel1)
        }.await { _, status ->
            assertFalse(status.error)
        }
    }

    @Test
    fun testSubscribeToChannelGroup() {
        addChannelsToGroup()

        pubnub.subscribe().apply {
            channelGroups = listOf(expectedGroup)
            withPresence = true
        }.execute()

        wait()

        assertTrue(pubnub.getSubscribedChannelGroups().contains(expectedGroup))
    }

    @Test
    fun testUnsubscribeFromChannelGroup() {
        addChannelsToGroup()

        pubnub.subscribe().apply {
            channelGroups = listOf(expectedGroup)
            withPresence = true
        }.execute()

        wait()

        pubnub.unsubscribe().apply {
            channelGroups = listOf(expectedGroup)
        }.execute()

        wait()

        assertTrue(pubnub.getSubscribedChannelGroups().isEmpty())
    }

    @Test
    fun testGetAllChannelsFrogroup() {
        addChannelsToGroup()

        pubnub.listChannelsForChannelGroup().apply {
            channelGroup = expectedGroup
        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(3, result!!.channels.size)
        }
    }

    @Test
    fun testAddChannelsToGroup() {
        pubnub.addChannelsToChannelGroup().apply {
            channelGroup = expectedGroup
            channels = listOf(expectedChannel1, expectedChannel2, expectedChannel3)
        }.await { _, status ->
            assertFalse(status.error)
            assertEquals(1, status.affectedChannelGroups.size)
            assertEquals(3, status.affectedChannels.size)
        }
    }

    private fun addChannelsToGroup() {
        pubnub.addChannelsToChannelGroup().apply {
            channelGroup = expectedGroup
            channels = listOf(expectedChannel1, expectedChannel2, expectedChannel3)
        }.await { _, status ->
            assertFalse(status.error)
        }
    }
}