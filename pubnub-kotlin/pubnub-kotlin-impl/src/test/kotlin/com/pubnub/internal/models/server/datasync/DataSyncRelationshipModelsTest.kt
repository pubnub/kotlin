package com.pubnub.internal.models.server.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import com.pubnub.internal.managers.MapperManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DataSyncRelationshipModelsTest {
    private val mapper = MapperManager(LogConfig("testPnInstanceId", "testUserId"))

    @Test
    fun createRelationshipRequest_drops_null_optionals() {
        val json = mapper.toJson(
            CreateRelationshipRequest(
                CreateRelationshipRequestData(
                    entityAId = "a1",
                    entityBId = "b1",
                    relationshipClass = "Friendship",
                    relationshipClassVersion = 1,
                ),
            ),
        )

        assertEquals(
            """{"data":{"entityAId":"a1","entityBId":"b1","relationshipClass":"Friendship","relationshipClassVersion":1}}""",
            json,
        )
        assertFalse(json.contains("\"id\""))
        assertFalse(json.contains("\"status\""))
        assertFalse(json.contains("\"payload\""))
        // Must NOT inherit the Membership body wire keys.
        assertFalse(json.contains("channelId"))
        assertFalse(json.contains("userId"))
    }

    @Test
    fun createRelationshipRequest_emits_all_fields_when_set() {
        val json = mapper.toJson(
            CreateRelationshipRequest(
                CreateRelationshipRequestData(
                    id = "r1",
                    entityAId = "a1",
                    entityBId = "b1",
                    relationshipClass = "Friendship",
                    relationshipClassVersion = 2,
                    status = "active",
                ),
            ),
        )

        assertTrue(json.contains("\"id\":\"r1\""))
        assertTrue(json.contains("\"entityAId\":\"a1\""))
        assertTrue(json.contains("\"entityBId\":\"b1\""))
        assertTrue(json.contains("\"relationshipClass\":\"Friendship\""))
        assertTrue(json.contains("\"relationshipClassVersion\":2"))
        assertTrue(json.contains("\"status\":\"active\""))
    }

    @Test
    fun setRelationshipRequest_emits_relationshipClassVersion_wire_key() {
        val json = mapper.toJson(
            SetRelationshipRequest(SetRelationshipRequestData(relationshipClassVersion = 3)),
        )

        assertEquals("""{"data":{"relationshipClassVersion":3}}""", json)
        // Must NOT reuse the entity wire key.
        assertFalse(json.contains("entityClassVersion"))
    }

    @Test
    fun dataSyncRelationship_deserializes_relationshipClass_wire_keys_into_class_fields() {
        // Regression guard for the @field:SerializedName mappings: the server serializes the
        // class-identity fields under the `relationshipClass*` wire keys, which must land on the public
        // `class*` properties.
        val json = """
            {
              "id": "r1",
              "entityAId": "a1",
              "entityBId": "b1",
              "relationshipClass": "Friendship",
              "relationshipClassVersion": 3,
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-02T00:00:00.000Z",
              "eTag": "etag123",
              "status": "active",
              "expiresAt": "2022-01-01T00:00:00.000Z",
              "payload": { "role": "admin" }
            }
        """.trimIndent()

        val relationship: PNDataSyncRelationship = mapper.fromJson(json, PNDataSyncRelationship::class.java)

        assertEquals("r1", relationship.id)
        assertEquals("a1", relationship.entityAId)
        assertEquals("b1", relationship.entityBId)
        assertEquals("Friendship", relationship.className)
        assertEquals(3, relationship.classVersion)
        assertEquals("2021-01-01T00:00:00.000Z", relationship.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", relationship.updatedAt)
        assertEquals("etag123", relationship.eTag)
        assertEquals("active", relationship.status)
        assertEquals("2022-01-01T00:00:00.000Z", relationship.expiresAt)
        assertEquals("admin", relationship.payload?.get("role"))
    }

    @Test
    fun dataSyncRelationship_deserializes_with_null_optionals() {
        val json = """
            {
              "id": "r2",
              "entityAId": "a2",
              "entityBId": "b2",
              "relationshipClass": "Friendship",
              "relationshipClassVersion": 1,
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-01T00:00:00.000Z",
              "eTag": "etag456",
              "expiresAt": "2022-01-01T00:00:00.000Z"
            }
        """.trimIndent()

        val relationship: PNDataSyncRelationship = mapper.fromJson(json, PNDataSyncRelationship::class.java)

        assertEquals("Friendship", relationship.className)
        assertEquals(1, relationship.classVersion)
        assertEquals("2022-01-01T00:00:00.000Z", relationship.expiresAt)
        assertNull(relationship.status)
        assertNull(relationship.payload)
    }
}
