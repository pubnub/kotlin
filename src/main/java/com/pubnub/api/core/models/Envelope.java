package com.pubnub.api.core.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Envelope<T> {
    int status;
    String message;
    String service;
    T payload;
    int occupancy;
    Object uuids;
}
