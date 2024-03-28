//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[encrypt](encrypt.md)

# encrypt

[jvm]\
abstract fun [encrypt](encrypt.md)(inputString: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

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
| [PubNubException](../../../../pubnub-gson/com.pubnub.api/-pub-nub-exception/index.md) | Throws exception in case of failed encryption. |
