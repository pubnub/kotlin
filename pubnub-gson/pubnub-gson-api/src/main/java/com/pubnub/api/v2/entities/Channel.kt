package com.pubnub.api.v2.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.endpoints.pubsub.PublishBuilder
import com.pubnub.api.v2.endpoints.pubsub.SignalBuilder
import com.pubnub.api.v2.subscriptions.Subscription

/**
 * A representation of a PubNub channel identified by its [name].
 *
 * You can get a [Subscription] to this channel through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.PubNub.channel] factory method to create instances of this interface.
 */
interface Channel : BaseChannel<EventListener, Subscription> {
    /**
     * Returns a [Subscription] that can be used to subscribe to this channel.
     *
     * The returned [Subscription] is initially inactive. You must call [Subscription.subscribe] on it
     * to start receiving events.
     *
     * @return An inactive [Subscription] to this channel.
     */
    fun subscription(): Subscription

    /**
     * Send a message to all subscribers of the channel.
     *
     * To publish a message you must first specify a valid [PNConfiguration.publishKey].
     * A successfully published message is replicated across the PubNub Real-Time Network and sent
     * simultaneously to all subscribed clients on the channel.
     *
     * Messages in transit can be secured from potential eavesdroppers with SSL/TLS by setting
     * [PNConfiguration.secure] to `true` during initialization.
     *
     * **Publish Anytime**
     *
     *
     * It is not required to be subscribed to a channel in order to publish to that channel.
     *
     * **Message Data:**
     *
     *
     * The message argument can contain any JSON serializable data, including: Objects, Arrays, Integers and Strings.
     * Data should not contain special Java/Kotlin classes or functions as these will not serialize.
     * String content can include any single-byte or multi-byte UTF-8 character.
     *
     *
     * @param message The payload.
     *                **Warning:** It is important to note that you should not serialize JSON
     *                when sending signals/messages via PubNub.
     *                Why? Because the serialization is done for you automatically.
     *                Instead just pass the full object as the message payload.
     *                PubNub takes care of everything for you.
     * @param meta Metadata object which can be used with the filtering ability.
     * @param shouldStore Store in history.
     *                    If not specified, then the history configuration of the key is used.
     * @param usePost Use HTTP POST to publish. Default is `false`
     * @param replicate Replicate the message. Is set to `true` by default.
     * @param ttl Set a per message time to live in storage.
     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
     *              with no expiry time.
     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
     *              the message is stored with an expiry time of `X` hours.
     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
     *            - If ttl isn't specified, then expiration of the message defaults
     *              back to the expiry value for the key.
     */
    fun publish(message: Any): PublishBuilder

    /**
     * Send a signal to all subscribers of a channel.
     *
     * By default, signals are limited to a message payload size of 30 bytes.
     * This limit applies only to the payload, and not to the URI or headers.
     * If you require a larger payload size, please [contact support](mailto:support@pubnub.com).
     *
     * @param message The payload which will be serialized and sent.
     */
    fun signal(message: Any): SignalBuilder

    /**
     * Send a message to PubNub Functions Event Handlers.
     *
     * These messages will go directly to any Event Handlers registered on the channel that you fire to
     * and will trigger their execution. The content of the fired request will be available for processing
     * within the Event Handler.
     *
     * The message sent via `fire()` isn't replicated, and so won't be received by any subscribers to the channel.
     * The message is also not stored in history.
     *
     *
     * @param message The payload.
     *                **Warning:** It is important to note that you should not serialize JSON
     *                when sending signals/messages via PubNub.
     *                Why? Because the serialization is done for you automatically.
     *                Instead just pass the full object as the message payload.
     *                PubNub takes care of everything for you.
     * @param meta Metadata object which can be used with the filtering ability.
     *             If not specified, then the history configuration of the key is used.
     * @param usePost Use HTTP POST to publish. Default is `false`
     * @param ttl Set a per message time to live in storage.
     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
     *              with no expiry time.
     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
     *              the message is stored with an expiry time of `X` hours.
     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
     *            - If ttl isn't specified, then expiration of the message defaults
     *              back to the expiry value for the key.
     */
    fun fire(message: Any): PublishBuilder
}
