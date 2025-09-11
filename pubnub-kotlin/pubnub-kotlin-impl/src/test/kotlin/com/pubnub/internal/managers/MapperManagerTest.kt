package com.pubnub.internal.managers

import com.pubnub.api.PubNubException
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class MapperManagerTest {
    val logConfig = LogConfig("testPnInstanceId", "testUserId")

    @Test
    @Throws(PubNubException::class)
    fun toJson_anonymousList() {
        val mapperManager = MapperManager(logConfig)
        val expected = "[1,2,3]"
        val anonList: List<Int> =
            object : ArrayList<Int>() {
                init {
                    add(1)
                    add(2)
                    add(3)
                }
            }
        val regularList: MutableList<Int> = ArrayList()
        regularList.add(1)
        regularList.add(2)
        regularList.add(3)
        val json1 = mapperManager.toJson(anonList)
        val json2 = mapperManager.toJson(regularList)
        assertEquals(expected, json1)
        assertEquals(expected, json2)
    }

    @Test
    @Throws(PubNubException::class)
    fun toJson_anonymousMap() {
        val mapperManager = MapperManager(logConfig)
        val expected = "{\"city\":\"Toronto\"}"
        val anonMap: HashMap<String, String> =
            object : HashMap<String, String>() {
                init {
                    put("city", "Toronto")
                }
            }
        val regularMap = HashMap<String, String>()
        regularMap["city"] = "Toronto"
        val json1 = mapperManager.toJson(anonMap)
        val json2 = mapperManager.toJson(regularMap)
        assertEquals(expected, json1)
        assertEquals(expected, json2)
    }

    @Test
    @Throws(PubNubException::class)
    fun toJson_anonymousSet() {
        val mapperManager = MapperManager(logConfig)
        val expected = "[1,2,3]"
        val anonSet: Set<Int> =
            object : HashSet<Int>() {
                init {
                    add(1)
                    add(2)
                    add(3)
                }
            }
        val regularSet: MutableSet<Int> = HashSet()
        regularSet.add(1)
        regularSet.add(2)
        regularSet.add(3)
        val json1 = mapperManager.toJson(anonSet)
        val json2 = mapperManager.toJson(regularSet)
        assertEquals(expected, json1)
        assertEquals(expected, json2)
    }

    @Test
    fun fromJson_numbers() {
        val mapperManager = MapperManager(logConfig)
        val map = mapOf(
            "a" to 12345678,
            "b" to 1.2313,
            "c" to 943809302493249023L,
            "d" to 1,
            "e" to 0,
            "f" to "943809302493249023"
        )
        val json = mapperManager.toJson(map)
        val decodedMap = mapperManager.fromJson<Map<String, Any?>>(json, Map::class.java)
        decodedMap.forEach { t, u ->
            println("$t: $u (${u!!::class}")
        }
        assertEquals(map["a"], decodedMap["a"].tryLong()?.toInt())
        assertEquals(map["b"], decodedMap["b"].tryDouble())
        assertEquals(map["c"], decodedMap["c"].tryLong())
        assertEquals(map["d"], decodedMap["d"].tryLong()?.toInt())
        assertEquals(map["e"], decodedMap["e"].tryLong()?.toInt())
        assertEquals(map["f"], decodedMap["f"])
        assertEquals(map["f"].toString().toLong(), decodedMap["f"].tryLong())
    }

    @Test
    fun fromJson_optionalsAndNulls() {
        val mapperManager = MapperManager(logConfig)
        val input = """
            { "id" : "myId", "name": null, "description": "myDescription", "eTag": "myEtag", "custom": { "a" : "b" } }
        """.trimIndent()

        val output: PNChannelMetadata = mapperManager.fromJson(input, PNChannelMetadata::class.java)

        assertEquals("myId", output.id)
        assertNotNull(output.name)
        assertNull(output.name?.value)

        assertEquals("myDescription", output.description?.value)

        assertNull(output.updated)
        assertNull(output.status)

        assertEquals(mapOf("a" to "b"), output.custom?.value)

        assertEquals("myEtag", output.eTag?.value)
    }
}

private fun Any?.tryLong(): Long? {
    return when (this) {
        is Number -> toLong()
        is String -> toLongOrNull()
        else -> null
    }
}

private fun Any?.tryDouble(): Double? {
    return when (this) {
        is Number -> toDouble()
        is String -> toDoubleOrNull()
        else -> null
    }
}
