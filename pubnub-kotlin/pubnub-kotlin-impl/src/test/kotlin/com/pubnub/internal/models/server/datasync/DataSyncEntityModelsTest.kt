package com.pubnub.internal.models.server.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.datasync.entity.DataSyncEntity
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

        val envelope: EntitiesEnvelope<DataSyncEntity> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<DataSyncEntity>>() {}.type,
        )

        assertEquals("TjQw", envelope.meta?.nextCursor)
        assertTrue(envelope.meta?.hasNext == true)
        assertEquals(20, envelope.meta?.limit)
    }

    @Test
    fun entitiesMeta_defaults_when_missing() {
        val json = """{ "status": 200, "data": [] }"""

        val envelope: EntitiesEnvelope<DataSyncEntity> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<DataSyncEntity>>() {}.type,
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

    @Test
    fun createEntityRequest_drops_null_optionals() {
        val json = mapper.toJson(
            CreateEntityRequest(CreateEntityRequestData(entityClass = "TestUser", entityClassVersion = 1)),
        )

        assertEquals("""{"data":{"entityClass":"TestUser","entityClassVersion":1}}""", json)
        assertFalse(json.contains("\"id\""))
        assertFalse(json.contains("\"entityClassLevel\""))
    }

    @Test
    fun createEntityRequest_emits_entityClassLevel_when_set() {
        // Regression guard: the create-only classLevel must reach the wire under the `entityClassLevel`
        // body field. Without the field on CreateEntityRequestData this assertion fails.
        val json = mapper.toJson(
            CreateEntityRequest(
                CreateEntityRequestData(
                    id = "e1",
                    entityClass = "TestUser",
                    entityClassVersion = 2,
                    entityClassLevel = "SubKey",
                ),
            ),
        )

        assertTrue(json.contains("\"id\":\"e1\""))
        assertTrue(json.contains("\"entityClass\":\"TestUser\""))
        assertTrue(json.contains("\"entityClassVersion\":2"))
        assertTrue(json.contains("\"entityClassLevel\":\"SubKey\""))
    }

    @Test
    fun dataSyncEntity_deserializes_entityClass_wire_keys_into_class_fields() {
        // Regression guard for the @field:SerializedName mappings: the server serializes the
        // class-identity fields under the `entityClass*` wire keys, which must land on the public
        // `class*` properties.
        val json = """
            {
              "id": "e1",
              "entityClass": "TestUser",
              "entityClassVersion": 3,
              "entityClassLevel": "SubKey",
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-02T00:00:00.000Z",
              "eTag": "etag123",
              "status": "active",
              "expiresAt": "2022-01-01T00:00:00.000Z",
              "payload": { "name": "alice" }
            }
        """.trimIndent()

        val entity: DataSyncEntity = mapper.fromJson(json, DataSyncEntity::class.java)

        assertEquals("e1", entity.id)
        assertEquals("TestUser", entity.className)
        assertEquals(3, entity.classVersion)
        assertEquals("SubKey", entity.classLevel)
        assertEquals("etag123", entity.eTag)
        assertEquals("alice", entity.payload?.get("name"))
    }
}
