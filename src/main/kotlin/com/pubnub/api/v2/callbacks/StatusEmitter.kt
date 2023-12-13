package com.pubnub.api.v2.callbacks

import com.pubnub.internal.v2.callbacks.Emitter

interface StatusEmitter : Emitter<StatusListener> {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: StatusListener)

    /**
     * Remove a listener.
     *
     * @param listener The listener to be removed, previously added with [addListener].
     */
    override fun removeListener(listener: StatusListener)

    /**
     * Removes all listeners.
     */
    override fun removeAllListeners()
}
