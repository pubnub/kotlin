package com.pubnub.api.java.v2.callbacks.handlers;

import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;

@FunctionalInterface
public interface OnFileHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to file event retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNFileEventResult} instance,
     * which contains the file.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnFileHandler handler = pnFileEventResult -> {
     *     System.out.println("Received file event: " + pnFileEventResult.getMessage());
     * };
     * }
     * </pre>
     *
     * @see PNFileEventResult for more information about the message result provided to this handler.
     */
    void handle(PNFileEventResult pnFileEventResult);
}
