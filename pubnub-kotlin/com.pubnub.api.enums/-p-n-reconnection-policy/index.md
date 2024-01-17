//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.enums](../index.md)/[PNReconnectionPolicy](index.md)

# PNReconnectionPolicy

[jvm]\
enum [PNReconnectionPolicy](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PNReconnectionPolicy](index.md)&gt;

## Entries

| | |
|---|---|
| [NONE](-n-o-n-e/index.md) | [jvm]<br>[NONE](-n-o-n-e/index.md)<br>No reconnection policy. If the subscribe loop gets cancelled due to network or other issues, the SDK will not attempt to try to restore it. |
| [LINEAR](-l-i-n-e-a-r/index.md) | [jvm]<br>[LINEAR](-l-i-n-e-a-r/index.md)<br>The SDK will try to reconnect to the network by doing so every 3 seconds. |
| [EXPONENTIAL](-e-x-p-o-n-e-n-t-i-a-l/index.md) | [jvm]<br>[EXPONENTIAL](-e-x-p-o-n-e-n-t-i-a-l/index.md)<br>The SDK will try to reconnect to the network by doing so in non-fixed intervals. ie. the interval between retries is another power of 2, starting from 0 to 5. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F-1216412040) | [jvm]<br>val [name](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F-1216412040): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F-1216412040) | [jvm]<br>val [ordinal](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
