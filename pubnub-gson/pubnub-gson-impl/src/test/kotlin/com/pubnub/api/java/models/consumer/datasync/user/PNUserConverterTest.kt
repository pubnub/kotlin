package com.pubnub.api.java.models.consumer.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNUser
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNUserConverterTest {
    @Test
    fun from() {
        val user = PNUser(
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

        val actual = PNUserConverter.from(user)

        assertEquals(user.id, actual.id)
        assertEquals(user.entityClass, actual.entityClass)
        assertEquals(user.entityClassVersion, actual.entityClassVersion)
        assertEquals(user.entityClassLevel, actual.entityClassLevel)
        assertEquals(user.createdAt, actual.createdAt)
        assertEquals(user.updatedAt, actual.updatedAt)
        assertEquals(user.eTag, actual.eTag)
        assertEquals(user.status, actual.status)
        assertEquals(user.expiresAt, actual.expiresAt)
        assertEquals(user.payload, actual.payload)
    }

    @Test
    fun fromNullableFields() {
        val user = PNUser(
            id = randomString(),
            entityClass = randomString(),
            entityClassVersion = 1,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
        )

        val actual = PNUserConverter.from(user)

        assertEquals(user.id, actual.id)
        assertEquals(null, actual.entityClassLevel)
        assertEquals(null, actual.status)
        assertEquals(null, actual.expiresAt)
        assertEquals(null, actual.payload)
    }

    private fun randomString() = RandomStringUtils.random(5, "abcdefgh")
}
