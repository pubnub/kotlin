package com.pubnub.api.java.v2.callbacks.handlers;

import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult;

@FunctionalInterface
public interface OnUuidMetadataHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to uuidMetadata event retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNUUIDMetadataResult} instance,
     * which contains the uuidMetadata.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnUuidMetadataHandler handler = pnUUIDMetadataResult -> {
     *     System.out.println("Received uuid Metadata event: " + pnUUIDMetadataResult.getEvent());
     * };
     * }
     * </pre>
     *
     * @see PNUUIDMetadataResult for more information about the channel metadata result provided to this handler.
     */
    void handle(PNUUIDMetadataResult pnUUIDMetadataResult);
}
