package com.pubnub.api.models.server;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Envelope<T> {
    private int status;
    private String message;
    private String service;
    private T payload;
    private int occupancy;
    private JsonNode uuids;
    private String action;
    private boolean error;
}
