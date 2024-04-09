//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[encryptInputStream](encrypt-input-stream.md)

# encryptInputStream

[jvm]\
abstract fun [encryptInputStream](encrypt-input-stream.md)(inputStream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)

Perform Cryptographic encryption of an input stream using provided cipher key.

#### Return

InputStream containing the encryption of `inputStream` using PNConfiguration.getCipherKey.

#### Parameters

jvm

| | |
|---|---|
| inputStream | InputStream to be encrypted. |
| cipherKey | Cipher key to be used for encryption. |

#### Throws

| | |
|---|---|
| [PubNubException](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-pub-nub-exception/index.md) | Throws exception in case of failed encryption. |
