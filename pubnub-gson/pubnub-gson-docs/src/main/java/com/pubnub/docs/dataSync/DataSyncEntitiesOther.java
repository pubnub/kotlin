package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataSyncEntitiesOther extends SnippetBase {
    private void getEntitiesFilterFast() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getEntitiesFilterFast
        // `price` and `stock` must be declared filterable (`simple` or `full`) on the `product` class
        pubnub.dataSync().getEntities("product")
                .filterFast("price < 100 && stock > 0")
                .sort(Collections.singletonList(new PNDataSyncSortField("price")))
                .limit(20)
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

    private void getEntitiesFilter() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getEntitiesFilter
        // `filter` only accepts properties declared with `full` filtering on the `product` class
        pubnub.dataSync().getEntities("product")
                .filter("name LIKE \"*Sneaker*\"")
                .limit(20)
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

    // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#pagination
    // snippet.getEntitiesPagination
    private void fetchEntitiesPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getEntities("product")
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchEntitiesPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchEntitiesPage(pubnub, null)
    // snippet.end

    private void updateEntityMultipleOperations() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#update-entity

        PubNub pubnub = createPubNub();

        // snippet.updateEntityMultipleOperations
        List<PNJsonPatchOperation> operations = Arrays.asList(
                // Fail the whole patch unless the name is still "Retro Sneaker"
                PNJsonPatchOperation.builder().op("test").path("/payload/name").value("Retro Sneaker").build(),
                // Keep the old price before changing it
                PNJsonPatchOperation.builder().op("copy").path("/payload/previousPrice").from("/payload/price").build(),
                PNJsonPatchOperation.builder().op("replace").path("/payload/price").value(79.99).build(),
                PNJsonPatchOperation.builder().op("replace").path("/payload/stock").value(8).build(),
                PNJsonPatchOperation.builder().op("add").path("/payload/sale").value(true).build(),
                // Rename a field
                PNJsonPatchOperation.builder().op("move").path("/payload/onSale").from("/payload/sale").build(),
                // The removed field must exist on the entity, otherwise the patch is rejected
                PNJsonPatchOperation.builder().op("remove").path("/payload/legacySku").build());

        pubnub.dataSync().updateEntity("product-sneaker-42", operations)
                // Use the eTag from an earlier read of the entity, not a literal value
                .ifMatch("a1b2c3d4e5f6")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Patched " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
