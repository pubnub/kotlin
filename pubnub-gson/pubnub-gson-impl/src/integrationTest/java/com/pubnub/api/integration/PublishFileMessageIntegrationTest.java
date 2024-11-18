package com.pubnub.api.integration;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
