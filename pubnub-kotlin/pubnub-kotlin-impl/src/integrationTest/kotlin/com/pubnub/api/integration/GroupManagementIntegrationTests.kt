package com.pubnub.api.integration

import com.pubnub.test.CommonUtils.randomValue
import com.pubnub.test.CommonUtils.retry
import com.pubnub.test.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

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

        pubnub.removeChannelsFromChannelGroup(
            channelGroup = expectedGroup,
            channels = listOf(expectedChannel1, expectedChannel2, expectedChannel3),
        ).await { result ->
            assertFalse(result.isFailure)
            assertEquals(0, pubnub.getSubscribedChannels().size)
            assertEquals(0, pubnub.getSubscribedChannelGroups().size)
        }
    }

    @Test
    fun testRemoveChannelFromGroup() {
        addChannelsToGroup()

        pubnub.removeChannelsFromChannelGroup(
            channelGroup = expectedGroup,
            channels = listOf(expectedChannel1),
        ).await { result ->
            assertFalse(result.isFailure)
        }
    }

    @Test
    fun testSubscribeToChannelGroup() {
        addChannelsToGroup()

        pubnub.subscribe(
            channelGroups = listOf(expectedGroup),
            withPresence = true,
        )

        retry {
            assertTrue(pubnub.getSubscribedChannelGroups().contains(expectedGroup))
        }
    }

    @Test
    fun testUnsubscribeFromChannelGroup() {
        addChannelsToGroup()

        pubnub.subscribe(
            channelGroups = listOf(expectedGroup),
            withPresence = true,
        )

        wait()

        pubnub.unsubscribe(
            channelGroups = listOf(expectedGroup),
        )

        wait()

        assertTrue(pubnub.getSubscribedChannelGroups().isEmpty())
    }

    @Test
    fun testGetAllChannelsFrogroup() {
        addChannelsToGroup()

        pubnub.listChannelsForChannelGroup(
            channelGroup = expectedGroup,
        ).await { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(3, it.channels.size)
            }
        }
    }

    @Test
    fun testAddChannelsToGroup() {
        pubnub.addChannelsToChannelGroup(
            channelGroup = expectedGroup,
            channels = listOf(expectedChannel1, expectedChannel2, expectedChannel3),
        ).await { result ->
            assertFalse(result.isFailure)
        }
    }

    @Test
    fun testDeleteChannelGroup() {
        pubnub.addChannelsToChannelGroup(
            channelGroup = expectedGroup,
            channels = listOf(expectedChannel1, expectedChannel2, expectedChannel3),
        ).sync()

        pubnub.deleteChannelGroup(channelGroup = expectedGroup).sync()

        val listAllChannelGroupsResult = pubnub.listAllChannelGroups().sync()
        assertFalse(listAllChannelGroupsResult.groups.contains(expectedGroup))
    }

    private fun addChannelsToGroup() {
        pubnub.addChannelsToChannelGroup(
            channelGroup = expectedGroup,
            channels = listOf(expectedChannel1, expectedChannel2, expectedChannel3),
        ).await { result ->
            assertFalse(result.isFailure)
        }
    }
}
