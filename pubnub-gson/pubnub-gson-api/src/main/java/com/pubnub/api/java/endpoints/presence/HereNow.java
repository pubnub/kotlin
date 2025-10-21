package com.pubnub.api.java.endpoints.presence;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;

/**
 * Obtain information about the current state of channels including a list of unique user IDs
 * currently subscribed and the total occupancy count.
 * <p>
 *
 * @see com.pubnub.api.models.consumer.presence.PNHereNowResult
 */
public interface HereNow extends Endpoint<PNHereNowResult> {
    /**
     * Set the channels to query for presence information.
     *
     * @param channels List of channel names to query.
     * @return {@code this} for method chaining
     * @see #channelGroups(java.util.List)
     */
    HereNow channels(java.util.List<String> channels);

    /**
     * Set the channel groups to query for presence information.
     *
     * @param channelGroups List of channel group names to query.
     * @return {@code this} for method chaining
     * @see #channels(java.util.List)
     */
    HereNow channelGroups(java.util.List<String> channelGroups);

    /**
     * Whether the response should include presence state information, if available.
     *
     * @param includeState {@code true} to include state information, {@code false} to exclude. Default: {@code false}
     * @return {@code this} for method chaining
     * @see #includeUUIDs(boolean)
     */
    HereNow includeState(boolean includeState);

    /**
     * Include the list of UUIDs currently present in each channel.
     *
     * @param includeUUIDs {@code true} to include UUID list, {@code false} for occupancy count only. Default: {@code true}
     * @return {@code this} for method chaining
     * @see #includeState(boolean)
     * @see #limit(int)
     */
    HereNow includeUUIDs(boolean includeUUIDs);

    /**
     * Set the maximum number of occupants to return per channel.
     * <p>
     * The server enforces a maximum limit of 1000. Values outside this range will be rejected by the server.
     * <p>
     * Special behavior:
     * <ul>
     *   <li>Use {@code limit = 0} to retrieve only occupancy counts without individual occupant UUIDs</li>
     * </ul>
     *
     * @param limit Maximum number of occupants to return (0-1000). Default: 1000
     * @return {@code this} for method chaining
     * @see #offset(Integer) for pagination support
     */
    HereNow limit(int limit);

    /**
     * Set the zero-based starting index for pagination through occupants.
     * <p>
     * Server-side validation applies:
     * <ul>
     *   <li>Must be >= 0 (negative values will be rejected)</li>
     * </ul>
     * <p>
     *
     * @param offset Zero-based starting position (must be >= 0). Default: null (no offset)
     * @return {@code this} for method chaining
     * @see #limit(int) for controlling result size
     */
    HereNow offset(Integer offset);
}
