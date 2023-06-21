package com.pubnub.contract.state

import com.pubnub.api.eventengine.EffectSink
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue

private const val HAPPENING_TYPE_INVOCATION = "invocation"
private const val HAPPENING_TYPE = "type"
private const val HAPPENING_NAME = "name"
private const val PATH_FROM_CLASS_NAME_SEPARATOR = "$"

class EffectSinkTestImpl(
    private val queue: LinkedBlockingQueue<SubscribeEffectInvocation>,
    private val happenings: CopyOnWriteArrayList<HashMap<String, String>>
) : EffectSink<SubscribeEffectInvocation> {
    override fun add(invocation: SubscribeEffectInvocation) {
        val invocationName = getInvocationName(invocation)
        val happeningTypeAndName = HashMap<String, String>().also {
            it[HAPPENING_TYPE] = HAPPENING_TYPE_INVOCATION
            it[HAPPENING_NAME] = invocationName
        }

        happenings.add(happeningTypeAndName)
        queue.add(invocation)
    }

    private fun getInvocationName(invocation: SubscribeEffectInvocation): String {
        val eventClassPath = invocation.javaClass.name
        return eventClassPath.subSequence(eventClassPath.indexOf(PATH_FROM_CLASS_NAME_SEPARATOR) + 1, eventClassPath.length).toString()
    }
}
