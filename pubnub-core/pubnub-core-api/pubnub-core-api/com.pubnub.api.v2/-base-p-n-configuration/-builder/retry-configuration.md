//[pubnub-core-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[BasePNConfiguration](../index.md)/[Builder](index.md)/[retryConfiguration](retry-configuration.md)

# retryConfiguration

[jvm]\
abstract val [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../../com.pubnub.api.retry/-retry-configuration/index.md)

Retry configuration for requests. Defaults to [RetryConfiguration.None](../../../com.pubnub.api.retry/-retry-configuration/-none/index.md).

Use [RetryConfiguration.Linear](../../../com.pubnub.api.retry/-retry-configuration/-linear/index.md) to set retry with linear delay interval Use [RetryConfiguration.Exponential](../../../com.pubnub.api.retry/-retry-configuration/-exponential/index.md) to set retry with exponential delay interval Delay will vary from provided value by random value.
