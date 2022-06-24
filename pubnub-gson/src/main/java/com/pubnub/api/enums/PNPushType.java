package com.pubnub.api.enums;

public enum PNPushType {

    APNS("apns"),

    MPNS("mpns"),

    /**
     * Use FCM instead
     */
    @Deprecated
    GCM("gcm"),

    FCM("gcm"),

    APNS2("apns2");

    private final String value;

    PNPushType(String name) {
        value = name;
    }

    @Override
    public String toString() {
        return this.value;
    }
}