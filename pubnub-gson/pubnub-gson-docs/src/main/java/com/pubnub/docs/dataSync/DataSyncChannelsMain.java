package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataSyncChannelsMain extends SnippetBase {
    private void createChannelBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#create-channel

        PubNub pubnub = createPubNub();

        // snippet.createChannelBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Summer Sale");
        payload.put("type", "promotion");

        pubnub.dataSync().createChannel(1)
                .channelId("channel-summer-sale")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created channel " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getChannelBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-channel

        PubNub pubnub = createPubNub();

        // snippet.getChannelBasicUsage
        pubnub.dataSync().getChannel("channel-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched channel " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getChannelsBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-all-channels

        PubNub pubnub = createPubNub();

        // snippet.getChannelsBasicUsage
        pubnub.dataSync().getChannels()
                .limit(10)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void setChannelBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#set-channel

        PubNub pubnub = createPubNub();

        // snippet.setChannelBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Summer Sale 2026");
        payload.put("type", "promotion");

        pubnub.dataSync().setChannel("channel-summer-sale", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced channel " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void updateChannelBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#update-channel

        PubNub pubnub = createPubNub();

        // snippet.updateChannelBasicUsage
        pubnub.dataSync().updateChannel("channel-summer-sale", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/name").value("Summer Sale 2026").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated channel " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void removeChannelBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#remove-channel

        PubNub pubnub = createPubNub();

        // snippet.removeChannelBasicUsage
        pubnub.dataSync().removeChannel("channel-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed channel, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
