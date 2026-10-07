package com.pubnub.kmp

import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNUnknownDataSyncEventMessage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import PubNub as PubNubJs

class DataSyncConvertersTest {
    private fun event(
        event: String,
        type: String,
        data: dynamic,
        objectType: String = type,
    ): PubNubJs.DataSyncEvent {
        val message: dynamic = js("({})")
        message.version = "1.0"
        message.source = "data-sync"
        message.type = type
        message.className = "TestNode"
        message.classLevel = "SubKey"
        message.classVersion = 2
        message.event = event
        message.objectType = objectType
        message.data = data
        val result: dynamic = js("({})")
        result.channel = "ref-channel"
        result.subscription = "ref-channel"
        result.timetoken = "17000000000000000"
        result.message = message
        return result.unsafeCast<PubNubJs.DataSyncEvent>()
    }

    private fun snapshot(extra: dynamic = js("({})")): dynamic {
        val data: dynamic = js("({})")
        data.id = "obj-1"
        data.createdAt = "2026-01-01T00:00:00Z"
        data.updatedAt = "2026-01-02T00:00:00Z"
        data.eTag = "etag-1"
        data.expiresAt = "2027-01-01T00:00:00Z"
        entriesOf(extra.unsafeCast<JsMap<Any?>>()).forEach { (key, value) -> data[key] = value }
        return data
    }

    @Test
    fun entityCreateMapsToSetEntityLeafWithEnvelopeAndPayload() {
        val payload: dynamic = js("({ username: 'Alice' })")
        val extra: dynamic = js("({})")
        extra.status = "active"
        extra.payload = payload

        val result = event("create", "entity", snapshot(extra)).toDataSyncEventResult()

        assertEquals("ref-channel", result.channel)
        assertEquals(17000000000000000L, result.timetoken)
        val leaf = assertIs<PNSetDataSyncEntityEventMessage>(result.extractedMessage)
        assertEquals(PNDataSyncSetEventType.CREATE, leaf.event)
        assertEquals("data-sync", leaf.source)
        assertEquals("1.0", leaf.version)
        assertEquals("entity", leaf.type)
        assertEquals("TestNode", leaf.className)
        assertEquals("SubKey", leaf.classLevel)
        assertEquals(2, leaf.classVersion)
        assertEquals("obj-1", leaf.data.id)
        assertEquals("etag-1", leaf.data.eTag)
        assertEquals("active", leaf.data.status)
        assertEquals(mapOf<String, Any?>("username" to "Alice"), leaf.data.payload)
    }

    @Test
    fun updateMapsToUpdateVerbAndAbsentOptionalsAreNull() {
        val leaf = assertIs<PNSetDataSyncUserEventMessage>(
            event("update", "user", snapshot()).toDataSyncEventResult().extractedMessage
        )
        assertEquals(PNDataSyncSetEventType.UPDATE, leaf.event)
        assertNull(leaf.data.status)
        assertNull(leaf.data.payload)
    }

    @Test
    fun channelMembershipAndRelationshipMapToTheirSetLeaves() {
        assertIs<PNSetDataSyncChannelEventMessage>(
            event("create", "channel", snapshot()).toDataSyncEventResult().extractedMessage
        )

        val membershipExtra: dynamic = js("({ channelId: 'ch-1', userId: 'u-1' })")
        val membership = assertIs<PNSetDataSyncMembershipEventMessage>(
            event("create", "membership", snapshot(membershipExtra)).toDataSyncEventResult().extractedMessage
        )
        assertEquals("ch-1", membership.data.channelId)
        assertEquals("u-1", membership.data.userId)

        val relationshipExtra: dynamic = js("({ entityAId: 'a-1', entityBId: 'b-1' })")
        val relationship = assertIs<PNSetDataSyncRelationshipEventMessage>(
            event("update", "relationship", snapshot(relationshipExtra)).toDataSyncEventResult().extractedMessage
        )
        assertEquals("a-1", relationship.data.entityAId)
        assertEquals("b-1", relationship.data.entityBId)
    }

    @Test
    fun deleteMapsToDeleteLeafWithIdAndDeletedAt() {
        val data: dynamic = js("({ id: 'm-1', deletedAt: '2026-01-03T00:00:00Z' })")
        val leaf = assertIs<PNDeleteDataSyncMembershipEventMessage>(
            event("delete", "membership", data).toDataSyncEventResult().extractedMessage
        )
        assertEquals("m-1", leaf.id)
        assertEquals("2026-01-03T00:00:00Z", leaf.deletedAt)
        assertEquals("membership", leaf.type)
    }

    @Test
    fun dispatchUsesWireTypeNotJsDerivedObjectType() {
        // The JS SDK can re-derive objectType from the class name; Kotlin must follow the wire type like the JVM.
        val leaf = event("create", "entity", snapshot(), objectType = "user").toDataSyncEventResult().extractedMessage
        assertIs<PNSetDataSyncEntityEventMessage>(leaf)
    }

    @Test
    fun unrecognizedTypeMapsToUnknownLeafPreservingRawData() {
        val data: dynamic = js("({ id: 'x-1', foo: 'bar' })")
        val leaf = assertIs<PNUnknownDataSyncEventMessage>(
            event("create", "widget", data).toDataSyncEventResult().extractedMessage
        )
        assertEquals("create", leaf.event)
        assertEquals("widget", leaf.type)
        assertEquals("widget", leaf.metadata?.get("type"))
        assertEquals("bar", leaf.data?.get("foo"))
    }

    @Test
    fun unrecognizedVerbMapsToUnknownLeaf() {
        assertIs<PNUnknownDataSyncEventMessage>(
            event("archive", "user", snapshot()).toDataSyncEventResult().extractedMessage
        )
    }
}
