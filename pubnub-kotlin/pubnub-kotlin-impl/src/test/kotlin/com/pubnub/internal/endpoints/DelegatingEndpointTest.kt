package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.EndpointInterface
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

internal class DelegatingEndpointTest {
    private lateinit var delegatingEndpoint: DelegatingEndpoint<Boolean, Boolean>

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
        silentCancelCalled = false
        retryCalled = false
        delegatingEndpoint =
            object : DelegatingEndpoint<Boolean, Boolean>(action) {
                override fun convertAction(remoteAction: ExtendedRemoteAction<Boolean>): ExtendedRemoteAction<Boolean> {
                    return remoteAction
                }
            }
    }

    @Test
    fun sync() {
        val result = delegatingEndpoint.sync()
        assertTrue(result)
    }

    @Test
    fun async() {
        delegatingEndpoint.async { result ->
            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow())
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
        assertEquals(PNOperationType.FileOperation, delegatingEndpoint.operationType())
    }
}
