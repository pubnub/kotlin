package com.pubnub.api.managers

import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.SubscribeEventEngine
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventEngineManagerTest {
    private lateinit var objectUnderTest: EventEngineManager

    val subscribeEventEngine: SubscribeEventEngine = mockk()
    val effectDispatcher: EffectDispatcher<SubscribeEffectInvocation> = mockk()
    val eventSink: Sink<Event> = mockk()
    val event: Event = mockk()

    @BeforeEach
    fun setUp() {
        objectUnderTest = EventEngineManager(subscribeEventEngine, effectDispatcher, eventSink)
    }

    @Test
    fun `should add event to eventSink when adding event to queue`() {
        // given
        every { eventSink.add(event) } returns Unit

        // when
        objectUnderTest.addEventToQueue(event)

        // then
        verify { eventSink.add(event) }
    }

    @Test
    fun `should start eventEngine and effectDispatcher when calling start`() {
        // given
        every { subscribeEventEngine.start() } returns Unit
        every { effectDispatcher.start() } returns Unit

        // when
        objectUnderTest.start()

        // then
        verify { subscribeEventEngine.start() }
        verify { effectDispatcher.start() }
    }

    @Test
    fun `should stop eventEngine and effectDispatcher when calling stop`() {
        // given
        every { subscribeEventEngine.stop() } returns Unit
        every { effectDispatcher.stop() } returns Unit

        // when
        objectUnderTest.stop()

        // then
        verify { subscribeEventEngine.stop() }
        verify { effectDispatcher.stop() }
    }
}
