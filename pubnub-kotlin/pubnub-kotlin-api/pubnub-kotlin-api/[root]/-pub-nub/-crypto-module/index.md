//[pubnub-kotlin-api](../../../../index.md)/[[root]](../../index.md)/[PubNub](../index.md)/[CryptoModule](index.md)

# CryptoModule

[js]\
open class [CryptoModule](index.md)(configuration: [PubNub.CryptoModuleConfiguration](../-crypto-module-configuration/index.md))

## Constructors

| | |
|---|---|
| [CryptoModule](-crypto-module.md) | [js]<br>constructor(configuration: [PubNub.CryptoModuleConfiguration](../-crypto-module-configuration/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [js]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [cryptors](cryptors.md) | [js]<br>open var [cryptors](cryptors.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;dynamic&gt; |
| [defaultCryptor](default-cryptor.md) | [js]<br>open var [defaultCryptor](default-cryptor.md): dynamic |

## Functions

| Name | Summary |
|---|---|
| [decrypt](decrypt.md) | [js]<br>open fun [decrypt](decrypt.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): dynamic<br>open fun [decrypt](decrypt.md)(data: [ArrayBuffer](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/org.khronos.webgl/-array-buffer/index.html)): dynamic |
| [decryptFile](decrypt-file.md) | [js]<br>open fun [decryptFile](decrypt-file.md)(file: [PubNub.PubNubFileType](../-pub-nub-file-type/index.md), fd: [PubNub.PubNubFileType](../-pub-nub-file-type/index.md)): [Promise](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.PubNubFileType](../-pub-nub-file-type/index.md)&gt; |
| [encrypt](encrypt.md) | [js]<br>open fun [encrypt](encrypt.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ArrayBuffer](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/org.khronos.webgl/-array-buffer/index.html)<br>open fun [encrypt](encrypt.md)(data: [ArrayBuffer](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/org.khronos.webgl/-array-buffer/index.html)): [ArrayBuffer](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/org.khronos.webgl/-array-buffer/index.html) |
| [encryptFile](encrypt-file.md) | [js]<br>open fun [encryptFile](encrypt-file.md)(file: [PubNub.PubNubFileType](../-pub-nub-file-type/index.md), fd: [PubNub.PubNubFileType](../-pub-nub-file-type/index.md)): [Promise](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.PubNubFileType](../-pub-nub-file-type/index.md)&gt; |
