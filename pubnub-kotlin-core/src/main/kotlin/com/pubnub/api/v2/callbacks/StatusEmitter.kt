package com.pubnub.api.v2.callbacks

import com.pubnub.internal.callbacks.Listener
import com.pubnub.internal.v2.callbacks.StatusListener

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
