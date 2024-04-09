//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[downloadFile](download-file.md)

# downloadFile

[jvm]\
abstract fun [downloadFile](download-file.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [DownloadFile](../../com.pubnub.api.endpoints.files/-download-file/index.md)

Download file from specified Channel.

#### Parameters

jvm

| | |
|---|---|
| channel | Name of channel to which the file has been uploaded. |
| fileName | Name under which the uploaded file is stored. |
| fileId | Unique identifier for the file, assigned during upload. |
| cipherKey | Key to be used to decrypt downloaded data. If a key is not provided,     the SDK uses the cipherKey from the @see [PNConfiguration](../-p-n-configuration/index.md). |
