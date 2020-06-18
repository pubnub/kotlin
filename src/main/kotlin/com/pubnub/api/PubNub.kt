package com.pubnub.api

import com.pubnub.api.builder.PresenceBuilder
import com.pubnub.api.builder.SubscribeBuilder
import com.pubnub.api.builder.UnsubscribeBuilder
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.endpoints.History
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.access.Grant
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.endpoints.presence.WhereNow
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.managers.BasePathManager
import com.pubnub.api.managers.MapperManager
import com.pubnub.api.managers.PublishSequenceManager
import com.pubnub.api.managers.RetrofitManager
import com.pubnub.api.managers.SubscriptionManager
import com.pubnub.api.managers.TelemetryManager
import com.pubnub.api.vendor.Crypto
import java.util.Date
import java.util.UUID

class PubNub(val configuration: PNConfiguration) {

    private companion object Constants {
        private const val TIMESTAMP_DIVIDER = 1000
        private const val SDK_VERSION = "4.0.0-dev"
        private const val MAX_SEQUENCE = 65535
    }

    private val basePathManager = BasePathManager(configuration)

    val mapper = MapperManager()
    internal val retrofitManager = RetrofitManager(this)
    internal val publishSequenceManager = PublishSequenceManager(MAX_SEQUENCE)
    internal val telemetryManager = TelemetryManager()
    internal val subscriptionManager = SubscriptionManager(this)

    val version = SDK_VERSION
    val instanceId = UUID.randomUUID().toString()

    fun baseUrl() = basePathManager.basePath()
    fun requestId() = UUID.randomUUID().toString()
    fun timestamp() = (Date().time / TIMESTAMP_DIVIDER).toInt()

    fun publish() = Publish(this)
    fun fire() = Publish(this).apply {
        shouldStore = false
        replicate = false
    }

    fun signal() = Signal(this)
    fun subscribe() = SubscribeBuilder(subscriptionManager)
    fun unsubscribe() = UnsubscribeBuilder(subscriptionManager)
    fun presence() = PresenceBuilder(subscriptionManager)

    fun addPushNotificationsOnChannels() = AddChannelsToPush(this)
    fun removePushNotificationsFromChannels() =
        RemoveChannelsFromPush(this)

    fun removeAllPushNotificationsFromDeviceWithPushToken() =
        RemoveAllPushChannelsForDevice(this)

    fun auditPushChannelProvisions() = ListPushProvisions(this)

    fun history() = History(this)
    fun messageCounts() = MessageCounts(this)
    fun fetchMessages() = FetchMessages(this)
    fun deleteMessages() = DeleteMessages(this)
    fun hereNow() = HereNow(this)
    fun whereNow() = WhereNow(this)
    fun setPresenceState() = SetState(this)
    fun getPresenceState() = GetState(this)
    fun time() = Time(this)
    fun addMessageAction() = AddMessageAction(this)
    fun getMessageActions() = GetMessageActions(this)
    fun removeMessageAction() = RemoveMessageAction(this)

    fun listAllChannelGroups() = ListAllChannelGroup(this)
    fun listChannelsForChannelGroup() = AllChannelsChannelGroup(this)
    fun addChannelsToChannelGroup() = AddChannelChannelGroup(this)
    fun removeChannelsFromChannelGroup() = RemoveChannelChannelGroup(this)
    fun deleteChannelGroup() = DeleteChannelGroup(this)

    fun grant() = Grant(this)

    fun addListener(listener: SubscribeCallback) {
        subscriptionManager.addListener(listener)
    }

    fun removeListener(listener: SubscribeCallback) {
        subscriptionManager.removeListener(listener)
    }

    fun getSubscribedChannels() = subscriptionManager.getSubscribedChannels()
    fun getSubscribedChannelGroups() = subscriptionManager.getSubscribedChannelGroups()
    fun unsubscribeAll() = subscriptionManager.unsubscribeAll()

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    fun decrypt(inputString: String): String {
        return decrypt(inputString, configuration.cipherKey)
    }

    /**
     * Perform Cryptographic decryption of an input string using the cipher key
     *
     * @param inputString String to be encrypted
     * @param cipherKey cipher key to be used for encryption
     * @return String containing the encryption of inputString using cipherKey
     * @throws PubNubException throws exception in case of failed encryption
     */
    fun decrypt(inputString: String, cipherKey: String): String {
        return Crypto(cipherKey).decrypt(inputString)
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    fun encrypt(inputString: String): String {
        return encrypt(inputString, configuration.cipherKey)
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key.
     *
     * @param inputString String to be encrypted
     * @param cipherKey cipher key to be used for encryption
     * @return String containing the encryption of inputString using cipherKey
     * @throws PubNubException throws exception in case of failed encryption
     */
    @Throws(PubNubException::class)
    fun encrypt(inputString: String, cipherKey: String): String {
        return Crypto(cipherKey).encrypt(inputString)
    }

    fun reconnect() {
        subscriptionManager.reconnect()
    }

    fun disconnect() {
        subscriptionManager.disconnect()
    }

    fun destroy() {
        subscriptionManager.destroy()
        retrofitManager.destroy()
    }

    fun forceDestroy() {
        subscriptionManager.destroy(true)
        retrofitManager.destroy(true)
        telemetryManager.stopCleanUpTimer()
    }
}
