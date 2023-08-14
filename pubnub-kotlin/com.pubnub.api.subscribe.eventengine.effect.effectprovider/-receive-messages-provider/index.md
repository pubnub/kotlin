//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.subscribe.eventengine.effect.effectprovider](../index.md)/[ReceiveMessagesProvider](index.md)

# ReceiveMessagesProvider

[jvm]\
fun interface [ReceiveMessagesProvider](index.md)

## Functions

| Name | Summary |
|---|---|
| [getReceiveMessagesRemoteAction](get-receive-messages-remote-action.md) | [jvm]<br>abstract fun [getReceiveMessagesRemoteAction](get-receive-messages-remote-action.md)(channels: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, channelGroups: [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;, subscriptionCursor: [SubscriptionCursor](../../com.pubnub.api.subscribe.eventengine.event/-subscription-cursor/index.md)): [RemoteAction](../../com.pubnub.api.endpoints.remoteaction/-remote-action/index.md)&lt;[ReceiveMessagesResult](../../com.pubnub.api.subscribe.eventengine.effect/-receive-messages-result/index.md)&gt; |
