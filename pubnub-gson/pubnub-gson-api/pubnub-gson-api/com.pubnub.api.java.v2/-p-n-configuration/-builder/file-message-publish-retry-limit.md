//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[fileMessagePublishRetryLimit](file-message-publish-retry-limit.md)

# fileMessagePublishRetryLimit

[jvm]\
abstract fun [~~fileMessagePublishRetryLimit~~](file-message-publish-retry-limit.md)(fileMessagePublishRetryLimit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [PNConfiguration.Builder](index.md)

---

### Deprecated

This setting is deprecated. Use retryConfiguration instead e.g. retryConfiguration = RetryConfiguration.Linear()

---

How many times publishing file message should automatically retry before marking the action as failed

Defaults to `5`

[jvm]\
abstract val [~~fileMessagePublishRetryLimit~~](file-message-publish-retry-limit.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)

---

### Deprecated

This setting is deprecated. Use retryConfiguration instead

---

How many times publishing file message should automatically retry before marking the action as failed

Defaults to `5`
