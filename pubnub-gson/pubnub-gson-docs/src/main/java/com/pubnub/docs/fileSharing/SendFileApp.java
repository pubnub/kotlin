package com.pubnub.docs.fileSharing;
// https://www.pubnub.com/docs/sdks/java/api-reference/files#basic-usage

// snippet.sendFileApp

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SendFileApp {
    public static void main(String[] args) throws PubNubException, InterruptedException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        String content = "This is a test image content";
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            // Input stream for the file to be sent. Replace with SendFileApp.class.getClassLoader().getResourceAsStream("your_picture.jpg")) {

            if (inputStream == null) {
                System.err.println("Error: cat_picture.jpg not found in resources.");
                return; // Exit if the resource is not found
            }

            String myChannel = "my_channel";
            String fileName = "your_picture.jpg";
            PNFileUploadResult pnFileUploadResult = pubnub.sendFile()
                    .channel(myChannel)
                    .fileName(fileName)
                    .inputStream(inputStream)
                    .message("Look at this photo!")
                    .customMessageType("file-message")
                    .sync();
            System.out.println("send timetoken: " + pnFileUploadResult.getTimetoken());
            System.out.println("send status: " + pnFileUploadResult.getStatus());
            System.out.println("send fileId: " + pnFileUploadResult.getFile().getId());
            System.out.println("send fileName: " + pnFileUploadResult.getFile().getName());

            PNFileUrlResult pnFileUrlResult = pubnub.getFileUrl()
                    .channel(myChannel)
                    .fileName(fileName)
                    .fileId(pnFileUploadResult.getFile().getId())
                    .sync();

            System.out.println("File URL: " + pnFileUrlResult.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(3000);
        System.exit(0);
    }
}
// snippet.end
