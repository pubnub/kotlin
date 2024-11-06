//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[encrypt](encrypt.md)

# encrypt

[jvm]\
abstract fun [encrypt](encrypt.md)(inputString: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Perform Cryptographic encryption of an input string and a cipher key.

#### Return

String containing the encryption of `inputString` using `cipherKey`.

#### Parameters

jvm

| | |
|---|---|
| inputString | String to be encrypted. |
| cipherKey | Cipher key to be used for encryption. Default is PNConfiguration.cipherKey |

#### Throws

| | |
|---|---|
| [PubNubException](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-exception/index.md) | Throws exception in case of failed encryption. |
