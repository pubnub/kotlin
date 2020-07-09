[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.message_actions](../index.md) / [PNMessageAction](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`PNMessageAction(type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, messageTimetoken: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`)`

Concrete implementation of a message action.

Add or remove actions on published messages to build features like receipts,
reactions or to associate custom metadata to messages.

Clients can subscribe to a channel to receive message action events on that channel.
They can also fetch past message actions from PubNub Storage independently or when they fetch original messages.

