package com.pubnub.internal.java.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.getOrThrow
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class DelegatingRemoteActionTest {
    private lateinit var delegatingRemoteAction: DelegatingRemoteAction<Boolean, Boolean>

    var validateParamsCalled = false
    var silentCancelCalled = false
    var retryCalled = false

    val action =
        object : ExtendedRemoteAction<Boolean> {
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
        }

    @BeforeEach
    fun setUp() {
        validateParamsCalled = false
        silentCancelCalled = false
        retryCalled = false

        delegatingRemoteAction =
            object : DelegatingRemoteAction<Boolean, Boolean>(mockk()) {
                override fun createRemoteAction(): ExtendedRemoteAction<Boolean> {
                    return action
                }

                override fun mapResult(action: ExtendedRemoteAction<Boolean>): ExtendedRemoteAction<Boolean> {
                    return action
                }

                override fun validateParams() {
                    validateParamsCalled = true
                }
            }
    }

    @Test
    fun `when sync is called it calls sync on delegate`() {
        val result = delegatingRemoteAction.sync()
        assertTrue(result)
        assertTrue(validateParamsCalled)
    }

    @Test
    fun `when remoteAction is called it returns action from createAction `() {
        assertEquals(action, delegatingRemoteAction.remoteAction)
    }

    @Test
    fun `when async is called it calls async on delegate`() {
        delegatingRemoteAction.async { result ->
            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow())
            assertTrue(validateParamsCalled)
        }
    }

    @Test
    fun `when retry is called calls retry on delegate`() {
        delegatingRemoteAction.retry()
        assertTrue(retryCalled)
    }

    @Test
    fun `when silentCancel is called calls silentCancel on delegate`() {
        delegatingRemoteAction.silentCancel()
        assertTrue(silentCancelCalled)
    }

    @Test
    fun `when operationType is called calls operationType on delegate`() {
        assertEquals(PNOperationType.FileOperation, delegatingRemoteAction.operationType)
        assertEquals(PNOperationType.FileOperation, delegatingRemoteAction.operationType())
    }

    @Test
    fun `IdentityMappingEndpoint returns the same remote action`() {
        delegatingRemoteAction = object : PassthroughRemoteAction<Boolean>(mockk()) {
            override fun createRemoteAction(): ExtendedRemoteAction<Boolean> = action
        }
        assertEquals(action, delegatingRemoteAction.remoteAction)
    }
}
