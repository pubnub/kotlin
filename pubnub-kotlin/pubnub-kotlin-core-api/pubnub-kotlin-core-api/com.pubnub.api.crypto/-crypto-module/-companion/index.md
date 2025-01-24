//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.crypto](../../index.md)/[CryptoModule](../index.md)/[Companion](index.md)

# Companion

[jvm]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [createAesCbcCryptoModule](create-aes-cbc-crypto-module.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [createAesCbcCryptoModule](create-aes-cbc-crypto-module.md)(cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), randomIv: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [CryptoModule](../index.md) |
| [createLegacyCryptoModule](create-legacy-crypto-module.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [createLegacyCryptoModule](create-legacy-crypto-module.md)(cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), randomIv: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = true): [CryptoModule](../index.md) |
| [createNewCryptoModule](create-new-crypto-module.md) | [jvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun [createNewCryptoModule](create-new-crypto-module.md)(defaultCryptor: [Cryptor](../../../com.pubnub.api.crypto.cryptor/-cryptor/index.md), cryptorsForDecryptionOnly: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Cryptor](../../../com.pubnub.api.crypto.cryptor/-cryptor/index.md)&gt; = listOf()): [CryptoModule](../index.md) |
