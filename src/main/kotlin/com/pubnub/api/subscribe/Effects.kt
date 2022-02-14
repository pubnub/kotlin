package com.pubnub.api.subscribe

import com.pubnub.api.models.server.SubscribeMessage
import com.pubnub.api.state.Effect

sealed interface SubscribeEffect

abstract class AbstractSubscribeEffect : Effect(), SubscribeEffect

data class NewMessagesEffect(val messages: List<SubscribeMessage>) : AbstractSubscribeEffect()

data class NewStateEffect(val name: String) : AbstractSubscribeEffect()
