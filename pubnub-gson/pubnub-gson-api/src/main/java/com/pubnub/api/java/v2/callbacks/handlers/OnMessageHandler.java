package com.pubnub.api.java.v2.callbacks.handlers;


import com.pubnub.api.models.consumer.pubsub.PNMessageResult;

@FunctionalInterface
public interface OnMessageHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to message retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNMessageResult} instance,
     * which contains the message.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnMessageHandler handler = pnMessageResult -> {
     *     System.out.println("Received message: " + pnMessageResult.getMessage());
     * };
     * }
     * </pre>
     *
     * @see PNMessageResult for more information about the message result provided to this handler.
     */
    void handle(PNMessageResult pnMessageResult);
}
