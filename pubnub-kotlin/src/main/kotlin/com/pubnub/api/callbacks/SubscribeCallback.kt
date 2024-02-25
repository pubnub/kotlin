package com.pubnub.api.callbacks

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener

abstract class SubscribeCallback : StatusListener, EventListener
