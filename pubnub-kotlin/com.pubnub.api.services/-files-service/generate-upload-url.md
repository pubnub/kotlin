//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.services](../index.md)/[FilesService](index.md)/[generateUploadUrl](generate-upload-url.md)

# generateUploadUrl

[jvm]\

@POST(value = &quot;/v1/files/{subKey}/channels/{channel}/generate-upload-url&quot;)

abstract fun [generateUploadUrl](generate-upload-url.md)(@Path(value = &quot;subKey&quot;)subKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;channel&quot;)channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Bodybody: [GenerateUploadUrlPayload](../../com.pubnub.api.models.server.files/-generate-upload-url-payload/index.md), @QueryMapoptions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): Call&lt;[GeneratedUploadUrlResponse](../../com.pubnub.api.models.server.files/-generated-upload-url-response/index.md)&gt;
