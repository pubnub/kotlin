package com.pubnub.api.utils

import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.pubnub.api.models.consumer.objects.channel.OptionalChange
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataChange
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataChangeInstanceCreator
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.Type

class OptionalChangeDeserializerTest {
    private val gson = GsonBuilder()
        .registerTypeAdapterFactory(OptionalChangeDeserializer.Factory())
        .registerTypeAdapter(PNUUIDMetadataChange::class.java, PNUUIDMetadataChangeInstanceCreator())
        .registerTypeAdapter(TestPayload::class.java, TestPayloadInstanceCreator())
        .create()
    private val expected = "test"

    class TestPayloadInstanceCreator : InstanceCreator<TestPayload> {
        override fun createInstance(type: Type?): TestPayload {
            return TestPayload(id = "N/A")
        }
    }

    data class TestPayload(
        val id: String,
        val f1: OptionalChange<String> = OptionalChange.NoChange(),
        val f2: OptionalChange<String> = OptionalChange.NoChange()
    )

    data class PayloadWrapper(
        val payload: TestPayload
    )

    @Test
    fun zzzz() {
        val jsonString = """{"payload": {"id":"id", "f1":"$expected"}}"""
        val result = gson.fromJson(jsonString, PayloadWrapper::class.java)
        assertEquals(PayloadWrapper(TestPayload("id", OptionalChange.Some(expected))), result)
    }

    @Test
    fun aaaa() {
        val jsonString = """{"id": "id", "f1":"$expected"}"""
        val result = gson.fromJson(jsonString, TestPayload::class.java)
        assertEquals(TestPayload("id", OptionalChange.Some(expected)), result)
    }

    @Test
    fun bbbb() {
        val jsonString = """{"id": "id", "f1":null}"""
        val result = gson.fromJson(jsonString, TestPayload::class.java)
        assertEquals(TestPayload("id", OptionalChange.None()), result)
    }

    @Test
    fun cccc() {
        val jsonString = """{"id":"id"}"""
        val result = gson.fromJson(jsonString, TestPayload::class.java)
        assertEquals(TestPayload("id", OptionalChange.NoChange()), result)
    }

    val json = """{"source":"objects","version":"2.0","event":"set","type":"uuid","data":{"eTag":"AYCbwfeEyID5oAE","id":"client-7bbb3c7a-da87-4709-bee8-9ff7ce1dc1d2","name":"C3355C2DA600448","updated":"2022-06-06T09:49:59.562741Z"}}"""

    val smallerJson = """{"eTag":"AYCbwfeEyID5oAE","name":"C3355C2DA600448","updated":"2022-06-06T09:49:59.562741Z"}"""

    @Test
    fun dddd() {
        val result = gson.fromJson(json, PNSetUUIDMetadataEventMessage::class.java)
        println(result)
    }

    @Test
    fun eeee() {
        val result = gson.fromJson(smallerJson, PNUUIDMetadataChange::class.java)
        println(result)
    }
}
