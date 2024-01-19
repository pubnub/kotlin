package com.pubnub.internal.managers

import com.pubnub.api.PubNubException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MapperManagerTest {
    @Test
    @Throws(PubNubException::class)
    fun toJson_anonymousList() {
        val mapperManager = MapperManager()
        val expected = "[1,2,3]"
        val anonList: List<Int> = object : ArrayList<Int>() {
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
        val mapperManager = MapperManager()
        val expected = "{\"city\":\"Toronto\"}"
        val anonMap: HashMap<String, String> = object : HashMap<String, String>() {
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
        val mapperManager = MapperManager()
        val expected = "[1,2,3]"
        val anonSet: Set<Int> = object : HashSet<Int>() {
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
}
