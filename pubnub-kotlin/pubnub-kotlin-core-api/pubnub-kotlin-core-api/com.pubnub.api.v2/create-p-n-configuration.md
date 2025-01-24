//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.v2](index.md)/[createPNConfiguration](create-p-n-configuration.md)

# createPNConfiguration

[common, apple, js, jvm]\
[common]\
expect fun [createPNConfiguration](create-p-n-configuration.md)(userId: [UserId](../com.pubnub.api/-user-id/index.md), subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, logVerbosity: [PNLogVerbosity](../com.pubnub.api.enums/-p-n-log-verbosity/index.md) = PNLogVerbosity.NONE, authToken: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [PNConfiguration](-p-n-configuration/index.md)

[apple, js]\
actual fun [createPNConfiguration](create-p-n-configuration.md)(userId: [UserId](../com.pubnub.api/-user-id/index.md), subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?, logVerbosity: [PNLogVerbosity](../com.pubnub.api.enums/-p-n-log-verbosity/index.md), authToken: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?): [PNConfiguration](-p-n-configuration/index.md)

[jvm]\
actual fun [createPNConfiguration](create-p-n-configuration.md)(userId: [UserId](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-user-id/index.md), subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?, logVerbosity: [PNLogVerbosity](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md), authToken: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?): [PNConfiguration](-p-n-configuration/index.md)

[common, apple, js, jvm]\
[common]\
expect fun [~~createPNConfiguration~~](create-p-n-configuration.md)(userId: [UserId](../com.pubnub.api/-user-id/index.md), subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, logVerbosity: [PNLogVerbosity](../com.pubnub.api.enums/-p-n-log-verbosity/index.md) = PNLogVerbosity.NONE): [PNConfiguration](-p-n-configuration/index.md)

[apple, js]\
actual fun [~~createPNConfiguration~~](create-p-n-configuration.md)(userId: [UserId](../com.pubnub.api/-user-id/index.md), subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?, authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?, logVerbosity: [PNLogVerbosity](../com.pubnub.api.enums/-p-n-log-verbosity/index.md)): [PNConfiguration](-p-n-configuration/index.md)

[jvm]\
actual fun [~~createPNConfiguration~~](create-p-n-configuration.md)(userId: [UserId](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-user-id/index.md), subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?, authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?, logVerbosity: [PNLogVerbosity](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md)): [PNConfiguration](-p-n-configuration/index.md)

---

### Deprecated

The authKey parameter is deprecated because it relates to deprecated Access Manager (PAM V2) and will be removed in the future.Please, use createPNConfiguration without authKey instead and migrate to new Access Manager (PAM V3) https://www.pubnub.com/docs/general/resources/migration-guides/pam-v3-migration 

#### Replace with

```kotlin
createPNConfiguration(userId, subscribeKey, publishKey, secretKey, logVerbosity)
```
---
