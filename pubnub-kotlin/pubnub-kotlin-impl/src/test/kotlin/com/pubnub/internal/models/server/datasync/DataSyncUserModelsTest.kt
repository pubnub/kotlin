package com.pubnub.internal.models.server.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser
import com.pubnub.internal.managers.MapperManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DataSyncUserModelsTest {
    private val mapper = MapperManager(LogConfig("testPnInstanceId", "testUserId"))

    @Test
    fun createUserRequest_drops_null_entityClass() {
        val json = mapper.toJson(
            CreateUserRequest(CreateUserRequestData(entityClassVersion = 1)),
        )

        assertEquals("""{"data":{"entityClassVersion":1}}""", json)
        assertFalse(json.contains("\"entityClass\""))
        assertFalse(json.contains("\"id\""))
        assertFalse(json.contains("\"entityClassLevel\""))
    }

    @Test
    fun createUserRequest_keeps_entityClass_when_set() {
        val json = mapper.toJson(
            CreateUserRequest(
                CreateUserRequestData(id = "u1", entityClass = "User.Admin", entityClassVersion = 2),
            ),
        )

        assertTrue(json.contains("\"id\":\"u1\""))
        assertTrue(json.contains("\"entityClass\":\"User.Admin\""))
        assertTrue(json.contains("\"entityClassVersion\":2"))
    }

    @Test
    fun createUserRequest_emits_entityClassLevel_when_set() {
        // Regression guard: the create-only classLevel must reach the wire under the `entityClassLevel`
        // body field. Without the field on CreateUserRequestData this assertion fails.
        val json = mapper.toJson(
            CreateUserRequest(
                CreateUserRequestData(
                    id = "u1",
                    entityClass = "User.Admin",
                    entityClassVersion = 2,
                    entityClassLevel = "SubKey",
                ),
            ),
        )

        assertTrue(json.contains("\"entityClassLevel\":\"SubKey\""))
    }

    @Test
    fun usersEnvelope_maps_snake_case_meta_cursor() {
        val json = """
            {
              "status": 200,
              "data": [],
              "meta": { "next_cursor": "TjQw", "has_next": true, "limit": 20 }
            }
        """.trimIndent()

        val envelope: EntitiesEnvelope<PNDataSyncUser> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<PNDataSyncUser>>() {}.type,
        )

        assertEquals("TjQw", envelope.meta?.nextCursor)
        assertTrue(envelope.meta?.hasNext == true)
        assertEquals(20, envelope.meta?.limit)
    }

    @Test
    fun usersMeta_defaults_when_missing() {
        val json = """{ "status": 200, "data": [] }"""

        val envelope: EntitiesEnvelope<PNDataSyncUser> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<PNDataSyncUser>>() {}.type,
        )

        assertNull(envelope.meta)
    }

    @Test
    fun dataSyncUser_deserializes_entityClass_wire_keys_into_class_fields() {
        // Regression guard for the @field:SerializedName mappings: the server serializes the
        // class-identity fields under the `entityClass*` wire keys, which must land on the public
        // `class*` properties.
        val json = """
            {
              "id": "u1",
              "entityClass": "User",
              "entityClassVersion": 3,
              "entityClassLevel": "Global",
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-02T00:00:00.000Z",
              "eTag": "etag123",
              "expiresAt": "2022-01-01T00:00:00.000Z",
              "payload": { "name": "alice" }
            }
        """.trimIndent()

        val user: PNDataSyncUser = mapper.fromJson(json, PNDataSyncUser::class.java)

        assertEquals("u1", user.id)
        assertEquals("User", user.className)
        assertEquals(3, user.classVersion)
        assertEquals("Global", user.classLevel)
        assertEquals("2022-01-01T00:00:00.000Z", user.expiresAt)
        assertEquals("alice", user.payload?.get("name"))
    }
}
