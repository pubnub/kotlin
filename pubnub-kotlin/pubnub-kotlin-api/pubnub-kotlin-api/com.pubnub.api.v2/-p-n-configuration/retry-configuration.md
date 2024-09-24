//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2](../index.md)/[PNConfiguration](index.md)/[retryConfiguration](retry-configuration.md)

# retryConfiguration

[jvm]\
abstract val [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../com.pubnub.api.retry/-retry-configuration/index.md)

Retry configuration for requests. Defaults to [RetryConfiguration.Exponential](../../com.pubnub.api.retry/-retry-configuration/-exponential/index.md) enabled only for subscription endpoint (other endpoints are excluded).

Use [RetryConfiguration.Linear](../../com.pubnub.api.retry/-retry-configuration/-linear/index.md) to set retry with linear delay interval Use [RetryConfiguration.Exponential](../../com.pubnub.api.retry/-retry-configuration/-exponential/index.md) to set retry with exponential delay interval Delay will vary from provided value by random value.
