//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Channel](index.md)/[sendFile](send-file.md)

# sendFile

[common]\
abstract fun [sendFile](send-file.md)(fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), inputStream: [Uploadable](../../com.pubnub.kmp/-uploadable/index.md), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, shouldStore: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, customMessageType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [SendFile](../../com.pubnub.api.endpoints.files/-send-file/index.md)

Upload file / data to the Channel.

#### Parameters

common

| | |
|---|---|
| fileName | Name of the file to send. |
| inputStream | Input stream with file content. The inputStream will be depleted after the call. |
| message | The payload.     **Warning:** It is important to note that you should not serialize JSON     when sending signals/messages via PubNub.     Why? Because the serialization is done for you automatically.     Instead just pass the full object as the message payload.     PubNub takes care of everything for you. |
| meta | Metadata object which can be used with the filtering ability. |
| ttl | Set a per message time to live in storage.     - If `shouldStore = true`, and `ttl = 0`, the message is stored       with no expiry time.     - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),       the message is stored with an expiry time of `X` hours.     - If `shouldStore = false`, the `ttl` parameter is ignored.     - If ttl isn't specified, then expiration of the message defaults       back to the expiry value for the key. |
| shouldStore | Store in history.     If not specified, then the history configuration of the key is used. |
| cipherKey | Key to be used to encrypt uploaded data. |
| customMessageType | The custom type associated with the message. |
