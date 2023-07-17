package com.pubnub.api.eventengine

interface EffectInvocation {
    val id: String
    val type: EffectInvocationType
}

sealed interface EffectInvocationType

data class Cancel(val idToCancel: String) : EffectInvocationType

object Managed : EffectInvocationType

object NonManaged : EffectInvocationType
