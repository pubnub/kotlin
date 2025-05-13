//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2](../index.md)/[PNConfiguration](index.md)

# PNConfiguration

[jvm]\
interface [PNConfiguration](index.md) : [PNConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>interface [Builder](-builder/index.md) : [PNConfigurationOverride.Builder](../-p-n-configuration-override/-builder/index.md) |
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [authKey](index.md#-77970311%2FProperties%2F126356644) | [jvm]<br>abstract val [~~authKey~~](index.md#-77970311%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [authToken](index.md#-2058033313%2FProperties%2F126356644) | [jvm]<br>abstract val [authToken](index.md#-2058033313%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [cacheBusting](index.md#-264887010%2FProperties%2F126356644) | [jvm]<br>abstract val [cacheBusting](index.md#-264887010%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [certificatePinner](index.md#731803315%2FProperties%2F126356644) | [jvm]<br>abstract val [certificatePinner](index.md#731803315%2FProperties%2F126356644): CertificatePinner? |
| [connectionSpec](index.md#-755649967%2FProperties%2F126356644) | [jvm]<br>abstract val [connectionSpec](index.md#-755649967%2FProperties%2F126356644): ConnectionSpec? |
| [connectTimeout](index.md#-735333709%2FProperties%2F126356644) | [jvm]<br>abstract val [connectTimeout](index.md#-735333709%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [cryptoModule](index.md#-1349644995%2FProperties%2F126356644) | [jvm]<br>abstract val [cryptoModule](index.md#-1349644995%2FProperties%2F126356644): [CryptoModule](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.crypto/-crypto-module/index.md)? |
| [dedupOnSubscribe](index.md#-1599419427%2FProperties%2F126356644) | [jvm]<br>abstract val [dedupOnSubscribe](index.md#-1599419427%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [fileMessagePublishRetryLimit](index.md#-1820492717%2FProperties%2F126356644) | [jvm]<br>abstract val [fileMessagePublishRetryLimit](index.md#-1820492717%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [filterExpression](index.md#2102789754%2FProperties%2F126356644) | [jvm]<br>abstract val [filterExpression](index.md#2102789754%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [googleAppEngineNetworking](index.md#-950733038%2FProperties%2F126356644) | [jvm]<br>abstract val [googleAppEngineNetworking](index.md#-950733038%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [heartbeatInterval](index.md#-1877024337%2FProperties%2F126356644) | [jvm]<br>abstract val [heartbeatInterval](index.md#-1877024337%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [heartbeatNotificationOptions](index.md#767335539%2FProperties%2F126356644) | [jvm]<br>abstract val [heartbeatNotificationOptions](index.md#767335539%2FProperties%2F126356644): [PNHeartbeatNotificationOptions](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md) |
| [hostnameVerifier](index.md#-1452668767%2FProperties%2F126356644) | [jvm]<br>abstract val [hostnameVerifier](index.md#-1452668767%2FProperties%2F126356644): [HostnameVerifier](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html)? |
| [httpLoggingInterceptor](index.md#-864968004%2FProperties%2F126356644) | [jvm]<br>abstract val [httpLoggingInterceptor](index.md#-864968004%2FProperties%2F126356644): HttpLoggingInterceptor? |
| [includeInstanceIdentifier](index.md#2091105866%2FProperties%2F126356644) | [jvm]<br>abstract val [includeInstanceIdentifier](index.md#2091105866%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](index.md#1417436378%2FProperties%2F126356644) | [jvm]<br>abstract val [includeRequestIdentifier](index.md#1417436378%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [logVerbosity](index.md#-1800821405%2FProperties%2F126356644) | [jvm]<br>abstract val [logVerbosity](index.md#-1800821405%2FProperties%2F126356644): [PNLogVerbosity](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md) |
| [maintainPresenceState](index.md#226726085%2FProperties%2F126356644) | [jvm]<br>abstract val [maintainPresenceState](index.md#226726085%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [managePresenceListManually](index.md#647784793%2FProperties%2F126356644) | [jvm]<br>abstract val [managePresenceListManually](index.md#647784793%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [maximumConnections](index.md#-1390173195%2FProperties%2F126356644) | [jvm]<br>abstract val [maximumConnections](index.md#-1390173195%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)? |
| [maximumMessagesCacheSize](index.md#-1859434733%2FProperties%2F126356644) | [jvm]<br>abstract val [maximumMessagesCacheSize](index.md#-1859434733%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [nonSubscribeReadTimeout](index.md#1929713730%2FProperties%2F126356644) | [jvm]<br>abstract val [nonSubscribeReadTimeout](index.md#1929713730%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [nonSubscribeRequestTimeout](index.md#1864792347%2FProperties%2F126356644) | [jvm]<br>open val [~~nonSubscribeRequestTimeout~~](index.md#1864792347%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [origin](index.md#1914998916%2FProperties%2F126356644) | [jvm]<br>abstract val [origin](index.md#1914998916%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [pnsdkSuffixes](index.md#-1282896651%2FProperties%2F126356644) | [jvm]<br>abstract val [pnsdkSuffixes](index.md#-1282896651%2FProperties%2F126356644): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; |
| [presenceTimeout](index.md#50825226%2FProperties%2F126356644) | [jvm]<br>abstract val [presenceTimeout](index.md#50825226%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [proxy](index.md#1211077186%2FProperties%2F126356644) | [jvm]<br>abstract val [proxy](index.md#1211077186%2FProperties%2F126356644): [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)? |
| [proxyAuthenticator](index.md#-1607474331%2FProperties%2F126356644) | [jvm]<br>abstract val [proxyAuthenticator](index.md#-1607474331%2FProperties%2F126356644): Authenticator? |
| [proxySelector](index.md#197693987%2FProperties%2F126356644) | [jvm]<br>abstract val [proxySelector](index.md#197693987%2FProperties%2F126356644): [ProxySelector](https://docs.oracle.com/javase/8/docs/api/java/net/ProxySelector.html)? |
| [publishKey](index.md#-1597294182%2FProperties%2F126356644) | [jvm]<br>abstract val [publishKey](index.md#-1597294182%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [retryConfiguration](index.md#-72226308%2FProperties%2F126356644) | [jvm]<br>abstract val [retryConfiguration](index.md#-72226308%2FProperties%2F126356644): [RetryConfiguration](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.retry/-retry-configuration/index.md) |
| [secretKey](index.md#-1909126975%2FProperties%2F126356644) | [jvm]<br>abstract val [secretKey](index.md#-1909126975%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [secure](index.md#-473452205%2FProperties%2F126356644) | [jvm]<br>abstract val [secure](index.md#-473452205%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [sslSocketFactory](index.md#-338805857%2FProperties%2F126356644) | [jvm]<br>abstract val [sslSocketFactory](index.md#-338805857%2FProperties%2F126356644): [SSLSocketFactory](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocketFactory.html)? |
| [subscribeKey](index.md#2036673557%2FProperties%2F126356644) | [jvm]<br>abstract val [subscribeKey](index.md#2036673557%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [subscribeTimeout](index.md#1092894675%2FProperties%2F126356644) | [jvm]<br>abstract val [subscribeTimeout](index.md#1092894675%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [suppressLeaveEvents](index.md#612754261%2FProperties%2F126356644) | [jvm]<br>abstract val [suppressLeaveEvents](index.md#612754261%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [userId](index.md#1734497636%2FProperties%2F126356644) | [jvm]<br>abstract val [userId](index.md#1734497636%2FProperties%2F126356644): [UserId](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-user-id/index.md) |
| [uuid](index.md#-796748593%2FProperties%2F126356644) | [jvm]<br>open val [~~uuid~~](index.md#-796748593%2FProperties%2F126356644): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [x509ExtendedTrustManager](index.md#1884098934%2FProperties%2F126356644) | [jvm]<br>abstract val [x509ExtendedTrustManager](index.md#1884098934%2FProperties%2F126356644): [X509ExtendedTrustManager](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)? |
