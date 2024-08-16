package com.pubnub.api.java.callbacks

import com.pubnub.api.java.PubNubForJava
import com.pubnub.api.java.v2.callbacks.EventListener
import com.pubnub.api.java.v2.callbacks.StatusListener
import com.pubnub.api.models.consumer.PNStatus

abstract class SubscribeCallback : StatusListener, EventListener {
    open class BaseSubscribeCallback : SubscribeCallback(), EventListener {
        override fun status(pubnub: PubNubForJava, pnStatus: PNStatus) {
        }
    }
}
