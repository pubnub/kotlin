package com.pubnub.api.v2.callbacks.handlers;

import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;

@FunctionalInterface
public interface OnChannelMetadataHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to channelMetadata event retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNChannelMetadataResult} instance,
     * which contains the channel metadata.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnChannelMetadataHandler handler = pnChannelMetadataResult -> {
     *     System.out.println("Received channel metadata event: " + pnChannelMetadataResult.getEvent());
     * };
     * }
     * </pre>
     *
     * @see PNChannelMetadataResult for more information about the channel metadata result provided to this handler.
     */
    void handle(PNChannelMetadataResult pnChannelMetadataResult);
}
