package com.pubnub.internal

import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf

internal class TestPubNub(configuration: PNConfiguration, eventEnginesConf: EventEnginesConf = EventEnginesConf()) : BasePubNub(configuration, eventEnginesConf)
