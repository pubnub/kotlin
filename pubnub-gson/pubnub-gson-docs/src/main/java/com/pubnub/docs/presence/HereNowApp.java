package com.pubnub.docs.presence;
// https://www.pubnub.com/docs/sdks/java/api-reference/presence#get-a-list-of-uuids-subscribed-to-channel
// snippet.hereNow

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
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
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Get presence information for specified channels
        pubnub.hereNow()
                .channels(Arrays.asList("coolChannel", "coolChannel2"))
                .includeUUIDs(true)
                .limit(100)
                .offset(10)
                .async(result -> {
                    result.onSuccess((PNHereNowResult res) -> {

                        System.out.println("Total Channels: " + res.getTotalChannels());
                        System.out.println("Total Occupancy: " + res.getTotalOccupancy());

                        for (PNHereNowChannelData channelData : res.getChannels().values()) {
                            System.out.println("---");
                            System.out.println("Channel: " + channelData.getChannelName());
                            System.out.println("Occupancy: " + channelData.getOccupancy());
                            System.out.println("Occupants:");
                            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                System.out.println("UUID: " + occupant.getUuid() + " State: " + occupant.getState());
                            }
                        }
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("Error retrieving hereNow data: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
