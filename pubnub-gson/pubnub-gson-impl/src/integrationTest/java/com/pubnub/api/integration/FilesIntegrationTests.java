package com.pubnub.api.integration;

import com.pubnub.api.PubNubException;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.files.PNUploadedFile;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.pubnub.api.integration.util.Utils.randomChannel;

public class FilesIntegrationTests extends BaseIntegrationTest {

    @Test
    public void uploadListDownloadDeleteWithCipher() throws PubNubException, InterruptedException, IOException {
        doItAllFilesTest(true);
    }

    @Test
    public void uploadListDownloadDeleteWithoutCipher() throws PubNubException, InterruptedException, IOException {
        doItAllFilesTest(false);
    }

    @Test
    public void uploadListGetUrlDownloadDelete() throws Exception {
        String channel = randomChannel();
        String fileName = "cat_picture_" + channel + ".jpg";
        String message = "Look at this photo!";
        String customMessageType = "file-message";
        String content = "This is a test image content";

        // Upload file
        PNFileUploadResult uploadResult;
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            uploadResult = pubNub.sendFile()
                    .channel(channel)
                    .fileName(fileName)
                    .inputStream(inputStream)
                    .message(message)
                    .customMessageType(customMessageType)
                    .sync();
        }
        String fileId = uploadResult.getFile().getId();
        Assert.assertNotNull(fileId);

        // List files
        PNListFilesResult listFilesResult = pubNub.listFiles()
                .channel(channel)
                .sync();
        boolean found = false;
        for (PNUploadedFile file : listFilesResult.getData()) {
            if (file.getId().equals(fileId) && file.getName().equals(fileName)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue("Uploaded file should be listed", found);

        // Get file URL
        String fileUrl = pubNub.getFileUrl()
                .channel(channel)
                .fileName(fileName)
                .fileId(fileId)
                .sync().getUrl();
        Assert.assertNotNull(fileUrl);
        Assert.assertTrue(fileUrl.contains(fileId));
        Assert.assertTrue(fileUrl.contains(fileName));
        // Assert that fileId appears before fileName in the URL path
        int fileIdIndex = fileUrl.indexOf(fileId);
        int fileNameIndex = fileUrl.indexOf(fileName);
        Assert.assertTrue(
                "fileId should appear before fileName in the file URL path. URL: " + fileUrl,
                fileIdIndex > 0 && fileNameIndex > 0 && fileIdIndex < fileNameIndex
        );

        // Download file
        PNDownloadFileResult downloadResult = pubNub.downloadFile()
                .channel(channel)
                .fileName(fileName)
                .fileId(fileId)
                .sync();
        try (InputStream is = downloadResult.getByteStream()) {
            Assert.assertEquals(content, readToString(is));
        }

        // Delete file
        pubNub.deleteFile()
                .channel(channel)
                .fileName(fileName)
                .fileId(fileId)
                .sync();
    }

    public void doItAllFilesTest(boolean withCipher) throws PubNubException, InterruptedException, IOException {
        if (withCipher) {
            pubNub = getPubNub(builder -> builder.cryptoModule(CryptoModule.createLegacyCryptoModule("enigma", true)));
        }
        String channel = randomChannel();
        String content = "This is content";
        String message = "This is message";
        String customMessageType = "myType01-_";
        String meta = "This is meta";
        String fileName = "fileName" + channel + ".txt";
        CountDownLatch connectedLatch = new CountDownLatch(1);
        CountDownLatch fileEventReceived = new CountDownLatch(1);

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {
                if (pnStatus.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    connectedLatch.countDown();
                }
            }

            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {
                if (pnFileEventResult.getFile().getName().equals(fileName) && pnFileEventResult.getCustomMessageType().equals(customMessageType)) {
                    fileEventReceived.countDown();
                }
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(channel))
                .execute();

        PNFileUploadResult sendResult;
        connectedLatch.await(10, TimeUnit.SECONDS);
        try (InputStream is = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            sendResult = pubNub.sendFile()
                    .channel(channel)
                    .fileName(fileName)
                    .inputStream(is)
                    .message(message)
                    .customMessageType(customMessageType)
                    .meta(meta)
                    .sync();
        }


        fileEventReceived.await(10, TimeUnit.SECONDS);
        PNListFilesResult listedFiles = pubNub.listFiles()
                .channel(channel)
                .sync();

        boolean fileFoundOnList = false;
        for (PNUploadedFile f : listedFiles.getData()) {
            if (f.getId().equals(sendResult.getFile().getId())) {
                fileFoundOnList = true;
                break;
            }
        }
        Assert.assertTrue(fileFoundOnList);

        pause(2);

        PNDownloadFileResult downloadResult = pubNub
                .downloadFile()
                .channel(channel)
                .fileName(fileName)
                .fileId(sendResult.getFile().getId())
                .sync();

        try (InputStream is = downloadResult.getByteStream()) {
            Assert.assertEquals(content, readToString(is));
        }

        pubNub.deleteFile()
                .channel(channel)
                .fileName(fileName)
                .fileId(sendResult.getFile().getId())
                .sync();
    }

    private String readToString(InputStream inputStream) {
        try (Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
