package com.pubnub.api.subscribe

import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.Effect
import com.pubnub.api.subscribe.internal.SubscriptionStatus

abstract class AbstractSubscribeEffect : Effect()

data class NewMessages(val messages: List<SubscribeMessage>) : AbstractSubscribeEffect()

data class NewState(val name: String, val status: SubscriptionStatus) : AbstractSubscribeEffect()
