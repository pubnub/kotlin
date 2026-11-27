package com.pubnub.docs.entities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Stdout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live test of the Java docs page entities/user-metadata (docs/sdks/java/entities/user-metadata.md). The one sample is a
 * "Main" sample that creates its own client, so it sits in {@code samples/} and this test calls it and asserts after it. The
 * name of the class keeps it apart from the tests of the other entity pages.
 */
@ExtendWith(LiveExtension.class)
class UserMetadataEntityDocsTest {
    @Test
    void userMetadataApp(LiveClients clients) throws Exception {
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.entities.samples.UserMetadataApp.main(new String[0]);

        // The sample only builds the local entity of the user, it sends no request, so the print is the whole effect.
        out.assertPrinted("User Metadata created for user ID: userId1");
        assertTrue(out.lines("Error").isEmpty(), "the sample printed an error");
    }
}
