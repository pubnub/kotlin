package com.pubnub.api.v2.callbacks.handlers;

import com.pubnub.api.models.consumer.pubsub.PNSignalResult;

@FunctionalInterface
public interface OnSignalHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to message retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNSignalResult} instance,
     * which contains the signal.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnSignalHandler handler = pnSignalResult -> {
     *     System.out.println("Received message: " + pnSignalResult.getMessage());
     * };
     * }
     * </pre>
     *
     * @see PNSignalResult for more information about the message result provided to this handler.
     */
    void handle(PNSignalResult pnSignalResult);
}
