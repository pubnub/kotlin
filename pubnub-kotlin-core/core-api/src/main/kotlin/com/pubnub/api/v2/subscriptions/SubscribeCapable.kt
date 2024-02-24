package com.pubnub.api.v2.subscriptions

interface SubscribeCapable {
    /**
     * Start receiving events from the subscriptions (or subscriptions) represented by this object.
     *
     * The PubNub client will start a network connection to the server if it doesn't have one already,
     * or will alter the existing connection to add channels and groups requested by this subscriptions if needed.
     *
     * Please note that passing a [cursor] to [subscribe] affects *all* subscriptions that are currently active in the
     * [com.pubnub.api.PubNub] client, as it will reset the global timetoken for the server connection. If an active
     * subscriptions had previously delivered *any* events to its listeners, it will only deliver events *newer* that the
     * last timetoken it recorded.
     *
     */
    fun subscribe(cursor: SubscriptionCursor = SubscriptionCursor(0))

    /**
     * Stop receiving events from this subscriptions.
     *
     * Please note that if there are any other subscriptions to the same channel or channel group, they will continue to
     * receive events until they are also unsubscribed. Only once there are no longer any active subscriptions to a
     * given channel/group, the [com.pubnub.api.PubNub] client will remove that channel/group from the connection.
     */
    fun unsubscribe()
}
