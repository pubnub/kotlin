package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.docs.SnippetBase;

import java.util.Collections;

public class DataSyncChannelsOther extends SnippetBase {
    private void getChannelsFilterFast() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getChannelsFilterFast
        pubnub.dataSync().getChannels()
                .filterFast("type == \"promotion\"")
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

    private void getChannelsFilter() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getChannelsFilter
        pubnub.dataSync().getChannels()
                .filter("name LIKE \"Summer*\"")
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
    // snippet.getChannelsPagination
    private void fetchChannelsPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getChannels()
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getId()));
                        if (value.getNext().isHasNext()) {
                            fetchChannelsPage(pubnub, value.getNext().getCursor());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
    }
    // Start with the first page by passing a null cursor: fetchChannelsPage(pubnub, null)
    // snippet.end
}
