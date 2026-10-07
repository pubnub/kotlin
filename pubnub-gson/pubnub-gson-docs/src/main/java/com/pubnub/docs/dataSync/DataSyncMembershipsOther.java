package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.docs.SnippetBase;

import java.util.Collections;

public class DataSyncMembershipsOther extends SnippetBase {
    private void getMembershipsFilterFast() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getMembershipsFilterFast
        // Membership payload fields such as `role` are not filterable, filter on `status` instead
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
                .filterFast("status == \"active\"")
                .sort(Collections.singletonList(new PNDataSyncSortField("createdAt", false)))
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

    private void getMembershipsFilter() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#filtering-and-sorting

        PubNub pubnub = createPubNub();

        // snippet.getMembershipsFilter
        // Membership payload fields such as `role` are not filterable, filter on `status` instead
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
                .filter("status LIKE \"act*\"")
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
    // snippet.getMembershipsPagination
    private void fetchMembershipsPage(PubNub pubnub, String cursor) {
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
                .limit(20)
                .cursor(cursor)
                .async(result -> {
                            result.onSuccess(value -> {
                                value.getData().forEach(item -> System.out.println(item.getId()));
                                if (value.getNext().isHasNext()) {
                                    fetchMembershipsPage(pubnub, value.getNext().getCursor());
                                }
                            }).onFailure(exception -> {
                                System.out.println("Failed: " + exception.getMessage());
                            });
                        });
            }
            // Start with the first page by passing a null cursor: fetchMembershipsPage(pubnub, null)
            // snippet.end

            private void getMembershipsByChannelId() throws PubNubException {
                // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-all-memberships

                PubNub pubnub = createPubNub();

                // snippet.getMembershipsByChannelId
                pubnub.dataSync().getMemberships()
                        .channelId("channel-summer-sale")
                        .limit(50)
                        .async(result -> {
                    result.onSuccess(value -> {
                        value.getData().forEach(item -> System.out.println(item.getUserId() + " is a member of " + item.getChannelId()));
                        System.out.println("More pages available: " + value.getNext().isHasNext());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
