package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.v2.callbacks.EventListener

class DataSyncSubscribeOther {
    private fun dataSyncUserSubscription(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#create-a-subscription-datasync-user

        // snippet.dataSyncUserSubscription
        val subscription = pubnub.dataSyncUser("user-alice").subscription()
        subscription.subscribe()
        // snippet.end
    }

    private fun dataSyncChannelSubscription(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#create-a-subscription-datasync-channel

        // snippet.dataSyncChannelSubscription
        val subscription = pubnub.dataSyncChannel("channel-summer-sale").subscription()
        subscription.subscribe()
        // snippet.end
    }

    private fun dataSyncEntitySubscription(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#create-a-subscription-datasync-entity

        // snippet.dataSyncEntitySubscription
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription()
        subscription.subscribe()
        // snippet.end
    }

    private fun dataSyncProjectionSubscription(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#create-a-subscription-datasync-projection

        // snippet.dataSyncProjectionSubscription
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription("admin")
        subscription.subscribe()
        // snippet.end
    }

    private fun addDataSyncListener(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#add-datasync-listeners

        // snippet.addDataSyncListener
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription()

        subscription.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
            }
        })

        subscription.subscribe()
        // snippet.end
    }

    private fun addDataSyncListenerLambda(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#add-datasync-listeners

        // snippet.addDataSyncListenerLambda
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription()

        subscription.onDataSync = { result ->
            println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
        }

        subscription.subscribe()
        // snippet.end
    }

    private fun addDataSyncListenerClient(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#add-datasync-listeners

        // snippet.addDataSyncListenerClient
        pubnub.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
            }
        })

        pubnub.dataSyncEntity("product-sneaker-42").subscription().subscribe()
        // snippet.end
    }

    private fun addDataSyncListenerSubscriptionSet(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#add-datasync-listeners

        // snippet.addDataSyncListenerSubscriptionSet
        val subscriptionSet = pubnub.subscriptionSetOf(
            setOf(
                pubnub.dataSyncUser("user-alice").subscription(),
                pubnub.dataSyncEntity("product-sneaker-42").subscription()
            )
        )

        subscriptionSet.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
            }
        })

        subscriptionSet.subscribe()
        // snippet.end
    }
}
