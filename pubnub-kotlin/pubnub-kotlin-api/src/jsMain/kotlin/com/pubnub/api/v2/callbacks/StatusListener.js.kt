package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus

import PubNub as PubNubJs

/**
 * Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for
 * PubNub connection status changes.
 */
actual typealias StatusListener = PubNubJs.StatusListenerParameters