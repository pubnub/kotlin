//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.vendor](../index.md)/[Base64](index.md)/[encodeToString](encode-to-string.md)

# encodeToString

[jvm]\
open fun [encodeToString](encode-to-string.md)(input: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)

Base64-encode the given data and return a newly allocated String with the result.

## Parameters

jvm

| | |
|---|---|
| input | the data to encode |
| flags | controls certain features of the encoded output. Passing `DEFAULT` results in output that adheres to RFC 2045. |

[jvm]\
open fun [encodeToString](encode-to-string.md)(input: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)

Base64-encode the given data and return a newly allocated String with the result.

## Parameters

jvm

| | |
|---|---|
| input | the data to encode |
| offset | the position within the input array at which to start |
| len | the number of bytes of input to encode |
| flags | controls certain features of the encoded output. Passing `DEFAULT` results in output that adheres to RFC 2045. |
