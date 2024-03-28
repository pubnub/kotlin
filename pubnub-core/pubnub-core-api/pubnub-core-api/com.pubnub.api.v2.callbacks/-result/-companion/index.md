//[pubnub-core-api](../../../../index.md)/[com.pubnub.api.v2.callbacks](../../index.md)/[Result](../index.md)/[Companion](index.md)

# Companion

[jvm]\
object [Companion](index.md)

Companion object for [Result](../index.md) class that contains its constructor functions [success](success.md) and [failure](failure.md).

## Functions

| Name | Summary |
|---|---|
| [failure](failure.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun &lt;[T](failure.md)&gt; [failure](failure.md)(exception: [PubNubException](../../../com.pubnub.api/-pub-nub-exception/index.md)): [Result](../index.md)&lt;[T](failure.md)&gt;<br>Returns an instance that encapsulates the given [PubNubException](failure.md) as failure. |
| [success](success.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun &lt;[T](success.md)&gt; [success](success.md)(value: [T](success.md)): [Result](../index.md)&lt;[T](success.md)&gt;<br>Returns an instance that encapsulates the given [value](success.md) as successful value. |
