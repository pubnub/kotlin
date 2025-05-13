//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.enums](../index.md)/[PNStatusCategory](index.md)

# PNStatusCategory

[common]\
enum [PNStatusCategory](index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PNStatusCategory](index.md)&gt; 

Check the status category via [PNStatus.category](../../com.pubnub.api.models.consumer/-p-n-status/category.md) in the com.pubnub.api.v2.callbacks.StatusListener added to a com.pubnub.api.PubNub object.

## Entries

| | |
|---|---|
| [PNConnectedCategory](-p-n-connected-category/index.md) | [common]<br>[PNConnectedCategory](-p-n-connected-category/index.md)<br>SDK successfully connected the Subscribe loop. |
| [PNSubscriptionChanged](-p-n-subscription-changed/index.md) | [common]<br>[PNSubscriptionChanged](-p-n-subscription-changed/index.md)<br>SDK subscribed with a new mix of channels (fired every time the channel / channel group mix changed) since the initial connection. |
| [PNUnexpectedDisconnectCategory](-p-n-unexpected-disconnect-category/index.md) | [common]<br>[PNUnexpectedDisconnectCategory](-p-n-unexpected-disconnect-category/index.md)<br>Previously started subscribe loop failed and at this moment client is disconnected from real-time data channels. |
| [PNDisconnectedCategory](-p-n-disconnected-category/index.md) | [common]<br>[PNDisconnectedCategory](-p-n-disconnected-category/index.md)<br>The subscription has been stopped as requested (when all channels and channel groups have been unsubscribed). |
| [PNConnectionError](-p-n-connection-error/index.md) | [common]<br>[PNConnectionError](-p-n-connection-error/index.md)<br>The subscription loop was not able to connect, and at this moment the client is disconnected from real-time data channels. |
| [PNHeartbeatFailed](-p-n-heartbeat-failed/index.md) | [common]<br>[PNHeartbeatFailed](-p-n-heartbeat-failed/index.md)<br>A background implicit Heartbeat request attempt failed. |
| [PNHeartbeatSuccess](-p-n-heartbeat-success/index.md) | [common]<br>[PNHeartbeatSuccess](-p-n-heartbeat-success/index.md)<br>A background implicit Heartbeat request was successful. |
| [PNMalformedResponseCategory](-p-n-malformed-response-category/index.md) | [common]<br>[PNMalformedResponseCategory](-p-n-malformed-response-category/index.md)<br>PubNub sent a malformed response. This may happen when you connect to a public WiFi Hotspot that requires you to auth via your web browser first, or if there is a proxy somewhere returning an HTML access denied error, or if there was an intermittent server issue. |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.enums/-enum-entries/index.html)&lt;[PNStatusCategory](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F1196661149) | [common]<br>val [name](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F1196661149): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F1196661149) | [common]<br>val [ordinal](../../com.pubnub.api.retry/-retryable-endpoint-group/-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F1196661149): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [PNStatusCategory](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[PNStatusCategory](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
