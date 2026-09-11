package com.pubnub.api.java.models.consumer.datasync.relationship

import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNDataSyncRelationshipConverterTest {
    @Test
    fun from() {
        val relationship = PNDataSyncRelationship(
            id = randomString(),
            entityAId = randomString(),
            entityBId = randomString(),
            className = randomString(),
            classVersion = 3,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
            expiresAt = randomString(),
            status = randomString(),
            payload = mapOf(randomString() to randomString()),
        )

        val actual = PNDataSyncRelationshipConverter.from(relationship)

        assertEquals(relationship.id, actual.id)
        assertEquals(relationship.entityAId, actual.entityAId)
        assertEquals(relationship.entityBId, actual.entityBId)
        assertEquals(relationship.className, actual.className)
        assertEquals(relationship.classVersion, actual.classVersion)
        assertEquals(relationship.createdAt, actual.createdAt)
        assertEquals(relationship.updatedAt, actual.updatedAt)
        assertEquals(relationship.eTag, actual.eTag)
        assertEquals(relationship.status, actual.status)
        assertEquals(relationship.expiresAt, actual.expiresAt)
        assertEquals(relationship.payload, actual.payload)
    }

    @Test
    fun fromNullableFields() {
        val relationship = PNDataSyncRelationship(
            id = randomString(),
            entityAId = randomString(),
            entityBId = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
            expiresAt = randomString(),
        )

        val actual = PNDataSyncRelationshipConverter.from(relationship)

        assertEquals(relationship.id, actual.id)
        assertEquals(relationship.entityAId, actual.entityAId)
        assertEquals(relationship.entityBId, actual.entityBId)
        assertEquals(relationship.expiresAt, actual.expiresAt)
        assertEquals(null, actual.status)
        assertEquals(null, actual.payload)
    }

    private fun randomString() = RandomStringUtils.random(5, "abcdefgh")
}
