package com.pubnub.internal.models.server.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.datasync.user.PNUser
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
    fun usersEnvelope_maps_snake_case_meta_cursor() {
        val json = """
            {
              "status": 200,
              "data": [],
              "meta": { "next_cursor": "TjQw", "has_next": true, "limit": 20 }
            }
        """.trimIndent()

        val envelope: EntitiesEnvelope<PNUser> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<PNUser>>() {}.type,
        )

        assertEquals("TjQw", envelope.meta?.nextCursor)
        assertTrue(envelope.meta?.hasNext == true)
        assertEquals(20, envelope.meta?.limit)
    }

    @Test
    fun usersMeta_defaults_when_missing() {
        val json = """{ "status": 200, "data": [] }"""

        val envelope: EntitiesEnvelope<PNUser> = mapper.fromJson(
            json,
            object : com.google.gson.reflect.TypeToken<EntitiesEnvelope<PNUser>>() {}.type,
        )

        assertNull(envelope.meta)
    }
}
