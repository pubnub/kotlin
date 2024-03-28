//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[BasePubNub](index.md)/[encryptInputStream](encrypt-input-stream.md)

# encryptInputStream

[jvm]\
abstract fun [encryptInputStream](encrypt-input-stream.md)(inputStream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html), cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)

Perform Cryptographic encryption of an input stream using provided cipher key.

#### Return

InputStream containing the encryption of `inputStream` using `cipherKey`.

#### Parameters

jvm

| | |
|---|---|
| inputStream | InputStream to be encrypted. |
| cipherKey | Cipher key to be used for encryption. |

#### Throws

| | |
|---|---|
| [PubNubException](../-pub-nub-exception/index.md) | Throws exception in case of failed encryption. |
