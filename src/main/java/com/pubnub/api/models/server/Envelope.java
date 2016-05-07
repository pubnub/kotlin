package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Envelope<T> {
    private int status;
    private String message;
    private String service;
    private T payload;
    private int occupancy;
    private Object uuids;
    private String action;
    private boolean error;
}
