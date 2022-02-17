package com.pubnub.api.subscribe.internal

import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import com.pubnub.api.subscribe.*
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test

class AlternativeDSLTest {
    @Test
    fun a() {
        val inputs = listOf(
            Commands.SubscribeIssued(channels = listOf("ch1")),
            HandshakeResult.HandshakeSucceeded(cursor = Cursor(timetoken = 5, region = "12")),
            ReceivingResult.ReceivingSucceeded(
                SubscribeEnvelope(
                    messages = listOf(),
                    metadata = SubscribeMetaData(timetoken = 5, region = "13")
                )
            )
        )
        val fn = subscribeTransitions { true }

        val (s, effects) = inputs.fold<SubscribeEvent, Pair<SubscribeState, Collection<AbstractSubscribeEffect>>>(Unsubscribed to listOf()) { (s, ef), ev ->
            val (ns, nEf) = fn(s, ev)
            ns to (ef + nEf)
        }

        println(effects)

        println(effects.mapNotNull { if (it is NewState) it.name else null })

        MatcherAssert.assertThat(
            effects.mapNotNull { if (it is NewState) it.name else null },
            Matchers.`is`(
                listOf(
                    Handshaking::class.simpleName,
                    Receiving::class.simpleName,
                    Receiving::class.simpleName
                )
            )
        )

    }

}