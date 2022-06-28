[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PNConfiguration](./index.md)

# PNConfiguration

`open class PNConfiguration`

A storage for user-provided information which describe further PubNub client behaviour.
Configuration instance contains additional set of properties which
allow performing precise PubNub client configuration.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PNConfiguration(uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, enableSubscribeBeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`<br>A storage for user-provided information which describe further PubNub client behaviour. Configuration instance contains additional set of properties which allow performing precise PubNub client configuration.`PNConfiguration(userId: `[`UserId`](../-user-id/index.md)`, enableSubscribeBeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)` |

### Properties

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | If Access Manager is utilized, client will use this authKey in all restricted requests.`var authKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [cacheBusting](cache-busting.md) | If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.`var cacheBusting: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [certificatePinner](certificate-pinner.md) | `var certificatePinner: CertificatePinner?` |
| [cipherKey](cipher-key.md) | If set, all communications to and from PubNub will be encrypted.`var cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [connectionSpec](connection-spec.md) | `var connectionSpec: ConnectionSpec?` |
| [connectTimeout](connect-timeout.md) | How long before the client gives up trying to connect with a subscribe call.`var connectTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [dedupOnSubscribe](dedup-on-subscribe.md) | `var dedupOnSubscribe: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [enableSubscribeBeta](enable-subscribe-beta.md) | `val enableSubscribeBeta: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md) | How many times publishing file message should automatically retry before marking the action as failed`var fileMessagePublishRetryLimit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [filterExpression](filter-expression.md) | Feature to subscribe with a custom filter expression.`var filterExpression: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [googleAppEngineNetworking](google-app-engine-networking.md) | Enable Google App Engine networking.`var googleAppEngineNetworking: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [heartbeatInterval](heartbeat-interval.md) | How often the client will announce itself to server.`var heartbeatInterval: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [heartbeatNotificationOptions](heartbeat-notification-options.md) | Set Heartbeat notification options.`var heartbeatNotificationOptions: `[`PNHeartbeatNotificationOptions`](../../com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md) |
| [hostnameVerifier](hostname-verifier.md) | `var hostnameVerifier: `[`HostnameVerifier`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/HostnameVerifier.html)`?` |
| [httpLoggingInterceptor](http-logging-interceptor.md) | Sets a custom [HttpLoggingInterceptor](#) for logging network traffic.`var httpLoggingInterceptor: HttpLoggingInterceptor?` |
| [includeInstanceIdentifier](include-instance-identifier.md) | Whether to include a [PubNub.instanceId](../-pub-nub/instance-id.md) with every request.`var includeInstanceIdentifier: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](include-request-identifier.md) | Whether to include a [PubNub.requestId](#) with every request.`var includeRequestIdentifier: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [logVerbosity](log-verbosity.md) | Set to [PNLogVerbosity.BODY](../../com.pubnub.api.enums/-p-n-log-verbosity/-b-o-d-y.md) to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE](../../com.pubnub.api.enums/-p-n-log-verbosity/-n-o-n-e.md).`var logVerbosity: `[`PNLogVerbosity`](../../com.pubnub.api.enums/-p-n-log-verbosity/index.md) |
| [maximumConnections](maximum-connections.md) | `var maximumConnections: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [maximumMessagesCacheSize](maximum-messages-cache-size.md) | `var maximumMessagesCacheSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [maximumReconnectionRetries](maximum-reconnection-retries.md) | Sets how many times to retry to reconnect before giving up. Must be used in combination with [reconnectionPolicy](reconnection-policy.md).`var maximumReconnectionRetries: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md) | For non subscribe operations (publish, herenow, etc), how long to wait to connect to PubNub before giving up with a connection timeout error.`var nonSubscribeRequestTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [origin](origin.md) | Custom origin if needed.`var origin: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [presenceTimeout](presence-timeout.md) | Sets the custom presence server timeout.`var presenceTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [proxy](proxy.md) | Instructs the SDK to use a proxy configuration when communicating with PubNub servers.`var proxy: `[`Proxy`](https://docs.oracle.com/javase/6/docs/api/java/net/Proxy.html)`?` |
| [proxyAuthenticator](proxy-authenticator.md) | `var proxyAuthenticator: Authenticator?` |
| [proxySelector](proxy-selector.md) | `var proxySelector: `[`ProxySelector`](https://docs.oracle.com/javase/6/docs/api/java/net/ProxySelector.html)`?` |
| [publishKey](publish-key.md) | The publish key from the admin panel (only required if publishing).`var publishKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [reconnectionPolicy](reconnection-policy.md) | Set to [PNReconnectionPolicy.LINEAR](../../com.pubnub.api.enums/-p-n-reconnection-policy/-l-i-n-e-a-r.md) for automatic reconnects.`var reconnectionPolicy: `[`PNReconnectionPolicy`](../../com.pubnub.api.enums/-p-n-reconnection-policy/index.md) |
| [requestMessageCountThreshold](request-message-count-threshold.md) | If the number of messages into the payload is above this value,`var requestMessageCountThreshold: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [secretKey](secret-key.md) | The secret key from the admin panel (only required for modifying/revealing access permissions).`var secretKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [secure](secure.md) | If set to `true`,  requests will be made over HTTPS.`var secure: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sslSocketFactory](ssl-socket-factory.md) | `var sslSocketFactory: `[`SSLSocketFactory`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/SSLSocketFactory.html)`?` |
| [startSubscriberThread](start-subscriber-thread.md) | Whether to start a separate subscriber thread when creating the instance.`var startSubscriberThread: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [subscribeKey](subscribe-key.md) | The subscribe key from the admin panel.`var subscribeKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeTimeout](subscribe-timeout.md) | The subscribe request timeout.`var subscribeTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [suppressLeaveEvents](suppress-leave-events.md) | When `true` the SDK doesn't send out the leave requests.`var suppressLeaveEvents: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [useRandomInitializationVector](use-random-initialization-vector.md) | Should initialization vector for encrypted messages be random.`var useRandomInitializationVector: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [userId](user-id.md) | `var userId: `[`UserId`](../-user-id/index.md) |
| [uuid](uuid.md) | `var ~~uuid~~: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [x509ExtendedTrustManager](x509-extended-trust-manager.md) | `var x509ExtendedTrustManager: `[`X509ExtendedTrustManager`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [addPnsdkSuffix](add-pnsdk-suffix.md) | `fun ~~addPnsdkSuffix~~(vararg nameToSuffixes: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun ~~addPnsdkSuffix~~(nameToSuffixes: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
