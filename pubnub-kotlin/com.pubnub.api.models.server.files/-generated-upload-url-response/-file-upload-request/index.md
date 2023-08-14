//[pubnub-kotlin](../../../../index.md)/[com.pubnub.api.models.server.files](../../index.md)/[GeneratedUploadUrlResponse](../index.md)/[FileUploadRequest](index.md)

# FileUploadRequest

[jvm]\
data class [FileUploadRequest](index.md)(val url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val method: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val expirationDate: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val formFields: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[FormField](../../-form-field/index.md)&gt;)

## Constructors

| | |
|---|---|
| [FileUploadRequest](-file-upload-request.md) | [jvm]<br>fun [FileUploadRequest](-file-upload-request.md)(url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), method: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), expirationDate: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), formFields: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[FormField](../../-form-field/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [expirationDate](expiration-date.md) | [jvm]<br>@SerializedName(value = &quot;expiration_date&quot;)<br>val [expirationDate](expiration-date.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [formFields](form-fields.md) | [jvm]<br>@SerializedName(value = &quot;form_fields&quot;)<br>val [formFields](form-fields.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[FormField](../../-form-field/index.md)&gt; |
| [method](method.md) | [jvm]<br>val [method](method.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [url](url.md) | [jvm]<br>val [url](url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
