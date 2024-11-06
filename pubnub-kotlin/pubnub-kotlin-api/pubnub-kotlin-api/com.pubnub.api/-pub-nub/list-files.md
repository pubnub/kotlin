//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[listFiles](list-files.md)

# listFiles

[common]\
expect abstract fun [listFiles](list-files.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null, next: [PNPage.PNNext](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)? = null): [ListFiles](../../com.pubnub.api.endpoints.files/-list-files/index.md)actual abstract fun [listFiles](list-files.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, next: [PNPage.PNNext](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)?): [ListFiles](../../com.pubnub.api.endpoints.files/-list-files/index.md)

[jvm]\
actual abstract fun [listFiles](list-files.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, next: [PNPage.PNNext](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)?): [ListFiles](../../com.pubnub.api.endpoints.files/-list-files/index.md)

Retrieve list of files uploaded to Channel.

#### Parameters

jvm

| | |
|---|---|
| channel | Channel name |
| limit | Number of files to return. Minimum value is 1, and maximum is 100. Default value is 100. |
| next | Previously-returned cursor bookmark for fetching the next page. @see [PNPage.PNNext](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md) |
