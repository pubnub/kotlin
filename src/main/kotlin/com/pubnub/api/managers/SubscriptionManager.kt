package com.pubnub.api.managers

import com.pubnub.api.PubNub
import com.pubnub.api.builder.PresenceOperation
import com.pubnub.api.builder.StateOperation
import com.pubnub.api.builder.SubscribeOperation
import com.pubnub.api.builder.UnsubscribeOperation
import com.pubnub.api.callbacks.ReconnectionCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.presence.Heartbeat
import com.pubnub.api.endpoints.presence.Leave
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.workers.SubscribeMessageWorker
import java.util.ArrayList
import java.util.Timer
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.timerTask

class SubscriptionManager(val pubnub: PubNub) {

    private companion object {
        private const val HEARTBEAT_INTERVAL_MULTIPLIER = 1000L
    }

    private var subscribeCall: Subscribe? = null
    private var heartbeatCall: Heartbeat? = null

    private var messageQueue: LinkedBlockingQueue<SubscribeMessage> = LinkedBlockingQueue()

    private var duplicationManager: DuplicationManager = DuplicationManager(pubnub.configuration)

    /**
     * Store the latest timetoken to subscribe with, null by default to get the latest timetoken.
     */
    private var timetoken = 0L
    private var storedTimetoken: Long? = null // when changing the channel mix, store the timetoken for a later date.

    /**
     * Keep track of Region to support PSV2 specification.
     */
    private var region: String? = null

    /**
     * Timer for heartbeat operations.
     */
    private var heartbeatTimer: Timer? = null

    private val subscriptionState = StateManager()
    internal val listenerManager = ListenerManager(pubnub)
    private val reconnectionManager = ReconnectionManager(pubnub)

    private var consumerThread: Thread? = null

    /**
     * lever to indicate if an announcement to the user about the subscription should be made.
     * the announcement happens only after the channel mix has been changed.
     */
    private var subscriptionStatusAnnounced: Boolean = false

