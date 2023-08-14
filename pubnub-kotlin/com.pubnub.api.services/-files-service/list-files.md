//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.services](../index.md)/[FilesService](index.md)/[listFiles](list-files.md)

# listFiles

[jvm]\

@GET(value = &quot;/v1/files/{subKey}/channels/{channel}/files&quot;)

abstract fun [listFiles](list-files.md)(@Path(value = &quot;subKey&quot;)subKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;channel&quot;)channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @QueryMapoptions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): Call&lt;[ListFilesResult](../../com.pubnub.api.models.server.files/-list-files-result/index.md)&gt;
