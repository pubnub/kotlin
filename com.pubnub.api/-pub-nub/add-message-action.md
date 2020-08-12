[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [addMessageAction](./add-message-action.md)

# addMessageAction

`fun addMessageAction(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, messageAction: `[`PNMessageAction`](../../com.pubnub.api.models.consumer.message_actions/-p-n-message-action/index.md)`): `[`AddMessageAction`](../../com.pubnub.api.endpoints.message_actions/-add-message-action/index.md)

Add an action on a published message. Returns the added action in the response.

### Parameters

`channel` - Channel to publish message actions to.

`messageAction` - The message action object containing the message action's type,
    value and the publish timetoken of the original message.