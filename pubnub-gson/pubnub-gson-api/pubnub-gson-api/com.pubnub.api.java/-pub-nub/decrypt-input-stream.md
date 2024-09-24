//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[decryptInputStream](decrypt-input-stream.md)

# decryptInputStream

[jvm]\
abstract fun [decryptInputStream](decrypt-input-stream.md)(inputStream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)

Perform Cryptographic decryption of an input stream using provided cipher key.

#### Return

InputStream containing the encryption of `inputStream` using PNConfiguration.getCipherKey

#### Parameters

jvm

| | |
|---|---|
| inputStream | InputStream to be encrypted. |

#### Throws

| | |
|---|---|
| [PubNubException](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub-exception/index.md) | Throws exception in case of failed decryption. |

[jvm]\
abstract fun [decryptInputStream](decrypt-input-stream.md)(inputStream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html), cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)

Perform Cryptographic decryption of an input stream using provided cipher key.

#### Return

InputStream containing the encryption of `inputStream` using `cipherKey`.

#### Parameters

jvm

| | |
|---|---|
| inputStream | InputStream to be encrypted. |
| cipherKey | Cipher key to be used for decryption. |

#### Throws

| | |
|---|---|
| [PubNubException](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-pub-nub-exception/index.md) | Throws exception in case of failed decryption. |
