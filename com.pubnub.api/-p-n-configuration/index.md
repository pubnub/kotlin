---
title: PNConfiguration - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api](../index.html) / [PNConfiguration](./index.html)

# PNConfiguration

`class PNConfiguration`

### Constructors

| [&lt;init&gt;](-init-.html) | `PNConfiguration()` |

### Properties

| [authKey](auth-key.html) | `lateinit var authKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [cacheBusting](cache-busting.html) | `var cacheBusting: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [certificatePinner](certificate-pinner.html) | `var certificatePinner: CertificatePinner?` |
| [cipherKey](cipher-key.html) | `lateinit var cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [connectionSpec](connection-spec.html) | `var connectionSpec: ConnectionSpec?` |
| [connectTimeout](connect-timeout.html) | `var connectTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [dedupOnSubscribe](dedup-on-subscribe.html) | `var dedupOnSubscribe: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [filterExpression](filter-expression.html) | `lateinit var filterExpression: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [googleAppEngineNetworking](google-app-engine-networking.html) | `var googleAppEngineNetworking: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [heartbeatInterval](heartbeat-interval.html) | `var heartbeatInterval: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [heartbeatNotificationOptions](heartbeat-notification-options.html) | `var heartbeatNotificationOptions: `[`PNHeartbeatNotificationOptions`](../../com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.html) |
| [hostnameVerifier](hostname-verifier.html) | `var hostnameVerifier: `[`HostnameVerifier`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/HostnameVerifier.html)`?` |
| [httpLoggingInterceptor](http-logging-interceptor.html) | `var httpLoggingInterceptor: HttpLoggingInterceptor?` |
| [includeInstanceIdentifier](include-instance-identifier.html) | `var includeInstanceIdentifier: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](include-request-identifier.html) | `var includeRequestIdentifier: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [logVerbosity](log-verbosity.html) | `var logVerbosity: `[`PNLogVerbosity`](../../com.pubnub.api.enums/-p-n-log-verbosity/index.html) |
| [maximumConnections](maximum-connections.html) | `var maximumConnections: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [maximumMessagesCacheSize](maximum-messages-cache-size.html) | `var maximumMessagesCacheSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [maximumReconnectionRetries](maximum-reconnection-retries.html) | `var maximumReconnectionRetries: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [nonSubscribeRequestTimeout](non-subscribe-request-timeout.html) | `var nonSubscribeRequestTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [origin](origin.html) | `lateinit var origin: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [presenceTimeout](presence-timeout.html) | `var presenceTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [proxy](proxy.html) | `var proxy: `[`Proxy`](https://docs.oracle.com/javase/6/docs/api/java/net/Proxy.html)`?` |
| [proxyAuthenticator](proxy-authenticator.html) | `var proxyAuthenticator: Authenticator?` |
| [proxySelector](proxy-selector.html) | `var proxySelector: `[`ProxySelector`](https://docs.oracle.com/javase/6/docs/api/java/net/ProxySelector.html)`?` |
| [publishKey](publish-key.html) | `lateinit var publishKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [reconnectionPolicy](reconnection-policy.html) | `var reconnectionPolicy: `[`PNReconnectionPolicy`](../../com.pubnub.api.enums/-p-n-reconnection-policy/index.html) |
| [requestMessageCountThreshold](request-message-count-threshold.html) | `var requestMessageCountThreshold: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [secretKey](secret-key.html) | `lateinit var secretKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [secure](secure.html) | `var secure: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sslSocketFactory](ssl-socket-factory.html) | `var sslSocketFactory: `[`SSLSocketFactory`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/SSLSocketFactory.html)`?` |
| [startSubscriberThread](start-subscriber-thread.html) | `var startSubscriberThread: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [subscribeKey](subscribe-key.html) | `lateinit var subscribeKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeTimeout](subscribe-timeout.html) | `var subscribeTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [suppressLeaveEvents](suppress-leave-events.html) | `var suppressLeaveEvents: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [uuid](uuid.html) | `var uuid: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [x509ExtendedTrustManager](x509-extended-trust-manager.html) | `var x509ExtendedTrustManager: `[`X509ExtendedTrustManager`](https://docs.oracle.com/javase/6/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)`?` |

