package com.pubnub.api.java.models.consumer.datasync.entity

import com.pubnub.api.models.consumer.datasync.entity.PNEntity
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNEntityConverterTest {
    @Test
    fun from() {
        val entity = PNEntity(
            id = randomString(),
            entityClass = randomString(),
            entityClassVersion = 3,
            entityClassLevel = randomString(),
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
            status = randomString(),
            expiresAt = randomString(),
            payload = mapOf(randomString() to randomString()),
        )

        val actual = PNEntityConverter.from(entity)

        assertEquals(entity.id, actual.id)
        assertEquals(entity.entityClass, actual.entityClass)
        assertEquals(entity.entityClassVersion, actual.entityClassVersion)
        assertEquals(entity.entityClassLevel, actual.entityClassLevel)
        assertEquals(entity.createdAt, actual.createdAt)
        assertEquals(entity.updatedAt, actual.updatedAt)
        assertEquals(entity.eTag, actual.eTag)
        assertEquals(entity.status, actual.status)
        assertEquals(entity.expiresAt, actual.expiresAt)
        assertEquals(entity.payload, actual.payload)
    }

    @Test
    fun fromNullableFields() {
        val entity = PNEntity(
            id = randomString(),
            entityClass = randomString(),
            entityClassVersion = 1,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
        )

        val actual = PNEntityConverter.from(entity)

        assertEquals(entity.id, actual.id)
        assertEquals(null, actual.entityClassLevel)
        assertEquals(null, actual.status)
        assertEquals(null, actual.expiresAt)
        assertEquals(null, actual.payload)
    }

    private fun randomString() = RandomStringUtils.random(5, "abcdefgh")
}
