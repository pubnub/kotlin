package com.pubnub.api.endpoints.pubsub

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.pubsub.ISignal

/**
 * @see [PubNubImpl.signal]
 */
class Signal internal constructor(signal: ISignal) : ISignal by signal
