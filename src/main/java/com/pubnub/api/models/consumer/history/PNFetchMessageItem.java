package com.pubnub.api.models.consumer.history;

import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

@Getter
@Builder
public class PNFetchMessageItem {

    @Setter
    private JsonElement message;
    private JsonElement meta;
    private Long timetoken;
    private HashMap<String, HashMap<String, List<Action>>> actions;

    @Getter
    @ToString
    public static class Action {
        private String uuid;
        private String actionTimetoken;
    }
}
