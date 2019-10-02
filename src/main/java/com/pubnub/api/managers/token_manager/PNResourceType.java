package com.pubnub.api.managers.token_manager;

public enum PNResourceType {
    CHANNEL("channel"),
    GROUP("group"),
    USER("user"),
    SPACE("space");

    private final String value;

    PNResourceType(String s) {
        value = s;
    }

    public String toString() {
        return this.value;
    }

}