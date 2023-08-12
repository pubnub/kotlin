package com.pubnub.api.managers

import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.PresenceEventEngine
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PresenceEventEngineManagerTest {
    private lateinit var objectUnderTest: PresenceEventEngineManager

    val presenceEventEngine: PresenceEventEngine = mockk()
    val effectDispatcher: EffectDispatcher<PresenceEffectInvocation> = mockk()
    val presenceEventSink: Sink<PresenceEvent> = mockk()
    val presenceEvent: PresenceEvent = mockk()

    @BeforeEach
    fun setUp() {
        objectUnderTest = PresenceEventEngineManager(presenceEventEngine, effectDispatcher, presenceEventSink)
    }

    @Test
    fun `should add event to eventSink when adding event to queue`() {
        // given
        every { presenceEventSink.add(presenceEvent) } returns Unit

        // when
        objectUnderTest.addEventToQueue(presenceEvent)

        // then
        verify { presenceEventSink.add(presenceEvent) }
    }

    @Test
    fun `should start eventEngine and effectDispatcher when calling start`() {
        // given
        every { presenceEventEngine.start() } returns Unit
        every { effectDispatcher.start() } returns Unit

        // when
        objectUnderTest.start()

        // then
        verify { presenceEventEngine.start() }
        verify { effectDispatcher.start() }
    }

    @Test
    fun `should stop eventEngine and effectDispatcher when calling stop`() {
        // given
        every { presenceEventEngine.stop() } returns Unit
        every { effectDispatcher.stop() } returns Unit

        // when
        objectUnderTest.stop()

        // then
        verify { presenceEventEngine.stop() }
        verify { effectDispatcher.stop() }
    }
}
