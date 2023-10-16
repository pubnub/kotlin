//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.crypto.cryptor](../index.md)/[AesCbcCryptor](index.md)

# AesCbcCryptor

[jvm]\
class [AesCbcCryptor](index.md)(val cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Cryptor](../-cryptor/index.md)

## Constructors

| | |
|---|---|
| [AesCbcCryptor](-aes-cbc-cryptor.md) | [jvm]<br>fun [AesCbcCryptor](-aes-cbc-cryptor.md)(cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [decrypt](decrypt.md) | [jvm]<br>open override fun [decrypt](decrypt.md)(encryptedData: [EncryptedData](../../com.pubnub.api.crypto.data/-encrypted-data/index.md)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [decryptStream](decrypt-stream.md) | [jvm]<br>open override fun [decryptStream](decrypt-stream.md)(encryptedData: [EncryptedStreamData](../../com.pubnub.api.crypto.data/-encrypted-stream-data/index.md)): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) |
| [encrypt](encrypt.md) | [jvm]<br>open override fun [encrypt](encrypt.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [EncryptedData](../../com.pubnub.api.crypto.data/-encrypted-data/index.md) |
| [encryptStream](encrypt-stream.md) | [jvm]<br>open override fun [encryptStream](encrypt-stream.md)(stream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)): [EncryptedStreamData](../../com.pubnub.api.crypto.data/-encrypted-stream-data/index.md) |
| [id](id.md) | [jvm]<br>open override fun [id](id.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |

## Properties

| Name | Summary |
|---|---|
| [cipherKey](cipher-key.md) | [jvm]<br>val [cipherKey](cipher-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
