//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[decryptInputStream](decrypt-input-stream.md)

# decryptInputStream

[jvm]\
fun [decryptInputStream](decrypt-input-stream.md)(inputStream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html), cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = configuration.cipherKey): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)

Perform Cryptographic decryption of an input stream using provided cipher key.

#### Return

InputStream containing the encryption of `inputStream` using `cipherKey`.

## Parameters

jvm

| | |
|---|---|
| inputStream | InputStream to be encrypted. |
| cipherKey | Cipher key to be used for decryption. If not provided [PNConfiguration.cipherKey](../-p-n-configuration/cipher-key.md) is used. |

## Throws

| | |
|---|---|
| [com.pubnub.api.PubNubException](../-pub-nub-exception/index.md) | Throws exception in case of failed decryption. |
