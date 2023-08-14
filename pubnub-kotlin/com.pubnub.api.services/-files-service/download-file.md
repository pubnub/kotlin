//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.services](../index.md)/[FilesService](index.md)/[downloadFile](download-file.md)

# downloadFile

[jvm]\

@GET(value = &quot;/v1/files/{subKey}/channels/{channel}/files/{fileId}/{fileName}&quot;)

abstract fun [downloadFile](download-file.md)(@Path(value = &quot;subKey&quot;)subKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;channel&quot;)channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;fileId&quot;)fileId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;fileName&quot;)fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @QueryMapoptions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): Call&lt;ResponseBody&gt;
