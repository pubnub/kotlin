//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfigurationOverride](../index.md)/[Builder](index.md)

# Builder

[jvm]\
interface [Builder](index.md) : [BasePNConfigurationOverride.Builder](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)

## Properties

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>abstract override var [authKey](auth-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [connectTimeout](connect-timeout.md) | [jvm]<br>abstract override var [connectTimeout](connect-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [cryptoModule](crypto-module.md) | [jvm]<br>abstract override var [cryptoModule](crypto-module.md): [CryptoModule](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)? |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>abstract override var [includeInstanceIdentifier](include-instance-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>abstract override var [includeRequestIdentifier](include-request-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [nonSubscribeReadTimeout](non-subscribe-read-timeout.md) | [jvm]<br>abstract override var [nonSubscribeReadTimeout](non-subscribe-read-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [publishKey](publish-key.md) | [jvm]<br>abstract override var [publishKey](publish-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>abstract override var [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md) |
| [secretKey](secret-key.md) | [jvm]<br>abstract override var [secretKey](secret-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeKey](subscribe-key.md) | [jvm]<br>abstract override var [subscribeKey](subscribe-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [userId](user-id.md) | [jvm]<br>abstract override var [userId](user-id.md): [UserId](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md) |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [jvm]<br>abstract fun [build](build.md)(): [PNConfiguration](../../-p-n-configuration/index.md)<br>Create a [PNConfiguration](../../-p-n-configuration/index.md) object with values from this builder. |
