//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[useRandomInitializationVector](use-random-initialization-vector.md)

# useRandomInitializationVector

[jvm]\
var [~~useRandomInitializationVector~~](use-random-initialization-vector.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

---

### Deprecated

Instead of cipherKey and useRandomInitializationVector use CryptoModule instead 
            e.g. config.cryptoModule = CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true) 
            or config.cryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = true)

---

Should initialization vector for encrypted messages be random.

Defaults to `true`.
