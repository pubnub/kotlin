package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.EffectInvocation
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test

class SubscribeTransitionTest {

    @Test
    fun HappyPathScenario() {
        //given
        val transition = subscribeTransition()
        val initialState = Unsubscribed
        val events = listOf(SubscriptionChanged(channels = listOf("ch1")))

        //when
        val (endState, effects) = events.fold<SubscribeEvent, Pair<SubscribeState, List<EffectInvocation>>>(initialState to listOf<EffectInvocation>()) { (state, effects), event ->
            val (newState, newEffects) = transition(state, event)
            newState to effects + newEffects
        }


        //then
        assertThat(endState, Matchers.isA(Handshaking::class.java))
    }
}