//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[setRetryConfiguration](set-retry-configuration.md)

# setRetryConfiguration

[jvm]\
fun [setRetryConfiguration](set-retry-configuration.md)(retryConfiguration: [RetryConfiguration](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md)): [PNConfiguration](index.md)

Retry configuration for requests. Defaults to [RetryConfiguration.None](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-none/index.md).

Use [RetryConfiguration.Linear](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-linear/index.md) to set retry with linear delay interval Use [RetryConfiguration.Exponential](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-exponential/index.md) to set retry with exponential delay interval Delay will valy from provided value by random value.
