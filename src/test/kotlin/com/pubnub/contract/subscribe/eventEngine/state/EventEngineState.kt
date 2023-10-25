package com.pubnub.contract.subscribe.eventEngine.state

import com.pubnub.api.PubNub
import com.pubnub.api.eventengine.QueueEventEngineConf
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.contract.state.World
import com.pubnub.contract.state.WorldState
import java.util.concurrent.CopyOnWriteArrayList

internal fun testEventEnginesConf(
    subscribeQueuedElements: MutableList<Pair<String, String>>,
    presenceQueuedElements: MutableList<Pair<String, String>>,

) = EventEnginesConf(
    QueueEventEngineConf(TestSinkSource(subscribeQueuedElements), TestSinkSource(subscribeQueuedElements)),
    QueueEventEngineConf(TestSinkSource(presenceQueuedElements), TestSinkSource(presenceQueuedElements))
)

class EventEngineState(world: World) : WorldState by world {
    val subscribeQueuedElements: MutableList<Pair<String, String>> = CopyOnWriteArrayList()
    val presenceQueuedElements: MutableList<Pair<String, String>> = CopyOnWriteArrayList()
    val channelName: String = "MyChannel_01"
    val messagesList: MutableList<PNEvent> = CopyOnWriteArrayList()
    val statusesList: MutableList<PNStatus> = CopyOnWriteArrayList()
    val pubnub: PubNub by lazy {
        PubNub(
            configuration,
            eventEnginesConf = testEventEnginesConf(subscribeQueuedElements, presenceQueuedElements)
        )
    }
}
