//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfigurationOverride](../index.md)/[Builder](index.md)/[retryConfiguration](retry-configuration.md)

# retryConfiguration

[jvm]\
abstract fun [retryConfiguration](retry-configuration.md)(retryConfiguration: [RetryConfiguration](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.retry/-retry-configuration/index.md)): [PNConfigurationOverride.Builder](index.md)

Retry configuration for requests. Defaults to [RetryConfiguration.Exponential](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.retry/-retry-configuration/-exponential/index.md) enabled only for subscription endpoint (other endpoints are excluded).

Use [RetryConfiguration.Linear](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.retry/-retry-configuration/-linear/index.md) to set retry with linear delay interval Use [RetryConfiguration.Exponential](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.retry/-retry-configuration/-exponential/index.md) to set retry with exponential delay interval Delay will vary from provided value by random value.
