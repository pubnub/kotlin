package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataSyncRelationshipsMain extends SnippetBase {
    private void createRelationshipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#create-relationship

        PubNub pubnub = createPubNub();

        // snippet.createRelationshipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("since", "2026-07-13");

        pubnub.dataSync().createRelationship("seller-bob", "product-sneaker-42", "ProductOwner", 1)
                .relationshipId("rel-bob-owns-sneaker-42")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created relationship " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getRelationshipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-relationship

        PubNub pubnub = createPubNub();

        // snippet.getRelationshipBasicUsage
        pubnub.dataSync().getRelationship("rel-bob-owns-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched relationship " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getRelationshipsBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-all-relationships

        PubNub pubnub = createPubNub();

        // snippet.getRelationshipsBasicUsage
        pubnub.dataSync().getRelationships("ProductOwner")
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

    private void setRelationshipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#set-relationship

        PubNub pubnub = createPubNub();

        // snippet.setRelationshipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("since", "2026-07-13");
        payload.put("tier", "gold");

        pubnub.dataSync().setRelationship("rel-bob-owns-sneaker-42", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced relationship " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void updateRelationshipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#update-relationship

        PubNub pubnub = createPubNub();

        // snippet.updateRelationshipBasicUsage
        pubnub.dataSync().updateRelationship("rel-bob-owns-sneaker-42", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/tier").value("platinum").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated relationship " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void removeRelationshipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#remove-relationship

        PubNub pubnub = createPubNub();

        // snippet.removeRelationshipBasicUsage
        pubnub.dataSync().removeRelationship("rel-bob-owns-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed relationship, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
