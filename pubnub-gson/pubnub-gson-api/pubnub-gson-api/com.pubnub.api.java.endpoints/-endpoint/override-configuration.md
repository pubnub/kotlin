//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints](../index.md)/[Endpoint](index.md)/[overrideConfiguration](override-configuration.md)

# overrideConfiguration

[jvm]\
abstract fun [overrideConfiguration](override-configuration.md)(configuration: [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](index.md)&lt;[T](index.md)&gt;

Allows to override certain configuration options (see [com.pubnub.api.v2.PNConfigurationOverride.Builder](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only.

[com.pubnub.api.java.v2.PNConfigurationOverride.from](../../com.pubnub.api.java.v2/-p-n-configuration-override/-companion/from.md) should be used to obtain a `PNConfigurationOverride.Builder`. Only options present in `PNConfigurationOverride.Builder` will be used for the override.

Example:

<pre>
configOverride = PNConfigurationOverride.from(pubnub.configuration)
configOverride.userId(UserId("example"))
endpoint.overrideConfiguration(configOverride.build()).sync()
</pre> *

#### Return

Returns the same instance for convenience, so [Endpoint.sync](../../../../../pubnub-gson/pubnub-gson-api/com.pubnub.api.java.endpoints/-endpoint/sync.md) or [Endpoint.async](../../../../../pubnub-gson/pubnub-gson-api/com.pubnub.api.java.endpoints/-endpoint/async.md) can be called next.
