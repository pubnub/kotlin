package com.pubnub.contract.subscribe.eventEngine.state

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.contract.state.World
import com.pubnub.contract.state.WorldState
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList

class EventEngineState(world: World) : WorldState by world {
    val queuedElements: MutableList<Pair<String, String>> = CopyOnWriteArrayList()
    val channelName: String = "MyChannel_01"
    val messagesList: MutableList<PNEvent> = CopyOnWriteArrayList()
    val statusesList: MutableList<PNStatus> = CopyOnWriteArrayList()
    val pubnub: PubNub by lazy {
        PubNub(
            configuration,
            eventEngineConf = EventEngineConfTestImpl(queuedElements),
            retryPolicy = configuration.retryPolicy(linearDelay = Duration.ofMillis(1))
        )
    }
}
