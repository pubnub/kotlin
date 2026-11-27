package com.pubnub.docs.fileSharing;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.files.PNBaseFile;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;
import com.pubnub.api.models.consumer.files.PNUploadedFile;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.docs.support.AppContextKit;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.MessagingKit;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Redact;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.io.InputStream;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page files (docs/sdks/java/api-reference/files.md). One test per snippet. The text between a
 * start marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions below it. The
 * samples name a file by a placeholder id ({@code d9515cb7-...}), the recorder swaps it for the id of the file that the test
 * uploaded. The sample {@code sendFileApp} ends with {@code System.exit(0)}, so it runs in a JVM of its own.
 */
@ExtendWith(LiveExtension.class)
class FilesDocsTest {
    private static final String OPEN = "open";
    private static final String DOCS_FILE_ID = "d9515cb7-48a7-41a4-9284-f4bf331bc770";

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    /** Uploads the fixture as cat_picture.jpg to my_channel and waits until it is listed. */
    private static PNBaseFile uploaded(com.pubnub.api.PubNub owner, Recorder recorder) {
        PNBaseFile file = MessagingKit.upload(owner, "my_channel", "cat_picture.jpg");
        Cleanup.record("history", "my_channel");
        MessagingKit.awaitListed(owner, "my_channel", file.getId());
        recorder.placeholder(DOCS_FILE_ID, file.getId());
        return file;
    }

    @Test
    void deleteFileBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub owner = kotlinClient(clients, "docs-receiver");
        PNBaseFile file = uploaded(owner, recorder);

        // snippet.deleteFileBasic
        pubNub.deleteFile()
                .channel("my_channel")
                .fileName("cat_picture.jpg")
                .fileId("d9515cb7-48a7-41a4-9284-f4bf331bc770")
                .async(result -> { /* check result */ });
        // snippet.end

