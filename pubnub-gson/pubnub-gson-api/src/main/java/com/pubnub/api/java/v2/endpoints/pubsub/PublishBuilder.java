package com.pubnub.api.java.v2.endpoints.pubsub;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPublishResult;

/**
 * Interface representing a builder for configuring a publish operation.
 * This interface extends {@link Endpoint} to provide a fluent API for setting various parameters
 * for the publish request.
 */
public interface PublishBuilder extends Endpoint<PNPublishResult> {
    /**
     * Specifies whether the message should be stored in the history of the channel.
     *
     * @param shouldStore Boolean indicating whether to store the message (true) or not (false). If not specified, then the history configuration of the key is used.
     * @return The current instance of {@code PublishBuilder} for method chaining.
     */
    PublishBuilder shouldStore(Boolean shouldStore);

    /**
     * Configures the publish request to use the POST HTTP method instead of GET.
     *
     * @param usePOST Boolean indicating whether to use POST (true) or GET (false) for the request. Default is `false`
     * @return The current instance of {@code PublishBuilder} for method chaining.
     */
    PublishBuilder usePOST(boolean usePOST);

    /**
     * Sets the metadata to be sent along with the message.
     * Metadata can be any custom object.
     *
     * @param meta Metadata object which can be used with the filtering ability.
     * @return The current instance of {@code PublishBuilder} for method chaining.
     */
    PublishBuilder meta(Object meta);


    /**
     * Specifies whether the message should be replicated across datacenters.
     *
     * @param replicate Boolean indicating whether to replicate the message (true) or not (false). Default is true.
     * @return The current instance of {@code PublishBuilder} for method chaining.
     */
    PublishBuilder replicate(boolean replicate);

    /**
     * Sets the time-to-live (TTL) in Message Persistence.
     * If shouldStore = true, and ttl = 0, the message is stored with no expiry time.
     * If shouldStore = true and ttl = X (X is an Integer value), the message is stored with an expiry time of X hours.
     * If shouldStore = false, the ttl parameter is ignored.
     * If ttl is not specified, then expiration of the message defaults back to the expiry value for the key.
     *
     * @param ttl The TTL value in minutes for the message.
     * @return The current instance of {@code PublishBuilder} for method chaining.
     */
    PublishBuilder ttl(Integer ttl);

    /**
     * Specifies a custom message type for the message.
     *
     * @param customMessageType The custom message type as a string.
     * @return The current instance of {@code PublishBuilder} for method chaining.
     */
    PublishBuilder customMessageType(String customMessageType);
}
