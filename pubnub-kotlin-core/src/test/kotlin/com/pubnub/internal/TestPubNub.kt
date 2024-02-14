package com.pubnub.internal

import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf

internal class TestPubNub(configuration: PNConfiguration, listenerManager: ListenerManager? = null, eventEnginesConf: EventEnginesConf = EventEnginesConf()) : BasePubNub(configuration) {
    override val listenerManager: ListenerManager = listenerManager ?: ListenerManager(this)
}