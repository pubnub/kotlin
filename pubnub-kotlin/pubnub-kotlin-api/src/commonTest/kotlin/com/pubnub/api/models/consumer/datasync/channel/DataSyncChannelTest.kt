package com.pubnub.api.models.consumer.datasync.channel

import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncPage
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DataSyncChannelTest {
    @Test
    fun holds_all_fields() {
        val id = randomString()
        val className = randomString()
        val eTag = randomString()
        val channel = DataSyncChannel(
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

        assertEquals(id, channel.id)
        assertEquals(className, channel.className)
        assertEquals(2, channel.classVersion)
        assertEquals("SubKey", channel.classLevel)
        assertEquals("2021-01-01T00:00:00.000Z", channel.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", channel.updatedAt)
        assertEquals(eTag, channel.eTag)
        assertEquals("active", channel.status)
        assertEquals("2022-01-01T00:00:00.000Z", channel.expiresAt)
        assertEquals(mapOf("key" to "value"), channel.payload)
    }

    @Test
    fun optional_fields_default_to_null() {
        val channel = DataSyncChannel(
            id = randomString(),
            className = randomString(),
            classVersion = 1,
            createdAt = "2021-01-01T00:00:00.000Z",
            updatedAt = "2021-01-01T00:00:00.000Z",
            eTag = randomString(),
        )

        assertNull(channel.classLevel)
        assertNull(channel.status)
        assertNull(channel.expiresAt)
        assertNull(channel.payload)
    }

    @Test
    fun classLevel_wire_values() {
        assertEquals("Global", PNDataSyncClassLevel.GLOBAL.value)
        assertEquals("SubKey", PNDataSyncClassLevel.SUBKEY.value)
        assertEquals("Account", PNDataSyncClassLevel.ACCOUNT.value)
    }

    @Test
    fun sortField_defaults_to_ascending() {
        assertTrue(PNDataSyncSortField("username").ascending)
        assertEquals(false, PNDataSyncSortField("email", ascending = false).ascending)
    }

    @Test
    fun page_defaults() {
        val page = PNDataSyncPage()
        assertNull(page.cursor)
        assertEquals(false, page.hasNext)
        assertNull(page.limit)
    }
}
