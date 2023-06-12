package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.models.consumer.PNStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EmitStatusEffectTest {
    private val statusConsumer: StatusConsumer = mockk()
    val status: PNStatus = mockk()

    @Test
    fun `should announce status when status provided`() {
        // given
        val emitStatusEffect = EmitStatusEffect(statusConsumer, status)
        val pnStatusCapture = slot<PNStatus>()
        every { statusConsumer.announce(capture(pnStatusCapture)) } returns Unit

        // when
        emitStatusEffect.runEffect()

        // then
        verify { statusConsumer.announce(capture(pnStatusCapture)) }
        assertEquals(status, pnStatusCapture.captured)
    }
}
