package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscription.Subscription

interface ChannelGroup : BaseChannelGroup<EventListener, Subscription>
