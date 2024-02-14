package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.pubsub.ISignal

/**
 * @see [PubNub.signal]
 */
class Signal internal constructor(signal: ISignal) : ISignal by signal
