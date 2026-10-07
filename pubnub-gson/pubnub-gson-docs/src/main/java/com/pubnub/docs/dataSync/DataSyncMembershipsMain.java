package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.docs.SnippetBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataSyncMembershipsMain extends SnippetBase {
    private void createMembershipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#create-membership

        PubNub pubnub = createPubNub();

        // snippet.createMembershipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("role", "viewer");

        pubnub.dataSync().createMembership("channel-summer-sale", "user-alice", 1)
                .membershipId("membership-alice-summer-sale")
                .status("active")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created membership " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getMembershipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-membership

        PubNub pubnub = createPubNub();

        // snippet.getMembershipBasicUsage
        pubnub.dataSync().getMembership("membership-alice-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched membership " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getMembershipsBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-all-memberships

        PubNub pubnub = createPubNub();

        // snippet.getMembershipsBasicUsage
        pubnub.dataSync().getMemberships()
                .userId("user-alice")
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

    private void setMembershipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#set-membership

        PubNub pubnub = createPubNub();

        // snippet.setMembershipBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("role", "moderator");

        pubnub.dataSync().setMembership("membership-alice-summer-sale", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced membership " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void updateMembershipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#update-membership

        PubNub pubnub = createPubNub();

        // snippet.updateMembershipBasicUsage
        pubnub.dataSync().updateMembership("membership-alice-summer-sale", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/role").value("moderator").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated membership " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void removeMembershipBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#remove-membership

        PubNub pubnub = createPubNub();

        // snippet.removeMembershipBasicUsage
        pubnub.dataSync().removeMembership("membership-alice-summer-sale")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed membership, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
