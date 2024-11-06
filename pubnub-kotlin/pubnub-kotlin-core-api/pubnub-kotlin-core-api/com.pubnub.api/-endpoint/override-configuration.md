//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[Endpoint](index.md)/[overrideConfiguration](override-configuration.md)

# overrideConfiguration

[jvm]\
abstract fun [overrideConfiguration](override-configuration.md)(action: [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt;

Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only.

#### Return

Returns the same instance for convenience, so sync or async can be called next.

[jvm]\
abstract fun [overrideConfiguration](override-configuration.md)(configuration: [PNConfiguration](../../com.pubnub.api.v2/-p-n-configuration/index.md)): [Endpoint](index.md)&lt;[OUTPUT](index.md)&gt;

Allows to override certain configuration options (see [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md)) for this request only.

[PNConfigurationOverride.from](../../com.pubnub.api.v2/-p-n-configuration-override/-companion/from.md) should be used to obtain a [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md). Only options present in [PNConfigurationOverride.Builder](../../com.pubnub.api.v2/-p-n-configuration-override/-builder/index.md) will be used for the override.

Example:

```kotlin
val configOverride = PNConfigurationOverride.from(pubnub.configuration).apply {
    userId = UserId("example")
}.build()

val result = endpoint.overrideConfiguration(configOverride).sync()
```

#### Return

Returns the same instance for convenience, so sync or async can be called next.
