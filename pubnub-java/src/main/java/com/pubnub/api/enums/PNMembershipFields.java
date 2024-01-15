package com.pubnub.api.enums;

public enum PNMembershipFields {
    CUSTOM("custom"),
    SPACE("space"),
    SPACE_CUSTOM("space.custom");

    private final String value;

    PNMembershipFields(String s) {
        value = s;
    }

    public String toString() {
        return this.value;
    }
}