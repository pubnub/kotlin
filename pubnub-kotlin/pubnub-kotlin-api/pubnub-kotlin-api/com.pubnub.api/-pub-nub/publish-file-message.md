//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[publishFileMessage](publish-file-message.md)

# publishFileMessage

[common]\
expect abstract fun [publishFileMessage](publish-file-message.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, shouldStore: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null): [PublishFileMessage](../../com.pubnub.api.endpoints.files/-publish-file-message/index.md)actual abstract fun [publishFileMessage](publish-file-message.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, shouldStore: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [PublishFileMessage](../../com.pubnub.api.endpoints.files/-publish-file-message/index.md)

[jvm]\
actual abstract fun [publishFileMessage](publish-file-message.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, ttl: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, shouldStore: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [PublishFileMessage](../../com.pubnub.api.endpoints.files/-publish-file-message/index.md)

Publish file message from specified Channel.

#### Parameters

jvm

| | |
|---|---|
| channel | Name of channel to which the file has been uploaded. |
| fileName | Name under which the uploaded file is stored. |
| fileId | Unique identifier for the file, assigned during upload. |
| message | The payload.     **Warning:** It is important to note that you should not serialize JSON     when sending signals/messages via PubNub.     Why? Because the serialization is done for you automatically.     Instead just pass the full object as the message payload.     PubNub takes care of everything for you. |
| meta | Metadata object which can be used with the filtering ability. |
| ttl | Set a per message time to live in storage.     - If `shouldStore = true`, and `ttl = 0`, the message is stored       with no expiry time.     - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),       the message is stored with an expiry time of `X` hours.     - If `shouldStore = false`, the `ttl` parameter is ignored.     - If ttl isn't specified, then expiration of the message defaults       back to the expiry value for the key. |
| shouldStore | Store in history.     If not specified, then the history configuration of the key is used. |
