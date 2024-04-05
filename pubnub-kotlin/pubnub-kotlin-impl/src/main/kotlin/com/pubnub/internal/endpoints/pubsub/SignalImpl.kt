package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.signal]
 */
class SignalImpl internal constructor(signal: SignalInterface) :
    Signal,
    SignalInterface by signal,
    EndpointImpl<PNPublishResult>(signal)
