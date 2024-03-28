//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[cipherKey](cipher-key.md)

# cipherKey

[jvm]\
var [~~cipherKey~~](cipher-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?

---

### Deprecated

Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)

---

If set, all communications to and from PubNub will be encrypted.
