package com.pubnub.api.models.consumer.datasync.entity

import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataSyncEntityTest {
    @Test
    fun holds_all_fields() {
        val id = randomString()
        val className = randomString()
        val eTag = randomString()
        val entity = DataSyncEntity(
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

        assertEquals(id, entity.id)
        assertEquals(className, entity.className)
        assertEquals(2, entity.classVersion)
        assertEquals("SubKey", entity.classLevel)
        assertEquals("2021-01-01T00:00:00.000Z", entity.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", entity.updatedAt)
        assertEquals(eTag, entity.eTag)
        assertEquals("active", entity.status)
        assertEquals("2022-01-01T00:00:00.000Z", entity.expiresAt)
        assertEquals(mapOf("key" to "value"), entity.payload)
    }

    @Test
    fun optional_fields_default_to_null() {
        val entity = DataSyncEntity(
            id = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-01T00:00:00.000Z",
            eTag = randomString(),
        )

        assertNull(entity.classLevel)
        assertNull(entity.status)
        assertNull(entity.expiresAt)
        assertNull(entity.payload)
    }
}
