//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api](../../index.md)/[PubNub](../index.md)/[Companion](index.md)

# Companion

[jvm]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [create](create.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [create](create.md)(configuration: [PNConfiguration](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)): [PubNub](../index.md)<br>Initialize and return an instance of the PubNub client.<br>[jvm]<br>@[JvmStatic](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [create](create.md)(userId: [UserId](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-user-id/index.md), subscribeKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), builder: [PNConfiguration.Builder](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)): [PubNub](../index.md) |
| [generateUUID](generate-u-u-i-d.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [generateUUID](generate-u-u-i-d.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>Generates random UUID to use. You should set a unique UUID to identify the user or the device that connects to PubNub. |
