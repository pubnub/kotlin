//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[reconnectionPolicy](reconnection-policy.md)

# reconnectionPolicy

[jvm]\
var [reconnectionPolicy](reconnection-policy.md): [PNReconnectionPolicy](../../com.pubnub.api.enums/-p-n-reconnection-policy/index.md)

Set to [PNReconnectionPolicy.LINEAR](../../com.pubnub.api.enums/-p-n-reconnection-policy/-l-i-n-e-a-r/index.md) for automatic reconnects.

Use [PNReconnectionPolicy.NONE](../../com.pubnub.api.enums/-p-n-reconnection-policy/-n-o-n-e/index.md) to disable automatic reconnects.

Use [PNReconnectionPolicy.EXPONENTIAL](../../com.pubnub.api.enums/-p-n-reconnection-policy/-e-x-p-o-n-e-n-t-i-a-l/index.md) to set exponential retry interval.

Defaults to [PNReconnectionPolicy.NONE](../../com.pubnub.api.enums/-p-n-reconnection-policy/-n-o-n-e/index.md).
