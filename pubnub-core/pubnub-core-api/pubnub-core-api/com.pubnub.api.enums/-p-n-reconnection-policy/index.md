//[pubnub-core-api](../../../index.md)/[com.pubnub.api.enums](../index.md)/[PNReconnectionPolicy](index.md)

# PNReconnectionPolicy

[jvm]\
enum [~~PNReconnectionPolicy~~](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PNReconnectionPolicy](index.md)&gt; ---

### Deprecated

Use [com.pubnub.api.retry.RetryConfiguration] instead.

---

## Entries

| | |
|---|---|
| [NONE](-n-o-n-e/index.md) | [jvm]<br>[NONE](-n-o-n-e/index.md)<br>No reconnection policy. If the subscribe loop gets cancelled due to network or other issues, the SDK will not attempt to try to restore it. |
| [LINEAR](-l-i-n-e-a-r/index.md) | [jvm]<br>[LINEAR](-l-i-n-e-a-r/index.md)<br>The SDK will try to reconnect to the network by doing so every 3 seconds. |
| [EXPONENTIAL](-e-x-p-o-n-e-n-t-i-a-l/index.md) | [jvm]<br>[EXPONENTIAL](-e-x-p-o-n-e-n-t-i-a-l/index.md)<br>The SDK will try to reconnect to the network by doing so in non-fixed intervals. ie. the interval between retries is another power of 2, starting from 0 to 5. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F1454713420) | [jvm]<br>val [name](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F1454713420): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F1454713420) | [jvm]<br>val [ordinal](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F1454713420): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [jvm]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNReconnectionPolicy](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [jvm]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[PNReconnectionPolicy](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
