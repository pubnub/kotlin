package com.pubnub.api.eventengine

import com.pubnub.internal.presence.eventengine.PresenceEventEngine
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventEngineManagerTest {
    private lateinit var objectUnderTest: EventEngineManager<PresenceEffectInvocation, PresenceEvent, PresenceState, PresenceEventEngine>
    private val eventEngine: PresenceEventEngine = mockk()
    private val effectDispatcher: EffectDispatcher<PresenceEffectInvocation> = mockk()
    private val eventSink: Sink<PresenceEvent> = mockk()

    @BeforeEach
    fun setUp() {
        objectUnderTest = EventEngineManager(eventEngine, effectDispatcher, eventSink)
    }

    @Test
    fun `should add event to eventSink when adding event to queue`() {
        // given
        val event: PresenceEvent = mockk()
        every { eventSink.add(event) } returns Unit

        // when
        objectUnderTest.addEventToQueue(event)

        // then
        verify { eventSink.add(event) }
    }

    @Test
    fun `should start eventEngine and effectDispatcher when calling start`() {
        // given
        every { eventEngine.start() } returns Unit
        every { effectDispatcher.start() } returns Unit

        // when
        objectUnderTest.start()

        // then
        verify { eventEngine.start() }
        verify { effectDispatcher.start() }
    }

    @Test
    fun `should stop eventEngine and effectDispatcher when calling stop`() {
        // given
        every { eventEngine.stop() } returns Unit
        every { effectDispatcher.stop() } returns Unit

        // when
        objectUnderTest.stop()

        // then
        verify { eventEngine.stop() }
        verify { effectDispatcher.stop() }
    }
}
