package com.pubnub.api.integration;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import static com.pubnub.api.integration.util.Utils.randomChannel;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublishFileMessageIntegrationTest extends BaseIntegrationTest {

    @Test
    public void can_publishFileMessage() throws PubNubException {
        PNPublishFileMessageResult publishFileMessageResult = pubNub.publishFileMessage()
                .channel("whatever")
                .fileName("whatever")
                .fileId("whatever")
                .message("whatever")
                .customMessageType("my-Custom")
                .sync();

        assertNotNull(publishFileMessageResult.getTimetoken());
    }

    @Test
    public void publishFileMessage_endToEnd() throws Exception {
        String channelName = randomChannel();
        String fileName = "fileName_" + channelName + ".txt";
        String messageSendFile = "This is a file message";
        String publishFileMessage = "This is a publishFileMessage";
        String customMessageType = "file-message";
        String content = "This is the file content";
        final CountDownLatch receivedFileEventFromSendFile = new CountDownLatch(1);
        final CountDownLatch receivedFileEventFromPublishFileMessage = new CountDownLatch(1);


        Channel channel = pubNub.channel(channelName);
        Subscription subscription = channel.subscription();
        subscription.setOnFile(pnFileEventResult -> {
            if (pnFileEventResult.getMessage().equals(messageSendFile)) {
                assertEquals(fileName, pnFileEventResult.getFile().getName());
                receivedFileEventFromSendFile.countDown();
            }
            if (pnFileEventResult.getMessage().equals(publishFileMessage)) {
                assertEquals(fileName, pnFileEventResult.getFile().getName());
                receivedFileEventFromPublishFileMessage.countDown();
            }
        });
        subscription.subscribe();
        Thread.sleep(1000);

        // 1. Upload a file using sendFile to get a real fileId and fileName
        PNFileUploadResult uploadResult;
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(UTF_8))) {
            uploadResult = pubNub.sendFile()
                    .channel(channelName)
                    .fileName(fileName)
                    .inputStream(inputStream)
                    .message(messageSendFile)
                    .customMessageType(customMessageType)
                    .sync();
        }
        String fileId = uploadResult.getFile().getId();
        assertNotNull(fileId);

        // 2. Use those values in publishFileMessage
        PNPublishFileMessageResult publishFileMessageResult = pubNub.publishFileMessage()
                .channel(channelName)
                .fileName(fileName)
                .fileId(fileId)
                .message(publishFileMessage)
                .customMessageType(customMessageType)
                .sync();
        assertNotNull(publishFileMessageResult.getTimetoken());


        try {
            assertTrue(receivedFileEventFromSendFile.await(10, SECONDS));
            assertTrue(receivedFileEventFromPublishFileMessage.await(10, SECONDS));
        } finally {
            // Cleanup: delete the uploaded file
            pubNub.deleteFile().channel(channelName).fileName(fileName).fileId(fileId).sync();
        }
    }
}
