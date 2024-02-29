package com.pubnub.api

import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.callbacks.BaseStatusEmitter
import com.pubnub.api.v2.callbacks.BaseStatusListener
import com.pubnub.api.v2.entities.BaseChannel
import com.pubnub.api.v2.entities.BaseChannelGroup
import com.pubnub.api.v2.entities.BaseChannelMetadata
import com.pubnub.api.v2.entities.BaseUserMetadata
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import java.io.InputStream

interface BasePubNub<
    EventListener : BaseEventListener,
    Subscription : BaseSubscription<EventListener>,
    Channel : BaseChannel<EventListener, Subscription>,
    ChannelGroup : BaseChannelGroup<EventListener, Subscription>,
    ChannelMetadata : BaseChannelMetadata<EventListener, Subscription>,
    UserMetadata : BaseUserMetadata<EventListener, Subscription>,
    SubscriptionSet : BaseSubscriptionSet<EventListener, Subscription>,
    StatusListener : BaseStatusListener,
    > : BaseEventEmitter<EventListener>, BaseStatusEmitter<StatusListener> {
    val timestamp: Int
    val baseUrl: String

    /**
     * The current version of the Kotlin SDK.
     */
    val version: String

    /**
     * Create a handle to a [Channel] that can be used to obtain a [Subscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel is required.
     *
     * The returned [Channel] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel to return. Supports wildcards by ending it with ".*". See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/overview)
     *
     * @return a [Channel] instance representing the channel with the given [name]
     */
    fun channel(name: String): Channel

    /**
     * Create a handle to a [ChannelGroup] that can be used to obtain a [Subscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel group is required.
     *
     * The returned [ChannelGroup] holds a reference to this [PubNub] instance internally.
     *
     * @param name the name of the channel group to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups)
     *
     * @return a [ChannelGroup] instance representing the channel group with the given [name]
     */
    fun channelGroup(name: String): ChannelGroup

    /**
     * Create a handle to a [ChannelMetadata] object that can be used to obtain a [Subscription] to metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a metadata channel is required.
     *
     * The returned [ChannelMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the channel metadata to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata)
     *
     * @return a [ChannelMetadata] instance representing the channel metadata with the given [id]
     */
    fun channelMetadata(id: String): ChannelMetadata

    /**
     * Create a handle to a [UserMetadata] object that can be used to obtain a [Subscription] to user metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a user metadata is required.
     *
     * The returned [UserMetadata] holds a reference to this [PubNub] instance internally.
     *
     * @param id the id of the user. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata)
     *
     * @return a [UserMetadata] instance representing the channel metadata with the given [id]
     */
    fun userMetadata(id: String): UserMetadata

    /**
     * Create a [SubscriptionSet] from the given [subscriptions].
     *
     * @param subscriptions the subscriptions that will be added to the returned [SubscriptionSet]
     * @return a [SubscriptionSet] containing all [subscriptions]
     */
    fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet

    /**
     * Create a [SubscriptionSet] containing [Subscription] objects for the given sets of [channels] and
     * [channelGroups].
     *
     * Please note that the subscriptions are not active until you call [SubscriptionSet.subscribe].
     *
     * This is a convenience method, and it is equal to calling [PubNub.channel] followed by [Channel.subscription] for
     * each channel, then creating a [subscriptionSetOf] using the returned [Subscription] objects (and similarly for
     * channel groups).
     *
     * @param channels the channels to create subscriptions for
     * @param channelGroups the channel groups to create subscriptions for
     * @param options the [SubscriptionOptions] to pass for each subscription. Refer to supported options in [Channel] and
     * [ChannelGroup] documentation.
     * @return a [SubscriptionSet] containing subscriptions for the given [channels] and [channelGroups]
     */
    fun subscriptionSetOf(
        channels: Set<String> = emptySet(),
        channelGroups: Set<String> = emptySet(),
        options: SubscriptionOptions = EmptyOptions,
    ): SubscriptionSet

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
     *
     * @param inputString String to be decrypted.
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    @Throws(PubNubException::class)
    fun decrypt(inputString: String): String

    /**
     * Perform Cryptographic decryption of an input string using a cipher key.
     *
     * @param inputString String to be decrypted.
     * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    @Throws(PubNubException::class)
    fun decrypt(
        inputString: String,
        cipherKey: String? = null,
    ): String

    /**
     * Perform Cryptographic decryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for decryption.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed decryption.
     */
    @Throws(PubNubException::class)
    fun decryptInputStream(
        inputStream: InputStream,
        cipherKey: String? = null,
    ): InputStream

    /**
     * Perform Cryptographic encryption of an input string and a cipher key.
     *
     * @param inputString String to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the encryption of `inputString` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    @Throws(PubNubException::class)
    fun encrypt(
        inputString: String,
        cipherKey: String? = null,
    ): String

    /**
     * Perform Cryptographic encryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for encryption.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    @Throws(PubNubException::class)
    fun encryptInputStream(
        inputStream: InputStream,
        cipherKey: String? = null,
    ): InputStream

    @Throws(PubNubException::class)
    fun parseToken(token: String): PNToken

    fun setToken(token: String?)

    /**
     * Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status]
     *
     * @param timetoken optional timetoken to use for the subscriptions on reconnection.
     * Only applicable when [PNConfiguration.enableEventEngine] is true, otherwise ignored
     */
    fun reconnect(timetoken: Long = 0L)

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    fun disconnect()

    /**
     * Unsubscribe from all channels and all channel groups
     */
    fun unsubscribeAll()

    /**
     * Frees up threads eventually and allows for a clean exit.
     */
    fun destroy()

    /**
     * Same as [destroy] but immediately.
     */
    fun forceDestroy()
}
