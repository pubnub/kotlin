package com.pubnub.test.integration

import com.pubnub.api.JsonElement
import com.pubnub.api.asDouble
import com.pubnub.api.asList
import com.pubnub.api.asLong
import com.pubnub.api.asMap
import com.pubnub.api.asString
import com.pubnub.api.isNull
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.kmp.PLATFORM
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.test
import kotlinx.coroutines.test.runTest
import kotlin.js.JsExport
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@JsExport
data class ABC(
    val aaa: Boolean = false,
    val bbb: Int = 123,
    val ccc: String = "platform: $PLATFORM"
)

class PublishTest : BaseIntegrationTest() {
    private val channel = "myChannel"

    @Test
    fun can_publish_message_string() =
        runTest {
            val result = pubnub.publish(channel, "some message").await()
            assertTrue { result.timetoken > 0 }
        }

    @Test
    fun can_signal() =
        runTest {
            val result = pubnub.signal(channel, "some message").await()
            assertTrue { result.timetoken > 0 }
        }

    @Test
    fun can_publish_message_map() =
        runTest {
            val result = pubnub.publish(channel, mapOf("platform" to PLATFORM, "otherKey" to 123, "another" to true)).await()
            assertTrue { result.timetoken > 0 }
        }

    @Test
    fun can_signal_map() =
        runTest {
            val result = pubnub.signal(channel, mapOf("platform" to PLATFORM, "otherKey" to 123, "another" to true)).await()
            assertTrue { result.timetoken > 0 }
        }

    @Test
    @Ignore // only JVM supports custom classes
    fun can_publish_message_object() =
        runTest {
            val result = pubnub.publish(channel, ABC()).await()
            assertTrue { result.timetoken > 0 }
        }

    @Test
    fun can_receive_message_with_map_metadata() = runTest {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            val mapData = mapOf(
                "stringValue" to "bbb",
                "mapValue" to mapOf("innerKey" to false),
                "booleanValue" to true,
                "numberValue" to 124,
                "float" to 1.3,
                "floatThatCanBeBool" to 1.0,
                "longValue" to 1000L,
                "listValue" to listOf(123, "aaa", mapOf("innerK" to "innerV"))
            )
            pubnub.publish(channel, "some message", mapData).await()
            val result = nextMessage()
            assertEquals("some message", result.message.asString())
            deepCompare(mapData, result.userMetadata!!)
        }
    }

    private fun isIOS() = PLATFORM == "iOS"

    @Test
    fun can_receive_message_with_primitive_payload() = runTest {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            pubnub.publish(
                channel,
                111,
            ).await()
            val result = nextMessage().message
            assertEquals(111, result.asLong())

//            pubnub.publish( // TODO JS doesn't support this?
//                channel,
//                false,
//            ).await()
//            val result2 = nextMessage().message
//            assertEquals(false, result2.asBoolean())
        }
    }

    @Test
    fun can_receive_message_with_map_payload() = runTest {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            val mapData = mapOf(
                "stringValue" to "bbb",
                "mapValue" to mapOf("innerKey" to false),
                "booleanValue" to true,
                "numberValue" to 124,
                "longValue" to 1000L,
                "listValue" to listOf(123, "aaa", mapOf("innerK" to "innerV"))
            )
            pubnub.publish(
                channel,
                mapData,
            ).await()
            val result = nextMessage().message
            deepCompare(mapData, result)
        }
    }

    @Test
    fun can_receive_signal_with_map_payload() = runTest {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            val mapData = mapOf(
                "stringValue" to "bbb",
                "mapValue" to mapOf("innerKey" to false)
            )
            pubnub.signal(
                channel,
                mapData,
            ).await()
            val result = nextEvent<PNSignalResult>().message
            deepCompare(mapData, result)
        }
    }

    @Test
    fun can_receive_message_with_payload_with_floats() = runTest {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            val mapData = mapOf(
                "floatValue" to 1.23f,
                "doubleValue" to 1.23,
                "otherValue" to 1.0f,
                "otherValue1" to 1.0,
                "otherValue3" to 0.0,
                "otherValue4" to 0.0f,
                "otherValue5" to 2.0,
            )
            pubnub.publish(
                channel,
                mapData,
            ).await()
            val result = nextMessage().message
            deepCompare(mapData, result)
        }
    }

    @Test
    @Ignore // todo js doesn't support primitive metadata?
    fun can_receive_message_with_primitive_metadata() = runTest {
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            pubnub.publish(channel, "some message", "some meta").await()
            val result = nextMessage()
            assertEquals("some message", result.message.asString())
            assertEquals("some meta", result.userMetadata?.asString())

            pubnub.publish(channel, "some message", 123).await()
            val result2 = nextMessage()
            assertEquals("some message", result2.message.asString())
            assertEquals("123", result2.userMetadata?.asString())
        }
    }
}

private fun deepCompare(expected: Any?, actual: JsonElement) {
//    println("e: " + expected.toString() + " " + expected!!::class)
//    println("a: " + actual.toString())
    if (expected is Map<*, *>) {
        val actualMap = actual.asMap()!!
//        println(actualMap)
        expected.forEach {
//            println(it.key.toString() + " " + actualMap[it.key])
            deepCompare(it.value, actualMap[it.key]!!)
        }
    } else if (expected is List<*>) {
        val actualList = actual.asList()!!
        expected.forEachIndexed { index: Int, value: Any? ->
            deepCompare(value, actualList[index])
        }
    } else if (expected is Double) { // Double must be first, then Long for JS, as all number types except Long are the same in JS
        assertEquals(expected, actual.asDouble()!!, 0.01)
    } else if (expected is Long) {
        assertEquals(expected, actual.asLong()!!)
    } else if (expected is Int) {
        assertEquals(expected.toLong(), actual.asLong()!!)
    } else if (expected is Float) {
        assertEquals(expected.toDouble(), actual.asDouble()!!, 0.01)
    } else if (expected is String) {
        assertEquals(expected, actual.asString()!!)
    } else if (expected == null) {
        assertTrue { actual.isNull() }
    }
}
