package com.pubnub.api.java.datasync;

/**
 * Entry point for the DataSync (App Context v4) API, reached via {@code pubnub.dataSync()}.
 */
public interface DataSync {
    /**
     * Entity operations ({@code get} / {@code create} / {@code delete}).
     */
    EntityApi entity();
}
