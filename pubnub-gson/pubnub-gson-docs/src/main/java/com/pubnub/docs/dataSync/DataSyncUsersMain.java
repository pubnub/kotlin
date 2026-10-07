package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataSyncUsersMain extends SnippetBase {
    private void createUserBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#create-user

        PubNub pubnub = createPubNub();

        // snippet.createUserBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Alice");
        payload.put("type", "shopper");

        pubnub.dataSync().createUser(1)
                .userId("user-alice")
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Created user " + value.getData().getId() + " with eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getUserBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-user

        PubNub pubnub = createPubNub();

        // snippet.getUserBasicUsage
        pubnub.dataSync().getUser("user-alice")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Fetched user " + value.getData().getId() + " with payload " + value.getData().getPayload());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void getUsersBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#get-all-users

        PubNub pubnub = createPubNub();

        // snippet.getUsersBasicUsage
        pubnub.dataSync().getUsers()
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

    private void setUserBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#set-user

        PubNub pubnub = createPubNub();

        // snippet.setUserBasicUsage
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Alice B.");
        payload.put("type", "shopper");

        pubnub.dataSync().setUser("user-alice", 1)
                .payload(payload)
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Replaced user " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void updateUserBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#update-user

        PubNub pubnub = createPubNub();

        // snippet.updateUserBasicUsage
        pubnub.dataSync().updateUser("user-alice", Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/payload/name").value("Alice B.").build()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Updated user " + value.getData().getId() + ", new eTag " + value.getData().getETag());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void removeUserBasicUsage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#remove-user

        PubNub pubnub = createPubNub();

        // snippet.removeUserBasicUsage
        pubnub.dataSync().removeUser("user-alice")
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Removed user, HTTP status " + value.getStatus());
                    }).onFailure(exception -> {
                        System.out.println("Failed: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
