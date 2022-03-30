package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.state.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

internal class PresenceEffectDispatcher(
    private val eventQueue: LinkedBlockingQueue<PresenceEvent>,
    private val httpExecutor: EffectHandlerFactory<PresenceHttpEffectInvocation>,
    private val scheduledExecutorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(),
    override val trackedHandlers: MutableMap<String, ManagedEffectHandler> = mutableMapOf(),
) : EffectDispatcher<PresenceEffectInvocation>, EffectTracker {

    override fun dispatch(effect: PresenceEffectInvocation) {
        val handler = when (effect) {
            is CancelEffectInvocation -> {
                stopTracking(effect.idToCancel)
                null
            }
            is PresenceHttpEffectInvocation -> httpExecutor.handler(effect)
            is NewState -> {
                null
            }
            is TimerEffectInvocation -> {
                scheduledExecutorService.schedule({
                    eventQueue.put(effect.event)
                }, 1000, TimeUnit.MILLISECONDS).let {
                    { it.cancel(true) }
                }
                null
            }
        }
        handler?.start()
        if (handler is ManagedEffectHandler) {
            startTracking(effect.id(), handler)
        }

    }

    fun cancel() {

    }
}

internal class HttpCallExecutor(
    private val pubnub: PubNub, private val eventQueue: LinkedBlockingQueue<PresenceEvent>
) : EffectHandlerFactory<PresenceHttpEffectInvocation> {

    override fun handler(effect: PresenceHttpEffectInvocation): EffectHandler {
        return when (effect) {
            is IAmAwayEffectInvocation -> {
                pubnub.iAmAway(
                    channels = effect.channels.toList(), channelGroups = effect.channelGroups.toList()
                ) { _, s ->
                    eventQueue.put(
                        if (!s.error) {
                            IAmAway.Succeed
                        } else {
                            IAmAway.Failed(s)
                        }
                    )
                }
                TODO()
            }

            is IAmHereEffectInvocation -> {
                pubnub.iAmHere(
                    channels = effect.channels.toList(), channelGroups = effect.channelGroups.toList()
                ) { _, s ->
                    eventQueue.put(
                        if (!s.error) {
                            IAmHere.Succeed
                        } else {
                            IAmHere.Failed(s)
                        }
                    )
                }
                TODO()
            }
        }
    }
}