    init {

        reconnectionManager.reconnectionCallback = object : ReconnectionCallback() {
            override fun onReconnection() {
                reconnect()
                listenerManager.announce(
                    PNStatus(
                        category = PNStatusCategory.PNReconnectedCategory,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false,
                        affectedChannels = subscriptionState.prepareChannelList(true),
                        affectedChannelGroups = subscriptionState.prepareChannelGroupList(true)
                    )
                )
                subscriptionStatusAnnounced = true
            }

            override fun onMaxReconnectionExhaustion() {
                listenerManager.announce(
                    PNStatus(
                        category = PNStatusCategory.PNReconnectionAttemptsExhausted,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false,
                        affectedChannels = subscriptionState.prepareChannelList(true),
                        affectedChannelGroups = subscriptionState.prepareChannelGroupList(true)
                    )
                )

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
            consumerThread?.start()
        }
    }

    @Synchronized
    fun getSubscribedChannels(): List<String> {
        return subscriptionState.prepareChannelList(false)
    }

    @Synchronized
    fun getSubscribedChannelGroups(): List<String> {
        return subscriptionState.prepareChannelGroupList(false)
    }

    @Synchronized
    internal fun adaptStateBuilder(stateOperation: StateOperation?) {
        subscriptionState.adaptStateBuilder(stateOperation!!)
        reconnect()
    }

    @Synchronized
    internal fun adaptSubscribeBuilder(subscribeOperation: SubscribeOperation) {
        subscriptionState.adaptSubscribeBuilder(subscribeOperation)

        // the channel mix changed, on the successful subscribe, there is going to be announcement.
        subscriptionStatusAnnounced = false
        duplicationManager.clearHistory()

        timetoken = subscribeOperation.timetoken

        // if the timetoken is not at starting position, reset the timetoken to get a connected event
        // and store the old timetoken to be reused later during subscribe.
        if (timetoken != 0L) {
            storedTimetoken = timetoken
        }
        timetoken = 0L
        reconnect()
    }

    @Synchronized
    fun reconnect() {
        startSubscribeLoop()
        registerHeartbeatTimer()
    }

    @Synchronized
    fun disconnect() {
        heartbeatTimer?.cancel()
        stopSubscribeLoop()
    }

    private fun registerHeartbeatTimer() {
        // make sure only one timer is running at a time.
        heartbeatTimer?.cancel()

        // if the interval is 0 or less, do not start the timer
        if (pubnub.configuration.heartbeatInterval <= 0) {
            return
        }

        heartbeatTimer = Timer()
        heartbeatTimer?.schedule(
            timerTask {
                performHeartbeatLoop()
            },
            0,
            pubnub.configuration.heartbeatInterval * HEARTBEAT_INTERVAL_MULTIPLIER
        )
    }

    private fun performHeartbeatLoop() {
        heartbeatCall?.silentCancel()
        val presenceChannels = subscriptionState.prepareChannelList(false)
        val presenceChannelGroups =
            subscriptionState.prepareChannelGroupList(false)
        val heartbeatChannels =
            subscriptionState.prepareHeartbeatChannelList(false)
        val heartbeatChannelGroups =
            subscriptionState.prepareHeartbeatChannelGroupList(false)
        // do not start the loop if we do not have any presence channels or channel groups enabled.
        if (presenceChannels.isEmpty()
            && presenceChannelGroups.isEmpty()
            && heartbeatChannels.isEmpty()
            && heartbeatChannelGroups.isEmpty()
        ) {
            return
        }
        val channels: MutableList<String> = ArrayList()
        channels.addAll(presenceChannels)
        channels.addAll(heartbeatChannels)
        val groups: MutableList<String> = ArrayList()
        groups.addAll(presenceChannelGroups)
        groups.addAll(heartbeatChannelGroups)
        heartbeatCall = Heartbeat(pubnub, channels, groups)
        heartbeatCall?.async { _, status ->
            val heartbeatVerbosity: PNHeartbeatNotificationOptions =
                pubnub.configuration.heartbeatNotificationOptions
            if (status.error) {
                if (heartbeatVerbosity == PNHeartbeatNotificationOptions.ALL
                    || heartbeatVerbosity == PNHeartbeatNotificationOptions.FAILURES
                ) {
                    listenerManager.announce(status)
                }
                // stop the heartbeating logic since an error happened.
                heartbeatTimer?.cancel()
            } else {
                if (heartbeatVerbosity == PNHeartbeatNotificationOptions.ALL) {
                    listenerManager.announce(status)
                }
            }
        }
    }

    private fun startSubscribeLoop() {
        // this function can be called from different points, make sure any old loop is closed
        stopSubscribeLoop()

        val combinedChannels = subscriptionState.prepareChannelList(true)
        val combinedChannelGroups = subscriptionState.prepareChannelGroupList(true)
        val stateStorage = subscriptionState.createStatePayload()

        // do not start the subscribe loop if we have no channels to subscribe to.
        if (combinedChannels.isEmpty() && combinedChannelGroups.isEmpty()) {
            return
        }

        subscribeCall = Subscribe(pubnub).apply {
            channels = combinedChannels
            channelGroups = combinedChannelGroups
            timetoken = this@SubscriptionManager.timetoken
            region = this@SubscriptionManager.region
            pubnub.configuration.isFilterExpressionKeyValid {
                filterExpression = this
            }
            state = stateStorage
        }

        subscribeCall?.async { result, status ->
            if (status.error) {
                if (status.category == PNStatusCategory.PNTimeoutCategory) {
                    startSubscribeLoop()
                    return@async
                }

                disconnect()
                listenerManager.announce(status)

                if (status.category == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // stop all announcements and ask the reconnection manager to start polling for connection restoration..
                    reconnectionManager.startPolling(pubnub.configuration)
                }

                return@async
            }

            if (!subscriptionStatusAnnounced) {
                val pnStatus = createPublicStatus(status).apply {
                    category = PNStatusCategory.PNConnectedCategory
                    error = false
                }
                subscriptionStatusAnnounced = true
                listenerManager.announce(pnStatus)
            }

            pubnub.configuration.requestMessageCountThreshold?.let {
                // todo default value of size if all ?s are null
                if (it <= result!!.messages.size) {
                    listenerManager.announce(
                        createPublicStatus(status).apply {
                            category = PNStatusCategory.PNRequestMessageCountExceededCategory
                            error = false
                        }
                    )
                }
            }

            if (result!!.messages.isNotEmpty()) {
                messageQueue.addAll(result.messages)
            }

            if (storedTimetoken != null) {
                timetoken = storedTimetoken!!
                storedTimetoken = null
            } else {
                timetoken = result.metadata.timetoken
            }

            region = result.metadata.region
            startSubscribeLoop()

        }

    }

    private fun stopSubscribeLoop() {
        subscribeCall?.silentCancel()
    }

    private fun stopHeartbeatLoop() {
        heartbeatCall?.silentCancel()
    }

    internal fun adaptPresenceBuilder(presenceOperation: PresenceOperation) {
        subscriptionState.adaptPresenceBuilder(presenceOperation)

        if (!pubnub.configuration.suppressLeaveEvents && !presenceOperation.connected) {
            Leave(pubnub).apply {
                channels = presenceOperation.channels
                channelGroups = presenceOperation.channelGroups
            }.async { _, status ->
                listenerManager.announce(status)
            }
        }

        registerHeartbeatTimer()
    }

    @Synchronized
    internal fun adaptUnsubscribeBuilder(unsubscribeOperation: UnsubscribeOperation) {
        subscriptionState.adaptUnsubscribeBuilder(unsubscribeOperation)
        subscriptionStatusAnnounced = false

        if (!pubnub.configuration.suppressLeaveEvents) {
            Leave(pubnub).apply {
                channels = unsubscribeOperation.channels
                channelGroups = unsubscribeOperation.channelGroups
            }.async { _, status ->
                listenerManager.announce(status)
            }
        }

        // if we unsubscribed from all the channels, reset the timetoken back to zero and remove the region.
        if (subscriptionState.isEmpty()) {
            region = null
            storedTimetoken = null
            timetoken = 0L
        } else {
            storedTimetoken = timetoken
            timetoken = 0L
        }

        reconnect()
    }

    @Synchronized
    fun unsubscribeAll() {
        adaptUnsubscribeBuilder(
            UnsubscribeOperation().apply {
                channels = subscriptionState.prepareChannelList(false)
                channelGroups = subscriptionState.prepareChannelGroupList(false)
            }
        )
    }

    private fun createPublicStatus(privateStatus: PNStatus): PNStatus {
        with(privateStatus) {
            return PNStatus(
                category = category,
                error = this.error,
                operation = operation,
                exception = exception,
                statusCode = statusCode,
                tlsEnabled = tlsEnabled,
                origin = origin,
                uuid = uuid,
                authKey = authKey,
                affectedChannels = affectedChannels,
                affectedChannelGroups = affectedChannelGroups
            )
        }
    }

    fun addListener(listener: SubscribeCallback) {
        listenerManager.addListener(listener)
    }

    fun removeListener(listener: SubscribeCallback) {
        listenerManager.removeListener(listener)
    }

    @Synchronized
    fun destroy(forceDestroy: Boolean = false) {
        synchronized(this) {
            disconnect()
            if (forceDestroy && consumerThread != null) {
                consumerThread!!.interrupt()
            }
        }
    }

}