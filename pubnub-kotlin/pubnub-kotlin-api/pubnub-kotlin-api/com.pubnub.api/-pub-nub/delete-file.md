//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[deleteFile](delete-file.md)

# deleteFile

[common]\
expect abstract fun [deleteFile](delete-file.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [DeleteFile](../../com.pubnub.api.endpoints.files/-delete-file/index.md)actual abstract fun [deleteFile](delete-file.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [DeleteFile](../../com.pubnub.api.endpoints.files/-delete-file/index.md)

[jvm]\
actual abstract fun [deleteFile](delete-file.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [DeleteFile](../../com.pubnub.api.endpoints.files/-delete-file/index.md)

Delete file from specified Channel.

#### Parameters

jvm

| | |
|---|---|
| channel | Name of channel to which the file has been uploaded. |
| fileName | Name under which the uploaded file is stored. |
| fileId | Unique identifier for the file, assigned during upload. |
