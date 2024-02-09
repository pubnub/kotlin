package com.pubnub.api.managers

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.builder.ConnectedStatusAnnouncedOperation
import com.pubnub.api.builder.NoOpOperation
import com.pubnub.api.builder.PresenceOperation
import com.pubnub.api.builder.PubSubOperation
import com.pubnub.api.builder.StateOperation
import com.pubnub.api.builder.SubscribeOperation
import com.pubnub.api.builder.TimetokenRegionOperation
import com.pubnub.api.builder.UnsubscribeOperation
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.ReconnectionCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.presence.Heartbeat
import com.pubnub.api.endpoints.presence.Leave
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.workers.SubscribeMessageWorker
import java.net.SocketTimeoutException
import java.util.Timer
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.timerTask

internal class SubscriptionManager(val pubnub: PubNub, private val listenerManager: ListenerManager, private val subscriptionState: StateManager = StateManager()) {

    private companion object {
        private const val HEARTBEAT_INTERVAL_MULTIPLIER = 1000L
    }

    private var subscribeCall: Subscribe? = null
    private var heartbeatCall: Heartbeat? = null

    private var messageQueue: LinkedBlockingQueue<SubscribeMessage> = LinkedBlockingQueue()

    private var duplicationManager: DuplicationManager = DuplicationManager(pubnub.configuration)

    /**
     * Timer for heartbeat operations.
     */
    private var heartbeatTimer: Timer? = null

    private val reconnectionManager = ReconnectionManager(pubnub)

    private var consumerThread: Thread? = null

    init {
        reconnectionManager.reconnectionCallback = object : ReconnectionCallback() {
            override fun onReconnection() {
                reconnect()
                subscriptionState.subscriptionStateData(true).let {
                    listenerManager.announce(
                        PNStatus.Reconnected(
                            currentTimetoken = it.timetoken,
                            channels = it.channels,
                            channelGroups = it.channelGroups
                        )
                    )
                }
            }

            override fun onMaxReconnectionExhaustion() {
                subscriptionState.subscriptionStateData(true).let {

                    listenerManager.announce(
                        PNStatus.UnexpectedDisconnect(PubNubException("Maximum reconnect retries exhausted."))
                    )
                }

                disconnect()
            }
        }

        if (pubnub.configuration.startSubscriberThread) {
            consumerThread = Thread(
                SubscribeMessageWorker(
                    pubnub,
                    listenerManager,
                    messageQueue,
                    duplicationManager
                )
            )
            consumerThread?.name = "Subscription Manager Consumer Thread"
            consumerThread?.isDaemon = true
            consumerThread?.start()
        }
    }

    fun getSubscribedChannels(): List<String> {
        return subscriptionState.subscriptionStateData(false).channels
    }

    fun getSubscribedChannelGroups(): List<String> {
        return subscriptionState.subscriptionStateData(false).channelGroups
    }

    internal fun adaptStateBuilder(stateOperation: StateOperation) {
        subscriptionState.handleOperation(stateOperation)
    }

    internal fun adaptSubscribeBuilder(subscribeOperation: SubscribeOperation) {
        reconnect(subscribeOperation)
    }

    internal fun reconnect(
        pubSubOperation: PubSubOperation = NoOpOperation
    ) {
        startSubscribeLoop(pubSubOperation)
        registerHeartbeatTimer(NoOpOperation)
    }

    fun disconnect() {
        heartbeatTimer?.cancel()
        stopSubscribeLoop()
    }

    @Synchronized
    private fun registerHeartbeatTimer(pubSubOperation: PubSubOperation) {
        subscriptionState.handleOperation(pubSubOperation)

        // make sure only one timer is running at a time.
        heartbeatTimer?.cancel()

        // if the interval is 0 or less, do not start the timer
        if (pubnub.configuration.heartbeatInterval <= 0) {
            return
        }

        heartbeatTimer = Timer("Subscription Manager Heartbeat Timer", true)
        heartbeatTimer?.schedule(
            timerTask {
                performHeartbeatLoop()
            },
            0,
            pubnub.configuration.heartbeatInterval * HEARTBEAT_INTERVAL_MULTIPLIER
        )
    }

