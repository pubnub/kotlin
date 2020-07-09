[pubnub-kotlin](../../index.md) / [com.pubnub.api.vendor](../index.md) / [Base64](index.md) / [decode](./decode.md)

# decode

`open static fun decode(str: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`

Decode the Base64-encoded data in input and return the data in a new byte array.

The padding '=' characters at the end are considered optional, but if any are present, there must be the correct number of them.

### Parameters

`str` - [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)!: the input String to decode, which is converted to bytes using the default charset

`flags` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): controls certain features of the decoded output. Pass `DEFAULT` to decode standard Base64.

### Exceptions

`IllegalArgumentException` - if the input contains incorrect padding`open static fun decode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`

Decode the Base64-encoded data in input and return the data in a new byte array.

The padding '=' characters at the end are considered optional, but if any are present, there must be the correct number of them.

### Parameters

`input` - [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)!: the input array to decode

`flags` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): controls certain features of the decoded output. Pass `DEFAULT` to decode standard Base64.

### Exceptions

`IllegalArgumentException` - if the input contains incorrect padding`open static fun decode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, len: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`

Decode the Base64-encoded data in input and return the data in a new byte array.

The padding '=' characters at the end are considered optional, but if any are present, there must be the correct number of them.

### Parameters

`input` - [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)!: the data to decode

`offset` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): the position within the input array at which to start

`len` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): the number of bytes of input to decode

`flags` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): controls certain features of the decoded output. Pass `DEFAULT` to decode standard Base64.

### Exceptions

`IllegalArgumentException` - if the input contains incorrect padding