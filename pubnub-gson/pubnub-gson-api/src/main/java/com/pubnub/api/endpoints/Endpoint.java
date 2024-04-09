package com.pubnub.api.endpoints;

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.v2.PNConfiguration;

public interface Endpoint<T> extends ExtendedRemoteAction<T> {
    /**
     * Allows to override certain configuration options (see {@link com.pubnub.api.v2.BasePNConfigurationOverride.Builder}) for this request only.
     * <p>
     * {@link com.pubnub.api.v2.PNConfigurationOverride#from(com.pubnub.api.v2.BasePNConfiguration)} should be used to obtain a <code>PNConfigurationOverride.Builder</code>.
     * Only options present in <code>PNConfigurationOverride.Builder</code> will be used for the override.
     * <p>
     * Example:
     * <pre>
     * configOverride = PNConfigurationOverride.from(pubnub.configuration)
     * configOverride.userId(UserId("example"))
     * endpoint.overrideConfiguration(configOverride.build()).sync()
     * </pre>
     *
     * @return Returns the same instance for convenience, so {@link Endpoint#sync()} or {@link Endpoint#async(java.util.function.Consumer)} can be called next.
     */
    Endpoint<T> overrideConfiguration(PNConfiguration configuration);
}
