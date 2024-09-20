package com.pubnub.api.v2.callbacks

import com.pubnub.api.callbacks.Listener

/**
 * Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for
 * PubNub connection status changes.
 */
actual interface StatusListener : Listener
