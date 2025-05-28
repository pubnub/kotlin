package com.pubnub.docs.publishAndSubscribe.signal

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.docs.SnippetBase

class SignalOthers : SnippetBase() {
    private fun signalBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-2

        // snippet.signalBasic
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }
        val pubnub = PubNub.create(configBuilder.build())
        val channel = pubnub.channel("myChannel")

        channel.signal(
            message = "This is a signal!",
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { signalResult ->
                println("Signal sent, timetoken: ${signalResult.timetoken}")
            }
        }
        // snippet.end
    }
}
