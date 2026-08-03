package com.pubnub.api.java.datasync;

/**
 * Entry point for the DataSync API, reached via {@code pubnub.dataSync()}.
 */
public interface DataSync {
    /**
     * Entity operations ({@code get} / {@code getAll} / {@code create} / {@code update} / {@code patch} / {@code delete}).
     */
    EntityApi entity();

    /**
     * User operations ({@code get} / {@code getAll} / {@code create} / {@code update} / {@code patch} / {@code delete}).
     */
    UserApi user();
}
