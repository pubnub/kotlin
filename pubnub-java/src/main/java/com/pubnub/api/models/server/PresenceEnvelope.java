package com.pubnub.api.models.server;

import com.google.gson.JsonElement;
import lombok.Getter;

@Getter
public class PresenceEnvelope {

    private String action;
    private String uuid;
    private Integer occupancy;
    private Long timestamp;
    private JsonElement data;

}
