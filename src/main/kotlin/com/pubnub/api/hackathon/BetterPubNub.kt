package com.pubnub.api.hackathon

import com.google.gson.JsonPrimitive
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.managers.MapperManager
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import java.lang.Exception
import java.util.*

class BetterPubNub internal constructor(
    private val pubnub: PubNub, private val newConfigVerification: (BetterPubNub) -> Boolean = {
        try {
            it.time().sync()
            true
        } catch (e: Exception) {
            false
        }
    }
) : IPubNub by pubnub {


    fun startAutoUpdates() {

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {

            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                if (pnMessageResult.channel == configuration.subscribeKey) {
                    synchronized(pubnub.configuration) {
                        handleConfigUpdate(
                            pubnub.mapper.convertValue(
                                pnMessageResult.message,
                                RemoteConfiguration::class.java
                            )
                        )
                    }
                }
            }
        })
        subscribe(channels = listOf(configuration.subscribeKey))
    }

    private val batcher = Batcher(pubnub = pubnub)

    private val listeners: MutableMap<SubscribeCallback, SubscribeCallback> =
        Collections.synchronizedMap(mutableMapOf())

    override fun addListener(listener: SubscribeCallback) {
        val listenerWrapper = object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                listener.status(pubnub, pnStatus)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {

                if (pnMessageResult.channel != configuration.subscribeKey) {
                    if (pnMessageResult.userMetadata == JsonPrimitive("batched") && pnMessageResult.message.isJsonArray) {
                        pnMessageResult.message.asJsonArray.forEach {
                            listener.message(pubnub, pnMessageResult.copy(message = it))
                        }
                    } else {
                        listener.message(pubnub, pnMessageResult)
                    }
                }
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                listener.presence(pubnub, pnPresenceEventResult)
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                listener.signal(pubnub, pnSignalResult)
            }

            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
                listener.messageAction(pubnub, pnMessageActionResult)
            }

            override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
                listener.objects(pubnub, objectEvent)
            }

            override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
                listener.file(pubnub, pnFileEventResult)
            }
        }
        pubnub.addListener(listenerWrapper)

        listeners.put(listener, listenerWrapper)

    }

    override fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?
    ): RemoteAction<PNPublishResult> = BatchablePublish(
        pubnub = pubnub, batcher = batcher, publish = Publish(
            pubnub = pubnub,
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = shouldStore,
            usePost = usePost,
            replicate = replicate,
            ttl = ttl
        )
    )

    fun handleConfigUpdate(remoteConfiguration: RemoteConfiguration) {
        val oldConfig = pubnub.configuration.copyRelevant()

        pubnub.disconnect()
        pubnub.configuration += remoteConfiguration

        if (!newConfigVerification(this)) {
            pubnub.configuration.copyRelevant(oldConfig)
            pubnub.configuration = pubnub.configuration
        }
        pubnub.reconnect()
        try {
            val result = pubnub.time().sync()
            println(result)
            println("Time called successfully!")
            println(pubnub.configuration)
            pubnub.reconnect()
        } catch (e: Exception) {
            pubnub.configuration.copyRelevant(oldConfig)
            pubnub.configuration = pubnub.configuration

            println("Config reverted to old: ${pubnub.configuration}")
        }
    }
}

fun betterPubNub(configuration: PNConfiguration): IPubNub {

    val mapperManager = MapperManager()

    val newConfig = configuration + retrieveConfigurationFromServer(mapperManager, configuration)

    return BetterPubNub(PubNub(newConfig))
}

private fun PNConfiguration.copyRelevant(): PNConfiguration {
    val newConfig = synchronized(this) {
        PNConfiguration(uuid = uuid).also { newC ->
            newC.origin = origin
            newC.secure = secure
            newC.logVerbosity = logVerbosity
            newC.reconnectionPolicy = reconnectionPolicy
            newC.presenceTimeout = presenceTimeout
            newC.heartbeatInterval = heartbeatInterval
            newC.subscribeTimeout = subscribeTimeout
            newC.connectTimeout = connectTimeout
            newC.nonSubscribeRequestTimeout = nonSubscribeRequestTimeout
            newC.maximumReconnectionRetries = maximumReconnectionRetries
            newC.maximumConnections = maximumConnections
            newC.requestMessageCountThreshold = requestMessageCountThreshold
            newC.subscribeKey = subscribeKey
            newC.publishKey = publishKey
        }
    }
    return newConfig
}

private fun PNConfiguration.copyRelevant(pnConfiguration: PNConfiguration) {
    synchronized(this) {
        pnConfiguration.also { newC ->
            origin = newC.origin
            secure = newC.secure
            logVerbosity = newC.logVerbosity
            reconnectionPolicy = newC.reconnectionPolicy
            presenceTimeout = newC.presenceTimeout
            heartbeatInterval = newC.heartbeatInterval
            subscribeTimeout = newC.subscribeTimeout
            connectTimeout = newC.connectTimeout
            nonSubscribeRequestTimeout = newC.nonSubscribeRequestTimeout
            maximumReconnectionRetries = newC.maximumReconnectionRetries
            maximumConnections = newC.maximumConnections
            requestMessageCountThreshold = newC.requestMessageCountThreshold
            subscribeKey = newC.subscribeKey
            publishKey = newC.publishKey
        }
    }

}


private operator fun PNConfiguration.plus(remoteConfig: RemoteConfiguration): PNConfiguration {
    synchronized(this) {
        remoteConfig.origin?.let { origin = it }
        remoteConfig.secure?.let { secure = it }
        remoteConfig.logVerbosity?.let { logVerbosity = it }
        remoteConfig.reconnectionPolicy?.let { reconnectionPolicy = it }
        remoteConfig.presenceTimeout?.let { presenceTimeout = it }
        remoteConfig.heartbeatInterval?.let { heartbeatInterval = it }
        remoteConfig.subscribeTimeout?.let { subscribeTimeout = it }
        remoteConfig.connectTimeout?.let { connectTimeout = it }
        remoteConfig.nonSubscribeRequestTimeout?.let { nonSubscribeRequestTimeout = it }
        remoteConfig.maximumReconnectionRetries?.let { maximumReconnectionRetries = it }
        remoteConfig.maximumConnections?.let { maximumConnections = it }
        remoteConfig.requestMessageCountThreshold?.let { requestMessageCountThreshold = it }
        remoteConfig.batchingMaxWindow?.let { batchingMaxWindow = it }
        remoteConfig.batchingNumberMessages?.let { batchingNumberMessages = it }
    }
    return this
}
