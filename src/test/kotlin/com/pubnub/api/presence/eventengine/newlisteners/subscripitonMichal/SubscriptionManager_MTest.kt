package com.pubnub.api.presence.eventengine.newlisteners.subscripitonMichal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import org.junit.Test

class SubscriptionManager_MTest{

    //    ≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠Michał's proposal
    // I am not sure about:
    // 1.onMessage separated. It can be assigned after subscription creation.
    // 2.Not sure if subscribe "subscribeChannel" is needed. I think it is enough to provide "subscribeChannels" that can be used to subscribe just to one channel.
    // 3.Discuss onMessage as parameter. pubnub.subscribeChannel_M(channel = channel, onMessage = myOnMessage)
    // 4.


    val pubnub = PubNub(PNConfiguration(UserId("testUser")))
    @Test
    fun `subscribe with one channel add onMessage handler then unsubscribe`() {
        val channel = "Channel01"
        val myOnMessageForSub01: (String) -> Unit = { println("-=Handling MESSAGE for one channel: Channel01. Message: $it") } //Consumer can be replaced by (String) -> Unit
        val subscription = pubnub.subscribeChannel_M(channel)
        subscription.onMessage = myOnMessageForSub01

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        subscription.cancel()                                                       // = remove subscription from subscriptionManger_M#channelSubscriptions
        println("-=after cancel. Should not get ")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))

    }

    @Test
    fun `subscribe with two channels add different onMessage to each of them then unsubscribe one`() {
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myOnMessageForSub01: (String) -> Unit = { println("-=Handling MESSAGE for one channel: Channel01. Message: $it") }
        val myOnMessageForSub02: (String) -> Unit = { println("-=Handling MESSAGE for one channel: Channel02. Message: $it") }
        val channels = setOf(channel01, channel02)

        val subscriptions = pubnub.subscribeChannels_M(channels)
        subscriptions[channel01]?.onMessage = myOnMessageForSub01
        subscriptions[channel02]?.onMessage = myOnMessageForSub02

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        subscriptions[channel01]?.cancel()
        println("-=After cancel. Should not get message for channel01")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }


    @Test
    fun `subscribe with two channels add the same onMessage to both of then then unsubsribe one`() {
        // given
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for two channels: Channel01, Channel02. Message: $it") }
        val channels = setOf(channel01, channel02)

        // when
        val subscriptions = pubnub.subscribeChannels_M(channels)  //todo add possibility to add onMessage in constructor
        subscriptions.onMessage = myOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        subscriptions[channel01]?.cancel()
        println("-=After cancel. Should not get message for channel01")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    @Test
    fun `subscribe with two channels add the same onMessage to both of then then unsubsribe both`() {
        // given
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for two channels: Channel01, Channel02. Message: $it") }
        val channels = setOf(channel01, channel02)

        // when
        val subscriptions = pubnub.subscribeChannels_M(channels)  //todo add possibility to add onMessage in constructor
        subscriptions.onMessage = myOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        subscriptions.cancel()
        Thread.sleep(1000)
        println("-=After cancel. Should not get message for channel01 and channel02")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    @Test
    fun `subscribe with one channel add onMessage to constructor of subscription then unsubsribe`() {
        val channel = "Channel01"
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for one channel: Channel01. Message: $it") }

        val subscription = pubnub.subscribeChannel_M(channel = channel, onMessage = myOnMessage)
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        subscription.cancel()
        println("-=After cancel. Should not get message for channel01")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
    }

    @Test
    fun `subscribe with two channels add onMessage to constructor of subscription then unsubscribe one`() {
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val channels = setOf(channel01, channel02)
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for one channel: Channel01. Message: $it") }

        val subscriptions = pubnub.subscribeChannels_M(channels = channels, onMessage = myOnMessage)
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        subscriptions[channel01]?.cancel()    //todo channel might be confused with cancel
        println("-=After cancel. Should not get message for channel01")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    @Test
    fun `subscribe with two channels add the same onMessage to both of them then pause one then resume it`() {
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for two channels: Channel01, Channel02. Message: $it") }
        val channels = setOf(channel01, channel02)

        val subscriptions = pubnub.subscribeChannels_M(channels)
        subscriptions.onMessage = myOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))

        subscriptions[channel01]?.pauseOnMessage()
        println("After pauseOnMessage on Channel01. Should not get messages for that channel.")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        subscriptions[channel01]?.resumeOnMessage()
        println("After resumeOnMessage on Channel01. Should get messages for that channel.")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    @Test
    fun `subcribe with two channels add the same onMessage to both of them then pause both then resume`() {
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for two channels: Channel01, Channel02. Message: $it") }
        val channels = setOf(channel01, channel02)

        val subscriptions = pubnub.subscribeChannels_M(channels)
        subscriptions.onMessage = myOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))

        subscriptions.pauseOnMessage()
        println("After pauseOnMessage on Channel01 and Channel02. Should not get messages for those channels.")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        subscriptions.resumeOnMessage()
        println("After resumeOnMessage on Channel01 and Channel02. Should get messages for those channels.")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    // ###Global
    @Test
    fun `subscribe with one channel add onMessage globally then unsubscribe all`() {
        val channel01 = "Channel01"
        val myGlobalOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for one global. Message: $it") }

        val subscriptions = pubnub.subscribeChannel_M(channel01)
        pubnub.onMessage = myGlobalOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))

        pubnub.unsubscribeAll_M()

        println("-=After unsubscribeALL. Should not get messages for any channel.")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    @Test
    fun `subscribe one channel in two subscription add onMessage globally then unsubscribe globally from one subscription`() {
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myGlobalOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for for global. Message: $it") }

        val subscription =
            pubnub.subscribeChannel_M(channel01) ///<--jak usuwamy channel to z pojedynczej subskrypcji nie usuwa.
        val subscription02 = pubnub.subscribeChannels_M(setOf(channel01, channel02))
        pubnub.onMessage = myGlobalOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))

        pubnub.unsubscribe_M(setOf(channel01))

        println("-=After unsubscribe Channel01. Should not get messages for Channel01.")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    @Test
    fun `subscribe with two channels add onMessage globally then override onMessage for one subscriptionChannel`() {
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myGlobalOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for for global. Message: $it") }
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for one channel: Channel01. Message: $it") }

        val subscription = pubnub.subscribeChannels_M(setOf(channel01, channel02))
        pubnub.onMessage = myGlobalOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))

        subscription[channel01]?.onMessage = myOnMessage

        println("-=OnMessage changed for Channel01. ")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

    @Test
    fun `subscribe with two channels add onMessage globally then pause then resume globally`() {
        val channel01 = "Channel01"
        val channel02 = "Channel02"
        val myGlobalOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for for global. Message: $it") }
        val myOnMessage: (String) -> Unit = { println("-=Handling MESSAGE for one channel: Channel01. Message: $it") }

        val subscriptions = pubnub.subscribeChannels_M(setOf(channel01, channel02))
        pubnub.onMessage = myGlobalOnMessage

        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))

        pubnub.pauseOnMessage()
        println("-=After pauseOnMessage should not get any message")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
        pubnub.resumeOnMessage()
        println("-=After resumeOnMessage should get messages")
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel01"))
        pubnub.subscriptionManger_M.simulateReceiveMessage(setOf("Channel02"))
    }

}