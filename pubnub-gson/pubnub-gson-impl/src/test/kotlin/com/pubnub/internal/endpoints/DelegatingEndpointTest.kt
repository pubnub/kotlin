package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.internal.EndpointInterface
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class DelegatingEndpointTest {
    private lateinit var delegatingEndpoint: DelegatingEndpoint<Boolean, Boolean>

    var validateParamsCalled = false
    var silentCancelCalled = false
    var retryCalled = false

    val action =
        object : EndpointInterface<Boolean> {
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

            override fun overrideConfiguration(configuration: BasePNConfiguration) {
                TODO("Not yet implemented")
            }

            override val configuration: BasePNConfiguration
                get() = TODO("Not yet implemented")

            override fun async(callback: Consumer<Result<Boolean>>) {
                callback.accept(Result.success(true))
            }
        }

    @BeforeEach
    fun setUp() {
        validateParamsCalled = false
        silentCancelCalled = false
        retryCalled = false

        delegatingEndpoint =
            object : DelegatingEndpoint<Boolean, Boolean>(mockk()) {
                override fun createAction(): EndpointInterface<Boolean> {
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
        val result = delegatingEndpoint.sync()
        assertTrue(result)
        assertTrue(validateParamsCalled)
    }

    @Test
    fun `when remoteAction is called it returns action from createAction `() {
        assertEquals(action, delegatingEndpoint.remoteAction)
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
        assertTrue(retryCalled)
    }

    @Test
    fun `when silentCancel is called calls silentCancel on delegate`() {
        delegatingEndpoint.silentCancel()
        assertTrue(silentCancelCalled)
    }

    @Test
    fun `when operationType is called calls operationType on delegate`() {
        assertEquals(PNOperationType.FileOperation, delegatingEndpoint.operationType)
        assertEquals(PNOperationType.FileOperation, delegatingEndpoint.operationType())
    }
}
