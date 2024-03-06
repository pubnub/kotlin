package com.pubnub.internal.callbacks

import com.pubnub.internal.v2.callbacks.EventListenerCore
import com.pubnub.internal.v2.callbacks.StatusListenerCore

interface SubscribeCallback : StatusListenerCore, EventListenerCore
