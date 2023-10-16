//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.crypto.cryptor](../index.md)/[ParseResult](index.md)

# ParseResult

[jvm]\
sealed class [ParseResult](index.md)&lt;[T](index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [NoHeader](-no-header/index.md) | [jvm]<br>object [NoHeader](-no-header/index.md) : [ParseResult](index.md)&lt;[Nothing](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)&gt; |
| [Success](-success/index.md) | [jvm]<br>data class [Success](-success/index.md)&lt;[T](-success/index.md)&gt;(val cryptoId: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), val cryptorData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), val encryptedData: [T](-success/index.md)) : [ParseResult](index.md)&lt;[T](-success/index.md)&gt; |

## Inheritors

| Name |
|---|
| [Success](-success/index.md) |
| [NoHeader](-no-header/index.md) |
