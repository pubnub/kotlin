package com.pubnub.api.models.consumer.pubsub.datasync

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PubSubResult

/**
 * A DataSync realtime change event delivered over PubNub subscribe.
 *
 * DataSync ships realtime create/update/delete events for its elements (User, Channel, Entity,
 * Relationship, Membership), analogous to how AppContext Objects (`e=2`) events are delivered. An event
 * arrives on the ref-channel of the element it concerns (or, for a non-default projection, on the
 * `__{projection}__{ref}` channel).
 *
 * A single subscribed ref-channel receives a **mix** of [PNDataSyncEventMessage] leaf types, not just the
 * element you named the handle after — for example subscribing to a user's ref also delivers the
 * `membership`/`relationship` events where that user is an endpoint, and propagated events for entities
 * joined to it. Branch on the sealed [extractedMessage] (or on [PNDataSyncEventMessage.type]) rather than
 * assuming a single type.
 *
 * @property extractedMessage the typed, deserialized change event.
 */
data class PNDataSyncEventResult(
    private val result: BasePubSubResult,
    val extractedMessage: PNDataSyncEventMessage,
) : PubSubResult by result

/**
 * Sealed hierarchy of DataSync realtime change events.
 *
 * There is one Set leaf (carrying the full current snapshot) and one Delete leaf (carrying only
 * `{id, deletedAt}`) per DataSync element type, plus [PNUnknownDataSyncEventMessage] for
 * forward-compatibility. Because the backend `type` set can grow in a future server version, an
 * exhaustive `when` over this hierarchy **must** include an `else` / [PNUnknownDataSyncEventMessage]
 * branch — that is what keeps future server-side additions non-breaking.
 *
 * The verb of the event is **not** uniform across leaves and so is not declared here: the `PNSet…`
 * leaves carry an `event: PNDataSyncSetEventType` (`CREATE` or `UPDATE`); the `PNDelete…` leaves have no
 * `event` (the leaf class itself is the delete); and [PNUnknownDataSyncEventMessage] keeps the raw wire
 * `event` string.
 *
 * @property source event source, always `"data-sync"`.
 * @property version envelope version.
 * @property type the wire discriminator: one of `user`, `channel`, `entity`, `relationship`,
 *   `membership` (an inheritance-aware value derived by the backend from the object's Global ancestor).
 * @property className the object's class name, if present on the envelope.
 * @property classLevel the level at which the class is defined (e.g. `SubKey` / `Global`). Modeled as a
 *   nullable [String] rather than an enum to stay source-compatible with future values.
 * @property classVersion the object's class version, if present on the envelope.
 */
sealed class PNDataSyncEventMessage {
    abstract val source: String
    abstract val version: String
    abstract val type: String
    abstract val className: String?
    abstract val classLevel: String?
    abstract val classVersion: Int?
}

/**
 * Full snapshot of a DataSync User at the time of a `create`/`update` event.
 *
 * DataSync realtime events always carry the full current row, never a delta. `status`/`payload` are
 * omitted (not sent) when absent.
 *
 * @property id the user id.
 * @property createdAt date and time the user was created.
 * @property updatedAt date and time the user was last updated.
 * @property eTag the content fingerprint.
 * @property expiresAt date and time the user expires.
 * @property status user status, if present.
 * @property payload arbitrary user-defined JSON object, if present.
 */
data class PNDataSyncUserData(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)

/**
 * Full snapshot of a DataSync Channel at the time of a `create`/`update` event. See [PNDataSyncUserData].
 */
data class PNDataSyncChannelData(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)

/**
 * Full snapshot of a DataSync Entity at the time of a `create`/`update` event. See [PNDataSyncUserData].
 */
data class PNDataSyncEntityData(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)

/**
 * Full snapshot of a DataSync Membership at the time of a `create`/`update` event. A Membership links a
 * Channel ([channelId], the A endpoint) and a User ([userId], the B endpoint). See [PNDataSyncUserData]
 * for the shared fields.
 *
 * @property channelId identifier of the Channel (A endpoint).
 * @property userId identifier of the User (B endpoint).
 */
data class PNDataSyncMembershipData(
    val id: String,
    val channelId: String,
    val userId: String,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)

/**
 * Full snapshot of a DataSync Relationship at the time of a `create`/`update` event. A Relationship is a
 * typed, non-directional link between two entities ([entityAId] / [entityBId]). See [PNDataSyncUserData]
 * for the shared fields.
 *
 * @property entityAId identifier of the A endpoint.
 * @property entityBId identifier of the B endpoint.
 */
data class PNDataSyncRelationshipData(
    val id: String,
    val entityAId: String,
    val entityBId: String,
    val createdAt: String,
    val updatedAt: String,
    val eTag: String,
    val expiresAt: String,
    val status: String? = null,
    val payload: Map<String, Any?>? = null,
)

/**
 * A DataSync User `create`/`update` event (wire `type:"user"`). [data] is the full current snapshot.
 *
 * Also fires for a custom subclass of the built-in User class — the backend reports `type:"user"` for the
 * whole User inheritance chain.
 *
 * `event` is `CREATE` or `UPDATE`.
 */
data class PNSetDataSyncUserEventMessage(
    override val source: String,
    override val version: String,
    val event: PNDataSyncSetEventType,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val data: PNDataSyncUserData,
) : PNDataSyncEventMessage()

/**
 * A DataSync User `delete` event (wire `type:"user"`). The wire collapses `data` to `{id, deletedAt}`.
 */
data class PNDeleteDataSyncUserEventMessage(
    override val source: String,
    override val version: String,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val id: String,
    val deletedAt: String,
) : PNDataSyncEventMessage()

