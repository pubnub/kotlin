package com.pubnub.api.models.consumer.datasync.membership

import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataSyncMembershipTest {
    @Test
    fun holds_all_fields() {
        val id = randomString()
        val channelId = randomString()
        val userId = randomString()
        val eTag = randomString()
        val membership = PNDataSyncMembership(
            id = id,
            channelId = channelId,
            userId = userId,
            className = "Membership",
            classVersion = 2,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-02T00:00:00.000Z",
            eTag = eTag,
            status = "active",
            expiresAt = "2022-01-01T00:00:00.000Z",
            payload = mapOf("role" to "admin"),
        )

        assertEquals(id, membership.id)
        assertEquals(channelId, membership.channelId)
        assertEquals(userId, membership.userId)
        assertEquals("Membership", membership.className)
        assertEquals(2, membership.classVersion)
        assertEquals("2021-01-01T00:00:00.000Z", membership.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", membership.updatedAt)
        assertEquals(eTag, membership.eTag)
        assertEquals("active", membership.status)
        assertEquals("2022-01-01T00:00:00.000Z", membership.expiresAt)
        assertEquals(mapOf("role" to "admin"), membership.payload)
    }

    @Test
    fun optional_fields_default_to_null() {
        val membership = PNDataSyncMembership(
            id = randomString(),
            channelId = randomString(),
            userId = randomString(),
            className = "Membership",
            classVersion = 1,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-01T00:00:00.000Z",
            eTag = randomString(),
        )

        assertNull(membership.status)
        assertNull(membership.expiresAt)
        assertNull(membership.payload)
    }
}
