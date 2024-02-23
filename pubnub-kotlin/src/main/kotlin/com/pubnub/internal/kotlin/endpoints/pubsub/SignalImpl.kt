package com.pubnub.internal.kotlin.endpoints.pubsub

import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.pubsub.ISignal

/**
 * @see [PubNubImpl.signal]
 */
class SignalImpl internal constructor(signal: ISignal) : Signal, ISignal by signal