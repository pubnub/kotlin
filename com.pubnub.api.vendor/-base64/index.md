---
title: Base64 - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.vendor](../index.html) / [Base64](./index.html)

# Base64

`open class Base64`

Utilities for encoding and decoding the Base64 representation of binary data. See RFCs [2045](http://www.ietf.org/rfc/rfc2045.txt) and [3548](http://www.ietf.org/rfc/rfc3548.txt).

### Properties

| [CRLF](-c-r-l-f.html) | Encoder flag bit to indicate lines should be terminated with a CRLF pair instead of just an LF. Has no effect if `NO_WRAP` is specified as well.`static val CRLF: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [DEFAULT](-d-e-f-a-u-l-t.html) | Default values for encoder/decoder flags.`static val DEFAULT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [NO_CLOSE](-n-o_-c-l-o-s-e.html) | Flag to pass to indicate that it should not close the output stream it is wrapping when it itself is closed.`static val NO_CLOSE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [NO_PADDING](-n-o_-p-a-d-d-i-n-g.html) | Encoder flag bit to omit the padding '=' characters at the end of the output (if any).`static val NO_PADDING: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [NO_WRAP](-n-o_-w-r-a-p.html) | Encoder flag bit to omit all line terminators (i.e., the output will be on one long line).`static val NO_WRAP: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [URL_SAFE](-u-r-l_-s-a-f-e.html) | Encoder/decoder flag bit to indicate using the "URL and filename safe" variant of Base64 (see RFC 3548 section 4) where `-` and `_` are used in place of `+` and `/`.`static val URL_SAFE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| [decode](decode.html) | Decode the Base64-encoded data in input and return the data in a new byte array. `open static fun decode(str: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`<br>`open static fun decode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`<br>`open static fun decode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, len: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!` |
| [encode](encode.html) | Base64-encode the given data and return a newly allocated byte[] with the result.`open static fun encode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!`<br>`open static fun encode(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, len: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!` |
| [encodeToString](encode-to-string.html) | Base64-encode the given data and return a newly allocated String with the result.`open static fun encodeToString(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!`<br>`open static fun encodeToString(input: `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)`!, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, len: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!` |

