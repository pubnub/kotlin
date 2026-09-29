package com.pubnub.internal.v2.entities

import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import com.pubnub.api.v2.entities.DataSyncChannel
import com.pubnub.api.v2.entities.DataSyncEntity
import com.pubnub.api.v2.entities.DataSyncUser
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

/**
 * Resolves a DataSync ref [id] + [projection] to the channel name events for it are published on. Delegates to
 * [DataSyncNamespace.refChannel], the single source of truth for the rule, shared with
 * [com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant.subscribe] (the gson-impl handles delegate here).
 */
internal fun resolveDataSyncChannel(id: String, projection: String): String = DataSyncNamespace.refChannel(id, projection)

private fun PubNubImpl.dataSyncSubscription(channelName: ChannelName, options: SubscriptionOptions): SubscriptionImpl {
    val channels = setOf(channelName)
    return SubscriptionImpl(
        this,
        channels,
        emptySet(),
        SubscriptionOptions.filter { result ->
            channels.any { it.id == result.channel }
        } + options,
    )
}

open class DataSyncUserImpl(val pubnub: PubNubImpl, override val id: String) : DataSyncUser {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl =
        pubnub.dataSyncSubscription(ChannelName(id), options)

    override fun subscription(projection: String, options: SubscriptionOptions): SubscriptionImpl =
        pubnub.dataSyncSubscription(ChannelName(resolveDataSyncChannel(id, projection)), options)

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is DataSyncUserImpl) {
            return false
        }
        return pubnub == other.pubnub && id == other.id
    }

    override fun hashCode(): Int = 31 * pubnub.hashCode() + id.hashCode()
}

open class DataSyncChannelImpl(val pubnub: PubNubImpl, override val id: String) : DataSyncChannel {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl =
        pubnub.dataSyncSubscription(ChannelName(id), options)

    override fun subscription(projection: String, options: SubscriptionOptions): SubscriptionImpl =
        pubnub.dataSyncSubscription(ChannelName(resolveDataSyncChannel(id, projection)), options)

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is DataSyncChannelImpl) {
            return false
        }
        return pubnub == other.pubnub && id == other.id
    }

    override fun hashCode(): Int = 31 * pubnub.hashCode() + id.hashCode()
}

open class DataSyncEntityImpl(val pubnub: PubNubImpl, override val id: String) : DataSyncEntity {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl =
        pubnub.dataSyncSubscription(ChannelName(id), options)

    override fun subscription(projection: String, options: SubscriptionOptions): SubscriptionImpl =
        pubnub.dataSyncSubscription(ChannelName(resolveDataSyncChannel(id, projection)), options)

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is DataSyncEntityImpl) {
            return false
        }
        return pubnub == other.pubnub && id == other.id
    }

    override fun hashCode(): Int = 31 * pubnub.hashCode() + id.hashCode()
}
