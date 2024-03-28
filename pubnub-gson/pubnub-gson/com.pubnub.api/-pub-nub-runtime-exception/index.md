//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PubNubRuntimeException](index.md)

# PubNubRuntimeException

[jvm]\
open class [PubNubRuntimeException](index.md) : [RuntimeException](https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)

## Constructors

| | |
|---|---|
| [PubNubRuntimeException](-pub-nub-runtime-exception.md) | [jvm]<br>constructor(errormsg: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), pubnubError: [PubNubError](../../../../pubnub-gson/com.pubnub.api/-pub-nub-error/index.md), jso: JsonElement, response: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), statusCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), affectedCall: Call, cause: [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)) |

## Properties

| Name | Summary |
|---|---|
| [cause](index.md#-1023347080%2FProperties%2F-395131529) | [jvm]<br>open val [cause](index.md#-1023347080%2FProperties%2F-395131529): [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) |
| [stackTrace](index.md#1573944892%2FProperties%2F-395131529) | [jvm]<br>open var [stackTrace](index.md#1573944892%2FProperties%2F-395131529): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt; |

## Functions

| Name | Summary |
|---|---|
| [addSuppressed](index.md#-1898257014%2FFunctions%2F-395131529) | [jvm]<br>fun [addSuppressed](index.md#-1898257014%2FFunctions%2F-395131529)(exception: [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)) |
| [fillInStackTrace](index.md#-1207709164%2FFunctions%2F-395131529) | [jvm]<br>open fun [fillInStackTrace](index.md#-1207709164%2FFunctions%2F-395131529)(): [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) |
| [getCause](get-cause.md) | [jvm]<br>open fun [getCause](get-cause.md)(): [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) |
| [getLocalizedMessage](index.md#-2138642817%2FFunctions%2F-395131529) | [jvm]<br>open fun [getLocalizedMessage](index.md#-2138642817%2FFunctions%2F-395131529)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getMessage](get-message.md) | [jvm]<br>open fun [getMessage](get-message.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getSuppressed](index.md#1678506999%2FFunctions%2F-395131529) | [jvm]<br>fun [getSuppressed](index.md#1678506999%2FFunctions%2F-395131529)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)&gt; |
| [initCause](index.md#-104903378%2FFunctions%2F-395131529) | [jvm]<br>open fun [initCause](index.md#-104903378%2FFunctions%2F-395131529)(cause: [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)): [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) |
| [printStackTrace](index.md#-1357294889%2FFunctions%2F-395131529) | [jvm]<br>open fun [printStackTrace](index.md#-1357294889%2FFunctions%2F-395131529)() |
| [toString](index.md#1869833549%2FFunctions%2F-395131529) | [jvm]<br>open fun [toString](index.md#1869833549%2FFunctions%2F-395131529)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
