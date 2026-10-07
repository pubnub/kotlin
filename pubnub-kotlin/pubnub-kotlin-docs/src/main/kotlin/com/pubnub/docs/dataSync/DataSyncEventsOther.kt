package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
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
import com.pubnub.api.v2.callbacks.EventListener

class DataSyncEventsOther {
    private fun dataSyncEventListener(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#listen-for-events

        // snippet.dataSyncEventListener
        val subscription = pubnub.channel("product-sneaker-42").subscription()

        subscription.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                when (val message = result.extractedMessage) {
                    is PNSetDataSyncUserEventMessage ->
                        println("User ${message.data.id} ${message.event}")
                    is PNSetDataSyncChannelEventMessage ->
                        println("Channel ${message.data.id} ${message.event}")
                    is PNSetDataSyncMembershipEventMessage ->
                        println("Membership ${message.data.id} ${message.event}")
                    is PNSetDataSyncEntityEventMessage ->
                        println("Entity ${message.data.id} ${message.event}")
                    is PNSetDataSyncRelationshipEventMessage ->
                        println("Relationship ${message.data.id} ${message.event}")
                    is PNDeleteDataSyncUserEventMessage ->
                        println("User ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncChannelEventMessage ->
                        println("Channel ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncMembershipEventMessage ->
                        println("Membership ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncEntityEventMessage ->
                        println("Entity ${message.id} deleted at ${message.deletedAt}")
                    is PNDeleteDataSyncRelationshipEventMessage ->
                        println("Relationship ${message.id} deleted at ${message.deletedAt}")
                    else ->
                        println("Unknown DataSync event of type ${message.type}")
                }
            }
        })

        subscription.subscribe()
        // snippet.end
    }

    private fun subscribeToProjectionChannels(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#subscribe-to-projection-channels

        // snippet.subscribeToProjectionChannels
        val entity = pubnub.dataSyncEntity("product-sneaker-42")

        val defaultSubscription = entity.subscription()
        val adminSubscription = entity.subscription("admin")

        defaultSubscription.subscribe()
        adminSubscription.subscribe()
        // snippet.end
    }
}
