package com.pubnub.docs.presence;
// https://www.pubnub.com/docs/sdks/java/api-reference/presence#get-a-list-of-uuids-subscribed-to-channel
// snippet.hereNow

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.endpoints.presence.HereNow;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;

import java.util.Arrays;
import java.util.List;

public class HereNowApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Get presence information for specified channels with automatic pagination
        List<String> channels = Arrays.asList("coolChannel", "coolChannel2");
        fetchHereNowWithPagination(pubnub, channels, null);
    }

    /**
     * Fetches hereNow data with automatic pagination handling.
     * This method recursively fetches all pages of results.
     *
     * @param pubnub PubNub instance
     * @param channels List of channels to query
     * @param offset Pagination offset (null for first page)
     */
    private static void fetchHereNowWithPagination(PubNub pubnub, List<String> channels, Integer offset) {
        HereNow builder = pubnub.hereNow()
                .channels(channels)
                .includeUUIDs(true)
                .limit(100);

        // Apply offset if provided (for subsequent pages)
        if (offset != null) {
            builder.offset(offset);
            System.out.println("\nFetching next page with offset: " + offset + "\n");
        }

        builder.async(result -> {
            result.onSuccess((PNHereNowResult res) -> {
                printHereNowResult(res);

                // Recursively fetch next page if available
                if (res.getNextOffset() != null) {
                    fetchHereNowWithPagination(pubnub, channels, res.getNextOffset());
                }
            }).onFailure((PubNubException exception) -> {
                System.out.println("Error retrieving hereNow data: " + exception.getMessage());
            });
        });
    }

    /**
     * Helper method to print HereNow presence information
     */
    private static void printHereNowResult(PNHereNowResult result) {
        System.out.println("Total Channels: " + result.getTotalChannels());
        System.out.println("Total Occupancy: " + result.getTotalOccupancy());

        for (PNHereNowChannelData channelData : result.getChannels().values()) {
            System.out.println("---");
            System.out.println("Channel: " + channelData.getChannelName());
            System.out.println("Occupancy: " + channelData.getOccupancy());
            System.out.println("Occupants:");
            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                System.out.println("UUID: " + occupant.getUuid() + " State: " + occupant.getState());
            }
        }

        if (result.getNextOffset() != null && result.getNextOffset() != 0) {
            System.out.println("Next Offset: " + result.getNextOffset());
        }
    }
}
// snippet.end
