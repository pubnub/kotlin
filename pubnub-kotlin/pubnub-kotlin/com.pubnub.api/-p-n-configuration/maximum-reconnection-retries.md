//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[maximumReconnectionRetries](maximum-reconnection-retries.md)

# maximumReconnectionRetries

[jvm]\
var [~~maximumReconnectionRetries~~](maximum-reconnection-retries.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

---

### Deprecated

Instead of reconnectionPolicy and maximumReconnectionRetries use retryConfiguration 
            e.g. config.retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 5) 
            or config.retryConfiguration = RetryConfiguration.Exponential(minDelayInSec = 3, maxDelayInSec = 10, maxRetryNumber = 5)

---

Sets how many times to retry to reconnect before giving up. Must be used in combination with [reconnectionPolicy](reconnection-policy.md).

The default value is `-1` which means unlimited retries.
