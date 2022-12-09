package com.pubnub.api.integration

import com.pubnub.api.CommonUtils
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.CommonUtils.emoji
import com.pubnub.api.CommonUtils.publishMixed
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.CommonUtils.retry
import com.pubnub.api.PubNubError
import com.pubnub.api.SpaceId
import com.pubnub.api.await
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.param
import org.awaitility.kotlin.await
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.aMapWithSize
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Duration

class HistoryIntegrationTest : BaseIntegrationTest() {

    @Test
    fun testHistorySingleChannel() {
        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        assertEquals(
            expectedMessageCount,
            publishMixed(pubnub, expectedMessageCount, expectedChannelName).size
        )

        retry {
            pubnub.history(
                channel = expectedChannelName
            ).sync()!!.run {
                messages.forEach {
                    assertNotNull(it.entry)
                    assertNull(it.meta)
                    assertNull(it.timetoken)
                }
            }
        }
    }

    @Test
    fun testHistorySingleChannel_Timetoken() {
        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        assertEquals(
            expectedMessageCount,
            publishMixed(pubnub, expectedMessageCount, expectedChannelName).size
        )

        retry {
            pubnub.history(
                channel = expectedChannelName,
                includeTimetoken = true
            ).sync()!!.run {
                this.messages.forEach {
                    assertNotNull(it.entry)
                    assertNotNull(it.timetoken)
                    assertNull(it.meta)
                }
            }
        }
    }

    @Test
    fun testHistorySingleChannel_Meta() {
        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        assertEquals(
            expectedMessageCount,
            publishMixed(pubnub, expectedMessageCount, expectedChannelName).size
        )

        retry {
            pubnub.history(
                channel = expectedChannelName,
                includeMeta = true
            ).sync()!!.run {
                messages.forEach {
                    assertNotNull(it.entry)
                    assertNull(it.timetoken)
                    assertNotNull(it.meta)
                }
            }
        }
    }

    @Test
    fun testHistorySingleChannel_Meta_Timetoken() {
        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        assertEquals(
            expectedMessageCount,
            publishMixed(pubnub, expectedMessageCount, expectedChannelName).size
        )

        retry {
            pubnub.history(
                channel = expectedChannelName,
                includeMeta = true,
                includeTimetoken = true
            ).sync()!!.run {
                messages.forEach {
                    assertNotNull(it.entry)
                    assertNotNull(it.timetoken)
                    assertNotNull(it.meta)
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel() {
        val expectedChannelName = randomValue()

        publishMixed(pubnub, 10, expectedChannelName)

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannelName),
                page = PNBoundedPage(
                    limit = 25
                )
            ).sync()!!.run {
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNull(it.meta)
                    assertNull(it.actions)
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel_Meta() {
        val expectedChannelName = randomValue()
        publishMixed(pubnub, 10, expectedChannelName)

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannelName),
                page = PNBoundedPage(limit = 25),
                includeMeta = true
            ).sync()!!.run {
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNotNull(it.meta)
                    assertNull(it.actions)
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel_Actions() {
        val expectedChannelName = randomValue()
        val results = publishMixed(pubnub, 120, expectedChannelName)

        // todo check with executorservice

        pubnub.addMessageAction(
            channel = expectedChannelName,
            messageAction = PNMessageAction(
                type = "reaction",
                value = emoji(),
                messageTimetoken = results[0].timetoken
            )
        ).sync()!!

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannelName),
                page = PNBoundedPage(limit = 25),
                includeMeta = false,
                includeMessageActions = true
            ).sync()!!.run {
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNull(it.meta)
                    if (it.timetoken == results[0].timetoken) {
                        assertNotNull(it.actions)
                    } else {
                        assertTrue(it.actions!!.isEmpty())
                    }
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel_ActionsMeta() {
        val expectedChannelName = randomValue()
        val results = publishMixed(pubnub, 2, expectedChannelName)

        pubnub.addMessageAction(
            channel = expectedChannelName,
            messageAction = PNMessageAction(
                type = "reaction",
                value = emoji(),
                messageTimetoken = results[0].timetoken
            )
        ).sync()!!

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannelName),
                page = PNBoundedPage(limit = 25),
                includeMeta = true,
                includeMessageActions = true
            ).sync()!!.run {
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNotNull(it.meta)
                    if (it.timetoken == results[0].timetoken) {
                        assertNotNull(it.actions)
                    } else {
                        assertTrue(it.actions!!.isEmpty())
                    }
                }
            }
        }
    }

