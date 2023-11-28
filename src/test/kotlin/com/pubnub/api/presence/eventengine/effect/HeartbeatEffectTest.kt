package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import com.pubnub.api.subscribe.eventengine.effect.TestEventSink
import com.pubnub.api.subscribe.eventengine.effect.failingRemoteAction
import com.pubnub.api.subscribe.eventengine.effect.successfulRemoteAction
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class HeartbeatEffectTest {
    private val eventSink = TestEventSink<PresenceEvent>()
    private val reason = PubNubException("Unknown error")
    private val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
    private val statusConsumer: StatusConsumer = mockk()

    companion object {
        @JvmStatic
        fun successfulHeartbeatWithNotificationOptions(): List<Arguments> = listOf(
            Arguments.of(PNHeartbeatNotificationOptions.NONE, false),
            Arguments.of(PNHeartbeatNotificationOptions.FAILURES, false),
            Arguments.of(PNHeartbeatNotificationOptions.ALL, true)
        )

        @JvmStatic
        fun unsuccessfulHeartbeatWithNotificationOptions(): List<Arguments> = listOf(
            Arguments.of(PNHeartbeatNotificationOptions.NONE, false),
            Arguments.of(PNHeartbeatNotificationOptions.FAILURES, true),
            Arguments.of(PNHeartbeatNotificationOptions.ALL, true)
        )
    }

//    @Test
//    fun `should deliver HeartbeatSuccess event when HeartbeatEffect succeeded`() {
//        // given
//        every { statusConsumer.announce(any()) } returns Unit
//        val heartbeatEffect =
//            HeartbeatEffect(successfulRemoteAction(true), eventSink, heartbeatNotificationOptions, statusConsumer)
//
//        // when
//        heartbeatEffect.runEffect()
//
//        // then
//        Awaitility.await()
//            .atMost(Durations.ONE_SECOND)
//            .with()
//            .pollInterval(Duration.ofMillis(20))
//            .untilAsserted {
//                assertEquals(listOf(PresenceEvent.HeartbeatSuccess), eventSink.events)
//            }
//    }
//
//    @Test
//    fun `should deliver HeartbeatFailure event when HeartbeatEffect failed`() {
//        // given
//        every { statusConsumer.announce(any()) } returns Unit
//        val heartbeatEffect =
//            HeartbeatEffect(failingRemoteAction(reason), eventSink, heartbeatNotificationOptions, statusConsumer)
//        // when
//        heartbeatEffect.runEffect()
//
//        // then
//        Awaitility.await()
//            .atMost(Durations.ONE_SECOND)
//            .with()
//            .pollInterval(Duration.ofMillis(20))
//            .untilAsserted {
//                assertEquals(listOf(PresenceEvent.HeartbeatFailure(reason)), eventSink.events)
//            }
//    }

    @ParameterizedTest
    @MethodSource("successfulHeartbeatWithNotificationOptions")
    fun `should announce status when HeartbeatEffect succeeded`(
        pnHeartbeatNotificationOptions: PNHeartbeatNotificationOptions,
        shouldAnnounce: Boolean
    ) {
        // given
        val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = pnHeartbeatNotificationOptions
        val heartbeatEffect =
            HeartbeatEffect(successfulRemoteAction(true), eventSink, heartbeatNotificationOptions, statusConsumer)

        // when
        heartbeatEffect.runEffect()

        // then
        if (shouldAnnounce) {
            verify(exactly = 1) { statusConsumer.announce(any()) }
        } else {
            verify(exactly = 0) { statusConsumer.announce(any()) }
        }
    }

    @ParameterizedTest
    @MethodSource("unsuccessfulHeartbeatWithNotificationOptions")
    fun `should announce status when HeartbeatEffect failed`(
        pnHeartbeatNotificationOptions: PNHeartbeatNotificationOptions,
        shouldAnnounce: Boolean
    ) {
        // given
        val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = pnHeartbeatNotificationOptions
        val heartbeatEffect =
            HeartbeatEffect(failingRemoteAction(reason), eventSink, heartbeatNotificationOptions, statusConsumer)

        // when
        heartbeatEffect.runEffect()

        // then
        if (shouldAnnounce) {
            verify(exactly = 1) { statusConsumer.announce(any()) }
        } else {
            verify(exactly = 0) { statusConsumer.announce(any()) }
        }
    }
}
