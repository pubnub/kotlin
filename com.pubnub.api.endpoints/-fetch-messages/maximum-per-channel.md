[pubnub-kotlin](../../index.md) / [com.pubnub.api.endpoints](../index.md) / [FetchMessages](index.md) / [maximumPerChannel](./maximum-per-channel.md)

# maximumPerChannel

`var maximumPerChannel: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Specifies the number of historical messages to return per channel.
If [includeMessageActions](include-message-actions.md) is `false`, then `1` is the default (and maximum) value.
Otherwise it's `25`.

