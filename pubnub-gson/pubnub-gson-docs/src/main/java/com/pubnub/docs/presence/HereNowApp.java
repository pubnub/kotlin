package com.pubnub.docs.presence;
// https://www.pubnub.com/docs/sdks/java/api-reference/presence#get-a-list-of-uuids-subscribed-to-channel
// snippet.hereNow

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;

import java.util.Arrays;

public class HereNowApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Get presence information for specified channels with pagination support
        pubnub.hereNow()
                .channels(Arrays.asList("coolChannel", "coolChannel2"))
                .includeUUIDs(true)
                .limit(100)
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {
                        printHereNowResult(res);

                        // Check if more results are available
                        if (res.getNextOffset() != null && res.getNextOffset() != 0) {
                            System.out.println("\nMore results available. Fetching next page...\n");

                            // Fetch next page using the offset from previous response
                            pubnub.hereNow()
                                    .channels(Arrays.asList("coolChannel", "coolChannel2"))
                                    .includeUUIDs(true)
                                    .limit(100)
                                    .offset(res.getNextOffset())
                                    .async(result2 -> {
                                        result2.onSuccess((PNHereNowResult res2) -> {
                                            printHereNowResult(res2);

                                            // Continue pagination if needed by checking res2.getNextOffset()
                                        }).onFailure((PubNubException exception) -> {
                                            System.out.println("Error retrieving hereNow data: " + exception.getMessage());
                                        });
                                    });
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
