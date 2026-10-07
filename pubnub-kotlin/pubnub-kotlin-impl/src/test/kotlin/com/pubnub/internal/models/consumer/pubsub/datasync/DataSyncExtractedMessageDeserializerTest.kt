package com.pubnub.internal.models.consumer.pubsub.datasync

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNUnknownDataSyncEventMessage
import com.pubnub.internal.managers.MapperManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Exercises the internal `e=5` twin deserializer (dispatch by `metadata.event` x `metadata.type`) and its
 * `toApi()` flattening onto the public sealed leaves.
 */
internal class DataSyncExtractedMessageDeserializerTest {
    private val mapper = MapperManager(LogConfig("testPnInstanceId", "testUserId"))

    private fun parse(json: String) = mapper.fromJson(json, PNDataSyncEventMessage::class.java).toApi()

    private fun setBody(event: String, type: String, extraData: String = "") =
        """
        {
          "version": "1.0",
          "metadata": {
            "source": "data-sync",
            "event": "$event",
            "type": "$type",
            "className": "MyClass",
            "classLevel": "Global",
            "classVersion": 3
          },
          "data": {
            "id": "id-1",
            "createdAt": "2026-01-01T00:00:00Z",
            "updatedAt": "2026-01-02T00:00:00Z",
            "eTag": "etag-1",
            "expiresAt": "2026-02-01T00:00:00Z"$extraData
          }
        }
        """.trimIndent()

    private fun deleteBody(type: String) =
        """
        {
          "version": "1.0",
          "metadata": { "event": "delete", "type": "$type" },
          "data": { "id": "id-1", "deletedAt": "2026-03-01T00:00:00Z" }
        }
        """.trimIndent()

    @Test
    fun `create user maps to set user leaf and flattens metadata`() {
        val result = parse(setBody("create", "user"))
        val leaf = assertInstanceOf(PNSetDataSyncUserEventMessage::class.java, result)
        assertEquals("data-sync", leaf.source)
        assertEquals("1.0", leaf.version)
        assertEquals(PNDataSyncSetEventType.CREATE, leaf.event)
        assertEquals("user", leaf.type)
        assertEquals("MyClass", leaf.className)
        assertEquals("Global", leaf.classLevel)
        assertEquals(3, leaf.classVersion)
        assertEquals("id-1", leaf.data.id)
        assertEquals("etag-1", leaf.data.eTag)
    }

    @Test
    fun `update user also maps to set user leaf`() {
        val leaf = assertInstanceOf(PNSetDataSyncUserEventMessage::class.java, parse(setBody("update", "user")))
        assertEquals(PNDataSyncSetEventType.UPDATE, leaf.event)
    }

    @Test
    fun `create channel maps to set channel leaf`() {
        assertInstanceOf(PNSetDataSyncChannelEventMessage::class.java, parse(setBody("create", "channel")))
    }

    @Test
    fun `create entity maps to set entity leaf`() {
        assertInstanceOf(PNSetDataSyncEntityEventMessage::class.java, parse(setBody("create", "entity")))
    }

    @Test
    fun `create membership carries channelId and userId`() {
        val result = parse(setBody("create", "membership", """, "channelId": "ch-1", "userId": "u-1""""))
        val leaf = assertInstanceOf(PNSetDataSyncMembershipEventMessage::class.java, result)
        assertEquals("ch-1", leaf.data.channelId)
        assertEquals("u-1", leaf.data.userId)
    }

    @Test
    fun `create relationship carries entityAId and entityBId`() {
        val result = parse(setBody("create", "relationship", """, "entityAId": "a-1", "entityBId": "b-1""""))
        val leaf = assertInstanceOf(PNSetDataSyncRelationshipEventMessage::class.java, result)
        assertEquals("a-1", leaf.data.entityAId)
        assertEquals("b-1", leaf.data.entityBId)
    }

    @Test
    fun `set leaf carries optional status and payload when present`() {
        val result = parse(setBody("create", "user", """, "status": "active", "payload": {"k": "v"}"""))
        val leaf = assertInstanceOf(PNSetDataSyncUserEventMessage::class.java, result)
        assertEquals("active", leaf.data.status)
        assertEquals("v", leaf.data.payload?.get("k"))
    }

    @Test
    fun `set leaf omits status and payload when absent`() {
        val leaf = assertInstanceOf(PNSetDataSyncUserEventMessage::class.java, parse(setBody("create", "user")))
        assertNull(leaf.data.status)
        assertNull(leaf.data.payload)
    }

    @Test
    fun `delete user maps to delete user leaf with id and deletedAt`() {
        val result = parse(deleteBody("user"))
        val leaf = assertInstanceOf(PNDeleteDataSyncUserEventMessage::class.java, result)
        assertEquals("id-1", leaf.id)
        assertEquals("2026-03-01T00:00:00Z", leaf.deletedAt)
    }

    @Test
    fun `delete channel maps to delete channel leaf`() {
        assertInstanceOf(PNDeleteDataSyncChannelEventMessage::class.java, parse(deleteBody("channel")))
    }

    @Test
    fun `delete entity maps to delete entity leaf`() {
        assertInstanceOf(PNDeleteDataSyncEntityEventMessage::class.java, parse(deleteBody("entity")))
    }

    @Test
    fun `delete membership maps to delete membership leaf`() {
        assertInstanceOf(PNDeleteDataSyncMembershipEventMessage::class.java, parse(deleteBody("membership")))
    }

    @Test
    fun `delete relationship maps to delete relationship leaf`() {
        assertInstanceOf(PNDeleteDataSyncRelationshipEventMessage::class.java, parse(deleteBody("relationship")))
    }

    @Test
    fun `unrecognized type maps to unknown leaf instead of throwing`() {
        val json =
            """
            {
              "version": "1.0",
              "metadata": { "event": "create", "type": "somethingNew", "className": "Future" },
              "data": { "id": "id-1", "future": true }
            }
            """.trimIndent()
        val result = parse(json)
        val leaf = assertInstanceOf(PNUnknownDataSyncEventMessage::class.java, result)
        assertEquals("create", leaf.event)
        assertEquals("somethingNew", leaf.type)
        assertEquals("Future", leaf.className)
        assertTrue(leaf.metadata?.containsKey("type") == true)
        assertEquals(true, leaf.data?.get("future"))
    }

    @Test
    fun `unrecognized event maps to unknown leaf`() {
        val json =
            """
            {
              "version": "1.0",
              "metadata": { "event": "patch", "type": "user" },
              "data": { "id": "id-1" }
            }
            """.trimIndent()
        val leaf = assertInstanceOf(PNUnknownDataSyncEventMessage::class.java, parse(json))
        // An unrecognized verb stays a raw String on the unknown leaf, never a fallback on a Set leaf.
        assertEquals("patch", leaf.event)
    }

    @Test
    fun `missing source defaults to data-sync`() {
        val json =
            """
            {
              "version": "1.0",
              "metadata": { "event": "create", "type": "user" },
              "data": {
                "id": "id-1",
                "createdAt": "2026-01-01T00:00:00Z",
                "updatedAt": "2026-01-02T00:00:00Z",
                "eTag": "etag-1",
                "expiresAt": "2026-02-01T00:00:00Z"
              }
            }
            """.trimIndent()
        val leaf = assertInstanceOf(PNSetDataSyncUserEventMessage::class.java, parse(json))
        assertEquals("data-sync", leaf.source)
    }
}
