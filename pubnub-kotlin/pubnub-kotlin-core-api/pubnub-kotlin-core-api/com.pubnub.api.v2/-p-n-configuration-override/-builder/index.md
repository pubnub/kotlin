//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfigurationOverride](../index.md)/[Builder](index.md)

# Builder

[jvm]\
interface [Builder](index.md)

## Properties

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>abstract var [~~authKey~~](auth-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>If Access Manager (deprecated PAM v2) is utilized, client will use this authKey in all restricted requests. |
| [authToken](auth-token.md) | [jvm]<br>abstract var [authToken](auth-token.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>Authentication token for the PubNub client. This token is required on the client side when Access Manager (PAM) is enabled for PubNub keys. It can be generated using the PubNub.grantToken method, which should be executed on the server side with a PubNub instance initialized using the secret key. |
| [connectTimeout](connect-timeout.md) | [jvm]<br>abstract var [connectTimeout](connect-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>How long before the client gives up trying to connect with the server. |
| [cryptoModule](crypto-module.md) | [jvm]<br>abstract var [cryptoModule](crypto-module.md): [CryptoModule](../../../com.pubnub.api.crypto/-crypto-module/index.md)?<br>CryptoModule is responsible for handling encryption and decryption. If set, all communications to and from PubNub will be encrypted. |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>abstract var [includeInstanceIdentifier](include-instance-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether to include a PubNub.instanceId with every request. |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>abstract var [includeRequestIdentifier](include-request-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether to include a PubNub.requestId with every request. |
| [nonSubscribeReadTimeout](non-subscribe-read-timeout.md) | [jvm]<br>abstract var [nonSubscribeReadTimeout](non-subscribe-read-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>For non subscribe operations (publish, herenow, etc), This property relates to a read timeout that is applied from the moment the connection between a client and the server has been successfully established. It defines a maximum time of inactivity between two data packets when waiting for the server’s response. |
| [publishKey](publish-key.md) | [jvm]<br>abstract var [publishKey](publish-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The publish key from the admin panel (only required if publishing). |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>abstract var [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.retry/-retry-configuration/index.md)<br>Retry configuration for requests. Defaults to [RetryConfiguration.Exponential](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.retry/-retry-configuration/-exponential/index.md) enabled only for subscription endpoint (other endpoints are excluded). |
| [secretKey](secret-key.md) | [jvm]<br>abstract var [secretKey](secret-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The secret key from the admin panel (only required for modifying/revealing access permissions). |
| [subscribeKey](subscribe-key.md) | [jvm]<br>abstract var [subscribeKey](subscribe-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The subscribe key from the admin panel. |
| [userId](user-id.md) | [jvm]<br>abstract var [userId](user-id.md): [UserId](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-user-id/index.md)<br>The user ID that the PubNub client will use. |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [jvm]<br>abstract fun [build](build.md)(): [PNConfiguration](../../-p-n-configuration/index.md)<br>Create a [PNConfiguration](../../-p-n-configuration/index.md) object with values from this builder. |
