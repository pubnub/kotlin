//[pubnub-kotlin](../../index.md)/[com.pubnub.api.crypto.cryptor](index.md)

# Package com.pubnub.api.crypto.cryptor

## Types

| Name | Summary |
|---|---|
| [AesCbcCryptor](-aes-cbc-cryptor/index.md) | [jvm]<br>class [AesCbcCryptor](-aes-cbc-cryptor/index.md)(val cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Cryptor](-cryptor/index.md) |
| [Cryptor](-cryptor/index.md) | [jvm]<br>interface [Cryptor](-cryptor/index.md) |
| [CryptorHeader](-cryptor-header/index.md) | [jvm]<br>class [CryptorHeader](-cryptor-header/index.md)(val sentinel: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), val version: [Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html), val cryptorId: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), val cryptorDataSize: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), val cryptorData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |
| [CryptorHeaderVersion](-cryptor-header-version/index.md) | [jvm]<br>enum [CryptorHeaderVersion](-cryptor-header-version/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[CryptorHeaderVersion](-cryptor-header-version/index.md)&gt; |
| [HeaderParser](-header-parser/index.md) | [jvm]<br>class [HeaderParser](-header-parser/index.md) |
| [LegacyCryptor](-legacy-cryptor/index.md) | [jvm]<br>class [LegacyCryptor](-legacy-cryptor/index.md)(val cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val useRandomIv: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true) : [Cryptor](-cryptor/index.md) |
| [ParseResult](-parse-result/index.md) | [jvm]<br>sealed class [ParseResult](-parse-result/index.md)&lt;[T](-parse-result/index.md)&gt; |
