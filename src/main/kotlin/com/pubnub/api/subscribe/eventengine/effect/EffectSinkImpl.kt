package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.EffectSink
import java.util.concurrent.LinkedBlockingQueue

class EffectSinkImpl(
    val queue: LinkedBlockingQueue<SubscribeEffectInvocation>
) : EffectSink<SubscribeEffectInvocation> {
    override fun add(invocation: SubscribeEffectInvocation) {
        queue.add(invocation)
    }
}
