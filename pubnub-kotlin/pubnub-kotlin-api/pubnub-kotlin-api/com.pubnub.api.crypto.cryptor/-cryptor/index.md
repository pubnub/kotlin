//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.crypto.cryptor](../index.md)/[Cryptor](index.md)

# Cryptor

[jvm]\
interface [Cryptor](index.md)

## Functions

| Name | Summary |
|---|---|
| [decrypt](decrypt.md) | [jvm]<br>abstract fun [decrypt](decrypt.md)(encryptedData: [EncryptedData](../../com.pubnub.api.crypto.data/-encrypted-data/index.md)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [decryptStream](decrypt-stream.md) | [jvm]<br>abstract fun [decryptStream](decrypt-stream.md)(encryptedData: [EncryptedStreamData](../../com.pubnub.api.crypto.data/-encrypted-stream-data/index.md)): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) |
| [encrypt](encrypt.md) | [jvm]<br>abstract fun [encrypt](encrypt.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [EncryptedData](../../com.pubnub.api.crypto.data/-encrypted-data/index.md) |
| [encryptStream](encrypt-stream.md) | [jvm]<br>abstract fun [encryptStream](encrypt-stream.md)(stream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)): [EncryptedStreamData](../../com.pubnub.api.crypto.data/-encrypted-stream-data/index.md) |
| [id](id.md) | [jvm]<br>abstract fun [id](id.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
