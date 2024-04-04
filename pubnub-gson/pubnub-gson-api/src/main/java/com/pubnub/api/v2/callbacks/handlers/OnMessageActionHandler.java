package com.pubnub.api.v2.callbacks.handlers;

import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;

@FunctionalInterface
public interface OnMessageActionHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to messageAction event retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNMessageActionResult} instance,
     * which contains the messageAction.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnMessageActionHandler handler = pnMessageActionResult -> {
     *     System.out.println("Received messageAction event: " + pnMessageActionResult.getMessageAction());
     * };
     * }
     * </pre>
     *
     * @see PNMessageActionResult for more information about the message result provided to this handler.
     */
    void handle(PNMessageActionResult pnMessageActionResult);
}
