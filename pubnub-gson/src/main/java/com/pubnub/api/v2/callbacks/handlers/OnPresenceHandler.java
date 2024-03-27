package com.pubnub.api.v2.callbacks.handlers;

import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

@FunctionalInterface
public interface OnPresenceHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to presence event retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNPresenceEventResult} instance,
     * which contains the presence data.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnPresenceHandler handler = pnPresenceEventResult -> {
     *     System.out.println("Received presence event: " + pnPresenceEventResult.getEvent());
     * };
     * }
     * </pre>
     *
     * @see PNPresenceEventResult for more information about the message result provided to this handler.
     */
    void handle(PNPresenceEventResult pnPresenceEventResult);
}
