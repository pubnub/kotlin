package com.pubnub.api.java.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.PNConfiguration

interface Endpoint<T> : ExtendedRemoteAction<T> {
    /**
     * Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder]) for this request only.
     *
     *
     * [PNConfigurationOverride.from] should be used to obtain a `PNConfigurationOverride.Builder`.
     * Only options present in `PNConfigurationOverride.Builder` will be used for the override.
     *
     *
     * Example:
     * <pre>
     * configOverride = PNConfigurationOverride.from(pubnub.configuration)
     * configOverride.userId(UserId("example"))
     * endpoint.overrideConfiguration(configOverride.build()).sync()
     </pre> *
     *
     * @return Returns the same instance for convenience, so [Endpoint.sync] or [Endpoint.async] can be called next.
     */
    fun overrideConfiguration(configuration: PNConfiguration): Endpoint<T>
}
