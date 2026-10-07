package com.pubnub.api.models.consumer.datasync.relationship

import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataSyncRelationshipTest {
    @Test
    fun holds_all_fields() {
        val id = randomString()
        val entityAId = randomString()
        val entityBId = randomString()
        val eTag = randomString()
        val relationship = PNDataSyncRelationship(
            id = id,
            entityAId = entityAId,
            entityBId = entityBId,
            className = "Friendship",
            classVersion = 2,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-02T00:00:00.000Z",
            eTag = eTag,
            status = "active",
            expiresAt = "2022-01-01T00:00:00.000Z",
            payload = mapOf("role" to "admin"),
        )

        assertEquals(id, relationship.id)
        assertEquals(entityAId, relationship.entityAId)
        assertEquals(entityBId, relationship.entityBId)
        assertEquals("Friendship", relationship.className)
        assertEquals(2, relationship.classVersion)
        assertEquals("2021-01-01T00:00:00.000Z", relationship.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", relationship.updatedAt)
        assertEquals(eTag, relationship.eTag)
        assertEquals("active", relationship.status)
        assertEquals("2022-01-01T00:00:00.000Z", relationship.expiresAt)
        assertEquals(mapOf("role" to "admin"), relationship.payload)
    }

    @Test
    fun optional_fields_default_to_null() {
        val relationship = PNDataSyncRelationship(
            id = randomString(),
            entityAId = randomString(),
            entityBId = randomString(),
            className = "Friendship",
            classVersion = 1,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-01T00:00:00.000Z",
            eTag = randomString(),
            expiresAt = "2022-01-01T00:00:00.000Z",
        )

        assertNull(relationship.status)
        assertNull(relationship.payload)
    }
}
