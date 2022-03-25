package com.pubnub.api.subscribe.internal

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.*
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass

internal class SubscribeEffectDispatcher(
    private val httpHandler: EffectHandlerFactory<SubscribeHttpEffectInvocation>,
    private val retryEffectExecutor: EffectHandlerFactory<ScheduleRetry>,
    private val newMessagesEffectExecutor: EffectHandlerFactory<NewMessages>,
    private val newStateEffectExecutor: EffectHandlerFactory<NewState>
) : EffectDispatcher<SubscribeEffectInvocation> {

    override fun dispatch(effect: SubscribeEffectInvocation) {
        when (effect) {
            is CancelEffectInvocation -> TODO()
            is SubscribeHttpEffectInvocation -> httpHandler.handler(effect)
            is NewState -> newStateEffectExecutor.handler(effect)
            is NewMessages -> newMessagesEffectExecutor.handler(effect)
            is ScheduleRetry -> retryEffectExecutor.handler(effect)
        }
    }

    fun cancel() {
    }
}

internal class NewStateEffectExecutor(private val listenerManager: ListenerManager) : EffectHandlerFactory<NewState> {
    private var previousStateRef: AtomicReference<NewState> = AtomicReference()

    override fun handler(effect: NewState): EffectHandler {
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
        TODO()
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
    private val pubnub: PubNub, private val eventQueue: LinkedBlockingQueue<SubscribeEvent>
) : EffectHandlerFactory<SubscribeHttpEffectInvocation> {

    override fun handler(effect: SubscribeHttpEffectInvocation): EffectHandler {
        return when (effect) {
            is SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation -> {
                pubnub.handshake(
                    channels = effect.subscriptionStatus.channels.toList(),
                    channelGroups = effect.subscriptionStatus.groups.toList()
                ) { r, s ->
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
                }
                TODO()
            }
            is SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation -> {
                pubnub.receiveMessages(
                    channels = effect.subscriptionStatus.channels.toList(),
                    channelGroups = effect.subscriptionStatus.groups.toList(),
                    timetoken = effect.subscriptionStatus.cursor!!.timetoken, //TODO figure out how to drop !! here
                    region = effect.subscriptionStatus.cursor.region
                ) { r, s ->
                    eventQueue.put(
                        if (!s.error) {
                            ReceivingResult.ReceivingSucceeded(r!!)
                        } else {
                            ReceivingResult.ReceivingFailed(s)
                        }
                    )

                }
                TODO()
            }
        }
    }
}

internal class RetryEffectExecutor(
    private val effectQueue: LinkedBlockingQueue<SubscribeEffectInvocation>,
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(3),
    private val retryPolicy: RetryPolicy = NoPolicy
) : EffectHandlerFactory<ScheduleRetry> {
    override fun handler(effect: ScheduleRetry): EffectHandler {
        executor.schedule(Callable {
            effectQueue.put(effect.retryableEffect)
        }, retryPolicy.computeDelay(effect.retryCount).seconds, TimeUnit.SECONDS).let {
            {
                it.cancel(true)
            }
        }
        TODO()
    }
}

internal interface IncomingPayloadProcessor {
    fun processIncomingPayload(message: SubscribeMessage)
}

internal class NewMessagesEffectExecutor(private val processor: IncomingPayloadProcessor) :
    EffectHandlerFactory<NewMessages> {
    override fun handler(effect: NewMessages): EffectHandler {
        effect.messages.forEach {
            processor.processIncomingPayload(it)
        }
        return TODO()
    }
}
