//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.endpoints.pubsub](../index.md)/[PublishBuilder](index.md)/[shouldStore](should-store.md)

# shouldStore

[jvm]\
abstract fun [shouldStore](should-store.md)(shouldStore: [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)): [PublishBuilder](index.md)

Specifies whether the message should be stored in the history of the channel.

#### Return

The current instance of `PublishBuilder` for method chaining.

#### Parameters

jvm

| | |
|---|---|
| shouldStore | Boolean indicating whether to store the message (true) or not (false). If not specified, then the history configuration of the key is used. |
