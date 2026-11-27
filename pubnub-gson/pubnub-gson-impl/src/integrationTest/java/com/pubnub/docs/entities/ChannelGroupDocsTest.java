package com.pubnub.docs.entities;

import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Stdout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page entities/channel-group (docs/sdks/java/entities/channel-group.md). One test per snippet.
 * The text between a start marker and {@code snippet.end} is the published sample, the assertions are below the call. The
 * sample only builds the entity and prints its name, it sends no request.
 */
@ExtendWith(LiveExtension.class)
class ChannelGroupDocsTest {
    @Test
    void channelGroupApp(LiveClients clients) throws Exception {
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.entities.samples.ChannelGroupApp.main(new String[0]);

        out.assertPrinted("Channel Group created: myGroup");
    }
}
