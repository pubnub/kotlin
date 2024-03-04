package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.getOrThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class DelegatingEndpointTest {

    private lateinit var delegatingEndpoint: DelegatingEndpoint<Boolean>

    var validateParamsCalled = false
    var silentCancelCalled = false
    var retryCalled = false

    val action = object : ExtendedRemoteAction<Boolean> {
        override fun operationType(): PNOperationType {
            return PNOperationType.FileOperation
        }

        override fun retry() {
            retryCalled = true
        }

        override fun sync(): Boolean {
            return true;
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
        delegatingEndpoint = object : DelegatingEndpoint<Boolean>(null) {
            override fun createAction(): ExtendedRemoteAction<Boolean> {
                return action
            }

            override fun validateParams() {
                validateParamsCalled = true
            }
        }
    }

    @Test
    fun sync() {
        val result = delegatingEndpoint.sync()
        assertTrue(result)
        assertTrue(validateParamsCalled)
    }

    @Test
    fun getRemoteAction() {
        assertEquals(action, delegatingEndpoint.remoteAction)
    }

    @Test
    fun async() {
        delegatingEndpoint.async { result ->
            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow())
            assertTrue(validateParamsCalled)
        }
    }

    @Test
    fun retry() {
        delegatingEndpoint.retry()
        assertTrue(retryCalled)
    }

    @Test
    fun silentCancel() {
        delegatingEndpoint.silentCancel()
        assertTrue(silentCancelCalled)
    }

    @Test
    fun operationType() {
        assertEquals(PNOperationType.FileOperation, delegatingEndpoint.operationType)
        assertEquals(PNOperationType.FileOperation, delegatingEndpoint.operationType())
    }
}