package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.state.*
import com.pubnub.api.state.internal.QueuedEngine
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

internal class PresenceEffectDispatcher private constructor(
    private val eventQueue: LinkedBlockingQueue<PresenceEvent>,
    private val longRunningEffectsTracker: LongRunningEffectsTracker,
    private val httpExecutor: EffectExecutor<PresenceHttpEffectInvocation>,
    private val scheduledExecutorService: ScheduledExecutorService,
    private val queuedEngine: QueuedEngine<PresenceEffectInvocation>
) : EffectDispatcher<PresenceEffectInvocation> {
    companion object {
        fun create(
            pubnub: PubNub,
            eventQueue: LinkedBlockingQueue<PresenceEvent>,
            effectQueue: LinkedBlockingQueue<PresenceEffectInvocation>
        ): PresenceEffectDispatcher {
            val longRunningEffectsTracker = LongRunningEffectsTracker()
            val queuedEngine = QueuedEngine(inputQueue = effectQueue, executorService = Executors.newFixedThreadPool(1))
            val httpExecutor = HttpCallExecutor(pubnub = pubnub, eventQueue = eventQueue)
            return PresenceEffectDispatcher(
                longRunningEffectsTracker = longRunningEffectsTracker,
                httpExecutor = httpExecutor,
                scheduledExecutorService = Executors.newScheduledThreadPool(2),
                eventQueue = eventQueue,
                queuedEngine = queuedEngine
            ).apply {
                queuedEngine.run(this::dispatch)
            }
        }
    }

    override fun dispatch(effect: PresenceEffectInvocation) {
        when (effect) {
            is CancelEffectInvocation -> {}
            is PresenceHttpEffectInvocation -> longRunningEffectsTracker.track(effect) {
                httpExecutor.execute(effect)
            }
            is NewState -> {}
            is TimerEffectInvocation -> longRunningEffectsTracker.track(effect) {
                scheduledExecutorService.schedule({
                    eventQueue.put(effect.event)
                }, 1000, TimeUnit.MILLISECONDS).let {
                    { it.cancel(true) }
                }
            }
        }

    }

    override fun cancel() {
        queuedEngine.cancel()
    }

}

internal class HttpCallExecutor(
    private val pubnub: PubNub, private val eventQueue: LinkedBlockingQueue<PresenceEvent>
) : EffectExecutor<PresenceHttpEffectInvocation> {

    override fun execute(effect: PresenceHttpEffectInvocation, longRunningEffectDone: (String) -> Unit): CancelFn {
        return when (effect) {
            is IAmAwayEffectInvocation -> pubnub.iAmAway(
                channels = effect.channels.toList(), channelGroups = effect.channelGroups.toList()
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

            is IAmHereEffectInvocation -> pubnub.iAmHere(
                channels = effect.channels.toList(), channelGroups = effect.channelGroups.toList()
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
