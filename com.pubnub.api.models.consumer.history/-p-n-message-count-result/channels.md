[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.history](../index.md) / [PNMessageCountResult](index.md) / [channels](./channels.md)

# channels

`val channels: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`>`

A map with values of Long for each channel. Channels without messages have a count of 0.
Channels with 10,000 messages or more have a count of `10000`.

### Property

`channels` - A map with values of Long for each channel. Channels without messages have a count of 0.
Channels with 10,000 messages or more have a count of `10000`.