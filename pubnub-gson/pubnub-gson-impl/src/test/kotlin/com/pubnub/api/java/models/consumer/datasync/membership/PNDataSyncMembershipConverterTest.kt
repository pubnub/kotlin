package com.pubnub.api.java.models.consumer.datasync.membership

import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNDataSyncMembershipConverterTest {
    @Test
    fun from() {
        val membership = PNDataSyncMembership(
            id = randomString(),
            channelId = randomString(),
            userId = randomString(),
            className = randomString(),
            classVersion = 3,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
            status = randomString(),
            expiresAt = randomString(),
            payload = mapOf(randomString() to randomString()),
        )

        val actual = PNDataSyncMembershipConverter.from(membership)

        assertEquals(membership.id, actual.id)
        assertEquals(membership.channelId, actual.channelId)
        assertEquals(membership.userId, actual.userId)
        assertEquals(membership.className, actual.className)
        assertEquals(membership.classVersion, actual.classVersion)
        assertEquals(membership.createdAt, actual.createdAt)
        assertEquals(membership.updatedAt, actual.updatedAt)
        assertEquals(membership.eTag, actual.eTag)
        assertEquals(membership.status, actual.status)
        assertEquals(membership.expiresAt, actual.expiresAt)
        assertEquals(membership.payload, actual.payload)
    }

    @Test
    fun fromNullableFields() {
        val membership = PNDataSyncMembership(
            id = randomString(),
            channelId = randomString(),
            userId = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = randomString(),
            updatedAt = randomString(),
            eTag = randomString(),
            expiresAt = randomString(),
        )

        val actual = PNDataSyncMembershipConverter.from(membership)

        assertEquals(membership.id, actual.id)
        assertEquals(membership.channelId, actual.channelId)
        assertEquals(membership.userId, actual.userId)
        assertEquals(membership.expiresAt, actual.expiresAt)
        assertEquals(null, actual.status)
        assertEquals(null, actual.payload)
    }

    private fun randomString() = RandomStringUtils.random(5, "abcdefgh")
}
