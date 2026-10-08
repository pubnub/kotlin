package com.pubnub.api.java.endpoints.access;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.access_manager.PNAccessManagerGrantResult;

public interface Grant extends Endpoint<PNAccessManagerGrantResult> {
    Grant read(boolean read);

    Grant write(boolean write);

    Grant manage(boolean manage);

    Grant delete(boolean delete);

    Grant get(boolean get);

    Grant update(boolean update);

    Grant join(boolean join);

    Grant ttl(int ttl);

    Grant authKeys(java.util.List<String> authKeys);

    Grant channels(java.util.List<String> channels);

    Grant channelGroups(java.util.List<String> channelGroups);

    Grant uuids(java.util.List<String> uuids);

    /**
     * Set to {@code true} to allow the auth keys to list all channel metadata on the keyset
     * ({@code getAllChannelMetadata}). A {@code get} on a named channel doesn't imply this.
     *
     * <p>When this or {@link #getAllUUIDs(boolean)} is {@code true}, the {@code get} permission is sent automatically
     * and also applies to any channels/channel groups/uuids in the same request. The server rejects the request if
     * any other permission is set, or if no auth keys are given. Revoking only the category permissions isn't
     * supported: revoke all permissions for the auth key instead, which also removes its other key-wide permissions.
     */
    Grant getAllChannels(boolean getAllChannels);

    /**
     * Set to {@code true} to allow the auth keys to list all uuid metadata on the keyset
     * ({@code getAllUUIDMetadata}). A {@code get} on a named uuid doesn't imply this.
     *
     * @see #getAllChannels(boolean)
     */
    Grant getAllUUIDs(boolean getAllUUIDs);
}
