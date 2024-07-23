package com.pubnub.kmp

import com.pubnub.api.asList
import com.pubnub.api.asString
import com.pubnub.api.createJsonElement
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonElementTest {

    @Test
    fun asList() {
        val list = listOf("abc", "def")
        val jsonList = createJsonElement(list.map { createJsonElement(it) })

        assertEquals(list, jsonList.asList()?.map { it.asString() })
    }
}