        recorder.awaitOk("deleteFile");
        Wait.settledRun("the deleted file is no longer listed", () ->
                assertTrue(!MessagingKit.fileIds(owner, "my_channel").contains(file.getId()), "the file is still listed after deleteFile"));
    }

    @Test
    void downloadFileBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub owner = kotlinClient(clients, "docs-receiver");
        uploaded(owner, recorder);
        Stdout out = Stdout.captureForTest();

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

        PNDownloadFileResult downloaded = (PNDownloadFileResult) recorder.awaitOk("downloadFile");
        assertEquals("cat_picture.jpg", downloaded.getFileName());
        assertArrayEquals(MessagingKit.fixtureBytes(), MessagingKit.readAll(downloaded.getByteStream()), "the downloaded bytes differ from the uploaded fixture");
        Wait.eventuallyRun("the sample printed the file name", () -> out.assertPrinted("downloadFile fileName: cat_picture.jpg"));
    }

    @Test
    void getFileUrlBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub owner = kotlinClient(clients, "docs-receiver");
        PNBaseFile file = uploaded(owner, recorder);
        Stdout out = Stdout.captureForTest();

        // snippet.getFileUrlBasic
        PNFileUrlResult pnFileUrlResult = pubNub.getFileUrl()
                .channel("my_channel")
                .fileName("cat_picture.jpg")
                .fileId("d9515cb7-48a7-41a4-9284-f4bf331bc770")
                .sync();

        System.out.println("FileUrl: " + pnFileUrlResult.getUrl());
        // snippet.end

        String url = pnFileUrlResult.getUrl();
        assertTrue(url.contains("/my_channel/") && url.contains(file.getId()) && url.contains("cat_picture.jpg"), "the url does not name the channel, the id and the file name");
        out.assertPrinted("FileUrl: " + url);
        MessagingKit.assertFixtureAt(url);
    }

    @Test
    void listFilesBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub owner = kotlinClient(clients, "docs-receiver");
        PNBaseFile stored = uploaded(owner, recorder);
        Stdout out = Stdout.captureForTest();

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

        PNListFilesResult listed = (PNListFilesResult) recorder.awaitOk("listFiles");
        PNUploadedFile entry = null;
        for (PNUploadedFile candidate : listed.getData()) {
            if (candidate.getId().equals(stored.getId())) {
                entry = candidate;
            }
        }
        assertTrue(entry != null, "the uploaded file is not listed");
        assertEquals("cat_picture.jpg", entry.getName());
        assertEquals(MessagingKit.fixtureBytes().length, entry.getSize());
        assertEquals(200, listed.getStatus());
        Wait.eventuallyRun("the sample printed the file", () -> {
            out.assertPrinted("files status: 200");
            out.assertPrinted("files fileId: " + stored.getId());
            out.assertPrinted("files fileName: cat_picture.jpg");
        });
    }

    @Test
    void sendFileApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub owner = kotlinClient(clients, "docs-receiver");
        // An earlier run may have left a file of the same name. Remove it, so that the file read back is the one of this run.
        for (PNUploadedFile old : owner.listFiles("my_channel", null, null).sync().getData()) {
            if ("your_picture.jpg".equals(old.getName())) {
                owner.deleteFile("my_channel", old.getName(), old.getId()).sync();
            }
        }
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-joiner");
        Wait.Expectation arrived = MessagingKit.armFile(receiver, "my_channel", "Look at this photo!");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Cleanup.record("history", "my_channel");

        // The sample ends with System.exit(0), so it runs in a JVM of its own. Its text cannot change.
        AppContextKit.ChildResult result = AppContextKit.runInChildJvm("com.pubnub.docs.fileSharing.samples.SendFileApp");
        String output = Redact.scrub(result.getOutput());
        String shown = output.substring(0, Math.min(1500, output.length()));

        assertEquals(0, result.getExitCode(), "the sample did not exit with 0. Output: " + shown);
        // The sample catches every exception and prints the stack trace, so the exit code alone proves nothing.
        assertTrue(output.contains("send fileId: "), "the sample did not send the file. Output: " + shown);
        PNFileEventResult event = (PNFileEventResult) arrived.await();
        assertEquals("your_picture.jpg", event.getFile().getName());
        assertEquals("demoUserId", event.getPublisher());
        assertEquals("file-message", event.getCustomMessageType());
        assertEquals("Look at this photo!", event.getJsonMessage().getAsString());
        String fileId = event.getFile().getId();
        Kit.cleanup("delete file your_picture.jpg of my_channel", () -> owner.deleteFile("my_channel", "your_picture.jpg", fileId).sync());
        assertTrue(output.contains("send timetoken: " + event.getTimetoken()), "the sample did not print the timetoken of the file message");
        assertTrue(output.contains("send status: 200"), "the sample did not print the status 200. Output: " + shown);
        assertTrue(output.contains("send fileId: " + fileId), "the sample did not print the id of the sent file");
        assertTrue(output.contains("send fileName: your_picture.jpg"), "the sample did not print the file name");
        MessagingKit.awaitListed(owner, "my_channel", fileId);
        String url = owner.getFileUrl("my_channel", "your_picture.jpg", fileId).sync().getUrl();
        assertTrue(output.contains("File URL: ") && url.contains(fileId), "the sample did not print the file url");
        kotlin.Pair<Integer, byte[]> download = MessagingKit.get(url);
        assertEquals(200, download.getFirst(), "the file could not be read back");
        assertArrayEquals("This is a test image content".getBytes(java.nio.charset.StandardCharsets.UTF_8), download.getSecond(), "the stored bytes differ from the content the sample sent");
        Kit.storedItem(receiver, "my_channel", event.getTimetoken());
    }

    @Test
    void publishFileMessageBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub owner = kotlinClient(clients, "docs-receiver");
        PNBaseFile file = uploaded(owner, recorder);
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-joiner");
        Wait.Expectation arrived = MessagingKit.armFile(receiver, "my_channel", "This is a sample message");
        Kit.subscribeAndConnect(receiver, Collections.singletonList("my_channel"));
        Stdout out = Stdout.captureForTest();

        // snippet.publishFileMessageBasic
        pubNub.publishFileMessage()
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

        PNPublishFileMessageResult published = (PNPublishFileMessageResult) recorder.awaitOk("publishFileMessage");
        PNFileEventResult event = (PNFileEventResult) arrived.await();
        assertEquals(published.getTimetoken(), event.getTimetoken());
        assertEquals(file.getId(), event.getFile().getId());
        assertEquals("cat_picture.jpg", event.getFile().getName());
        assertEquals("yourUserId", event.getPublisher());
        assertEquals("file-message", event.getCustomMessageType());
        assertEquals("This is a sample message", event.getJsonMessage().getAsString());
        Wait.eventuallyRun("the sample printed the timetoken", () -> out.assertPrinted("send timetoken: " + published.getTimetoken()));
        Kit.storedItem(receiver, "my_channel", published.getTimetoken());
    }
}
