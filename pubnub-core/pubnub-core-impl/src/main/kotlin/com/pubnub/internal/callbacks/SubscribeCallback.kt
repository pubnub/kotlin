package com.pubnub.internal.callbacks

import com.pubnub.internal.v2.callbacks.InternalEventListener
import com.pubnub.internal.v2.callbacks.InternalStatusListener

interface SubscribeCallback : InternalStatusListener, InternalEventListener
