package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

/**
 * A handle for subscribing to DataSync realtime events about a Channel by its ref/id.
 *
 * A DataSync handle is a **channel filter on the ref**, not a type filter: subscribing to
 * `dataSyncChannel(id)` delivers the mix of events routed to that ref-channel — the channel's own
 * create/update/delete, the memberships/relationships where the channel is an endpoint, and propagated
 * events for entities joined to it. Branch on the sealed
 * [com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult.extractedMessage].
 *
 * This handle is a convenience over `pubnub.channel(id).subscription()`; a plain channel subscription on
 * the same ref also delivers these events. The handle additionally resolves projection channels.
 *
 * **PAM:** because subscribing is a plain PubSub read of the ref-channel, an authorized client needs a
 * channel `read` grant on the resolved ref-channel name — `ChannelGrant.name(id, read = true)` for the
 * default projection, `ChannelGrant.name("__{projection}__{id}", read = true)` for a non-default one. This
 * is separate from the DataSync CRUD permission on the channel *record*
 * ([com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant] with `get`/`update`/`delete`/`create`):
 * the record permissions do not authorize the subscribe, and channel `read` alone does not authorize record
 * CRUD.
 *
 * Use [com.pubnub.api.PubNub.dataSyncChannel] to create instances.
 */
interface DataSyncChannel : Subscribable {
    /**
     * The channel's ref/id.
     */
    val id: String

    /**
     * Returns a [Subscription] to this channel's default-projection ref-channel.
     */
    override fun subscription(options: SubscriptionOptions): Subscription

    /**
     * Returns a [Subscription] to this channel's events for the given [projection].
     *
     * `"default"` / `"__default__"` resolve to the base ref channel (`id`); any other value resolves to
     * `__{projection}__{id}`. A blank projection is rejected. `"__default__"` is the canonical server
     * projection name; `"default"` is accepted as a convenience alias.
     */
    fun subscription(projection: String, options: SubscriptionOptions = com.pubnub.api.v2.subscriptions.EmptyOptions): Subscription
}
