package com.pubnub.docs.publishAndSubscribe.publish;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.docs.SnippetBase;
import org.json.JSONObject;

import java.util.Arrays;

public class PublishOld extends SnippetBase {

    private void publishOldMessageToChannel() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publish-a-message-to-a-channel-1

        PubNub pubNub = createPubNub();

        // snippet.publishOld
        JsonObject position = new JsonObject();
        position.addProperty("lat", 32L);
        position.addProperty("lng", 32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position)
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
    }

    private void publishOldWithMetadata() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publish-with-metadata-1

        PubNub pubNub = createPubNub();

        // snippet.publishOldWithMetadata
        PNPublishResult pnPublishResult = pubNub.publish()
                .message(Arrays.asList("hello", "there"))
                .channel("suchChannel")
                .shouldStore(true)
                .meta("meta") // optional meta data object which can be used with the filtering ability.
                .usePOST(true)
                .sync();
        // snippet.end
    }

    private void publishJsonObjectGson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonobject-google-gson-1

        PubNub pubNub = createPubNub();

        // snippet.publishJsonObjectGson
        JsonObject position = new JsonObject();
        position.addProperty("lat", 32L);
        position.addProperty("lng", 32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position)
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
    }

    private void publishJsonArrayGson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonarray-google-gson-1

        PubNub pubNub = createPubNub();

        // snippet.publishJsonArrayGson
        JsonArray position = new JsonArray();
        position.add(32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position)
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
    }

    private void publishJsonObjectOrgJson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonobject-orgjson-1

        PubNub pubNub = createPubNub();

        // snippet.publishJsonObjectOrgJson
        JSONObject position = new JSONObject();
        position.put("lat", 32L);
        position.put("lng", 32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position.toMap())
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
    }

    private void publishJsonArrayOrgJson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonarray-orgjson-1

        PubNub pubNub = createPubNub();

        // snippet.publishJsonArrayOrgJson
        org.json.JSONArray position = new org.json.JSONArray();
        position.put(32L);

        System.out.println("before pub: " + position);
        pubNub.publish()
                .message(position.toList())
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("pub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
    }

    private void publishMessagesFor10Hours () throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#store-the-published-message-for-10-hours-1

        PubNub pubNub = createPubNub();

        // snippet.publishMessagesFor10Hours
        PNPublishResult result = pubNub.publish()
                .channel("coolChannel")
                .message("test")
                .shouldStore(true)
                .ttl(10)
                .sync();
        // snippet.end
    }
}
