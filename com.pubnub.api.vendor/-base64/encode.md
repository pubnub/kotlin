---
title: Base64.encode - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.vendor](../index.html) / [Base64](index.html) / [encode](./encode.html)

# encode

`open static fun encode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`

Base64-encode the given data and return a newly allocated byte[] with the result.

### Parameters

`input` - [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)!: the data to encode

`flags` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): controls certain features of the encoded output. Passing `DEFAULT` results in output that adheres to RFC 2045.`open static fun encode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, len: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`

Base64-encode the given data and return a newly allocated byte[] with the result.

### Parameters

`input` - [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)!: the data to encode

`offset` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): the position within the input array at which to start

`len` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): the number of bytes of input to encode

`flags` - [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html): controls certain features of the encoded output. Passing `DEFAULT` results in output that adheres to RFC 2045.