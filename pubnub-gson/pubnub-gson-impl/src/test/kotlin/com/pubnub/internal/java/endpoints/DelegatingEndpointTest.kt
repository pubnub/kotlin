package com.pubnub.internal.java.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.UserId
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.java.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.getOrThrow
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class DelegatingEndpointTest {
    private lateinit var delegatingEndpoint: DelegatingEndpoint<Boolean, Boolean>
    var validateParamsCalled = false

    var mapping: (ExtendedRemoteAction<Boolean>) -> ExtendedRemoteAction<Boolean> = { it }
    val action = TestAction()

    @BeforeEach
    fun setUp() {
        delegatingEndpoint =
            object : DelegatingEndpoint<Boolean, Boolean>(mockk()) {
                override fun createRemoteAction(): Endpoint<Boolean> {
                    return action
                }

                override fun mapResult(action: ExtendedRemoteAction<Boolean>): ExtendedRemoteAction<Boolean> {
                    return mapping(action)
                }

                override fun validateParams() {
                    validateParamsCalled = true
                }
            }
    }

    @Test
    fun `when sync is called it calls sync on delegate`() {
        val result = delegatingEndpoint.sync()
        assertTrue(result)
        assertTrue(validateParamsCalled)
    }

    @Test
    fun `when remoteAction is called it returns action from createAction `() {
        assertEquals(action, delegatingEndpoint.remoteAction)
    }

    @Test
    fun `when remoteAction is called and mapping is set it returns mapped action from createAction `() {
        var mappedAction: ExtendedRemoteAction<Boolean>? = null
        mapping = { MappingRemoteAction(it) { result -> !result }.also { mapped -> mappedAction = mapped } }

        delegatingEndpoint.remoteAction

        assertEquals(mappedAction, delegatingEndpoint.remoteAction)
    }

    @Test
    fun `when mapping is set then overrideConfiguration sets override on original endpoint `() {
        mapping = { MappingRemoteAction(it) { result -> !result } }
        val overridingConfig = PNConfiguration.builder(UserId("myUser"), "mySub").build()

        delegatingEndpoint.overrideConfiguration(overridingConfig)
        delegatingEndpoint.remoteAction

        assertEquals(overridingConfig, action.overridenConfiguration)
    }

    @Test
    fun `when async is called it calls async on delegate`() {
        delegatingEndpoint.async { result ->
            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow())
            assertTrue(validateParamsCalled)
        }
    }

    @Test
    fun `when retry is called calls retry on delegate`() {
        delegatingEndpoint.retry()
        assertTrue(action.retryCalled)
    }

    @Test
    fun `when silentCancel is called calls silentCancel on delegate`() {
        delegatingEndpoint.silentCancel()
        assertTrue(action.silentCancelCalled)
    }

    @Test
    fun `when operationType is called calls operationType on delegate`() {
        assertEquals(PNOperationType.FileOperation, delegatingEndpoint.operationType)
        assertEquals(PNOperationType.FileOperation, delegatingEndpoint.operationType())
    }

    @Test
    fun `IdentityMappingEndpoint returns the same remote action`() {
        delegatingEndpoint = object : PassthroughEndpoint<Boolean>(mockk()) {
            override fun createRemoteAction(): Endpoint<Boolean> = action
        }
        assertEquals(action, delegatingEndpoint.remoteAction)
    }
}

class TestAction() : Endpoint<Boolean> {
    lateinit var overridenConfiguration: com.pubnub.api.v2.PNConfiguration
    var retryCalled: Boolean = false
    var silentCancelCalled: Boolean = false

    override fun operationType(): PNOperationType {
        return PNOperationType.FileOperation
    }

    override fun retry() {
        retryCalled = true
    }

    override fun sync(): Boolean {
        return true
    }

    override fun silentCancel() {
        silentCancelCalled = true
    }

    override fun async(callback: Consumer<Result<Boolean>>) {
        callback.accept(Result.success(true))
    }

    override fun overrideConfiguration(action: PNConfigurationOverride.Builder.() -> Unit): Endpoint<Boolean> {
        TODO("Not yet implemented")
    }

    override fun overrideConfiguration(configuration: com.pubnub.api.v2.PNConfiguration): Endpoint<Boolean> {
        overridenConfiguration = configuration
        return this
    }
}
