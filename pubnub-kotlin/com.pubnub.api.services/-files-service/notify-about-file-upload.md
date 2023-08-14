//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.services](../index.md)/[FilesService](index.md)/[notifyAboutFileUpload](notify-about-file-upload.md)

# notifyAboutFileUpload

[jvm]\

@GET(value = &quot;/v1/files/publish-file/{pubKey}/{subKey}/0/{channel}/0/{message}&quot;)

abstract fun [notifyAboutFileUpload](notify-about-file-upload.md)(@Path(value = &quot;pubKey&quot;)pubKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;subKey&quot;)subKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;channel&quot;)channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @Path(value = &quot;message&quot;)message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), @QueryMapoptions: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): Call&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;&gt;
