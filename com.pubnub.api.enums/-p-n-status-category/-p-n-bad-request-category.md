[pubnub-kotlin](../../index.md) / [com.pubnub.api.enums](../index.md) / [PNStatusCategory](index.md) / [PNBadRequestCategory](./-p-n-bad-request-category.md)

# PNBadRequestCategory

`PNBadRequestCategory`

PubNub API server was unable to parse SDK request correctly.

Request can't be completed because not all required values have been passed or passed values have
unexpected data type.

The SDK will send a `PNBadRequestCategory` when one or more parameters are missing
like message, channel, subscribe key, publish key.

