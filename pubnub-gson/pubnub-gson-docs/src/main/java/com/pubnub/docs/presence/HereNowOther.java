package com.pubnub.docs.presence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class HereNowOther extends SnippetBase {

    private void hereNowReturningState() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#returning-state

        PubNub pubNub = createPubNub();

        // snippet.hereNowReturningState
        pubNub.hereNow()
                .channels(Arrays.asList("my_channel")) // who is present on those channels?
                .includeState(true) // include state with request (false by default)
                .includeUUIDs(true) // if false, only shows occupancy count
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {
                        // Iterate through channels if the request was successful
                        for (PNHereNowChannelData channelData : res.getChannels().values()) {
                            System.out.println("Channel: " + channelData.getChannelName());
                            System.out.println("Occupancy: " + channelData.getOccupancy());
                            System.out.println("Occupants:");
                            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                System.out.println("UUID: " + occupant.getUuid() + " State: " + occupant.getState());
                            }
                        }
                    }).onFailure((PubNubException exception) -> {
                        // Handle errors if the request fails
                        System.err.println("Error retrieving hereNow data: " + exception.getMessage());
                        exception.getPubnubError();
                        exception.getCause();
                        exception.getStatusCode();
                    });
                });
        // snippet.end
    }

    private void hereNowOccupancyOnly() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#return-occupancy-only

        PubNub pubNub = createPubNub();

        // snippet.hereNowOccupancyOnly
        pubNub.hereNow()
                .channels(Arrays.asList("my_channel")) // who is present on those channels?
                .includeState(false) // include state with request (false by default)
                .includeUUIDs(false) // if false, only shows occupancy count
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {
                        // Iterate through channels if the request was successful
                        for (PNHereNowChannelData channelData : res.getChannels().values()) {
                            System.out.println("Channel: " + channelData.getChannelName()); // my_channel
                            System.out.println("Occupancy: " + channelData.getOccupancy()); // 3
                        }
                    }).onFailure((PubNubException exception) -> {
                        // Handle errors if the request fails
                        System.err.println("Error retrieving hereNow data: " + exception.getMessage());
                        exception.getPubnubError();
                        exception.getCause();
                        exception.getStatusCode();
                    });
                });
        // snippet.end
    }

    private void hereNowForChannelGroups() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#here-now-for-channel-groups

        PubNub pubNub = createPubNub();

        // snippet.hereNowForChannel
        pubNub.hereNow()
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3")) // who is present on channel groups?
                .includeState(true) // include state with request (false by default)
                .includeUUIDs(true) // if false, only shows occupancy count
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {
                        // Get total occupancy across all channels if the request was successful
                        System.out.println("Total Occupancy: " + res.getTotalOccupancy()); // 12

                        // Iterate through channels in the channel group
                        for (PNHereNowChannelData channelData : res.getChannels().values()) {
                            System.out.println("Channel: " + channelData.getChannelName());
                            System.out.println("Occupancy: " + channelData.getOccupancy());
                            System.out.println("Occupants:");
                            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                System.out.println("UUID: " + occupant.getUuid() + " State: " + occupant.getState());
                            }
                        }
                    }).onFailure((PubNubException exception) -> {
                        // Handle errors if the request fails
                        System.err.println("Error retrieving hereNow data: " + exception.getMessage());
                        exception.getPubnubError();
                        exception.getCause();
                        exception.getStatusCode();
                    });
                });
        // snippet.end
    }
}