    @Test
    fun testFetchMultiChannel() {
        val expectedChannelNames = generateSequence { randomValue() }
            .take(2).toList()
            .also {
                it.forEach { channel ->
                    publishMixed(pubnub, 10, channel)
                }
            }

        retry {
            pubnub.fetchMessages(
                channels = expectedChannelNames,
                page = PNBoundedPage(
                    limit = 25
                )
            ).sync()!!.run {
                expectedChannelNames.forEach { expectedChannel ->
                    channels[expectedChannel]!!.forEach {
                        assertNotNull(it.message)
                        assertNotNull(it.timetoken)
                        assertNull(it.meta)
                        assertNull(it.actions)
                    }
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel_NoLimit() {
        val expectedChannelName = randomValue()
        assertEquals(10, publishMixed(pubnub, 10, expectedChannelName).size)

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannelName)
            ).sync()!!.run {
                assertEquals(1, channels[expectedChannelName]!!.size)
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNull(it.meta)
                    assertNull(it.actions)
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel_OverflowLimit() {
        val expectedChannelName = randomValue()

        assertEquals(10, publishMixed(pubnub, 10, expectedChannelName).size)

        retry {
            pubnub.fetchMessages(
                channels = listOf(expectedChannelName),
                page = PNBoundedPage(
                    limit = 100
                )
            ).sync()!!.apply {
                assertEquals(10, channels[expectedChannelName]!!.size)
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNull(it.meta)
                    assertNull(it.actions)
                }
            }
        }
    }

    @Test
    fun testHistorySingleChannel_IncludeAll_Crypto() {
        val expectedCipherKey = randomValue()
        pubnub.configuration.cipherKey = expectedCipherKey

        val observer = createPubNub().apply {
            configuration.cipherKey = expectedCipherKey
        }

        assertEquals(pubnub.configuration.cipherKey, observer.configuration.cipherKey)

        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        assertEquals(
            expectedMessageCount,
            publishMixed(pubnub, expectedMessageCount, expectedChannelName).size
        )

        retry {
            observer.history(
                channel = expectedChannelName,
                includeTimetoken = true,
                includeMeta = true
            ).sync()!!.run {
                messages.forEach {
                    assertNotNull(it.entry)
                    assertNotNull(it.timetoken)
                    assertNotNull(it.meta)
                    assertTrue(it.entry.toString().contains("_msg"))
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel_IncludeAll_Crypto() {
        val expectedCipherKey = randomValue()
        pubnub.configuration.cipherKey = expectedCipherKey

        val observer = createPubNub().apply {
            configuration.cipherKey = expectedCipherKey
        }

        assertEquals(pubnub.configuration.cipherKey, observer.configuration.cipherKey)

        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        assertEquals(
            expectedMessageCount,
            publishMixed(pubnub, expectedMessageCount, expectedChannelName).size
        )

        retry {
            observer.fetchMessages(
                channels = listOf(expectedChannelName),
                includeMeta = true
            ).sync()!!.run {
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNotNull(it.meta)
                    assertNull(it.actions)
                    assertTrue(it.message.toString().contains("_msg"))
                }
            }
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_IncludeAll_Crypto() {
        val expectedCipherKey = randomValue()
        pubnub.configuration.cipherKey = expectedCipherKey

        val observer = createPubNub().apply {
            configuration.cipherKey = expectedCipherKey
        }

        assertEquals(pubnub.configuration.cipherKey, observer.configuration.cipherKey)

        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        val mixed = publishMixed(pubnub, expectedMessageCount, expectedChannelName)
        assertEquals(expectedMessageCount, mixed.size)

        val messagesWithActions = mutableListOf<Long>()

        mixed.forEachIndexed { i, it ->
            if (i % 2 == 0) {
                val reaction = pubnub.addMessageAction(
                    channel = expectedChannelName,
                    messageAction = PNMessageAction(
                        type = "reaction",
                        value = emoji(),
                        messageTimetoken = it.timetoken
                    )
                ).sync()!!
                messagesWithActions.add(reaction.messageTimetoken)
            }
        }

        retry {
            observer.fetchMessages(
                channels = listOf(expectedChannelName),
                includeMeta = true,
                includeMessageActions = true
            ).sync()!!.run {
                channels[expectedChannelName]!!.forEach {
                    assertNotNull(it.message)
                    assertNotNull(it.timetoken)
                    assertNotNull(it.meta)
                    if (messagesWithActions.contains(it.timetoken)) {
                        assertNotNull(it.actions)
                    } else {
                        assertTrue(it.actions!!.isEmpty())
                    }
                    assertTrue(it.message.toString().contains("_msg"))
                }
            }
        }
    }

    @Test
    fun testFetchMultiChannel_WithMessageActions_Exception() {
        try {
            pubnub.fetchMessages(
                channels = listOf(randomValue(), randomValue()),
                includeMessageActions = true
            ).sync()!!
        } catch (e: Exception) {
            assertPnException(PubNubError.HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS, e)
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_Default() {
        pubnub.fetchMessages(
            channels = listOf(randomValue())
        ).await { _, status ->
            assertEquals("1", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_Low() {
        pubnub.fetchMessages(
            channels = listOf(randomValue()),
            page = PNBoundedPage(
                limit = -1
            )
        ).await { _, status ->
            assertEquals("1", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_Valid() {
        pubnub.fetchMessages(
            channels = listOf(randomValue()),
            page = PNBoundedPage(
                limit = 15
            )
        ).await { _, status ->
            assertEquals("15", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_High() {
        pubnub.fetchMessages(
            channels = listOf(randomValue()),
            page = PNBoundedPage(
                limit = 100
            )
        ).await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_Default() {
        pubnub.fetchMessages(
            channels = listOf(randomValue()),
            includeMessageActions = true
        ).await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_Low() {
        pubnub.fetchMessages(
            channels = listOf(randomValue()),
            page = PNBoundedPage(
                limit = -1
            ),
            includeMessageActions = true
        ).await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_High() {
        pubnub.fetchMessages(
            channels = listOf(randomValue()),
            page = PNBoundedPage(
                limit = 200
            ),
            includeMessageActions = true
        ).await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_Valid() {
        pubnub.fetchMessages(
            channels = listOf(randomValue()),
            page = PNBoundedPage(
                limit = 15
            ),
            includeMessageActions = true
        ).await { _, status ->
            assertEquals("15", status.param("max"))
        }
    }
    @Test
    fun testEmptyMeta() {
        val expectedChannel = randomChannel()

        // publish a message without any metadata
        pubnub.publish(
            message = randomValue(),
            channel = expectedChannel
        ).sync()!!

        // /v2/history
        retry {
            pubnub.history(
                channel = expectedChannel,
                includeMeta = true
            ).sync()!!.run {
                assertEquals(1, messages.size)
                assertNotNull(messages[0].meta)
            }
        }

        // /v3/history
        pubnub.fetchMessages(
            channels = listOf(expectedChannel),
            includeMeta = true
        ).sync()!!.run {
            assertEquals(1, channels[expectedChannel]!!.size)
            assertNotNull(channels[expectedChannel]!![0].meta)
        }

        // /v3/history-with-actions
        pubnub.fetchMessages(
            channels = listOf(expectedChannel),
            includeMeta = true,
            includeMessageActions = true
        ).sync()!!.run {
            assertEquals(1, channels[expectedChannel]!!.size)
            assertNotNull(channels[expectedChannel]!![0].meta)
        }

        // three responses from three different APIs will return a non-null meta field
    }

    @Test
    fun testCountFallback() {
        val expectedChannel = randomChannel()

        pubnub.history(
            channel = expectedChannel,
            includeMeta = true,
            count = -1
        ).await { _, status ->
            assertFalse(status.error)
            assertEquals("100", status.param("count"))
        }
    }

    @Test
    fun testFetchMessagesContainSpaceIdAndMessageType() {
        val channel = randomChannel()
        val expectedMessageType = MessageType("thisIsMessageType")
        val expectedSpaceId = SpaceId("thisIsSpace")

        val result = pubnub.publish(
            channel = channel,
            message = CommonUtils.generatePayload(),
            spaceId = expectedSpaceId,
            messageType = expectedMessageType
        ).sync()

        assertNotNull(result)

        var fetchResult: PNFetchMessagesResult? = null

        await
            .pollInterval(Duration.ofMillis(1_000))
            .pollDelay(Duration.ofMillis(1_000))
            .atMost(Duration.ofMillis(10_000))
            .untilAsserted {
                fetchResult = pubnub.fetchMessages(
                    includeSpaceId = true,
                    channels = listOf(channel)
                ).sync()
                assertNotNull(fetchResult)
                assertThat(fetchResult?.channels, aMapWithSize(not(0)))
            }
        val fetchMessageItem = fetchResult!!.channels.values.first().first()
        assertEquals(expectedMessageType, fetchMessageItem.messageType)
        assertEquals(expectedSpaceId, fetchMessageItem.spaceId)
    }
}
