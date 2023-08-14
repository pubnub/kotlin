//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.server.files](../index.md)/[GeneratedUploadUrlResponse](index.md)

# GeneratedUploadUrlResponse

[jvm]\
data class [GeneratedUploadUrlResponse](index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [PNUploadedFile](../../com.pubnub.api.models.consumer.files/-p-n-uploaded-file/index.md), val fileUploadRequest: [GeneratedUploadUrlResponse.FileUploadRequest](-file-upload-request/index.md))

## Constructors

| | |
|---|---|
| [GeneratedUploadUrlResponse](-generated-upload-url-response.md) | [jvm]<br>fun [GeneratedUploadUrlResponse](-generated-upload-url-response.md)(status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), data: [PNUploadedFile](../../com.pubnub.api.models.consumer.files/-p-n-uploaded-file/index.md), fileUploadRequest: [GeneratedUploadUrlResponse.FileUploadRequest](-file-upload-request/index.md)) |

## Types

| Name | Summary |
|---|---|
| [FileUploadRequest](-file-upload-request/index.md) | [jvm]<br>data class [FileUploadRequest](-file-upload-request/index.md)(val url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val method: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val expirationDate: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val formFields: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[FormField](../-form-field/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [data](data.md) | [jvm]<br>val [data](data.md): [PNUploadedFile](../../com.pubnub.api.models.consumer.files/-p-n-uploaded-file/index.md) |
| [fileUploadRequest](file-upload-request.md) | [jvm]<br>@SerializedName(value = &quot;file_upload_request&quot;)<br>val [fileUploadRequest](file-upload-request.md): [GeneratedUploadUrlResponse.FileUploadRequest](-file-upload-request/index.md) |
| [status](status.md) | [jvm]<br>val [status](status.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
