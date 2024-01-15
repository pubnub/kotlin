package com.pubnub.api.models.consumer.pubsub.objects;

import com.google.gson.JsonElement;

import lombok.Data;

@Data
public class ObjectPayload {

    private String source;
    private String version;
    private String event;
    private String type;
    private JsonElement data;

}
