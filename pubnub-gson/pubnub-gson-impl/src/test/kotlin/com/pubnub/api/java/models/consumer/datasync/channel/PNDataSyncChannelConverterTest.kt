package com.pubnub.api.java.models.consumer.datasync.channel

import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncChannel
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNDataSyncChannelConverterTest {
    @Test
    fun from() {
        val channel = PNDataSyncChannel(
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

        val actual = PNDataSyncChannelConverter.from(channel)

        assertEquals(channel.id, actual.id)
        assertEquals(channel.className, actual.className)
        assertEquals(channel.classVersion, actual.classVersion)
        assertEquals(channel.classLevel, actual.classLevel)
        assertEquals(channel.createdAt, actual.createdAt)
        assertEquals(channel.updatedAt, actual.updatedAt)
        assertEquals(channel.eTag, actual.eTag)
        assertEquals(channel.status, actual.status)
        assertEquals(channel.expiresAt, actual.expiresAt)
        assertEquals(channel.payload, actual.payload)
    }

    @Test
    fun fromNullableFields() {
        val channel = PNDataSyncChannel(
            id = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
        )

        val actual = PNDataSyncChannelConverter.from(channel)

        assertEquals(channel.id, actual.id)
        assertEquals(null, actual.classLevel)
        assertEquals(null, actual.status)
        assertEquals(null, actual.expiresAt)
        assertEquals(null, actual.payload)
    }

    private fun randomString() = RandomStringUtils.random(5, "abcdefgh")
}
