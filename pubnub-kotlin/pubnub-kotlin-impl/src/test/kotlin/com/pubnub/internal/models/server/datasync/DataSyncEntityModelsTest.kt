package com.pubnub.internal.models.server.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.datasync.entity.PNEntity
import com.pubnub.internal.managers.MapperManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DataSyncEntityModelsTest {
    private val mapper = MapperManager(LogConfig("testPnInstanceId", "testUserId"))

    @Test
    fun entitiesEnvelope_maps_snake_case_meta_cursor() {
        val json = """
            {
              "status": 200,
              "data": [],
              "meta": { "next_cursor": "TjQw", "has_next": true, "limit": 20 }
            }
        """.trimIndent()

        val envelope: EntitiesEnvelope<PNEntity> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<PNEntity>>() {}.type,
        )

        assertEquals("TjQw", envelope.meta?.nextCursor)
        assertTrue(envelope.meta?.hasNext == true)
        assertEquals(20, envelope.meta?.limit)
    }

    @Test
    fun entitiesMeta_defaults_when_missing() {
        val json = """{ "status": 200, "data": [] }"""

        val envelope: EntitiesEnvelope<PNEntity> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<PNEntity>>() {}.type,
        )

        assertNull(envelope.meta)
    }

    @Test
    fun jsonPatchOperation_drops_null_value_and_from() {
        val json = mapper.toJson(
            listOf(JsonPatchOperation(op = "replace", path = "/status")),
        )

        assertEquals("""[{"op":"replace","path":"/status"}]""", json)
        assertFalse(json.contains("value"))
        assertFalse(json.contains("from"))
    }

    @Test
    fun jsonPatchOperation_keeps_value_and_from_when_set() {
        val json = mapper.toJson(
            listOf(
                JsonPatchOperation(op = "replace", path = "/status", value = "active"),
                JsonPatchOperation(op = "move", path = "/b", from = "/a"),
            ),
        )

        assertTrue(json.contains("\"value\":\"active\""))
        assertTrue(json.contains("\"from\":\"/a\""))
    }

    @Test
    fun setEntityRequest_drops_null_status_and_payload() {
        val json = mapper.toJson(SetEntityRequest(SetEntityRequestData(entityClassVersion = 2)))

        assertEquals("""{"data":{"entityClassVersion":2}}""", json)
    }
}
