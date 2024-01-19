package com.pubnub.api.integration;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.ITTestConfig;
import com.pubnub.api.models.consumer.files.PNDownloadFileResult;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNotNull;

public class PAMFilesIntegrationTests extends BaseIntegrationTest {
    //See README.md in integrationTest directory for more info on running integration tests
    private static ITTestConfig IT_TEST_CONFIG = ConfigFactory.create(ITTestConfig.class, System.getenv());

    private static final String FILENAME = "file.txt";

    private static final String CHANNEL = "chan-" + randomId();
    private static final String CLIENT_UUID = "client-" + randomId();
    private static final String CLIENT_AUTH_KEY = randomId();

    private static final String FILE_CONTENT = "some string";

    @Test
    public void canSendAndDownloadFileWithPAM() throws PubNubException, IOException {
        assumeNotNull(getServer().getConfiguration().getSecretKey());

        final PubNub adminPubnub = getServer();
        final PubNub pubnub = getPubNub();

        try {
            adminPubnub.grant()
                    .authKeys(singletonList(CLIENT_AUTH_KEY))
                    .channels(singletonList(CHANNEL))
                    .read(true)
                    .write(true)
                    .join(false)
                    .manage(false)
                    .get(false)
                    .update(false)
                    .delete(false)
                    .ttl(0)
                    .sync();

            final byte[] fileContentBytes = FILE_CONTENT.getBytes(StandardCharsets.UTF_8);

            final PNFileUploadResult pnFileUploadResult = pubnub.sendFile()
                    .channel(CHANNEL)
                    .fileName(FILENAME)
                    .inputStream(new ByteArrayInputStream(fileContentBytes))
                    .sync();

            final PNDownloadFileResult pnDownloadFileResult = pubnub.downloadFile().channel(CHANNEL)
                    .fileName(pnFileUploadResult.getFile().getName())
                    .fileId(pnFileUploadResult.getFile().getId())
                    .sync();

            final String retrievedContent = IOUtils.toString(pnDownloadFileResult.getByteStream());
            assertEquals(FILE_CONTENT, retrievedContent);
        }
        finally {
            adminPubnub.forceDestroy();
            pubnub.forceDestroy();
        }
    }

    private static String randomId() {
        return UUID.randomUUID().toString();
    }
}
