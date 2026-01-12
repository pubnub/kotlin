//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[connectionPoolMaxIdleConnections](connection-pool-max-idle-connections.md)

# connectionPoolMaxIdleConnections

[jvm]\
abstract fun [connectionPoolMaxIdleConnections](connection-pool-max-idle-connections.md)(connectionPoolMaxIdleConnections: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [PNConfiguration.Builder](index.md)

Maximum number of idle connections to keep in the OkHttp connection pool.

When set to 0, connection pooling is disabled and connections are closed immediately after use. This is recommended for mobile applications to minimize battery drain from TCP keep-alive probes.

Note: This setting affects only OkHttp's *idle* connection pool behavior. It does not control TCP keep-alive intervals used by the OS, the network, or PubNub servers, and it does not affect connections that are actively in use (e.g., long-poll subscribe requests).

The value must be non-negative.

Defaults to 5 (OkHttp default).

#### See also

| |
|---|
| ConnectionPool |

[jvm]\
abstract val [connectionPoolMaxIdleConnections](connection-pool-max-idle-connections.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)

Maximum number of idle connections to keep in the OkHttp connection pool.

When set to 0, connection pooling is disabled and connections are closed immediately after use.

Note: This setting affects only OkHttp's *idle* connection pool behavior. It does not control TCP keep-alive intervals used by the OS, the network, or PubNub servers, and it does not affect connections that are actively in use (e.g., long-poll subscribe requests).

The value must be non-negative.

Defaults to 5 (OkHttp default).

#### See also

| |
|---|
| ConnectionPool |
