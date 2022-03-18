package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.*
import com.pubnub.api.state.internal.QueuedEngine
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass

internal class SubscribeEffectDispatcher(
    private val queuedEngine: QueuedEngine<SubscribeEffectInvocation>,
    private val longRunningEffectsTracker: LongRunningEffectsTracker,
    private val httpEffectExecutor: EffectExecutor<SubscribeHttpEffectInvocation>,
    private val retryEffectExecutor: EffectExecutor<ScheduleRetry>,
    private val newMessagesEffectExecutor: EffectExecutor<NewMessages>,
    private val newStateEffectExecutor: EffectExecutor<NewState>
) : EffectDispatcher<SubscribeEffectInvocation> {

    companion object {
        fun create(
            pubnub: PubNub,
            eventQueue: LinkedBlockingQueue<SubscribeEvent>,
            effectQueue: LinkedBlockingQueue<SubscribeEffectInvocation>,
            retryPolicy: RetryPolicy,
            incomingPayloadProcessor: IncomingPayloadProcessor,
            listenerManager: ListenerManager
        ): SubscribeEffectDispatcher {
            val longRunningEffectsTracker = LongRunningEffectsTracker()
            val httpEffectExecutor = HttpCallExecutor(
                pubNub = pubnub, eventQueue = eventQueue
            )
            val retryEffectExecutor = RetryEffectExecutor(
                effectQueue = effectQueue, retryPolicy = retryPolicy
            )
            val newMessagesEffectExecutor = NewMessagesEffectExecutor(
                incomingPayloadProcessor
            )
            val newStateEffectExecutor = NewStateEffectExecutor(listenerManager)
            val queuedEngine = QueuedEngine(inputQueue = effectQueue, executorService = Executors.newFixedThreadPool(1))
            return SubscribeEffectDispatcher(
                longRunningEffectsTracker = longRunningEffectsTracker,
                httpEffectExecutor = httpEffectExecutor,
                retryEffectExecutor = retryEffectExecutor,
                newMessagesEffectExecutor = newMessagesEffectExecutor,
                newStateEffectExecutor = newStateEffectExecutor,
                queuedEngine = queuedEngine
            ).apply {
                queuedEngine.run(this::dispatch)
            }
        }
    }

    private val logger = LoggerFactory.getLogger(SubscribeEffectDispatcher::class.java)

    override fun dispatch(effect: SubscribeEffectInvocation) {
        when (effect) {
            is CancelEffectInvocation -> longRunningEffectsTracker.cancel(effect.idToCancel)
            is SubscribeHttpEffectInvocation -> longRunningEffectsTracker.track(effect) {
                httpEffectExecutor.execute(
                    effect, longRunningEffectDone = longRunningEffectsTracker::stopTracking
                )
            }
            is NewState -> {
                newStateEffectExecutor.execute(effect)
                logger.info("New state: $effect")
            }
            is NewMessages -> {
                newMessagesEffectExecutor.execute(effect)
                logger.info(
                    "New messages. Hopefully they're fine ;) ${effect.messages}"
                )
            }
            is ScheduleRetry -> longRunningEffectsTracker.track(effect) {
                retryEffectExecutor.execute(
                    effect, longRunningEffectDone = longRunningEffectsTracker::stopTracking
                )
            }
        }
    }

    override fun cancel() {
        queuedEngine.cancel()
    }
}

internal class NewStateEffectExecutor(private val listenerManager: ListenerManager) : EffectExecutor<NewState> {
    private var previousStateRef: AtomicReference<NewState> = AtomicReference()

    override fun execute(effect: NewState, longRunningEffectDone: (String) -> Unit): CancelFn {
        val previousState = previousStateRef.getAndSet(effect)

        when {
            transition(
                previousState, effect, Handshaking::class, Receiving::class
            ) -> listenerManager.announce(
                PNStatus(
                    PNStatusCategory.PNConnectedCategory,
                    error = false,
                    operation = PNOperationType.PNSubscribeOperation
                )
            )
            transition(previousState, effect, Reconnecting::class, Receiving::class) -> listenerManager.announce(
                PNStatus(
                    PNStatusCategory.PNReconnectedCategory,
                    error = false,
                    operation = PNOperationType.PNSubscribeOperation
                )
            )

            state(effect, ReconnectingFailed::class) -> listenerManager.announce(
                PNStatus(
                    PNStatusCategory.PNReconnectionAttemptsExhausted,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )

            )

        }

        return {}
    }

    private fun <T : SubscribeState> state(new: NewState, state: KClass<T>): Boolean {
        return new.name == state.simpleName!!
    }

    private fun <T : SubscribeState, U : SubscribeState> transition(
        prev: NewState?, new: NewState, from: KClass<T>, to: KClass<U>
    ): Boolean {
        return prev != null && prev.name == from.simpleName!! && new.name == to.simpleName!!
    }
}

internal class HttpCallExecutor(
    private val pubNub: PubNub, private val eventQueue: LinkedBlockingQueue<SubscribeEvent>
) : EffectExecutor<SubscribeHttpEffectInvocation> {

    override fun execute(effect: SubscribeHttpEffectInvocation, longRunningEffectDone: (String) -> Unit): CancelFn {
        return when (effect) {
            is SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation -> {
                pubNub.handshake(
                    channels = effect.subscriptionStatus.channels.toList(),
                    channelGroups = effect.subscriptionStatus.groups.toList()
                ) { r, s ->
                    longRunningEffectDone(effect.id)
                    eventQueue.put(
                        if (!s.error) {
                            HandshakeResult.HandshakeSucceeded(
                                Cursor(
                                    timetoken = r!!.metadata.timetoken, //TODO we could improve callback to avoid !! here
                                    region = r.metadata.region
                                )
                            )
                        } else {
                            HandshakeResult.HandshakeFailed(s)
                        }
                    )
                }.let { { it.silentCancel() } }
            }
            is SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation -> pubNub.receiveMessages(
                channels = effect.subscriptionStatus.channels.toList(),
                channelGroups = effect.subscriptionStatus.groups.toList(),
                timetoken = effect.subscriptionStatus.cursor!!.timetoken, //TODO figure out how to drop !! here
                region = effect.subscriptionStatus.cursor.region
            ) { r, s ->
                longRunningEffectDone(effect.id)
                eventQueue.put(
                    if (!s.error) {
                        ReceivingResult.ReceivingSucceeded(r!!)
                    } else {
                        ReceivingResult.ReceivingFailed(s)
                    }
                )
            }.let { { it.silentCancel() } }
        }
    }
}

internal class RetryEffectExecutor(
    private val effectQueue: LinkedBlockingQueue<SubscribeEffectInvocation>,
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(3),
    private val retryPolicy: RetryPolicy = NoPolicy
) : EffectExecutor<ScheduleRetry> {
    override fun execute(effect: ScheduleRetry, longRunningEffectDone: (String) -> Unit): CancelFn {
        return executor.schedule(Callable {
            longRunningEffectDone(effect.id)
            effectQueue.put(effect.retryableEffect)
        }, retryPolicy.computeDelay(effect.retryCount).seconds, TimeUnit.SECONDS).let {
            {
                it.cancel(true)
            }
        }
    }
}

internal interface IncomingPayloadProcessor {
    fun processIncomingPayload(message: SubscribeMessage)
}

internal class NewMessagesEffectExecutor(private val processor: IncomingPayloadProcessor) :
    EffectExecutor<NewMessages> {
    override fun execute(effect: NewMessages, longRunningEffectDone: (String) -> Unit): CancelFn {
        effect.messages.forEach {
            processor.processIncomingPayload(it)
        }
        return {}
    }
}
