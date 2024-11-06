//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[getFileUrl](get-file-url.md)

# getFileUrl

[common]\
expect abstract fun [getFileUrl](get-file-url.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [GetFileUrl](../../com.pubnub.api.endpoints.files/-get-file-url/index.md)actual abstract fun [getFileUrl](get-file-url.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [GetFileUrl](../../com.pubnub.api.endpoints.files/-get-file-url/index.md)

[jvm]\
actual abstract fun [getFileUrl](get-file-url.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [GetFileUrl](../../com.pubnub.api.endpoints.files/-get-file-url/index.md)

Generate URL which can be used to download file from target Channel.

#### Parameters

jvm

| | |
|---|---|
| channel | Name of channel to which the file has been uploaded. |
| fileName | Name under which the uploaded file is stored. |
| fileId | Unique identifier for the file, assigned during upload. |
