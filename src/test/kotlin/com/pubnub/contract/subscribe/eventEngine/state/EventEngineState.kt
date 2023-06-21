package com.pubnub.contract.subscribe.eventEngine.state

import java.util.concurrent.CopyOnWriteArrayList

class EventEngineState {
    // list of events and invocations
    var eventEngineHappenings: List<HashMap<String, String>> = CopyOnWriteArrayList()
    val channelName: String = "MyChannel_01"
    var responseMessage: String = ""
}
