package com.pubnub.internal.models.server.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership
import com.pubnub.internal.managers.MapperManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DataSyncMembershipModelsTest {
    private val mapper = MapperManager(LogConfig("testPnInstanceId", "testUserId"))

    @Test
    fun createMembershipRequest_drops_null_optionals() {
        val json = mapper.toJson(
            CreateMembershipRequest(
                CreateMembershipRequestData(
                    channelId = "c1",
                    userId = "u1",
                    relationshipClassVersion = 1,
                ),
            ),
        )

        assertEquals(
            """{"data":{"channelId":"c1","userId":"u1","relationshipClassVersion":1}}""",
            json,
        )
        assertFalse(json.contains("\"id\""))
        // There is no relationshipClass field on create — the server implies the built-in Membership class.
        assertFalse(json.contains("\"relationshipClass\""))
        assertFalse(json.contains("\"status\""))
        assertFalse(json.contains("\"payload\""))
    }

    @Test
    fun createMembershipRequest_emits_all_fields_when_set() {
        val json = mapper.toJson(
            CreateMembershipRequest(
                CreateMembershipRequestData(
                    id = "m1",
                    channelId = "c1",
                    userId = "u1",
                    relationshipClassVersion = 2,
                    status = "active",
                ),
            ),
        )

        assertTrue(json.contains("\"id\":\"m1\""))
        assertTrue(json.contains("\"channelId\":\"c1\""))
        assertTrue(json.contains("\"userId\":\"u1\""))
        assertTrue(json.contains("\"relationshipClassVersion\":2"))
        assertTrue(json.contains("\"status\":\"active\""))
    }

    @Test
    fun setMembershipRequest_emits_relationshipClassVersion_wire_key() {
        val json = mapper.toJson(
            SetMembershipRequest(SetMembershipRequestData(relationshipClassVersion = 3)),
        )

        assertEquals("""{"data":{"relationshipClassVersion":3}}""", json)
        // Must NOT reuse the entity wire key.
        assertFalse(json.contains("entityClassVersion"))
    }

    @Test
    fun dataSyncMembership_deserializes_relationshipClass_wire_keys_into_class_fields() {
        // Regression guard for the @field:SerializedName mappings: the server serializes the
        // class-identity fields under the `relationshipClass*` wire keys, which must land on the public
        // `class*` properties. `channelId` / `userId` are re-exposed relationship endpoints.
        val json = """
            {
              "id": "m1",
              "channelId": "c1",
              "userId": "u1",
              "relationshipClass": "Membership",
              "relationshipClassVersion": 3,
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-02T00:00:00.000Z",
              "eTag": "etag123",
              "status": "active",
              "expiresAt": "2022-01-01T00:00:00.000Z",
              "payload": { "role": "admin" }
            }
        """.trimIndent()

        val membership: PNDataSyncMembership = mapper.fromJson(json, PNDataSyncMembership::class.java)

        assertEquals("m1", membership.id)
        assertEquals("c1", membership.channelId)
        assertEquals("u1", membership.userId)
        assertEquals("Membership", membership.className)
        assertEquals(3, membership.classVersion)
        assertEquals("2021-01-01T00:00:00.000Z", membership.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", membership.updatedAt)
        assertEquals("etag123", membership.eTag)
        assertEquals("active", membership.status)
        assertEquals("2022-01-01T00:00:00.000Z", membership.expiresAt)
        assertEquals("admin", membership.payload?.get("role"))
    }

    @Test
    fun dataSyncMembership_deserializes_with_null_optionals() {
        val json = """
            {
              "id": "m2",
              "channelId": "c2",
              "userId": "u2",
              "relationshipClass": "Membership",
              "relationshipClassVersion": 1,
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-01T00:00:00.000Z",
              "eTag": "etag456",
              "expiresAt": "2022-01-01T00:00:00.000Z"
            }
        """.trimIndent()

        val membership: PNDataSyncMembership = mapper.fromJson(json, PNDataSyncMembership::class.java)

        assertEquals("Membership", membership.className)
        assertEquals(1, membership.classVersion)
        assertEquals("2022-01-01T00:00:00.000Z", membership.expiresAt)
        assertNull(membership.status)
        assertNull(membership.payload)
    }
}
