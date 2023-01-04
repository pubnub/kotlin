package com.pubnub.contract.subscribe

import com.pubnub.api.models.consumer.pubsub.PNEvent
import java.util.concurrent.CopyOnWriteArrayList

class SubscribeState {
    val messagesList: MutableList<PNEvent> = CopyOnWriteArrayList()
}
