package com.pubnub.api.utils

import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.Type

class OptionalChangeDeserializerTest {
    private val gson = GsonBuilder().registerTypeAdapterFactory(OptionalChangeDeserializer.Factory())
        .registerTypeAdapter(TestPayload::class.java, TestPayloadInstanceCreator()).create()
    private val expected = "test"
    private val id = "id"

    class TestPayloadInstanceCreator : InstanceCreator<TestPayload> {
        override fun createInstance(type: Type?): TestPayload {
            return TestPayload(id = "N/A")
        }
    }

    data class TestPayload(
        val id: String,
        val f1: OptionalChange<String> = OptionalChange.Unchanged(),
        val f2: OptionalChange<String> = OptionalChange.Unchanged()
    )

    data class PayloadWrapper(
        val payload: TestPayload
    )

    @Test
    fun deserializedWrappedInOtherObject() {
        val jsonString = """{"payload": {"id": "$id", "f1":"$expected"}}"""
        val result = gson.fromJson(jsonString, PayloadWrapper::class.java)
        assertEquals(PayloadWrapper(TestPayload(id = id, f1 = OptionalChange.Changed(expected))), result)
    }

    @Test
    fun deserializeProperlyValueToOptionalChange() {
        val jsonString = """{"id": "$id", "f1":"$expected"}"""
        val result = gson.fromJson(jsonString, TestPayload::class.java)
        assertEquals(TestPayload(id = id, f1 = OptionalChange.Changed(expected)), result)
    }

    @Test
    fun deserializeProperlyNullToNone() {
        val jsonString = """{"id": "$id", "f1":null}"""
        val result = gson.fromJson(jsonString, TestPayload::class.java)
        assertEquals(TestPayload(id = id, f1 = OptionalChange.Removed()), result)
    }

    @Test
    fun deserializeProperlyLackOfFieldToNoChange() {
        val jsonString = """{"id": "$id"}"""
        val result = gson.fromJson(jsonString, TestPayload::class.java)
        assertEquals(TestPayload(id = id, f1 = OptionalChange.Unchanged()), result)
    }
}
