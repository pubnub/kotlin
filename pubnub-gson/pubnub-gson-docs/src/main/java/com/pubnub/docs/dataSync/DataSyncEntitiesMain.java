package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataSyncEntitiesMain extends SnippetBase {
    private void createEntityBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#create-entity

        PubNub pubnub = createPubNub();

        // snippet.createEntityBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Retro Sneaker");
        payload.put("price", 89.99);
        payload.put("stock", 12);

        pubnub.dataSync().createEntity("product", 1)
                .entityId("product-sneaker-42")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created entity " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getEntityBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-entity

        PubNub pubnub = createPubNub();

        // snippet.getEntityBasicUsage
        pubnub.dataSync().getEntity("product-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched entity " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getEntitiesBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-all-entities

        PubNub pubnub = createPubNub();

        // snippet.getEntitiesBasicUsage
        pubnub.dataSync().getEntities("product")
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

    private void setEntityBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#set-entity

        PubNub pubnub = createPubNub();

        // snippet.setEntityBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Retro Sneaker");
        payload.put("price", 79.99);
        payload.put("stock", 8);

        pubnub.dataSync().setEntity("product-sneaker-42", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced entity " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void updateEntityBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#update-entity

        PubNub pubnub = createPubNub();

        // snippet.updateEntityBasicUsage
        pubnub.dataSync().updateEntity("product-sneaker-42", Arrays.asList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/price").value(79.99).build(),
                PNJsonPatchOperation.builder().op("replace").path("/payload/stock").value(8).build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated entity " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void removeEntityBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#remove-entity

        PubNub pubnub = createPubNub();

        // snippet.removeEntityBasicUsage
        pubnub.dataSync().removeEntity("product-sneaker-42")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed entity, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
