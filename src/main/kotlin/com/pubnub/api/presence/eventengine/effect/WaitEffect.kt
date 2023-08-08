package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import java.util.Timer
import kotlin.concurrent.timerTask

private const val HEARTBEAT_INTERVAL_MULTIPLIER = 1000L

class WaitEffect( // todo handle pubnub.configuration.heartbeatInterval <= 0
    private val heartbeatIntervalInSec: Int, // todo if the interval is 0 or less, do not start the timer
    private val presenceEventSink: Sink<PresenceEvent>, // todo this should be check at PresenceEventEngineCreation
    private var waitForNextHeartbeatTimer: Timer = Timer("Heartbeat Timer")
) : ManagedEffect {

    override fun runEffect() {
        waitForNextHeartbeatTimer.schedule(
            timerTask {
                presenceEventSink.add(PresenceEvent.TimesUp)
            },
            0,
            heartbeatIntervalInSec * HEARTBEAT_INTERVAL_MULTIPLIER // todo do we want to have it going endlessly and have "CancelWait" to cancel it
        )
    }

    override fun cancel() {
        waitForNextHeartbeatTimer.cancel()
    }
}
