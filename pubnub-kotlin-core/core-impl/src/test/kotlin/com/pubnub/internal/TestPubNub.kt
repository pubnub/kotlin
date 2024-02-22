package com.pubnub.internal

import com.pubnub.api.callbacks.Listener
import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import com.pubnub.internal.v2.subscription.BaseSubscriptionSetImpl

internal class TestPubNub(configuration: PNConfiguration, eventEnginesConf: EventEnginesConf = EventEnginesConf()) :
    BasePubNubImpl<Listener>(configuration, eventEnginesConf, ::BaseSubscriptionImpl, ::BaseSubscriptionSetImpl)
