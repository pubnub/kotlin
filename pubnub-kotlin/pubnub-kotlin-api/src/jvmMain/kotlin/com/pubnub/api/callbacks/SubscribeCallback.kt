package com.pubnub.api.callbacks

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener

/**
 * A combined listener for status and event updates.
 * Included for backward compatibility with previous versions of the PubNub SDK.
 *
 * Setting explicit [StatusListener] and/or [EventListener] should be preferred.
 */
abstract class SubscribeCallback : StatusListener, EventListener