//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [EndpointImpl](-endpoint-impl/index.md) | [js]<br>open class [EndpointImpl](-endpoint-impl/index.md)&lt;[T](-endpoint-impl/index.md), [U](-endpoint-impl/index.md)&gt;(promiseFactory: () -&gt; [Promise](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-promise/index.html)&lt;[T](-endpoint-impl/index.md)&gt;, responseMapping: ([T](-endpoint-impl/index.md)) -&gt; [U](-endpoint-impl/index.md)) : [PNFuture](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-p-n-future/index.md)&lt;[U](-endpoint-impl/index.md)&gt; |
| [PubNub](-pub-nub/index.md) | [common, jvm]<br>[common]<br>@[ObjCName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.native/-obj-c-name/index.html)(name = &quot;PubNubInterface&quot;)<br>expect interface [PubNub](-pub-nub/index.md)<br>@[ObjCName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.native/-obj-c-name/index.html)(name = &quot;PubNubInterface&quot;)<br>actual interface [PubNub](-pub-nub/index.md)<br>[jvm]<br>actual interface [PubNub](-pub-nub/index.md) : [StatusEmitter](../com.pubnub.api.v2.callbacks/-status-emitter/index.md), [EventEmitter](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.callbacks/-event-emitter/index.md) |
| [PubNubImpl](../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/[js]-pub-nub-impl/index.md) | [ios, js]<br>[ios]<br>class [PubNubImpl]([ios]-pub-nub-impl/index.md)(pubNubObjC: <!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- --->) : [PubNub](-pub-nub/index.md)<br>[js]<br>class [PubNubImpl]([js]-pub-nub-impl/index.md)(val jsPubNub: [PubNub](../[root]/-pub-nub/index.md)) : [PubNub](-pub-nub/index.md) |
| [SpaceId](-space-id/index.md) | [common]<br>data class [SpaceId](-space-id/index.md)(val value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [toJs](to-js.md) | [js]<br>fun [PNConfiguration](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md).[toJs](to-js.md)(): [PubNub.PNConfiguration](../[root]/-pub-nub/-p-n-configuration/index.md) |
