package com.pubnub.docs.publishAndSubscribe.subscribe

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.docs.SnippetBase

class SubscribeOld : SnippetBase() {
    private fun subscribeWithLogging() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-subscribe-with-logging

        // snippet.subscribeWithLogging
        val pnConfiguration = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "my_pubkey"
        }

        val pubnub = PubNub.create(pnConfiguration.build())

        pubnub.subscribe(
            channels = listOf("my_channel")
        )
        // snippet.end
    }

    private fun subscribe() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-13
        val pubnub = createPubNub()

        // snippet.subscribe
        pubnub.subscribe(
            channels = listOf("my_channel")
        )

        // snippet.end
    }

    private fun subscribeWithMultipleChannels() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#subscribing-to-multiple-channels

        val pubnub = createPubNub()

        // snippet.subscribeWithMultipleChannels
        pubnub.subscribe(
            channels = listOf("ch1", "ch2") // subscribe to channels information
        )
        // snippet.end
    }

    private fun subscribeToPresenceChannel() {
        //  https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#subscribing-to-a-presence-channel

        val pubnub = createPubNub()

        // snippet.subscribeToPresenceChannel
        pubnub.subscribe(
            channels = listOf("my_channel"), // subscribe to channels
            withPresence = true // also subscribe to related presence information
        )
        // snippet.end
    }

    private fun presenceEvents() {
        val pubnub = createPubNub()

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#join-event

        // snippet.joinEvent
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "join") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#leave-event
        // snippet.leaveEvent
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "leave") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#timeout-event
        // snippet.timeout
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "timeout") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#timeout-event
        // snippet.stateChange
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "state-change") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                    pnPresenceEventResult.state?.asJsonObject // {"data":{"isTyping":true}}
                }
            }
        })
        // snippet.end

        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#interval-event
        // snippet.interval
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "interval") {
                    pnPresenceEventResult.uuid // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                }
            }
        })
        // snippet.end

        // snippet.intervalExampleWithTwoNewUUIDs
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "interval") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                    pnPresenceEventResult.join // ["uuid1", "uuid2"]
                    pnPresenceEventResult.timeout // ["uuid3"]
                }
            }
        })
        // snippet.end

        // snippet.intervalExampleWithHerNowRefresh
        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "interval") {
                    pnPresenceEventResult.timestamp // 1345546797
                    pnPresenceEventResult.occupancy // # users in channel
                    pnPresenceEventResult.hereNowRefresh // true
                }
            }
        })
        // snippet.end
    }

    private fun wildCardSubscribeToChannels() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#wildcard-subscribe-to-channels

        val pubnub = createPubNub()

        // snippet.wildcardSubscribeToChannels
        pubnub.subscribe(
            channels = listOf("foo.*") // subscribe to channels information
        )
        // snippet.end
    }

    private fun subscribeWithState() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#subscribing-with-state

        // snippet.subscribeWithState
        val pnConfiguration = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }

        class ComplexData(
            val fieldA: String,
            val fieldB: Int
        )

        val pubnub = PubNub.create(pnConfiguration.build())

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
                if (status.category == PNStatusCategory.PNConnectedCategory) {
                    pubnub.setPresenceState(
                        channels = listOf("awesomeChannel"),
                        channelGroups = listOf("awesomeChannelGroup"),
                        state = ComplexData("Awesome", 10)
                    ).async { result ->
                        // handle set state response
                    }
                }
            }
        })

        pubnub.subscribe(
            channels = listOf("awesomeChannel")
        )

        // snippet.end
    }

    private fun subscribeToChannelGroup() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#subscribe-to-a-channel-group

        val pubnub = createPubNub()

        // snippet.subscribeToChannelGroup
        pubnub.subscribe(
            channels = listOf("ch1", "ch2"), // subscribe to channels
            channelGroups = listOf("cg1", "cg2"), // subscribe to channel groups
            withTimetoken = 1337L, // optional, pass a timetoken
            withPresence = true // also subscribe to related presence information
        )
        // snippet.end
    }

    private fun subscribeToPresenceChannelOfChannelGroup() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#subscribe-to-the-presence-channel-of-a-channel-group

        val pubnub = createPubNub()

        // snippet.subscribeToPresenceChannelOfChannelGroup
        pubnub.subscribe(
            channelGroups = listOf("cg1", "cg2"), // subscribe to channel groups
            withTimetoken = 1337L, // optional, pass a timetoken
            withPresence = true // also subscribe to related presence information
        )
        // snippet.end
    }
}
