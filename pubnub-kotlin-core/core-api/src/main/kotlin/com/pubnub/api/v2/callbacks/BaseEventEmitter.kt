package com.pubnub.api.v2.callbacks

import com.pubnub.api.callbacks.Listener

interface BaseEventEmitter {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    fun addListener(listener: BaseEventListener)

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
