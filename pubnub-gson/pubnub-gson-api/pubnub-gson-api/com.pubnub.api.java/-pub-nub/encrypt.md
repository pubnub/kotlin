//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[encrypt](encrypt.md)

# encrypt

[jvm]\
abstract fun [encrypt](encrypt.md)(inputString: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), cipherKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)

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

[jvm]\
abstract fun [encrypt](encrypt.md)(inputString: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)

Perform Cryptographic encryption of an input string and a cipher key.

#### Return

String containing the encryption of `inputString` using PNConfiguration.getCipherKey.

#### Parameters

jvm

| | |
|---|---|
| inputString | String to be encrypted. |

#### Throws

| | |
|---|---|
| [PubNubException](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-pub-nub-exception/index.md) | Throws exception in case of failed encryption. |
