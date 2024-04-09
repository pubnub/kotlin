//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfigurationOverride](../index.md)/[Builder](index.md)

# Builder

interface [Builder](index.md) : [BasePNConfigurationOverride.Builder](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration-override/-builder/index.md)

#### Inheritors

| |
|---|
| [Builder](../../-p-n-configuration/-builder/index.md) |

## Properties

| Name | Summary |
|---|---|
| [authKey](index.md#763529489%2FProperties%2F126356644) | [jvm]<br>abstract val [authKey](index.md#763529489%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [connectTimeout](index.md#-492727525%2FProperties%2F126356644) | [jvm]<br>abstract val [connectTimeout](index.md#-492727525%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [cryptoModule](index.md#-1554978907%2FProperties%2F126356644) | [jvm]<br>abstract val [cryptoModule](index.md#-1554978907%2FProperties%2F126356644): [CryptoModule](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)? |
| [includeInstanceIdentifier](index.md#-739439390%2FProperties%2F126356644) | [jvm]<br>abstract val [includeInstanceIdentifier](index.md#-739439390%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](index.md#771939138%2FProperties%2F126356644) | [jvm]<br>abstract val [includeRequestIdentifier](index.md#771939138%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [nonSubscribeReadTimeout](index.md#1493249242%2FProperties%2F126356644) | [jvm]<br>abstract val [nonSubscribeReadTimeout](index.md#1493249242%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [publishKey](index.md#2094108162%2FProperties%2F126356644) | [jvm]<br>abstract val [publishKey](index.md#2094108162%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [retryConfiguration](index.md#569464420%2FProperties%2F126356644) | [jvm]<br>abstract val [retryConfiguration](index.md#569464420%2FProperties%2F126356644): [RetryConfiguration](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md) |
| [secretKey](index.md#-681670823%2FProperties%2F126356644) | [jvm]<br>abstract val [secretKey](index.md#-681670823%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeKey](index.md#1831339645%2FProperties%2F126356644) | [jvm]<br>abstract val [subscribeKey](index.md#1831339645%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [userId](index.md#-1286398516%2FProperties%2F126356644) | [jvm]<br>abstract val [userId](index.md#-1286398516%2FProperties%2F126356644): [UserId](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md) |

## Functions

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>abstract fun [authKey](auth-key.md)(authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md)<br>If Access Manager is utilized, client will use this authKey in all restricted requests. |
| [build](build.md) | [jvm]<br>abstract fun [build](build.md)(): [PNConfiguration](../../-p-n-configuration/index.md)<br>Create a [PNConfiguration](../../-p-n-configuration/index.md) object with values from this builder. |
| [connectTimeout](connect-timeout.md) | [jvm]<br>abstract fun [connectTimeout](connect-timeout.md)(connectTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfigurationOverride.Builder](index.md)<br>How long before the client gives up trying to connect with the server. |
| [cryptoModule](crypto-module.md) | [jvm]<br>abstract fun [cryptoModule](crypto-module.md)(cryptoModule: [CryptoModule](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)?): [PNConfigurationOverride.Builder](index.md)<br>CryptoModule is responsible for handling encryption and decryption. If set, all communications to and from PubNub will be encrypted. |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>abstract fun [includeInstanceIdentifier](include-instance-identifier.md)(includeInstanceIdentifier: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfigurationOverride.Builder](index.md)<br>Whether to include a PubNubCore.instanceId with every request. |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>abstract fun [includeRequestIdentifier](include-request-identifier.md)(includeRequestIdentifier: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfigurationOverride.Builder](index.md)<br>Whether to include a PubNubCore.requestId with every request. |
| [nonSubscribeReadTimeout](non-subscribe-read-timeout.md) | [jvm]<br>abstract fun [nonSubscribeReadTimeout](non-subscribe-read-timeout.md)(nonSubscribeReadTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfigurationOverride.Builder](index.md)<br>For non subscribe operations (publish, herenow, etc), This property relates to a read timeout that is applied from the moment the connection between a client and the server has been successfully established. It defines a maximum time of inactivity between two data packets when waiting for the server’s response. |
| [publishKey](publish-key.md) | [jvm]<br>abstract fun [publishKey](publish-key.md)(publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md)<br>The publish key from the admin panel (only required if publishing). |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>abstract fun [retryConfiguration](retry-configuration.md)(retryConfiguration: [RetryConfiguration](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md)): [PNConfigurationOverride.Builder](index.md)<br>Retry configuration for requests. Defaults to [RetryConfiguration.None](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-none/index.md). |
| [secretKey](secret-key.md) | [jvm]<br>abstract fun [secretKey](secret-key.md)(secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md)<br>The secret key from the admin panel (only required for modifying/revealing access permissions). |
| [setUserId](set-user-id.md) | [jvm]<br>abstract fun [setUserId](set-user-id.md)(userId: [UserId](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md)): [PNConfigurationOverride.Builder](index.md) |
| [subscribeKey](subscribe-key.md) | [jvm]<br>abstract fun [subscribeKey](subscribe-key.md)(subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md) |
