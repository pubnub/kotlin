package com.pubnub.api.java.models.consumer.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.DataSyncEntity
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DataSyncEntityConverterTest {
    @Test
    fun from() {
        val entity = DataSyncEntity(
            id = randomString(),
            className = randomString(),
            classVersion = 3,
            classLevel = randomString(),
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
            status = randomString(),
            expiresAt = randomString(),
            payload = mapOf(randomString() to randomString()),
        )

        val actual = DataSyncEntityConverter.from(entity)

        assertEquals(entity.id, actual.id)
        assertEquals(entity.className, actual.className)
        assertEquals(entity.classVersion, actual.classVersion)
        assertEquals(entity.classLevel, actual.classLevel)
        assertEquals(entity.createdAt, actual.createdAt)
        assertEquals(entity.updatedAt, actual.updatedAt)
        assertEquals(entity.eTag, actual.eTag)
        assertEquals(entity.status, actual.status)
        assertEquals(entity.expiresAt, actual.expiresAt)
        assertEquals(entity.payload, actual.payload)
    }

    @Test
    fun fromNullableFields() {
        val entity = DataSyncEntity(
            id = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
        )

        val actual = DataSyncEntityConverter.from(entity)

        assertEquals(entity.id, actual.id)
        assertEquals(null, actual.classLevel)
        assertEquals(null, actual.status)
        assertEquals(null, actual.expiresAt)
        assertEquals(null, actual.payload)
    }

    private fun randomString() = RandomStringUtils.random(5, "abcdefgh")
}
