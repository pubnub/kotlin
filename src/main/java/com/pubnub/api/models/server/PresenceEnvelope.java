package com.pubnub.api.models.server;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class PresenceEnvelope {

    private String action;
    private String uuid;
    private Integer occupancy;
    private Long timestamp;
    private JsonNode data;

}
