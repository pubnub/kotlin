//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[decryptInputStream](decrypt-input-stream.md)

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
| [PubNubException](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-pub-nub-exception/index.md) | Throws exception in case of failed decryption. |
