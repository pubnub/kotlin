package com.pubnub.api.managers.token_manager;

public enum PNMatchType {
    RESOURCE("resource"),
    PATTERN("pattern");

    private final String value;

    PNMatchType(String s) {
        value = s;
    }

    public String toString() {
        return this.value;
    }
}