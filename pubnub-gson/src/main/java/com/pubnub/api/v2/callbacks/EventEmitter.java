package com.pubnub.api.v2.callbacks;

import com.pubnub.api.v2.callbacks.handlers.OnChannelMetadataHandler;
import com.pubnub.api.v2.callbacks.handlers.OnFileHandler;
import com.pubnub.api.v2.callbacks.handlers.OnMembershipHandler;
import com.pubnub.api.v2.callbacks.handlers.OnMessageActionHandler;
import com.pubnub.api.v2.callbacks.handlers.OnMessageHandler;
import com.pubnub.api.v2.callbacks.handlers.OnPresenceHandler;
import com.pubnub.api.v2.callbacks.handlers.OnSignalHandler;
import com.pubnub.api.v2.callbacks.handlers.OnUuidMetadataHandler;

/**
 * Interface implemented by objects that are the source of real time events from the PubNub network.
 */
public interface EventEmitter extends BaseEventEmitter<EventListener> {
    /**
     * Sets the handler for incoming message events.
     * This method allows the assignment of an {@link OnMessageHandler} implementation or lambda expression to handle
     * incoming messages.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to message events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * setOnMessage(pnMessageResult -> System.out.println("Received: " + pnMessageResult.getMessage()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * setOnMessage(null);
     * }</pre>
     *
     * @param onMessageHandler An implementation of {@link OnMessageHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnMessage(OnMessageHandler onMessageHandler);

    /**
     * Sets the handler for incoming signal events.
     * This method allows the assignment of an {@link OnSignalHandler} implementation or lambda expression to handle
     * incoming signals.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to signal events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * setOnSignal(pnSignalResult -> System.out.println("Received: " + pnSignalResult.getMessage()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * setOnSignal(null);
     * }</pre>
     *
     * @param onSignalHandler An implementation of {@link OnSignalHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnSignal(OnSignalHandler onSignalHandler);

    /**
     * Sets the handler for incoming presence events.
     * This method allows the assignment of an {@link OnPresenceHandler} implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to presence events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * onPresenceHandler(pnPresenceEventResult -> System.out.println("Received: " + pnPresenceEventResult.getEvent()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * onPresenceHandler(null);
     * }</pre>
     *
     * @param onPresenceHandler An implementation of {@link OnPresenceHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnPresence(OnPresenceHandler onPresenceHandler);

    /**
     * Sets the handler for incoming messageAction events.
     * This method allows the assignment of an {@link OnMessageActionHandler} implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to messageAction events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * onMessageActionHandler(pnMessageActionResult -> System.out.println("Received: " + pnMessageActionResult.getMessageAction()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * onMessageActionHandler(null);
     * }</pre>
     *
     * @param onMessageActionHandler An implementation of {@link OnMessageActionHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnMessageAction(OnMessageActionHandler onMessageActionHandler);

    /**
     * Sets the handler for incoming uuidMetadata events.
     * This method allows the assignment of an {@link OnUuidMetadataHandler} implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to uuidMetadata events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * onUuidMetadataHandler(pnUUIDMetadataResult -> System.out.println("Received: " + pnUUIDMetadataResult.getData()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * onUuidMetadataHandler(null);
     * }</pre>
     *
     * @param onUuidMetadataHandler An implementation of {@link OnUuidMetadataHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnUuidMetadata(OnUuidMetadataHandler onUuidMetadataHandler);

    /**
     * Sets the handler for incoming channelMetadata events.
     * This method allows the assignment of an {@link OnChannelMetadataHandler} implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to channelMetadata events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * onChannelMetadataHandler(pnChannelMetadataResult -> System.out.println("Received: " +  pnChannelMetadataResult.getEvent()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * onChannelMetadataHandler(null);
     * }</pre>
     *
     * @param onChannelMetadataHandler An implementation of {@link OnChannelMetadataHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnChannelMetadata(OnChannelMetadataHandler onChannelMetadataHandler);

    /**
     * Sets the handler for incoming membership events.
     * This method allows the assignment of an {@link OnMembershipHandler} implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to membership events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * onMembershipHandler(pnMembershipResult -> System.out.println("Received: " +  pnMembershipResult.getEvent()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * onMembershipHandler(null);
     * }</pre>
     *
     * @param onMembershipHandler An implementation of {@link OnMembershipHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnMembership(OnMembershipHandler onMembershipHandler);

    /**
     * Sets the handler for incoming file events.
     * This method allows the assignment of an {@link OnFileHandler} implementation or lambda expression to handle
     * incoming presence events.
     *
     * To deactivate the current behavior, simply set this property to `null`.
     *
     * Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one.
     * For scenarios requiring multiple behaviors in response to file events, it is advisable
     * to utilize {@link EventEmitter#addListener}.
     *
     * <p><strong>Setting a Behavior Example:</strong></p>
     * <pre>{@code
     * onFileHandler(pnFileEventResult -> System.out.println("Received: " +  pnFileEventResult.getMessage()));
     * }</pre>
     *
     * <p><strong>Removing a Behavior Example:</strong></p>
     * <pre>{@code
     * onFileHandler(null);
     * }</pre>
     *
     * @param onFileHandler An implementation of {@link OnFileHandler} or a lambda expression to handle
     * incoming messages. It can be {@code null} to remove the current handler.
     */
    void setOnFile(OnFileHandler onFileHandler);
}
