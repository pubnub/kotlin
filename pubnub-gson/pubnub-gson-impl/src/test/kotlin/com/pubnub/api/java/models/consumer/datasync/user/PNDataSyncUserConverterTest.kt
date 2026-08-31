package com.pubnub.api.java.models.consumer.datasync.user

import com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNDataSyncUserConverterTest {
    @Test
    fun from() {
        val user = PNDataSyncUser(
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

        val actual = PNDataSyncUserConverter.from(user)

        assertEquals(user.id, actual.id)
        assertEquals(user.className, actual.className)
        assertEquals(user.classVersion, actual.classVersion)
        assertEquals(user.classLevel, actual.classLevel)
        assertEquals(user.createdAt, actual.createdAt)
        assertEquals(user.updatedAt, actual.updatedAt)
        assertEquals(user.eTag, actual.eTag)
        assertEquals(user.status, actual.status)
        assertEquals(user.expiresAt, actual.expiresAt)
        assertEquals(user.payload, actual.payload)
    }

    @Test
    fun fromNullableFields() {
        val user = PNDataSyncUser(
            id = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
        )

        val actual = PNDataSyncUserConverter.from(user)

        assertEquals(user.id, actual.id)
        assertEquals(null, actual.classLevel)
        assertEquals(null, actual.status)
        assertEquals(null, actual.expiresAt)
        assertEquals(null, actual.payload)
    }

    private fun randomString() = RandomStringUtils.random(5, "abcdefgh")
}
