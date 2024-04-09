//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.endpoints](../index.md)/[Endpoint](index.md)/[overrideConfiguration](override-configuration.md)

# overrideConfiguration

[jvm]\
abstract fun [overrideConfiguration](override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](index.md)&lt;[T](index.md)&gt;

Allows to override certain configuration options (see [com.pubnub.api.v2.BasePNConfigurationOverride.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)) for this request only. 

[from](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2/-p-n-configuration-override/from.md) should be used to obtain a `PNConfigurationOverride.Builder`. Only options present in `PNConfigurationOverride.Builder` will be used for the override. 

 Example: 

```kotlin
configOverride = PNConfigurationOverride.from(pubnub.configuration)
configOverride.userId(UserId("example"))
endpoint.overrideConfiguration(configOverride.build()).sync()

```

#### Return

Returns the same instance for convenience, so [sync](../../com.pubnub.api.v2.endpoints.pubsub/-signal-builder/index.md#40193115%2FFunctions%2F126356644) or [async](../../com.pubnub.api.v2.endpoints.pubsub/-signal-builder/index.md#1418965989%2FFunctions%2F126356644) can be called next.
