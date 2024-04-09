//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.callbacks](../index.md)/[PNCallback](index.md)

# PNCallback

[jvm]\
interface [PNCallback](index.md)&lt;[X](index.md)&gt; : [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[T](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&gt;

## Functions

| Name | Summary |
|---|---|
| [accept](accept.md) | [jvm]<br>open fun [accept](accept.md)(xResult: [Result](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-result/index.md)&lt;[X](index.md)&gt;) |
| [andThen](index.md#-232252597%2FFunctions%2F126356644) | [jvm]<br>open fun [andThen](index.md#-232252597%2FFunctions%2F126356644)(after: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;out [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;): [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;[T](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&gt; |
| [onResponse](on-response.md) | [jvm]<br>abstract fun [onResponse](on-response.md)(@Nullableresult: @Nullable[X](index.md), @Nullableexception: @Nullable[PubNubException](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-pub-nub-exception/index.md)) |
