//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfigurationOverride](../index.md)/[Builder](index.md)

# Builder

interface [Builder](index.md)

#### Inheritors

| |
|---|
| [Builder](../../-p-n-configuration/-builder/index.md) |

## Functions

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>abstract fun [authKey](auth-key.md)(authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md)<br>If Access Manager is utilized, client will use this authKey in all restricted requests. |
| [build](build.md) | [jvm]<br>abstract fun [build](build.md)(): [PNConfiguration](../../-p-n-configuration/index.md)<br>Create a [PNConfiguration](../../-p-n-configuration/index.md) object with values from this builder. |
| [connectTimeout](connect-timeout.md) | [jvm]<br>abstract fun [connectTimeout](connect-timeout.md)(connectTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfigurationOverride.Builder](index.md)<br>How long before the client gives up trying to connect with the server. |
| [cryptoModule](crypto-module.md) | [jvm]<br>abstract fun [cryptoModule](crypto-module.md)(cryptoModule: [CryptoModule](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.crypto/-crypto-module/index.md)?): [PNConfigurationOverride.Builder](index.md)<br>CryptoModule is responsible for handling encryption and decryption. If set, all communications to and from PubNub will be encrypted. |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>abstract fun [includeInstanceIdentifier](include-instance-identifier.md)(includeInstanceIdentifier: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfigurationOverride.Builder](index.md)<br>Whether to include a PubNubCore.instanceId with every request. |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>abstract fun [includeRequestIdentifier](include-request-identifier.md)(includeRequestIdentifier: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfigurationOverride.Builder](index.md)<br>Whether to include a PubNubCore.requestId with every request. |
| [nonSubscribeReadTimeout](non-subscribe-read-timeout.md) | [jvm]<br>abstract fun [nonSubscribeReadTimeout](non-subscribe-read-timeout.md)(nonSubscribeReadTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfigurationOverride.Builder](index.md)<br>For non subscribe operations (publish, herenow, etc), This property relates to a read timeout that is applied from the moment the connection between a client and the server has been successfully established. It defines a maximum time of inactivity between two data packets when waiting for the server’s response. |
| [publishKey](publish-key.md) | [jvm]<br>abstract fun [publishKey](publish-key.md)(publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md)<br>The publish key from the admin panel (only required if publishing). |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>abstract fun [retryConfiguration](retry-configuration.md)(retryConfiguration: [RetryConfiguration](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.retry/-retry-configuration/index.md)): [PNConfigurationOverride.Builder](index.md)<br>Retry configuration for requests. Defaults to [RetryConfiguration.Exponential](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.retry/-retry-configuration/-exponential/index.md) enabled only for subscription endpoint (other endpoints are excluded). |
| [secretKey](secret-key.md) | [jvm]<br>abstract fun [secretKey](secret-key.md)(secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md)<br>The secret key from the admin panel (only required for modifying/revealing access permissions). |
| [setUserId](set-user-id.md) | [jvm]<br>abstract fun [setUserId](set-user-id.md)(userId: [UserId](../../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api/-user-id/index.md)): [PNConfigurationOverride.Builder](index.md) |
| [subscribeKey](subscribe-key.md) | [jvm]<br>abstract fun [subscribeKey](subscribe-key.md)(subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfigurationOverride.Builder](index.md) |
