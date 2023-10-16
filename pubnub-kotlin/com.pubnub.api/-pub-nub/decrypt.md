//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[decrypt](decrypt.md)

# decrypt

[jvm]\
fun [decrypt](decrypt.md)(inputString: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey](../-p-n-configuration/cipher-key.md).

#### Return

String containing the decryption of `inputString` using `cipherKey`.

## Parameters

jvm

| | |
|---|---|
| inputString | String to be decrypted. |

## Throws

| | |
|---|---|
| [com.pubnub.api.PubNubException](../-pub-nub-exception/index.md) | throws exception in case of failed decryption. |

[jvm]\
fun [decrypt](decrypt.md)(inputString: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic decryption of an input string using a cipher key.

#### Return

String containing the decryption of `inputString` using `cipherKey`.

## Parameters

jvm

| | |
|---|---|
| inputString | String to be decrypted. |
| cipherKey | cipher key to be used for decryption. Default is [PNConfiguration.cipherKey](../-p-n-configuration/cipher-key.md) |

## Throws

| | |
|---|---|
| [com.pubnub.api.PubNubException](../-pub-nub-exception/index.md) | throws exception in case of failed decryption. |
