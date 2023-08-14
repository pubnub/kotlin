//[pubnub-kotlin](../../index.md)/[com.pubnub.api.models.consumer.access_manager.v3](index.md)

# Package com.pubnub.api.models.consumer.access_manager.v3

## Types

| Name | Summary |
|---|---|
| [ChannelGrant](-channel-grant/index.md) | [jvm]<br>interface [ChannelGrant](-channel-grant/index.md) : [PNGrant](-p-n-grant/index.md) |
| [ChannelGroupGrant](-channel-group-grant/index.md) | [jvm]<br>interface [ChannelGroupGrant](-channel-group-grant/index.md) : [PNGrant](-p-n-grant/index.md) |
| [PNGrant](-p-n-grant/index.md) | [jvm]<br>interface [PNGrant](-p-n-grant/index.md) |
| [PNGrantTokenResult](-p-n-grant-token-result/index.md) | [jvm]<br>data class [PNGrantTokenResult](-p-n-grant-token-result/index.md)(val token: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [PNToken](-p-n-token/index.md) | [jvm]<br>data class [PNToken](-p-n-token/index.md)(val version: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val timestamp: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, val ttl: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0, val authorizedUUID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val resources: [PNToken.PNTokenResources](-p-n-token/-p-n-token-resources/index.md), val patterns: [PNToken.PNTokenResources](-p-n-token/-p-n-token-resources/index.md), val meta: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null) |
| [UUIDGrant](-u-u-i-d-grant/index.md) | [jvm]<br>interface [UUIDGrant](-u-u-i-d-grant/index.md) : [PNGrant](-p-n-grant/index.md) |
