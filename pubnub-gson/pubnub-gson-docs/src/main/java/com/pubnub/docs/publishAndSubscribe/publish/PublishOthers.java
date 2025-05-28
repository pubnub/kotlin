package com.pubnub.docs.publishAndSubscribe.publish;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Arrays;

public class PublishOthers {
    private void publishWithMetadata() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publish-with-metadata

        // snippet.publishWithMetadata
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        channel.publish(Arrays.asList("hello", "there"))
                .customMessageType("text-message")
                .shouldStore(true)
                .meta("meta") // optional meta data object which can be used with the filtering ability.
                .usePOST(true)
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("Message published with timetoken: " + publishResult.getTimetoken());
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("Error publishing message: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void publishJsonObjectGson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonobject-google-gson

        // snippet.publishJsonObject
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JsonObject position = new JsonObject();
        position.addProperty("lat", 32L);
        position.addProperty("lng", 32L);

        System.out.println("position: " + position);

        PNPublishResult publishResult = channel.publish(position)
                .customMessageType("text-message")
                .sync();

        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
        // snippet.end
    }

    private void publishJsonArrayGson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonarray-google-gson

        // snippet.publishJsonArray
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JsonArray position = new JsonArray();
        position.add(32L);

        System.out.println("before pub: " + position);

        channel.publish(position)
                .customMessageType("text-message")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
    }

    private void publishJsonObjectOrgJson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonobject-orgjson

        // snippet.publishJsonObjectOrgJson
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JSONObject position = new JSONObject();
        position.put("lat", 32L);
        position.put("lng", 32L);

        System.out.println("before pub: " + position);
        PNPublishResult publishResult = channel.publish(position.toMap())
                .customMessageType("text-message")
                .sync();

        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
        // snippet.end
    }

    private void publishJsonArrayOrgJson() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publishing-jsonarray-orgjson

        // snippet.publishJsonArrayOrgJson
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        JSONArray position = new JSONArray();
        position.put(32L);

        System.out.println("before pub: " + position);

        channel.publish(position.toList())
                .customMessageType("text-message")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
                    });
                });
        // snippet.end
    }

    private void publishWithTTL() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#store-the-published-message-for-10-hours

        // snippet.publishWithTTL
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        PNPublishResult publishResult = channel.publish("test-message")
                .customMessageType("text-message")
                .shouldStore(true)
                .ttl(10)
                .sync();

        System.out.println("PubNub timetoken: " + publishResult.getTimetoken());
        // snippet.end
    }
}
