//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.logging](../../index.md)/[LogMessageContent](../index.md)/[Error](index.md)

# Error

[jvm]\
data class [Error](index.md)(val message: [ErrorDetails](../../-error-details/index.md)) : [LogMessageContent](../index.md)

Error message with type, message, and stack trace.

## Constructors

| | |
|---|---|
| [Error](-error.md) | [jvm]<br>constructor(message: [ErrorDetails](../../-error-details/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [message](message.md) | [jvm]<br>@SerializedName(value = &quot;message&quot;)<br>val [message](message.md): [ErrorDetails](../../-error-details/index.md) |
