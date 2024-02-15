package com.pubnub.api.v2.callbacks

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult

interface EventEmitter : BaseEventEmitter {
    /**
     * A nullable property that can be set to a function (or lambda expression) to handle incoming message events.
     * This function is invoked whenever a new message is received, providing a convenient way to process or react to messages.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to message events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * **Setting a Behavior Example**:
     * ```
     * onMessage = { pnMessageResult ->
     *     println("Received message: ${pnMessageResult.message}")
     * }
     * ```
     * **Removing a Behavior Example**:
     * ```
     * onMessage = null
     * ```
     */
    var onMessage: ((PNMessageResult) -> Unit)?

    /**
     * A nullable property designed to set a function or lambda expression for handling incoming presence events.
     * This function is called whenever a new presence event occurs, offering an efficient method for tracking presence updates.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to presence events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * **Setting a Behavior Example**:
     * ```kotlin
     * onPresence = { pnPresenceEventResult ->
     *     println("Presence event: ${pnPresenceEventResult.event}")
     * }
     * ```
     * **Removing a Behavior Example**:
     * ```kotlin
     * onPresence = null
     * ```
     */
    var onPresence: ((PNPresenceEventResult) -> Unit)?

    /**
     * A nullable property for assigning a function or lambda expression to handle incoming signal events.
     * This function is called whenever a new signal is received, providing a convenient way to process or react to signals.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to signal events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     * To deactivate a behavior, assign `null` to this property.
     *
     * **Setting a Behavior Example**:
     * ```kotlin
     * onSignal = { pnSignalResult ->
     *     println("Received signal: ${pnSignalResult.message}")
     * }
     * ```
     * **Removing a Behavior Example**:
     * ```kotlin
     * onSignal = null
     * ```
     */
    var onSignal: ((PNSignalResult) -> Unit)?

    /**
     * A nullable property that allows setting a function or lambda to react to message action events.
     * This function is invoked whenever a new message action is received, providing a convenient way to process or react to message actions.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to message action events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * **Setting a Behavior Example**:
     * ```kotlin
     * onMessageAction = { pnMessageActionResult ->
     *     println("Message action event: ${pnMessageActionResult.data}")
     * }
     * ```
     * **Removing a Behavior Example**:
     * ```kotlin
     * onMessageAction = null
     * ```
     */
    var onMessageAction: ((PNMessageActionResult) -> Unit)?

    /**
     * A nullable property for assigning a function or lambda to handle object events.
     * This function is triggered with each new object event, providing a mechanism to manage object-related updates.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to object event, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * **Setting a Behavior Example**:
     * ```kotlin
     * onObjects = { pnObjectEventResult ->
     *     println("Object event: ${pnObjectEventResult.result}")
     * }
     * ```
     * **Removing a Behavior Example**:
     * ```kotlin
     * onObjects = null
     * ```
     */
    var onObjects: ((PNObjectEventResult) -> Unit)?

    /**
     * A nullable property to set a function or lambda for responding to file events.
     * This function is invoked whenever a new file event is received, providing a convenient way to process or react to file events.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to file event, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * **Setting a Behavior Example**:
     * ```kotlin
     * onFile = { pnFileEventResult ->
     *     println("File event: ${pnFileEventResult.message}")
     * }
     * ```
     * **Removing a Behavior Example**:
     * ```kotlin
     * onFile = null
     * ```
     */
    var onFile: ((PNFileEventResult) -> Unit)?
}
