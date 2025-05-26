package com.pubnub.docs.fileSharing;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.files.PNUploadedFile;
import com.pubnub.docs.SnippetBase;

public class FileOthers extends SnippetBase {
    private void listFilesBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/files#basic-usage-1

        PubNub pubNub = createPubNub();

        // snippet.listFilesBasic
        pubNub.listFiles()
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess((PNListFilesResult res) -> {
                        System.out.println("files status: " + res.getStatus());
                        System.out.println("files count: " + res.getCount());
                        for (PNUploadedFile file : res.getData()) {
                            System.out.println("files fileId: " + file.getId());
                            System.out.println("files fileName: " + file.getName());
                            System.out.println("files fileSize: " + file.getSize());
                            System.out.println("files fileCreated: " + file.getCreated());
                        }
                    });
                });
        // snippet.end
    }

    private void getFileUrlBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/files#basic-usage-2

        PubNub pubNub = createPubNub();

        // snippet.getFileUrlBasic
        PNFileUrlResult pnFileUrlResult = pubNub.getFileUrl()
                .channel("my_channel")
                .fileName("cat_picture.jpg")
                .fileId("d9515cb7-48a7-41a4-9284-f4bf331bc770")
                .sync();

        System.out.println("FileUrl: " + pnFileUrlResult.getUrl());
        // snippet.end
    }

    private void downloadFileBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/files#basic-usage-3

        PubNub pubNub = createPubNub();

        // snippet.downloadFileBasic
        pubNub.downloadFile()
                .channel("my_channel")
                .fileName("cat_picture.jpg")
                .fileId("d9515cb7-48a7-41a4-9284-f4bf331bc770")
                .async(result -> {
                    result.onSuccess(res -> {
                        System.out.println("downloadFile fileName: " + res.getFileName());
                        System.out.println("downloadFile byteStream: " + res.getByteStream());
                    });
                });
        // snippet.end
    }

    private void deleteFileBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/files#basic-usage-4

        PubNub pubNub = createPubNub();

        // snippet.deleteFileBasic
        pubNub.deleteFile()
                .channel("my_channel")
                .fileName("cat_picture.jpg")
                .fileId("d9515cb7-48a7-41a4-9284-f4bf331bc770")
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void publishFileMessageBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/files#publish-file-message

        PubNub pubNub = createPubNub();

        // snippet.publishFileMessageBasic
        pubnub.publishFileMessage()
                .channel("my_channel")
                .fileName("cat_picture.jpg")
                .fileId("d9515cb7-48a7-41a4-9284-f4bf331bc770")
                .message("This is a sample message")
                .customMessageType("file-message")
                .async(result -> {
                    result.onSuccess(res -> {
                        System.out.println("send timetoken: " + res.getTimetoken());
                    });
                });
        // snippet.end
    }
}
