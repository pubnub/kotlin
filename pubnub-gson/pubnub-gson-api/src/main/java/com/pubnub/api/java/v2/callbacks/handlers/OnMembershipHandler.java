package com.pubnub.api.java.v2.callbacks.handlers;

import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult;

@FunctionalInterface
public interface OnMembershipHandler {
    /**
     * <p>
     * This interface is designed for implementing custom handlers that respond to membershipMetadata event retrieval operations.
     * It defines a single {@code handle} method that is called with a {@link PNMembershipResult} instance,
     * which contains the membership metadata.
     * </p>
     * <p>
     * Usage example:
     * </p>
     * <pre>
     * {@code
     * OnMembershipHandler handler = pnMembershipResult -> {
     *     System.out.println("Received membership event: " + pnMembershipResult.getEvent());
     * };
     * }
     * </pre>
     *
     * @see PNMembershipResult for more information about the message result provided to this handler.
     */
    void handle(PNMembershipResult pnMembershipResult);
}
