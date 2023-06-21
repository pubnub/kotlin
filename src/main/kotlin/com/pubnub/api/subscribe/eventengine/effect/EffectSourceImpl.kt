package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.EffectSource
import java.util.concurrent.LinkedBlockingQueue

class EffectSourceImpl<T : EffectInvocation>(
    val queue: LinkedBlockingQueue<T>
) : EffectSource<T> {
    override fun take(): T {
        return queue.take()
    }
}
