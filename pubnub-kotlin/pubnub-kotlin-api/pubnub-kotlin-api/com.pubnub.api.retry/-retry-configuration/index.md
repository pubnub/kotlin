//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.retry](../index.md)/[RetryConfiguration](index.md)

# RetryConfiguration

sealed class [RetryConfiguration](index.md)

This sealed class represents the various retry policies for a request.

#### Inheritors

| |
|---|
| [None](-none/index.md) |
| [Linear](-linear/index.md) |
| [Exponential](-exponential/index.md) |

## Types

| Name | Summary |
|---|---|
| [Exponential](-exponential/index.md) | [common]<br>class [Exponential](-exponential/index.md) : [RetryConfiguration](index.md)<br>This class represents an exponential retry policy with a minimum and maximum delay between retries, a maximum number of retries, and a list of operations to exclude from retry attempts. |
| [Linear](-linear/index.md) | [common]<br>class [Linear](-linear/index.md) : [RetryConfiguration](index.md)<br>This data class represents a linear retry policy for network requests with a delay between retries, a maximum number of retries and a list of operations to exclude from retries. |
| [None](-none/index.md) | [common]<br>object [None](-none/index.md) : [RetryConfiguration](index.md)<br>None represents no retry policy in a network request |
