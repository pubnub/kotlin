//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.vendor](../index.md)/[Base64](index.md)/[decode](decode.md)

# decode

[jvm]\
open fun [decode](decode.md)(str: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;

Decode the Base64-encoded data in input and return the data in a new byte array. 

The padding '=' characters at the end are considered optional, but if any are present, there must be the correct number of them.

## Parameters

jvm

| | |
|---|---|
| str | the input String to decode, which is converted to bytes using the default charset |
| flags | controls certain features of the decoded output. Pass `DEFAULT` to decode standard Base64. |

## Throws

| | |
|---|---|
| [java.lang.IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html) | if the input contains incorrect padding |

[jvm]\
open fun [decode](decode.md)(input: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;

Decode the Base64-encoded data in input and return the data in a new byte array. 

The padding '=' characters at the end are considered optional, but if any are present, there must be the correct number of them.

## Parameters

jvm

| | |
|---|---|
| input | the input array to decode |
| flags | controls certain features of the decoded output. Pass `DEFAULT` to decode standard Base64. |

## Throws

| | |
|---|---|
| [java.lang.IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html) | if the input contains incorrect padding |

[jvm]\
open fun [decode](decode.md)(input: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), flags: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;

Decode the Base64-encoded data in input and return the data in a new byte array. 

The padding '=' characters at the end are considered optional, but if any are present, there must be the correct number of them.

## Parameters

jvm

| | |
|---|---|
| input | the data to decode |
| offset | the position within the input array at which to start |
| len | the number of bytes of input to decode |
| flags | controls certain features of the decoded output. Pass `DEFAULT` to decode standard Base64. |

## Throws

| | |
|---|---|
| [java.lang.IllegalArgumentException](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html) | if the input contains incorrect padding |
