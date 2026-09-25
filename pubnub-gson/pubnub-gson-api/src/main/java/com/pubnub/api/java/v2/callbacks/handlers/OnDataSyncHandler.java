package com.pubnub.api.java.v2.callbacks.handlers;

import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult;

@FunctionalInterface
public interface OnDataSyncHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to DataSync realtime events.
     * It defines a single {@code handle} method that is called with a {@link PNDataSyncEventResult} instance,
     * which contains the DataSync create/update/delete event.
     * </p>
     * <p>
     * A single subscribed ref-channel delivers a mix of DataSync leaf types, so branch on the sealed
     * {@link PNDataSyncEventResult#getExtractedMessage()}.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnDataSyncHandler handler = pnDataSyncEventResult -> {
     *     System.out.println("Received DataSync event: " + pnDataSyncEventResult.getExtractedMessage());
     * };
     * }
     * </pre>
     *
     * @see PNDataSyncEventResult for more information about the result provided to this handler.
     */
    void handle(PNDataSyncEventResult pnDataSyncEventResult);
}