package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.docs.SnippetBase;

import java.util.Collections;

public class DataSyncUsersOther extends SnippetBase {
    private void getUsersFilterFast() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getUsersFilterFast
        pubnub.dataSync().getUsers()
                .filterFast("type == \"shopper\"")
                .sort(Collections.singletonList(new PNDataSyncSortField("name")))
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

    private void getUsersFilter() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getUsersFilter
        pubnub.dataSync().getUsers()
                .filter("name LIKE \"Alice*\"")
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
    // snippet.getUsersPagination
    private void fetchUsersPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getUsers()
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchUsersPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchUsersPage(pubnub, null)
    // snippet.end
}
