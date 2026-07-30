package com.pubnub.api.models.consumer.datasync.entity

import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PNEntityTest {
    @Test
    fun holds_all_fields() {
        val id = randomString()
        val entityClass = randomString()
        val eTag = randomString()
        val entity = PNEntity(
            id = id,
            entityClass = entityClass,
            entityClassVersion = 2,
            entityClassLevel = "SubKey",
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-02T00:00:00.000Z",
            eTag = eTag,
            status = "active",
            expiresAt = "2022-01-01T00:00:00.000Z",
            payload = mapOf("key" to "value"),
        )

        assertEquals(id, entity.id)
        assertEquals(entityClass, entity.entityClass)
        assertEquals(2, entity.entityClassVersion)
        assertEquals("SubKey", entity.entityClassLevel)
        assertEquals("2021-01-01T00:00:00.000Z", entity.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", entity.updatedAt)
        assertEquals(eTag, entity.eTag)
        assertEquals("active", entity.status)
        assertEquals("2022-01-01T00:00:00.000Z", entity.expiresAt)
        assertEquals(mapOf("key" to "value"), entity.payload)
    }

    @Test
    fun optional_fields_default_to_null() {
        val entity = PNEntity(
            id = randomString(),
            entityClass = randomString(),
            entityClassVersion = 1,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-01T00:00:00.000Z",
            eTag = randomString(),
        )

        assertNull(entity.entityClassLevel)
        assertNull(entity.status)
        assertNull(entity.expiresAt)
        assertNull(entity.payload)
    }
}
