package com.pubnub.internal.endpoints

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.Endpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.Result.Companion.failure
import com.pubnub.internal.EndpointInterface
import com.pubnub.internal.PubNubCore
import org.jetbrains.annotations.TestOnly
import java.util.function.Consumer

abstract class DelegatingRemoteAction<U, T>(
    @JvmField protected val pubnub: PubNubCore,
) : ExtendedRemoteAction<T> {
    @get:TestOnly
    internal open val remoteAction: ExtendedRemoteAction<T> by lazy {
        mapResult(createAction())
    }

    protected abstract fun createAction(): ExtendedRemoteAction<U>

    protected abstract fun mapResult(action: ExtendedRemoteAction<U>): ExtendedRemoteAction<T>

    @Throws(PubNubException::class)
    override fun sync(): T {
        validateParams()
        return remoteAction.sync()
    }

    @Throws(PubNubException::class)
    protected open fun validateParams() {
    }

    override fun async(callback: Consumer<Result<T>>) {
        try {
            validateParams()
        } catch (pubnubException: PubNubException) {
            callback.accept(failure(pubnubException))
            return
        }
        remoteAction.async(callback)
    }

    override fun retry() {
        remoteAction.retry()
    }

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun operationType(): PNOperationType {
        return remoteAction.operationType()
    }

    val operationType: PNOperationType
        get() = operationType()
}

abstract class DelegatingEndpoint<U, T>(pubnub: PubNubCore) : DelegatingRemoteAction<U, T>(pubnub), Endpoint<T> {
    override val remoteAction: ExtendedRemoteAction<T> by lazy {
        val newAction = createAction()
        overrideConfiguration?.let { overrideConfigNonNull ->
            newAction.overrideConfiguration(overrideConfigNonNull)
        }
        mapResult(newAction)
    }
    private var overrideConfiguration: PNConfiguration? = null

    override fun overrideConfiguration(configuration: PNConfiguration) {
        this.overrideConfiguration = configuration
    }

    abstract override fun createAction(): EndpointInterface<U>
}

abstract class IdentityMappingEndpoint<T>(pubnub: PubNubCore) : DelegatingEndpoint<T, T>(pubnub) {
    final override fun mapResult(action: ExtendedRemoteAction<T>): ExtendedRemoteAction<T> {
        return action
    }
}

abstract class IdentityMappingAction<T>(pubnub: PubNubCore) : DelegatingRemoteAction<T, T>(pubnub) {
    final override fun mapResult(action: ExtendedRemoteAction<T>): ExtendedRemoteAction<T> {
        return action
    }
}
