package com.pubnub.internal.java.endpoints

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.java.endpoints.Endpoint
import com.pubnub.api.v2.PNConfiguration

abstract class DelegatingEndpoint<U, T>(pubnub: PubNub) : DelegatingRemoteAction<U, T>(pubnub), Endpoint<T> {
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

    abstract override fun createAction(): com.pubnub.api.Endpoint<U>
}

abstract class IdentityMappingEndpoint<T>(pubnub: PubNub) : DelegatingEndpoint<T, T>(pubnub) {
    final override fun mapResult(action: ExtendedRemoteAction<T>): ExtendedRemoteAction<T> {
        return action
    }
}
