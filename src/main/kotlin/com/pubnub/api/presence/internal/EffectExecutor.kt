package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.state.CancelFn
import com.pubnub.api.state.EffectExecutor
import java.util.concurrent.LinkedBlockingQueue

internal class HttpCallExecutor(
    private val pubNub: PubNub,
    private val eventQueue: LinkedBlockingQueue<PresenceEvent>
) : EffectExecutor<PresenceHttpEffect> {

    override fun execute(effect: PresenceHttpEffect, longRunningEffectDone: (String) -> Unit): CancelFn {
        return when (effect) {
            is IAmAwayEffect -> pubNub.iAmAway(
                channels = effect.channels.toList(),
                channelGroups = effect.channelGroups.toList()
            ) { _, s ->
                longRunningEffectDone(effect.id)
                eventQueue.put(
                    if (!s.error) {
                        IAmAway.Succeed
                    } else {
                        IAmAway.Failed(s)
                    }
                )
            }.let { { it.silentCancel() } }

            is IAmHereEffect -> pubNub.iAmHere(
                channels = effect.channels.toList(),
                channelGroups = effect.channelGroups.toList()
            ) { _, s ->
                longRunningEffectDone(effect.id)
                eventQueue.put(
                    if (!s.error) {
                        IAmHere.Succeed
                    } else {
                        IAmHere.Failed(s)
                    }
                )
            }.let { { it.silentCancel() } }
        }
    }
}
