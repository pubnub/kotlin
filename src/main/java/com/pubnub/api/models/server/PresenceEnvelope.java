package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PresenceEnvelope {

    private String action;
    private String uuid;
    private Integer occupancy;
    private Long timestamp;
    private JsonNode data;

}
