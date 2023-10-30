package com.pubnub.internal.presence.eventengine.data

import java.util.concurrent.ConcurrentHashMap

internal class PresenceData {
    internal val channelStates: ConcurrentHashMap<String, Any> = ConcurrentHashMap()
}
