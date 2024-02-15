package com.pubnub.api.v2.callbacks

import com.pubnub.api.callbacks.Listener
import com.pubnub.internal.v2.callbacks.EventListener

interface EventEmitter {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    fun addListener(listener: EventListener)

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
