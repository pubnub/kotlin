package com.pubnub.api.models.consumer.datasync.user

import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PNUserTest {
    @Test
    fun holds_all_fields() {
        val id = randomString()
        val entityClass = randomString()
        val eTag = randomString()
        val user = PNUser(
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

        assertEquals(id, user.id)
        assertEquals(entityClass, user.entityClass)
        assertEquals(2, user.entityClassVersion)
        assertEquals("SubKey", user.entityClassLevel)
        assertEquals("2021-01-01T00:00:00.000Z", user.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", user.updatedAt)
        assertEquals(eTag, user.eTag)
        assertEquals("active", user.status)
        assertEquals("2022-01-01T00:00:00.000Z", user.expiresAt)
        assertEquals(mapOf("key" to "value"), user.payload)
    }

    @Test
    fun optional_fields_default_to_null() {
        val user = PNUser(
            id = randomString(),
            entityClass = randomString(),
            entityClassVersion = 1,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-01T00:00:00.000Z",
            eTag = randomString(),
        )

        assertNull(user.entityClassLevel)
        assertNull(user.status)
        assertNull(user.expiresAt)
        assertNull(user.payload)
    }
}