    @Synchronized
    private fun performHeartbeatLoop() {
        heartbeatCall?.silentCancel()

        val subscriptionStateData = subscriptionState.subscriptionStateData(false)

        // do not start the loop if we do not have any presence channels or channel groups enabled.
        if (subscriptionStateData.channels.isEmpty() &&
            subscriptionStateData.channelGroups.isEmpty() &&
            subscriptionStateData.heartbeatChannels.isEmpty() &&
            subscriptionStateData.heartbeatChannelGroups.isEmpty()
        ) {
            return
        }
        val channels = subscriptionStateData.channels + subscriptionStateData.heartbeatChannels
        val groups = subscriptionStateData.channelGroups + subscriptionStateData.heartbeatChannelGroups
        heartbeatCall = Heartbeat(pubnub, channels, groups)
        heartbeatCall?.async { result ->
            val heartbeatVerbosity = pubnub.configuration.heartbeatNotificationOptions

            if (result.isFailure) {
                if (heartbeatVerbosity == PNHeartbeatNotificationOptions.ALL ||
                    heartbeatVerbosity == PNHeartbeatNotificationOptions.FAILURES
                ) {
//                    listenerManager.announce(status) // TODO announce heartbeat status
                }
                // stop the heartbeating logic since an error happened.
                heartbeatTimer?.cancel()
            } else {
                if (heartbeatVerbosity == PNHeartbeatNotificationOptions.ALL) {
//                    listenerManager.announce(status) // TODO announce heartbeat status
                }
            }
        }
    }

    @Synchronized
    private fun startSubscribeLoop(
        vararg pubSubOperations: PubSubOperation = arrayOf(NoOpOperation)
    ) {
        // this function can be called from different points, make sure any old loop is closed
        stopSubscribeLoop()
        subscriptionState.handleOperation(*pubSubOperations)

        if (pubSubOperations.any { it is SubscribeOperation }) {
            duplicationManager.clearHistory()
        }

        val stateData = subscriptionState.subscriptionStateData(true)
        // do not start the subscribe loop if we have no channels to subscribe to.
        if (stateData.channels.isEmpty() && stateData.channelGroups.isEmpty()) {
            return
        }

        subscribeCall = Subscribe(pubnub).apply {
            channels = stateData.channels
            channelGroups = stateData.channelGroups
            timetoken = stateData.timetoken
            region = stateData.region
            filterExpression = pubnub.configuration.filterExpression.ifBlank { null }
            state = stateData.statePayload
        }.also { call ->
            call.async { result ->
                result.onFailure { exception ->
                    if (exception is SocketTimeoutException || exception.cause is SocketTimeoutException) {
                        startSubscribeLoop()
                        return@async
                    }

                    disconnect()
                    listenerManager.announce(PNStatus.UnexpectedDisconnect(exception = PubNubException.from(exception)))

                    reconnectionManager.startPolling(pubnub.configuration)
                    return@async
                }

                result.onSuccess { data ->
                    val announcedOperation = if (stateData.shouldAnnounce) {
                        listenerManager.announce(
                            PNStatus.Connected(
                                data.metadata.timetoken,
                                call.channels,
                                call.channelGroups
                            )
                        )
                        ConnectedStatusAnnouncedOperation
                    } else {
                        null
                    }


                    if (data.messages.isNotEmpty()) {
                        messageQueue.addAll(data.messages)
                    }

                    startSubscribeLoop(
                        TimetokenRegionOperation(
                            timetoken = data.metadata.timetoken,
                            region = data.metadata.region
                        ),
                        announcedOperation ?: NoOpOperation
                    )
                }
            }
        }
    }

    private fun stopSubscribeLoop() {
        subscribeCall?.silentCancel()
    }

    private fun stopHeartbeatLoop() {
        heartbeatCall?.silentCancel()
    }

    internal fun adaptPresenceBuilder(presenceOperation: PresenceOperation) {

        if (!pubnub.configuration.suppressLeaveEvents && !presenceOperation.connected) {
            Leave(pubnub).apply {
                channels = presenceOperation.channels
                channelGroups = presenceOperation.channelGroups
            }.async { result ->
                result.onSuccess {
                    listenerManager.announce(
                        PNStatus.Leave(
                            presenceOperation.channels,
                            presenceOperation.channelGroups
                        )
                    )
                }
            }
        }

        registerHeartbeatTimer(presenceOperation)
    }

    internal fun adaptUnsubscribeBuilder(unsubscribeOperation: UnsubscribeOperation) {
        if (!pubnub.configuration.suppressLeaveEvents) {
            Leave(pubnub).apply {
                channels = unsubscribeOperation.channels
                channelGroups = unsubscribeOperation.channelGroups
            }.async { result ->
                result.onSuccess {
                    listenerManager.announce(PNStatus.Leave(
                        unsubscribeOperation.channels,
                        unsubscribeOperation.channelGroups
                    ))
                }
            }
        }
        reconnect(pubSubOperation = unsubscribeOperation)
    }

    fun unsubscribeAll() {
        subscriptionState.subscriptionStateData(false).let {
            adaptUnsubscribeBuilder(
                UnsubscribeOperation(
                    channels = it.channels,
                    channelGroups = it.channelGroups
                )
            )
        }
    }

    fun addListener(listener: SubscribeCallback) {
        listenerManager.addListener(listener)
    }

    fun removeListener(listener: Listener) {
        listenerManager.removeListener(listener)
    }

    @Synchronized
    fun destroy(forceDestroy: Boolean = false) {
        disconnect()
        if (forceDestroy && consumerThread != null) {
            consumerThread!!.interrupt()
        }
    }
}
