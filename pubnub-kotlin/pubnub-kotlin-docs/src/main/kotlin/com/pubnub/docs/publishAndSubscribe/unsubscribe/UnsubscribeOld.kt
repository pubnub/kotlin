package com.pubnub.docs.publishAndSubscribe.unsubscribe

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.docs.SnippetBase

class UnsubscribeOld : SnippetBase() {
    private fun unsubscribeAndOnPresenceLeave() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-14

        val pubnub = createPubNub()

        // snippet.unsubscribeFromChannel
        pubnub.unsubscribe(
            channels = listOf("my_channel") // subscribe to channel groups
        )
        // snippet.end

        // snippet.unsubscribeAndOnPresenceLeave
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // 2
                    pnPresenceEventResult.uuid // left_uuid
                }
            }
        })
        // snippet.end
    }

    private fun unsubscribeFromMultipleAndOnPresenceLeave() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#unsubscribing-from-multiple-channels

        val pubnub = createPubNub()

        // snippet.unsubscribeFromMultiple
        pubnub.unsubscribe(
            channels = listOf("ch1", "ch2", "ch3"),
            channelGroups = listOf("cg1", "cg2", "cg3")
        )
        // snippet.end

        // snippet.unsubscribeFromMultipleAndOnPresenceLeave
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // 2
                    pnPresenceEventResult.uuid // left_uuid
                }
            }
        })
        // snippet.end
    }

    private fun unsubscribeFromChannelGroup() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#unsubscribing-from-a-channel-group

        val pubnub = createPubNub()

        // snippet.unsubscribeFromChannelGroup
        pubnub.unsubscribe(
            channelGroups = listOf("cg1", "cg2", "cg3")
        )
        // snippet.end

        // snippet.unsubscribeFromChannelGroupAndOnPresenceLeave
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // 2
                    pnPresenceEventResult.uuid // left_uuid
                }
            }
        })
        // snippet.end
    }
}
