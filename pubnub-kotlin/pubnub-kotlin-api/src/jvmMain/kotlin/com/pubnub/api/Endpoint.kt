package com.pubnub.api

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride

actual interface Endpoint<OUTPUT> : ExtendedRemoteAction<OUTPUT> {
    /**
     * Allows to override certain configuration options (see [PNConfigurationOverride.Builder]) for this request only.
     *
     * @return Returns the same instance for convenience, so [sync] or [async] can be called next.
     */
    fun overrideConfiguration(action: PNConfigurationOverride.Builder.() -> Unit): Endpoint<OUTPUT>

    /**
     * Allows to override certain configuration options (see [PNConfigurationOverride.Builder]) for this request only.
     *
     * [PNConfigurationOverride.from] should be used to obtain a [PNConfigurationOverride.Builder]. Only options
     * present in [PNConfigurationOverride.Builder] will be used for the override.
     *
     * Example:
     * ```
     * val configOverride = PNConfigurationOverride.from(pubnub.configuration).apply {
     *     userId = UserId("example")
     * }.build()
     *
     * val result = endpoint.overrideConfiguration(configOverride).sync()
     * ```
     *
     * @return Returns the same instance for convenience, so [sync] or [async] can be called next.
     */
    fun overrideConfiguration(configuration: PNConfiguration): Endpoint<OUTPUT>
    actual fun async(action: (Result<OUTPUT>) -> Unit)
}
