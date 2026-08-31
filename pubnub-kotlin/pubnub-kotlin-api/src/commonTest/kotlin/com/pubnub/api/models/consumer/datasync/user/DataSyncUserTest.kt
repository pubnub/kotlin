package com.pubnub.api.models.consumer.datasync.user

import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataSyncUserTest {
    @Test
    fun holds_all_fields() {
        val id = randomString()
        val className = randomString()
        val eTag = randomString()
        val user = PNDataSyncUser(
            id = id,
            className = className,
            classVersion = 2,
            classLevel = "SubKey",
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-02T00:00:00.000Z",
            eTag = eTag,
            status = "active",
            expiresAt = "2022-01-01T00:00:00.000Z",
            payload = mapOf("key" to "value"),
        )

        assertEquals(id, user.id)
        assertEquals(className, user.className)
        assertEquals(2, user.classVersion)
        assertEquals("SubKey", user.classLevel)
        assertEquals("2021-01-01T00:00:00.000Z", user.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", user.updatedAt)
        assertEquals(eTag, user.eTag)
        assertEquals("active", user.status)
        assertEquals("2022-01-01T00:00:00.000Z", user.expiresAt)
        assertEquals(mapOf("key" to "value"), user.payload)
    }

    @Test
    fun optional_fields_default_to_null() {
        val user = PNDataSyncUser(
            id = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-01T00:00:00.000Z",
            eTag = randomString(),
        )

        assertNull(user.classLevel)
        assertNull(user.status)
        assertNull(user.expiresAt)
        assertNull(user.payload)
    }
}
