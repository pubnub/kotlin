//[pubnub-kotlin](../../index.md)/[com.pubnub.api.models.consumer.files](index.md)

# Package com.pubnub.api.models.consumer.files

## Types

| Name | Summary |
|---|---|
| [PNBaseFile](-p-n-base-file/index.md) | [jvm]<br>data class [PNBaseFile](-p-n-base-file/index.md)(val id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PNFile](-p-n-file/index.md) |
| [PNDeleteFileResult](-p-n-delete-file-result/index.md) | [jvm]<br>data class [PNDeleteFileResult](-p-n-delete-file-result/index.md)(val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [PNDownloadableFile](-p-n-downloadable-file/index.md) | [jvm]<br>data class [PNDownloadableFile](-p-n-downloadable-file/index.md)(val id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PNFile](-p-n-file/index.md) |
| [PNDownloadFileResult](-p-n-download-file-result/index.md) | [jvm]<br>data class [PNDownloadFileResult](-p-n-download-file-result/index.md)(val fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val byteStream: [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html)?) |
| [PNFile](-p-n-file/index.md) | [jvm]<br>interface [PNFile](-p-n-file/index.md) |
| [PNFileUploadResult](-p-n-file-upload-result/index.md) | [jvm]<br>data class [PNFileUploadResult](-p-n-file-upload-result/index.md)(val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val file: [PNFile](-p-n-file/index.md)) |
| [PNFileUrlResult](-p-n-file-url-result/index.md) | [jvm]<br>data class [PNFileUrlResult](-p-n-file-url-result/index.md)(val url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [PNListFilesResult](-p-n-list-files-result/index.md) | [jvm]<br>data class [PNListFilesResult](-p-n-list-files-result/index.md)(val count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val next: [PNPage.PNNext](../com.pubnub.api.models.consumer.objects/-p-n-page/-p-n-next/index.md)?, val status: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val data: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[PNUploadedFile](-p-n-uploaded-file/index.md)&gt;) |
| [PNPublishFileMessageResult](-p-n-publish-file-message-result/index.md) | [jvm]<br>data class [PNPublishFileMessageResult](-p-n-publish-file-message-result/index.md)(val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |
| [PNUploadedFile](-p-n-uploaded-file/index.md) | [jvm]<br>data class [PNUploadedFile](-p-n-uploaded-file/index.md)(val id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val created: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PNFile](-p-n-file/index.md) |
