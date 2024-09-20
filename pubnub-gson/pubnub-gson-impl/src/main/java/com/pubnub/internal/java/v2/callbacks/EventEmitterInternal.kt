package com.pubnub.internal.java.v2.callbacks

import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataResult
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult
import com.pubnub.api.java.v2.callbacks.handlers.OnChannelMetadataHandler
import com.pubnub.api.java.v2.callbacks.handlers.OnFileHandler
import com.pubnub.api.java.v2.callbacks.handlers.OnMembershipHandler
import com.pubnub.api.java.v2.callbacks.handlers.OnMessageActionHandler
import com.pubnub.api.java.v2.callbacks.handlers.OnMessageHandler
import com.pubnub.api.java.v2.callbacks.handlers.OnPresenceHandler
import com.pubnub.api.java.v2.callbacks.handlers.OnSignalHandler
import com.pubnub.api.java.v2.callbacks.handlers.OnUuidMetadataHandler
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventEmitter

/**
 * Interface implemented by objects that are the source of real time events from the PubNub network.
 */
interface EventEmitterInternal : EventEmitter, com.pubnub.api.java.v2.callbacks.EventEmitter {
    /**
     * Sets the handler for incoming message events.
     * This method allows the assignment of an [OnMessageHandler] implementation or lambda expression to handle
     * incoming messages.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to message events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`setOnMessage(pnMessageResult -> System.out.println("Received: " + pnMessageResult.getMessage()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`setOnMessage(null);
     `</pre> *
     *
     * @param onMessageHandler An implementation of [OnMessageHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnMessage(onMessageHandler: OnMessageHandler?) {
        onMessage = onMessageHandler?.let { handler ->
            handler::handle
        }
    }

    /**
     * Sets the handler for incoming signal events.
     * This method allows the assignment of an [OnSignalHandler] implementation or lambda expression to handle
     * incoming signals.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to signal events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`setOnSignal(pnSignalResult -> System.out.println("Received: " + pnSignalResult.getMessage()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`setOnSignal(null);
     `</pre> *
     *
     * @param onSignalHandler An implementation of [OnSignalHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnSignal(onSignalHandler: OnSignalHandler?) {
        onSignal = onSignalHandler?.let { handler ->
            handler::handle
        }
    }

    /**
     * Sets the handler for incoming presence events.
     * This method allows the assignment of an [OnPresenceHandler] implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to presence events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`onPresenceHandler(pnPresenceEventResult -> System.out.println("Received: " + pnPresenceEventResult.getEvent()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`onPresenceHandler(null);
     `</pre> *
     *
     * @param onPresenceHandler An implementation of [OnPresenceHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnPresence(onPresenceHandler: OnPresenceHandler?) {
        onPresence = onPresenceHandler?.let { handler ->
            handler::handle
        }
    }

    /**
     * Sets the handler for incoming messageAction events.
     * This method allows the assignment of an [OnMessageActionHandler] implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to messageAction events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`onMessageActionHandler(pnMessageActionResult -> System.out.println("Received: " + pnMessageActionResult.getMessageAction()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`onMessageActionHandler(null);
     `</pre> *
     *
     * @param onMessageActionHandler An implementation of [OnMessageActionHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnMessageAction(onMessageActionHandler: OnMessageActionHandler?) {
        onMessageAction = onMessageActionHandler?.let { handler ->
            handler::handle
        }
    }

    /**
     * Sets the handler for incoming uuidMetadata events.
     * This method allows the assignment of an [OnUuidMetadataHandler] implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to uuidMetadata events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`onUuidMetadataHandler(pnUUIDMetadataResult -> System.out.println("Received: " + pnUUIDMetadataResult.getData()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`onUuidMetadataHandler(null);
     `</pre> *
     *
     * @param onUuidMetadataHandler An implementation of [OnUuidMetadataHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnUuidMetadata(onUuidMetadataHandler: OnUuidMetadataHandler?) {
        onObjects = (onObjects as? CombinedObjectHandler)?.copy(uuidHandler = onUuidMetadataHandler) ?: CombinedObjectHandler(uuidHandler = onUuidMetadataHandler)
    }

    /**
     * Sets the handler for incoming channelMetadata events.
     * This method allows the assignment of an [OnChannelMetadataHandler] implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to channelMetadata events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`onChannelMetadataHandler(pnChannelMetadataResult -> System.out.println("Received: " +  pnChannelMetadataResult.getEvent()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`onChannelMetadataHandler(null);
     `</pre> *
     *
     * @param onChannelMetadataHandler An implementation of [OnChannelMetadataHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnChannelMetadata(onChannelMetadataHandler: OnChannelMetadataHandler?) {
        onObjects = (onObjects as? CombinedObjectHandler)?.copy(channelHandler = onChannelMetadataHandler) ?: CombinedObjectHandler(channelHandler = onChannelMetadataHandler)
    }

    /**
     * Sets the handler for incoming membership events.
     * This method allows the assignment of an [OnMembershipHandler] implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to membership events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`onMembershipHandler(pnMembershipResult -> System.out.println("Received: " +  pnMembershipResult.getEvent()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`onMembershipHandler(null);
     `</pre> *
     *
     * @param onMembershipHandler An implementation of [OnMembershipHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnMembership(onMembershipHandler: OnMembershipHandler?) {
        onObjects = (onObjects as? CombinedObjectHandler)?.copy(membershipHandler = onMembershipHandler) ?: CombinedObjectHandler(membershipHandler = onMembershipHandler)
    }

    /**
     * Sets the handler for incoming file events.
     * This method allows the assignment of an [OnFileHandler] implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to file events, it is advisable
     * to utilize [EventEmitter.addListener].
     *
     *
     * **Setting a Behavior Example:**
     * <pre>`onFileHandler(pnFileEventResult -> System.out.println("Received: " +  pnFileEventResult.getMessage()));
     `</pre> *
     *
     *
     * **Removing a Behavior Example:**
     * <pre>`onFileHandler(null);
     `</pre> *
     *
     * @param onFileHandler An implementation of [OnFileHandler] or a lambda expression to handle
     * incoming messages. It can be `null` to remove the current handler.
     */
    override fun setOnFile(onFileHandler: OnFileHandler?) {
        onFile = onFileHandler?.let { handler ->
            handler::handle
        }
    }
}

private data class CombinedObjectHandler(
    val membershipHandler: OnMembershipHandler? = null,
    val uuidHandler: OnUuidMetadataHandler? = null,
    val channelHandler: OnChannelMetadataHandler? = null,
) : ((PNObjectEventResult) -> Unit) {
    override fun invoke(objectEvent: PNObjectEventResult) {
        val objectResult = Converters.objects(objectEvent)
        when (objectResult) {
            is PNMembershipResult -> membershipHandler?.handle(objectResult)
            is PNChannelMetadataResult -> channelHandler?.handle(objectResult)
            is PNUUIDMetadataResult -> uuidHandler?.handle(objectResult)
        }
    }
}
