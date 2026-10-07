package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.docs.SnippetBase;

import java.util.Collections;

public class DataSyncRelationshipsOther extends SnippetBase {
    private void getRelationshipsFilterFast() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getRelationshipsFilterFast
        // `tier` and `since` must be declared filterable (`simple` or `full`) on the `ProductOwner` class
        pubnub.dataSync().getRelationships("ProductOwner")
                .filterFast("tier == \"gold\"")
                .sort(Collections.singletonList(new PNDataSyncSortField("since", false)))
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

    private void getRelationshipsFilter() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getRelationshipsFilter
        // `filter` only accepts properties declared with `full` filtering on the `ProductOwner` class
        pubnub.dataSync().getRelationships("ProductOwner")
                .filter("tier LIKE \"gold*\"")
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
    // snippet.getRelationshipsPagination
    private void fetchRelationshipsPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getRelationships("ProductOwner")
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                            result.onSuccess(value -> {
                                value.getData().forEach(item -> System.out.println(item.getId()));
                                if (value.getNext().isHasNext()) {
                                    fetchRelationshipsPage(pubnub, value.getNext().getCursor());
                                }
                            }).onFailure(exception -> {
                                System.out.println("Failed: " + exception.getMessage());
                            });
                        });
            }
            // Start with the first page by passing a null cursor: fetchRelationshipsPage(pubnub, null)
            // snippet.end

            private void getRelationshipsByEntityBId() throws PubNubException {
                // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-all-relationships

                PubNub pubnub = createPubNub();

                // snippet.getRelationshipsByEntityBId
                pubnub.dataSync().getRelationships("ProductOwner")
                        .entityBId("product-sneaker-42")
                        .limit(50)
                        .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getEntityAId() + " owns " + item.getEntityBId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
