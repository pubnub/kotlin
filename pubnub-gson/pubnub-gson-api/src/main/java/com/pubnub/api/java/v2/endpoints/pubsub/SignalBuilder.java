package com.pubnub.api.java.v2.endpoints.pubsub;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPublishResult;

/**
 * Interface representing a builder for configuring a signal operation.
 * This interface extends {@link Endpoint} to provide a fluent API for setting parameters
 * for the signal request.
 */
public interface SignalBuilder extends Endpoint<PNPublishResult> {
    /**
     * Specifies a custom message type for the signal.
     *
     * @param customMessageType The custom message type as a string.
     * @return The current instance of {@code SignalBuilder} for method chaining.
     */
    SignalBuilder customMessageType(String customMessageType);
}
