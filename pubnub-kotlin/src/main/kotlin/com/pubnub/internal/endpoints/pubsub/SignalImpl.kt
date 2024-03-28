package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.signal]
 */
class SignalImpl internal constructor(signal: SignalInterface) : Signal, SignalInterface by signal
