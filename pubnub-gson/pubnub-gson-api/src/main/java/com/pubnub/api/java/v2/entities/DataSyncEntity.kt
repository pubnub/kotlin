package com.pubnub.api.java.v2.entities

import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

/**
 * A handle for subscribing to DataSync realtime events about an Entity by its ref/id.
 *
 * A DataSync handle is a **channel filter on the ref**, not a type filter: subscribing to
 * `dataSyncEntity(id)` delivers the mix of events routed to that ref-channel. Branch on the sealed
 * [com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult.extractedMessage].
 *
 * **PAM:** because subscribing is a plain PubSub read of the ref-channel, an authorized client needs a
 * channel `read` grant on the resolved ref-channel name — `ChannelGrant.name(id).read()` for the default
 * projection, `ChannelGrant.name("__{projection}__{id}").read()` for a non-default one. This is separate
 * from the DataSync CRUD permission on the entity *record*
 * ([com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant.entity]): the record grant does not
 * authorize the subscribe, and the channel `read` grant does not authorize record CRUD.
 *
 * Use the [com.pubnub.api.java.PubNub.dataSyncEntity] factory method to create instances of this interface.
 */
interface DataSyncEntity : Subscribable {
    /**
     * The entity's ref/id.
     */
    val id: String

    /**
     * Returns a [Subscription] to this entity's default-projection ref-channel.
     *
     * @param options optional [SubscriptionOptions].
     * @return an inactive [Subscription]. You must call [Subscription.subscribe] to start receiving events.
     */
    override fun subscription(options: SubscriptionOptions): Subscription

    /**
     * Returns a [Subscription] to this entity's default-projection ref-channel.
     *
     * @return an inactive [Subscription]. You must call [Subscription.subscribe] to start receiving events.
     */
    fun subscription(): Subscription

    /**
     * Returns a [Subscription] to this entity's events for the given [projection].
     *
     * `"default"` / `"__default__"` resolve to the base ref channel (`id`); any other value resolves to
     * `__{projection}__{id}`. A blank projection is rejected.
     */
    fun subscription(projection: String, options: SubscriptionOptions = EmptyOptions): Subscription

    /**
     * Returns a [Subscription] to this entity's events for the given [projection].
     */
    fun subscription(projection: String): Subscription
}
