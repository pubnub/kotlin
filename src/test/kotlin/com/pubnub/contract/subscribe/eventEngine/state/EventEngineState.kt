package com.pubnub.contract.subscribe.eventEngine.state

import java.util.concurrent.CopyOnWriteArrayList

class EventEngineState {
    val queuedElements: MutableList<Pair<String, String>> = CopyOnWriteArrayList()
    val channelName: String = "MyChannel_01"
    var responseMessage: String = ""
}
