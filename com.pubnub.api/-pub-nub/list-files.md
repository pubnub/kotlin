[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](index.md) / [listFiles](./list-files.md)

# listFiles

`fun listFiles(channel: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, next: PNNext? = null): `[`ListFiles`](../../com.pubnub.api.endpoints.files/-list-files/index.md)

Retrieve list of files uploaded to Channel.

### Parameters

`channel` - Channel name

`limit` - Number of files to return. Minimum value is 1, and maximum is 100. Default value is 100.

`next` - Previously-returned cursor bookmark for fetching the next page. @see [PNPage.PNNext](../../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)