//[pubnub-gson](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[retryConfiguration](retry-configuration.md)

# retryConfiguration

[jvm]\
abstract fun [retryConfiguration](retry-configuration.md)(retryConfiguration: [RetryConfiguration](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md)): [PNConfiguration.Builder](index.md)

Retry configuration for requests. Defaults to [RetryConfiguration.None](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-none/index.md).

Use [RetryConfiguration.Linear](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-linear/index.md) to set retry with linear delay interval Use [RetryConfiguration.Exponential](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-exponential/index.md) to set retry with exponential delay interval Delay will valy from provided value by random value.
