//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[reconnectionPolicy](reconnection-policy.md)

# reconnectionPolicy

[jvm]\
var [~~reconnectionPolicy~~](reconnection-policy.md): [PNReconnectionPolicy](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/index.md)

---

### Deprecated

Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration 
            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) 
            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)

---

Set to [PNReconnectionPolicy.LINEAR](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/-l-i-n-e-a-r/index.md) for automatic reconnects.

Use [PNReconnectionPolicy.NONE](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/-n-o-n-e/index.md) to disable automatic reconnects.

Use [PNReconnectionPolicy.EXPONENTIAL](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/-e-x-p-o-n-e-n-t-i-a-l/index.md) to set exponential retry interval.

Defaults to [PNReconnectionPolicy.NONE](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/-n-o-n-e/index.md).
