package com.pubnub.api.datasync

/**
 * Entry point for the DataSync (App Context v4) API, reached via `pubnub.dataSync`.
 */
interface DataSync {
    /**
     * Entity operations (`get` / `create` / `delete`).
     */
    val entity: EntityApi
}
