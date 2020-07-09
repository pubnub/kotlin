[pubnub-kotlin](../../index.md) / [com.pubnub.api.enums](../index.md) / [PNStatusCategory](./index.md)

# PNStatusCategory

`enum class PNStatusCategory`

Check the status category via [PNStatus.category](../../com.pubnub.api.models.consumer/-p-n-status/category.md) in the `async` callback
when executing API methods like [PubNub.publish](../../com.pubnub.api/-pub-nub/publish.md) or [PubNub.history](../../com.pubnub.api/-pub-nub/history.md).

Or in the [SubscribeCallback.status](../../com.pubnub.api.callbacks/-subscribe-callback/status.md) for API methods like [PubNub.subscribe](../../com.pubnub.api/-pub-nub/subscribe.md) or [PubNub.unsubscribeAll](../../com.pubnub.api/-pub-nub/unsubscribe-all.md) ie.
methods able to manage the channel mix.

### Enum Values

| Name | Summary |
|---|---|
| [PNAcknowledgmentCategory](-p-n-acknowledgment-category.md) | Successful acknowledgment of an operation. |
| [PNAccessDeniedCategory](-p-n-access-denied-category.md) | Request failed because of access error (active PAM). [PNStatus.affectedChannels](../../com.pubnub.api.models.consumer/-p-n-status/affected-channels.md) or [PNStatus.affectedChannelGroups](../../com.pubnub.api.models.consumer/-p-n-status/affected-channel-groups.md) contain list of channels and groups the client can't access to. |
| [PNTimeoutCategory](-p-n-timeout-category.md) | Processing has failed because of request time out. This may happen due to very slow connection when the request doesn't have enough time to complete processing. |
| [PNConnectedCategory](-p-n-connected-category.md) | SDK subscribed with a new mix of channels (fired every time the channel / channel group mix changed). |
| [PNReconnectedCategory](-p-n-reconnected-category.md) | SDK was able to reconnect to PubNub, i.e. the subscription loop has been reconnected. |
| [PNUnexpectedDisconnectCategory](-p-n-unexpected-disconnect-category.md) | Previously started subscribe loop did fail and at this moment client disconnected from real-time data channels. |
| [PNCancelledCategory](-p-n-cancelled-category.md) | Request was cancelled by user. |
| [PNBadRequestCategory](-p-n-bad-request-category.md) | PubNub API server was unable to parse SDK request correctly. |
| [PNMalformedResponseCategory](-p-n-malformed-response-category.md) | PubNub sent a malformed response. This may happen when you connect to a public WiFi Hotspot that requires you to auth via your web browser first, or if there is a proxy somewhere returning an HTML access denied error, or if there was an intermittent server issue. |
| [PNRequestMessageCountExceededCategory](-p-n-request-message-count-exceeded-category.md) | Fired when the limit is exceeded by the number of messages received in a single subscribe request. |
| [PNReconnectionAttemptsExhausted](-p-n-reconnection-attempts-exhausted.md) | The subscribe loop has been stopped due maximum reconnection exhausted. |
| [PNNotFoundCategory](-p-n-not-found-category.md) | The subscriber got a HTTP 404 from the server. |
| [PNUnknownCategory](-p-n-unknown-category.md) | The subscriber got a 4xx code from the server, other than 400, 403 and 404 |
