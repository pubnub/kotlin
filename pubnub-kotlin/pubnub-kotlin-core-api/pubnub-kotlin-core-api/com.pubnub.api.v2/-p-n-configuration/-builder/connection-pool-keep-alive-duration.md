//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[connectionPoolKeepAliveDuration](connection-pool-keep-alive-duration.md)

# connectionPoolKeepAliveDuration

[jvm]\
abstract var [connectionPoolKeepAliveDuration](connection-pool-keep-alive-duration.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)

How long to keep idle connections alive in the OkHttp connection pool before evicting them.

The value is in seconds and must be at least 1 (OkHttp requirement). Values less than 1 will be coerced to 1.

Note: This setting affects only OkHttp's *idle* connection pool behavior. It does not control TCP keep-alive intervals used by the OS, the network, or PubNub servers, and it does not affect connections that are actively in use (e.g., long-poll subscribe requests).

Defaults to 300 seconds (5 minutes, OkHttp default).

#### See also

| |
|---|
| ConnectionPool |
