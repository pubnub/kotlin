//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.kmp](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Downloadable](-downloadable/index.md) | [common, apple, js, jvm]<br>[common]<br>expect abstract class [Downloadable](-downloadable/index.md)<br>[apple, js]<br>actual abstract class [Downloadable](-downloadable/index.md)<br>[jvm]<br>actual typealias [Downloadable](-downloadable/index.md) = [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) |
| [DownloadableImpl](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/[js]-downloadable-impl/index.md) | [apple, js]<br>[apple]<br>class [DownloadableImpl]([apple]-downloadable-impl/index.md)(inputStream: <!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- --->) : [Downloadable](-downloadable/index.md)<br>[js]<br>class [DownloadableImpl]([js]-downloadable-impl/index.md)(val pubnubFile: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)) : [Downloadable](-downloadable/index.md) |
| [JsMap](-js-map/index.md) | [js]<br>external interface [JsMap](-js-map/index.md)&lt;[V](-js-map/index.md)&gt; |
| [PNFuture](-p-n-future/index.md) | [common]<br>fun interface [PNFuture](-p-n-future/index.md)&lt;out [OUTPUT](-p-n-future/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [combine](combine.md) | [js]<br>fun &lt;[T](combine.md), [R](combine.md)&gt; [T](combine.md).[combine](combine.md)(map: [JsMap](-js-map/index.md)&lt;[R](combine.md)&gt;?): [T](combine.md) |
| [createJsObject](create-js-object.md) | [js]<br>fun &lt;[T](create-js-object.md)&gt; [createJsObject](create-js-object.md)(configure: [T](create-js-object.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): [T](create-js-object.md) |
| [entriesOf](entries-of.md) | [js]<br>fun &lt;[V](entries-of.md)&gt; [entriesOf](entries-of.md)(jsObject: [JsMap](-js-map/index.md)&lt;[V](entries-of.md)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [V](entries-of.md)&gt;&gt; |
| [toJsMap](to-js-map.md) | [js]<br>fun &lt;[V](to-js-map.md)&gt; [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [V](to-js-map.md)&gt;.[toJsMap](to-js-map.md)(): [JsMap](-js-map/index.md)&lt;[V](to-js-map.md)&gt; |
| [toMap](to-map.md) | [js]<br>fun &lt;[V](to-map.md)&gt; [JsMap](-js-map/index.md)&lt;[V](to-map.md)&gt;.[toMap](to-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [V](to-map.md)&gt; |
