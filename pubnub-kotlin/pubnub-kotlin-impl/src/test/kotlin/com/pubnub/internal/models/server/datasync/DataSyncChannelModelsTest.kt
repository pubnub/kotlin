package com.pubnub.internal.models.server.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncChannel
import com.pubnub.internal.managers.MapperManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DataSyncChannelModelsTest {
    private val mapper = MapperManager(LogConfig("testPnInstanceId", "testUserId"))

    @Test
    fun createChannelRequest_drops_null_optionals() {
        val json = mapper.toJson(
            CreateChannelRequest(CreateChannelRequestData(entityClassVersion = 1)),
        )

        assertEquals("""{"data":{"entityClassVersion":1}}""", json)
        assertFalse(json.contains("\"id\""))
        assertFalse(json.contains("\"entityClass\""))
        assertFalse(json.contains("\"entityClassLevel\""))
    }

    @Test
    fun createChannelRequest_emits_entityClassLevel_when_set() {
        val json = mapper.toJson(
            CreateChannelRequest(
                CreateChannelRequestData(
                    id = "c1",
                    entityClass = "Channel.Public",
                    entityClassVersion = 2,
                    entityClassLevel = "SubKey",
                ),
            ),
        )

        assertTrue(json.contains("\"id\":\"c1\""))
        assertTrue(json.contains("\"entityClass\":\"Channel.Public\""))
        assertTrue(json.contains("\"entityClassVersion\":2"))
        assertTrue(json.contains("\"entityClassLevel\":\"SubKey\""))
    }

    @Test
    fun dataSyncChannel_deserializes_entityClass_wire_keys_into_class_fields() {
        // Regression guard for the @field:SerializedName mappings: the server serializes the
        // class-identity fields under the `entityClass*` wire keys, which must land on the public
        // `class*` properties.
        val json = """
            {
              "id": "c1",
              "entityClass": "Channel.Public",
              "entityClassVersion": 3,
              "entityClassLevel": "SubKey",
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-02T00:00:00.000Z",
              "eTag": "etag123",
              "status": "active",
              "expiresAt": "2022-01-01T00:00:00.000Z",
              "payload": { "name": "general" }
            }
        """.trimIndent()

        val channel: PNDataSyncChannel = mapper.fromJson(json, PNDataSyncChannel::class.java)

        assertEquals("c1", channel.id)
        assertEquals("Channel.Public", channel.className)
        assertEquals(3, channel.classVersion)
        assertEquals("SubKey", channel.classLevel)
        assertEquals("2021-01-01T00:00:00.000Z", channel.createdAt)
        assertEquals("2021-01-02T00:00:00.000Z", channel.updatedAt)
        assertEquals("etag123", channel.eTag)
        assertEquals("active", channel.status)
        assertEquals("2022-01-01T00:00:00.000Z", channel.expiresAt)
        assertEquals("general", channel.payload?.get("name"))
    }

    @Test
    fun dataSyncChannel_deserializes_with_null_optionals() {
        val json = """
            {
              "id": "c2",
              "entityClass": "Channel",
              "entityClassVersion": 1,
              "createdAt": "2021-01-01T00:00:00.000Z",
              "updatedAt": "2021-01-01T00:00:00.000Z",
              "eTag": "etag456"
            }
        """.trimIndent()

        val channel: PNDataSyncChannel = mapper.fromJson(json, PNDataSyncChannel::class.java)

        assertEquals("Channel", channel.className)
        assertEquals(1, channel.classVersion)
        assertNull(channel.classLevel)
        assertNull(channel.status)
        assertNull(channel.expiresAt)
        assertNull(channel.payload)
    }
}
