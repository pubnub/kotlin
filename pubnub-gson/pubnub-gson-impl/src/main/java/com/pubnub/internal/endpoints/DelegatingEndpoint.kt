package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.Endpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.EndpointInterface
import com.pubnub.internal.PubNubCore

abstract class DelegatingEndpoint<U, T>(pubnub: PubNubCore) : DelegatingRemoteAction<U, T>(pubnub), Endpoint<T> {
    override val remoteAction: ExtendedRemoteAction<T> by lazy {
        val newAction = createAction()
        overrideConfiguration?.let { overrideConfigNonNull ->
            newAction.overrideConfiguration(overrideConfigNonNull)
        }
        mapResult(newAction)
    }
    private var overrideConfiguration: PNConfiguration? = null

    override fun overrideConfiguration(configuration: PNConfiguration): Endpoint<T> {
        this.overrideConfiguration = configuration
        return this
    }

    abstract override fun createAction(): EndpointInterface<U>
}

abstract class IdentityMappingEndpoint<T>(pubnub: PubNubCore) : DelegatingEndpoint<T, T>(pubnub) {
    final override fun mapResult(action: ExtendedRemoteAction<T>): ExtendedRemoteAction<T> {
        return action
    }
}