/**
 * A DataSync Channel `create`/`update` event (wire `type:"channel"`). See [PNSetDataSyncUserEventMessage].
 *
 * `event` is `CREATE` or `UPDATE`.
 */
data class PNSetDataSyncChannelEventMessage(
    override val source: String,
    override val version: String,
    val event: PNDataSyncSetEventType,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val data: PNDataSyncChannelData,
) : PNDataSyncEventMessage()

/**
 * A DataSync Channel `delete` event (wire `type:"channel"`). See [PNDeleteDataSyncUserEventMessage].
 */
data class PNDeleteDataSyncChannelEventMessage(
    override val source: String,
    override val version: String,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val id: String,
    val deletedAt: String,
) : PNDataSyncEventMessage()

/**
 * A DataSync Entity `create`/`update` event (wire `type:"entity"`). See [PNSetDataSyncUserEventMessage].
 *
 * `event` is `CREATE` or `UPDATE`.
 */
data class PNSetDataSyncEntityEventMessage(
    override val source: String,
    override val version: String,
    val event: PNDataSyncSetEventType,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val data: PNDataSyncEntityData,
) : PNDataSyncEventMessage()

/**
 * A DataSync Entity `delete` event (wire `type:"entity"`). See [PNDeleteDataSyncUserEventMessage].
 */
data class PNDeleteDataSyncEntityEventMessage(
    override val source: String,
    override val version: String,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val id: String,
    val deletedAt: String,
) : PNDataSyncEventMessage()

/**
 * A DataSync Membership `create`/`update` event (wire `type:"membership"`). [data] is the full snapshot,
 * carrying both endpoint refs. This event is published to **both** endpoint ref-channels (the channel's
 * ref and the user's ref), never to a membership-id channel — so subscribe to a Channel/User ref to hear
 * about its memberships.
 *
 * `event` is `CREATE` or `UPDATE`.
 */
data class PNSetDataSyncMembershipEventMessage(
    override val source: String,
    override val version: String,
    val event: PNDataSyncSetEventType,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val data: PNDataSyncMembershipData,
) : PNDataSyncEventMessage()

/**
 * A DataSync Membership `delete` event (wire `type:"membership"`).
 *
 * **Backend limitation:** the delete wire carries only `{id, deletedAt}` — no `channelId`/`userId`, and no
 * A/B side marker. The peer endpoint id is **not recoverable from a delete event alone**, and the
 * subscriber cannot tell from the payload whether the ref-channel it arrived on was the channel (A) or the
 * user (B) endpoint. To resolve the peer on delete, maintain a local `membershipId -> (channelId, userId)`
 * map populated from the Set events (which do carry both refs) and look up by [id].
 *
 * **Cascade:** deleting a Channel/User (or Entity) also emits a separate membership-delete event of this
 * shape for each affected membership, routed to both endpoint refs — so a subscriber on the surviving peer
 * also sees the membership vanish. One logical delete can therefore produce multiple delete events, with
 * no cross-object ordering guarantee. Do not treat this leaf as implying its own endpoint was the object
 * that was deleted.
 */
data class PNDeleteDataSyncMembershipEventMessage(
    override val source: String,
    override val version: String,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val id: String,
    val deletedAt: String,
) : PNDataSyncEventMessage()

/**
 * A DataSync Relationship `create`/`update` event (wire `type:"relationship"`). [data] is the full
 * snapshot, carrying both endpoint refs. Published to **both** endpoint ref-channels, never to a
 * relationship-id channel — so subscribe to an endpoint's ref to hear about its relationships.
 *
 * `event` is `CREATE` or `UPDATE`.
 */
data class PNSetDataSyncRelationshipEventMessage(
    override val source: String,
    override val version: String,
    val event: PNDataSyncSetEventType,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val data: PNDataSyncRelationshipData,
) : PNDataSyncEventMessage()

/**
 * A DataSync Relationship `delete` event (wire `type:"relationship"`).
 *
 * **Backend limitation:** the delete wire carries only `{id, deletedAt}` — no `entityAId`/`entityBId`, and
 * no A/B side marker. The peer endpoint id is **not recoverable from a delete event alone**. To resolve
 * the peer on delete, maintain a local `relationshipId -> (entityAId, entityBId)` map populated from the
 * Set events (which do carry both refs) and look up by [id].
 *
 * **Cascade:** deleting an endpoint entity also emits a separate relationship-delete of this shape for
 * each affected relationship, routed to both endpoint refs. See
 * [PNDeleteDataSyncMembershipEventMessage] for the fan-out/ordering caveats.
 */
data class PNDeleteDataSyncRelationshipEventMessage(
    override val source: String,
    override val version: String,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val id: String,
    val deletedAt: String,
) : PNDataSyncEventMessage()

/**
 * A DataSync event whose `type`/`event` combination is not recognized by this SDK version — a
 * forward-compatibility catch-all for element kinds or event kinds added in a future server version.
 *
 * The (present) [type]/[event] discriminators are surfaced so a customer can still branch on the
 * unrecognized `type` string, and the raw [metadata] + [data] are preserved so nothing is lost. Your
 * exhaustive `when` over [PNDataSyncEventMessage] should route unmapped events here (via an `else`
 * branch) rather than assuming the hierarchy is closed.
 *
 * @property metadata the raw `metadata` envelope object as a map.
 * @property data the raw `data` payload as a map, if present.
 */
data class PNUnknownDataSyncEventMessage(
    override val source: String,
    override val version: String,
    val event: String,
    override val type: String,
    override val className: String?,
    override val classLevel: String?,
    override val classVersion: Int?,
    val metadata: Map<String, Any?>?,
    val data: Map<String, Any?>?,
) : PNDataSyncEventMessage()
