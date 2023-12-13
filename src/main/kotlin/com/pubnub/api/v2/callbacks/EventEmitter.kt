package com.pubnub.api.v2.callbacks

import com.pubnub.internal.v2.callbacks.Emitter

interface EventEmitter : Emitter<EventListener> {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener)

    /**
     * Remove a listener.
     *
     * @param listener The listener to be removed, previously added with [addListener].
     */
    override fun removeListener(listener: EventListener)

    /**
     * Removes all listeners.
     */
    override fun removeAllListeners()
}
