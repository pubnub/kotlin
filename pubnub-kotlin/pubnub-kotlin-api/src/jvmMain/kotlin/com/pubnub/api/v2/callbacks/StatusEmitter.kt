package com.pubnub.api.v2.callbacks

import com.pubnub.api.callbacks.Listener

/**
 * Interface implemented by objects that manage the subscription connection to the PubNub network and can be monitored
 * for connection state changes.
 */
interface StatusEmitter {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    fun addListener(listener: StatusListener)

    /**
     * Remove a listener.
     *
     * @param listener The listener to be removed, previously added with [addListener].
     */
    fun removeListener(listener: Listener)

    /**
     * Removes all listeners.
     */
    fun removeAllListeners()
}
