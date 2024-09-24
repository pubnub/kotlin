//[pubnub-kotlin-api](../../index.md)/[com.pubnub.kmp](index.md)/[awaitAll](await-all.md)

# awaitAll

[common]\
fun &lt;[T](await-all.md)&gt; [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNFuture](-p-n-future/index.md)&lt;[T](await-all.md)&gt;&gt;.[awaitAll](await-all.md)(): [PNFuture](-p-n-future/index.md)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](await-all.md)&gt;&gt;

fun &lt;[T](await-all.md), [U](await-all.md)&gt; [awaitAll](await-all.md)(future1: [PNFuture](-p-n-future/index.md)&lt;[T](await-all.md)&gt;, future2: [PNFuture](-p-n-future/index.md)&lt;[U](await-all.md)&gt;): [PNFuture](-p-n-future/index.md)&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[T](await-all.md), [U](await-all.md)&gt;&gt;

fun &lt;[T](await-all.md), [U](await-all.md), [X](await-all.md)&gt; [awaitAll](await-all.md)(future1: [PNFuture](-p-n-future/index.md)&lt;[T](await-all.md)&gt;, future2: [PNFuture](-p-n-future/index.md)&lt;[U](await-all.md)&gt;, future3: [PNFuture](-p-n-future/index.md)&lt;[X](await-all.md)&gt;): [PNFuture](-p-n-future/index.md)&lt;[Triple](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-triple/index.html)&lt;[T](await-all.md), [U](await-all.md), [X](await-all.md)&gt;&gt;
