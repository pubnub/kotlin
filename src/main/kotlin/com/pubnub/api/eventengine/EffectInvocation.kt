package com.pubnub.api.eventengine

internal interface EffectInvocation {
    val id: String
    val type: EffectInvocationType
}

internal sealed interface EffectInvocationType

internal data class Cancel(val idToCancel: String) : EffectInvocationType

internal object Managed : EffectInvocationType

internal object NonManaged : EffectInvocationType
