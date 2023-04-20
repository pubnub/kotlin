package com.pubnub.api.subscribe.eventengine.effect

import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasKey
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap

class EffectDispatcherTest {

    sealed class TestEffectInvocation : EffectInvocation {
        override val id: String = this::class.java.simpleName
    }

    object TestEffect :
        TestEffectInvocation(),
        ManagedEffectInvocation
    object ImmediateEndingTestEffect :
        TestEffectInvocation(),
        ManagedEffectInvocation
    object CancelTestEffect : TestEffectInvocation(), CancelEffectInvocation {
        override val idToCancel: String = TestEffect::class.java.simpleName
    }

    class EffectHandlerFactoryImpl : EffectHandlerFactory<TestEffectInvocation> {
        override fun create(effectInvocation: TestEffectInvocation): ManagedEffect {
            return object : ManagedEffect {
                override val id: String = effectInvocation.id

                override fun run(completionBlock: () -> Unit) {
                    when (effectInvocation) {
                        is ImmediateEndingTestEffect -> {
                            completionBlock()
                        }

                        else -> {
                        }
                    }
                }

                override fun cancel() {
                }
            }
        }
    }

    @Test
    fun managedEffectIsNotEvictedTillCancelled() {
        // given
        val managedEffects = ConcurrentHashMap<String, ManagedEffect>()
        val effectDispatcher = EffectDispatcher(
            effectHandlerFactory = EffectHandlerFactoryImpl(),
            managedEffects = managedEffects
        )

        // when
        effectDispatcher.dispatch(TestEffect)

        // then
        assertThat(managedEffects, hasKey(TestEffect.id))
    }

    @Test
    fun managedEffectIsEvictedAfterCancel() {
        // given
        val managedEffects = ConcurrentHashMap<String, ManagedEffect>()
        val effectDispatcher = EffectDispatcher(
            effectHandlerFactory = EffectHandlerFactoryImpl(),
            managedEffects = managedEffects
        )

        // when
        effectDispatcher.dispatch(TestEffect)
        effectDispatcher.dispatch(CancelTestEffect)

        // then
        assertThat(managedEffects, not(hasKey(TestEffect.id)))
    }

    @Test
    fun managedEffectIsEvictingItselfAfterCompletion() {
        // given
        val managedEffects = ConcurrentHashMap<String, ManagedEffect>()
        val effectDispatcher = EffectDispatcher(
            effectHandlerFactory = EffectHandlerFactoryImpl(),
            managedEffects = managedEffects
        )

        // when
        effectDispatcher.dispatch(ImmediateEndingTestEffect)

        // then
        assertThat(managedEffects, not(hasKey(ImmediateEndingTestEffect.id)))
    }

    @Test
    fun canCancelEvictedEffect() {
        // given
        val managedEffects = ConcurrentHashMap<String, ManagedEffect>()
        val effectDispatcher = EffectDispatcher(
            effectHandlerFactory = EffectHandlerFactoryImpl(),
            managedEffects = managedEffects
        )

        // when
        effectDispatcher.dispatch(TestEffect)
        effectDispatcher.dispatch(CancelTestEffect)
        effectDispatcher.dispatch(CancelTestEffect)

        // then
        assertThat(managedEffects, not(hasKey(TestEffect.id)))
    }

    @Test
    fun puttingEffectWithSameIdCancelsTheFirstOne() {
        // given
        val managedEffects = ConcurrentHashMap<String, ManagedEffect>()
        val effectHandlerFactory = EffectHandlerFactoryImpl()
        val managedEffect = spyk(effectHandlerFactory.create(TestEffect))
        managedEffects[TestEffect.id] = managedEffect
        val effectDispatcher = EffectDispatcher(
            effectHandlerFactory = effectHandlerFactory,
            managedEffects = managedEffects
        )

        // when
        effectDispatcher.dispatch(TestEffect)

        // then
        verify(exactly = 1) { managedEffect.cancel() }
        assertThat(managedEffects, hasKey(TestEffect.id))
    }
}
