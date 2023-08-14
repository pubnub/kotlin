//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.enums](../index.md)/[PNStatusCategory](index.md)

# PNStatusCategory

[jvm]\
enum [PNStatusCategory](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[PNStatusCategory](index.md)&gt; 

Check the status category via [PNStatus.category](../../com.pubnub.api.models.consumer/-p-n-status/category.md) in the `async` callback when executing API methods like [PubNub.publish](../../com.pubnub.api/-pub-nub/publish.md) or [PubNub.history](../../com.pubnub.api/-pub-nub/history.md).

Or in the [SubscribeCallback.status](../../com.pubnub.api.callbacks/-subscribe-callback/status.md) for API methods like [PubNub.subscribe](../../com.pubnub.api/-pub-nub/subscribe.md) or [PubNub.unsubscribeAll](../../com.pubnub.api/-pub-nub/unsubscribe-all.md) ie. methods able to manage the channel mix.

## Entries

| | |
|---|---|
| [PNAcknowledgmentCategory](-p-n-acknowledgment-category/index.md) | [jvm]<br>[PNAcknowledgmentCategory](-p-n-acknowledgment-category/index.md)<br>Successful acknowledgment of an operation. |
| [PNAccessDeniedCategory](-p-n-access-denied-category/index.md) | [jvm]<br>[PNAccessDeniedCategory](-p-n-access-denied-category/index.md)<br>Request failed because of access error (active PAM). [PNStatus.affectedChannels](../../com.pubnub.api.models.consumer/-p-n-status/affected-channels.md) or [PNStatus.affectedChannelGroups](../../com.pubnub.api.models.consumer/-p-n-status/affected-channel-groups.md) contain list of channels and groups the client can't access to. |
| [PNTimeoutCategory](-p-n-timeout-category/index.md) | [jvm]<br>[PNTimeoutCategory](-p-n-timeout-category/index.md)<br>Processing has failed because of request time out. This may happen due to very slow connection when the request doesn't have enough time to complete processing. |
| [PNConnectedCategory](-p-n-connected-category/index.md) | [jvm]<br>[PNConnectedCategory](-p-n-connected-category/index.md)<br>SDK subscribed with a new mix of channels (fired every time the channel / channel group mix changed). |
| [PNReconnectedCategory](-p-n-reconnected-category/index.md) | [jvm]<br>[PNReconnectedCategory](-p-n-reconnected-category/index.md)<br>SDK was able to reconnect to PubNub, i.e. the subscription loop has been reconnected. |
| [PNUnexpectedDisconnectCategory](-p-n-unexpected-disconnect-category/index.md) | [jvm]<br>[PNUnexpectedDisconnectCategory](-p-n-unexpected-disconnect-category/index.md)<br>Previously started subscribe loop did fail and at this moment client disconnected from real-time data channels. |
| [PNCancelledCategory](-p-n-cancelled-category/index.md) | [jvm]<br>[PNCancelledCategory](-p-n-cancelled-category/index.md)<br>Request was cancelled by user. |
| [PNBadRequestCategory](-p-n-bad-request-category/index.md) | [jvm]<br>[PNBadRequestCategory](-p-n-bad-request-category/index.md)<br>PubNub API server was unable to parse SDK request correctly. |
| [PNMalformedResponseCategory](-p-n-malformed-response-category/index.md) | [jvm]<br>[PNMalformedResponseCategory](-p-n-malformed-response-category/index.md)<br>PubNub sent a malformed response. This may happen when you connect to a public WiFi Hotspot that requires you to auth via your web browser first, or if there is a proxy somewhere returning an HTML access denied error, or if there was an intermittent server issue. |
| [PNRequestMessageCountExceededCategory](-p-n-request-message-count-exceeded-category/index.md) | [jvm]<br>[PNRequestMessageCountExceededCategory](-p-n-request-message-count-exceeded-category/index.md)<br>Fired when the limit is exceeded by the number of messages received in a single subscribe request. |
| [PNReconnectionAttemptsExhausted](-p-n-reconnection-attempts-exhausted/index.md) | [jvm]<br>[PNReconnectionAttemptsExhausted](-p-n-reconnection-attempts-exhausted/index.md)<br>The subscribe loop has been stopped due maximum reconnection exhausted. |
| [PNNotFoundCategory](-p-n-not-found-category/index.md) | [jvm]<br>[PNNotFoundCategory](-p-n-not-found-category/index.md)<br>The subscriber got a HTTP 404 from the server. |
| [PNUnknownCategory](-p-n-unknown-category/index.md) | [jvm]<br>[PNUnknownCategory](-p-n-unknown-category/index.md)<br>The subscriber got a 4xx code from the server, other than 400, 403 and 404 |
| [PNDisconnectedCategory](-p-n-disconnected-category/index.md) | [jvm]<br>[PNDisconnectedCategory](-p-n-disconnected-category/index.md)<br>The subscription has been stopped. |
| [PNConnectionError](-p-n-connection-error/index.md) | [jvm]<br>[PNConnectionError](-p-n-connection-error/index.md)<br>Previously started subscribe loop did fail and at this moment client disconnected from real-time data channels. |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/-c-h-a-n-n-e-l_-w-i-t-h_-c-u-s-t-o-m/index.md#-372974862%2FProperties%2F-1216412040) | [jvm]<br>val [name](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/-c-h-a-n-n-e-l_-w-i-t-h_-c-u-s-t-o-m/index.md#-372974862%2FProperties%2F-1216412040): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/-c-h-a-n-n-e-l_-w-i-t-h_-c-u-s-t-o-m/index.md#-739389684%2FProperties%2F-1216412040) | [jvm]<br>val [ordinal](../../com.pubnub.api.models.consumer.objects.membership/-p-n-channel-details-level/-c-h-a-n-n-e-l_-w-i-t-h_-c-u-s-t-o-m/index.md#-739389684%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
