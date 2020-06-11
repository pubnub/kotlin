package com.pubnub.api.integration

import com.pubnub.api.PubNubError
import com.pubnub.api.assertPnException
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.param
import com.pubnub.api.suite.await
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HistoryIntegrationTest : BaseIntegrationTest() {

    @Test
    fun testHistorySingleChannel() {
        val expectedChannelName = randomValue()
        val expectedMessageCount = 10

        assertEquals(
            expectedMessageCount,
            publishMixed(pubnub, expectedMessageCount, expectedChannelName).size
        )

        pubnub.history().apply {
            channel = expectedChannelName
        }.sync()!!.run {
            messages.forEach {
                assertNotNull(it.entry)
                assertNull(it.meta)
                assertNull(it.timetoken)
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

        pubnub.history().apply {
            channel = expectedChannelName
            includeTimetoken = true
        }.sync()!!.run {
            this.messages.forEach {
                assertNotNull(it.entry)
                assertNotNull(it.timetoken)
                assertNull(it.meta)
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

        pubnub.history().apply {
            channel = expectedChannelName
            includeMeta = true
        }.sync()!!.run {
            messages.forEach {
                assertNotNull(it.entry)
                assertNull(it.timetoken)
                assertNotNull(it.meta)
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

        pubnub.history().apply {
            channel = expectedChannelName
            includeMeta = true
            includeTimetoken = true
        }.sync()!!.run {
            messages.forEach {
                assertNotNull(it.entry)
                assertNotNull(it.timetoken)
                assertNotNull(it.meta)
            }
        }
    }

    @Test
    fun testFetchSingleChannel() {
        val expectedChannelName = randomValue()

        publishMixed(pubnub, 10, expectedChannelName)

        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannelName)
            maximumPerChannel = 25
        }.sync()!!.run {
            channels[expectedChannelName]!!.forEach {
                assertNotNull(it.message)
                assertNotNull(it.timetoken)
                assertNull(it.meta)
                assertNull(it.actions)
            }
        }
    }

    @Test
    fun testFetchSingleChannel_Meta() {
        val expectedChannelName = randomValue()
        publishMixed(pubnub, 10, expectedChannelName)

        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannelName)
            maximumPerChannel = 25
            includeMeta = true
        }.sync()!!.run {
            channels[expectedChannelName]!!.forEach {
                assertNotNull(it.message)
                assertNotNull(it.timetoken)
                assertNotNull(it.meta)
                assertNull(it.actions)
            }
        }
    }

    @Test
    fun testFetchSingleChannel_Actions() {
        val expectedChannelName = randomValue()
        val results = publishMixed(pubnub, 120, expectedChannelName)

        // todo check with executorservice

        pubnub.addMessageAction().apply {
            channel = expectedChannelName
            messageAction = (PNMessageAction(
                type = "reaction",
                value = emoji(),
                messageTimetoken = results[0].timetoken
            ))
        }.sync()!!

        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannelName)
            maximumPerChannel = 25
            includeMessageActions = true
            includeMeta = false
        }.sync()!!.run {
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

    @Test
    fun testFetchSingleChannel_ActionsMeta() {
        val expectedChannelName = randomValue()
        val results = publishMixed(pubnub, 2, expectedChannelName)

        pubnub.addMessageAction().apply {
            channel = expectedChannelName
            messageAction = PNMessageAction(
                type = "reaction",
                value = emoji(),
                messageTimetoken = results[0].timetoken
            )
        }.sync()!!

        wait()

        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannelName)
            maximumPerChannel = 25
            includeMessageActions = true
            includeMeta = true
        }.sync()!!.run {
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

    @Test
    fun testFetchMultiChannel() {
        val expectedChannelNames = generateSequence { randomValue() }
            .take(2).toList()
            .also {
                it.forEach { channel ->
                    publishMixed(pubnub, 10, channel)
                }
            }

        pubnub.fetchMessages().apply {
            channels = expectedChannelNames
            maximumPerChannel = 25
        }.sync()!!.run {
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

    @Test
    fun testFetchSingleChannel_NoLimit() {
        val expectedChannelName = randomValue()
        assertEquals(10, publishMixed(pubnub, 10, expectedChannelName).size)

        wait()

        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannelName)
        }.sync()!!.run {
            assertEquals(1, channels[expectedChannelName]!!.size)
            channels[expectedChannelName]!!.forEach {
                assertNotNull(it.message)
                assertNotNull(it.timetoken)
                assertNull(it.meta)
                assertNull(it.actions)
            }
        }
    }

    @Test
    fun testFetchSingleChannel_OverflowLimit() {
        val expectedChannelName = randomValue()

        assertEquals(10, publishMixed(pubnub, 10, expectedChannelName).size)

        wait()

        pubnub.fetchMessages().apply {
            channels = (listOf(expectedChannelName))
            maximumPerChannel = 100
        }.sync()!!.apply {
            assertEquals(10, channels[expectedChannelName]!!.size)
            channels[expectedChannelName]!!.forEach {
                assertNotNull(it.message)
                assertNotNull(it.timetoken)
                assertNull(it.meta)
                assertNull(it.actions)
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

        observer.history().apply {
            channel = expectedChannelName
            includeTimetoken = true
            includeMeta = true
        }.sync()!!.run {
            messages.forEach {
                assertNotNull(it.entry)
                assertNotNull(it.timetoken)
                assertNotNull(it.meta)
                assertTrue(it.entry.toString().contains("_msg"))
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

        observer.fetchMessages().apply {
            channels = listOf(expectedChannelName)
            includeMeta = true
        }.sync()!!.run {
            channels[expectedChannelName]!!.forEach {
                assertNotNull(it.message)
                assertNotNull(it.timetoken)
                assertNotNull(it.meta)
                assertNull(it.actions)
                assertTrue(it.message.toString().contains("_msg"))
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
                val reaction = pubnub.addMessageAction().apply {
                    channel = expectedChannelName
                    messageAction = PNMessageAction(
                        type = "reaction",
                        value = emoji(),
                        messageTimetoken = it.timetoken
                    )
                }.sync()!!
                messagesWithActions.add(reaction.messageTimetoken)
            }
        }

        observer.fetchMessages().apply {
            channels = listOf(expectedChannelName)
            includeMeta = true
            includeMessageActions = true
        }.sync()!!.run {
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

    @Test
    fun testFetchMultiChannel_WithMessageActions_Exception() {
        try {
            pubnub.fetchMessages().apply {
                channels = listOf(randomValue(), randomValue())
                includeMessageActions = true
            }.sync()!!
        } catch (e: Exception) {
            assertPnException(PubNubError.HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS, e)
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_Default() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
        }.await { _, status ->
            assertEquals("1", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_Low() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
            maximumPerChannel = -1
        }.await { _, status ->
            assertEquals("1", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_Valid() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
            maximumPerChannel = 15
        }.await { _, status ->
            assertEquals("15", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_NoActions_Limit_High() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
            maximumPerChannel = 100
        }.await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_Default() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
            includeMessageActions = true
        }.await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_Low() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
            includeMessageActions = true
            maximumPerChannel = -1
        }.await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_High() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
            includeMessageActions = true
            maximumPerChannel = 200
        }.await { _, status ->
            assertEquals("25", status.param("max"))
        }
    }

    @Test
    fun testFetchSingleChannel_WithActions_Limit_Valid() {
        pubnub.fetchMessages().apply {
            channels = listOf(randomValue())
            includeMessageActions = true
            maximumPerChannel = 15
        }.await { result, status ->
            assertEquals("15", status.param("max"))
        }
    }

    @Test
    fun testEmptyMeta() {
        val expectedChannel = randomValue()

        // publish a message without any metadata
        pubnub.publish().apply {
            message = randomValue()
            channel = expectedChannel
        }.sync()!!

        wait()

        // /v2/history
        pubnub.history().apply {
            channel = expectedChannel
            includeMeta = true
        }.sync()!!.run {
            assertEquals(1, messages.size)
            assertNotNull(messages[0].meta)
        }

        // /v3/history
        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannel)
            includeMeta = true
        }.sync()!!.run {
            assertEquals(1, channels[expectedChannel]!!.size)
            assertNotNull(channels[expectedChannel]!![0].meta)
        }

        // /v3/history-with-actions
        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannel)
            includeMeta = true
            includeMessageActions = true
        }.sync()!!.run {
            assertEquals(1, channels[expectedChannel]!!.size)
            assertNotNull(channels[expectedChannel]!![0].meta)
        }

        // three responses from three different APIs will return a non-null meta field
    }

}