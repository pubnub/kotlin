package com.pubnub.api.datasync

/**
 * Entry point for the DataSync (App Context v4) API, reached via `pubnub.dataSync`.
 */
interface DataSync {
    /**
     * Entity operations (`get` / `getAll` / `create` / `update` / `patch` / `delete`).
     */
    val entity: EntityApi
}
