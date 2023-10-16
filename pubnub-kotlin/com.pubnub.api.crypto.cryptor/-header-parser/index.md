//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.crypto.cryptor](../index.md)/[HeaderParser](index.md)

# HeaderParser

[jvm]\
class [HeaderParser](index.md)

## Constructors

| | |
|---|---|
| [HeaderParser](-header-parser.md) | [jvm]<br>fun [HeaderParser](-header-parser.md)() |

## Functions

| Name | Summary |
|---|---|
| [createCryptorHeader](create-cryptor-header.md) | [jvm]<br>fun [createCryptorHeader](create-cryptor-header.md)(cryptorId: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), cryptorData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [parseDataWithHeader](parse-data-with-header.md) | [jvm]<br>fun [parseDataWithHeader](parse-data-with-header.md)(stream: [BufferedInputStream](https://docs.oracle.com/javase/8/docs/api/java/io/BufferedInputStream.html)): [ParseResult](../-parse-result/index.md)&lt;out [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)&gt;<br>fun [parseDataWithHeader](parse-data-with-header.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [ParseResult](../-parse-result/index.md)&lt;out [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)&gt; |
