package com.pubnub.api.eventengine

import com.pubnub.api.subscribe.eventengine.event.Event

interface EffectSink<T : EffectInvocation> : Sink<T>

interface EventSink : Sink<Event>
interface Sink<T> {
    fun add(el: T)
}

interface EffectSource<T : EffectInvocation> : Source<T>
interface EventSource : Source<Event>
interface Source<T> {
    fun next(): T
}
