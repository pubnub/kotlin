package com.pubnub.internal.eventengine

import com.pubnub.contract.subscribe.eventEngine.state.TestSinkSource
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.ScheduledExecutorService

class EventEngineTest {

    internal object TestEffectInvocation : EffectInvocation {
        override val id: String = "id"
        override val type: EffectInvocationType = NonManaged
    }

    internal object TestState : State<TestEffectInvocation, Event, TestState> {
        override fun transition(event: Event): Pair<TestState, Set<TestEffectInvocation>> {
            return transitionTo(TestState, TestEffectInvocation)
        }
    }

    private val executorService: ScheduledExecutorService = mockk()

    @Test
    internal fun `should pass invocations for handling when event provided`() {
        // given
        val queuedElements = mutableListOf<Pair<String, String>>()
        val eventEngineConf =
            QueueEventEngineConf<TestEffectInvocation, Event>(effectSinkSource = TestSinkSource(queuedElements))
        val eventEngine = EventEngine(
            effectSink = eventEngineConf.effectSink,
            eventSource = eventEngineConf.eventSource,
            currentState = TestState,
            executorService = executorService
        )

        // when
        eventEngine.performTransitionAndEmitEffects(object : Event {})

        // then
        Assertions.assertEquals(listOf("invocation" to "TEST_EFFECT_INVOCATION"), queuedElements)
    }
}
