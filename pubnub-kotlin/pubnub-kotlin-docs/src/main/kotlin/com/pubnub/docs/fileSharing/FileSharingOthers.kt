package com.pubnub.docs.fileSharing

import com.pubnub.api.PubNub

class FileSharingOthers {
    private fun listFilesBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/files#basic-usage-1

        // snippet.listFiles
        pubnub.listFiles(
            channel = "my_channel"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun getFileUrlBasicUsage(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/files#basic-usage-2

        // snippet.getFileUrlBasicUsage
        pubnub.getFileUrl(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "someFileId"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun downloadFileBasicUsage(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/files#basic-usage-3

        // snippet.downloadFileBasicUsage
        pubnub.downloadFile(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "d9515cb7-48a7-41a4-9284-f4bf331bc770"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun deleteFileBasicUsage(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/files#basic-usage-4

        // snippet.deleteFileBasicUsage
        pubnub.deleteFile(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "someFileId"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun publishFileMessageBasicUsage(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/files#basic-usage-5

        // snippet.publishFileMessageBasicUsage
        pubnub.publishFileMessage(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "d9515cb7-48a7-41a4-9284-f4bf331bc770",
            message = "This is a sample message",
            customMessageType = "file-message"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }
}
