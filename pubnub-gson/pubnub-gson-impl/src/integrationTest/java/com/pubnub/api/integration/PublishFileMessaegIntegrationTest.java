package com.pubnub.api.integration;

import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PublishFileMessaegIntegrationTest extends BaseIntegrationTest {

    @Test
    public void can_publishFileMessage() {
        PNPublishFileMessageResult publishFileMessageResult = pubNub.publishFileMessage()
                .channel("whatever")
                .fileName("whatever")
                .fileId("whatever")
                .message("whatever")
                .customMessageType("myCustom").
                sync();

        assertNotNull(publishFileMessageResult.getTimetoken());
    }
}
