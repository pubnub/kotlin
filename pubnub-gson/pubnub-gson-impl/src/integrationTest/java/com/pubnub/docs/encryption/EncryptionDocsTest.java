package com.pubnub.docs.encryption;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Wait;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page encryption (docs/sdks/java/api-reference/encryption.md) and of the encryption samples of
 * {@code MiscellaneousOthers} that no page embeds (createCryptoModuleBasic, encryptApnsBasic, decryptingMessage). The embedded
 * cryptoModuleConfiguration sample sits with the configuration tests. One test per snippet. The text between a start marker
 * and {@code snippet.end} is the published sample, the arrangement is above it and the assertions below it.
 */
@ExtendWith(LiveExtension.class)
class EncryptionDocsTest {
    private static final String OPEN = "open";
    private static final String CIPHER_KEY = "pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ";

    // the header of data that the crypto module encrypted with AES-CBC: "PNED", the format version and the cryptor id "ACRH"
    private static final byte[] HEADER = {80, 78, 69, 68, 1, 65, 67, 82, 72};

    private static boolean startsWithHeader(byte[] data) {
        return data.length > HEADER.length && Arrays.equals(Arrays.copyOfRange(data, 0, HEADER.length), HEADER);
    }

    private static byte[] readAll(InputStream stream) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] chunk = new byte[512];
        int read;
        while ((read = stream.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toByteArray();
    }

    @Test
    void encryptString() throws Exception {
        // snippet.encryptString
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true);
        String stringToBeEncrypted = "string to be encrypted";
        byte[] encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        // snippet.end

        assertFalse(Arrays.equals(encryptedData, stringToBeEncrypted.getBytes(StandardCharsets.UTF_8)), "the data was not encrypted");
        assertTrue(startsWithHeader(encryptedData), "the encrypted data does not carry the AES-CBC header");
        assertFalse(new String(encryptedData, StandardCharsets.ISO_8859_1).contains(stringToBeEncrypted), "the text is still in the data");
        // a random IV: encrypting twice gives two different results, both decrypt
        byte[] again = aesCbcCryptoModule.encrypt(stringToBeEncrypted.getBytes(StandardCharsets.UTF_8));
        assertFalse(Arrays.equals(again, encryptedData), "two encryptions of the same text are equal");
        assertEquals(stringToBeEncrypted, new String(aesCbcCryptoModule.decrypt(again), StandardCharsets.UTF_8));
        assertEquals(stringToBeEncrypted, new String(aesCbcCryptoModule.decrypt(encryptedData), StandardCharsets.UTF_8));
        // another cipher key cannot read it
        CryptoModule other = CryptoModule.createAesCbcCryptoModule("another-cipher-key-of-this-test", true);
        assertThrows(PubNubException.class, () -> other.decrypt(encryptedData));
    }

    @Test
    void encryptInputStream() throws Exception {
        // snippet.encryptInputStream
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true);
        String stringToBeEncrypted = "string to be encrypted";
        InputStream inputStream = new java.io.ByteArrayInputStream(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        InputStream encryptedStream = aesCbcCryptoModule.encryptStream(inputStream);
        // snippet.end

        byte[] encryptedBytes = readAll(encryptedStream);
        assertTrue(startsWithHeader(encryptedBytes), "the encrypted stream does not carry the AES-CBC header");
        assertFalse(new String(encryptedBytes, StandardCharsets.ISO_8859_1).contains(stringToBeEncrypted), "the text is still in the stream");
        byte[] decrypted = readAll(aesCbcCryptoModule.decryptStream(new java.io.ByteArrayInputStream(encryptedBytes)));
        assertEquals(stringToBeEncrypted, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void decryptString() throws Exception {
        // snippet.decryptString
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true);
        String stringToBeEncrypted = "string to be encrypted";

        byte[] encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        byte[] decryptedData = aesCbcCryptoModule.decrypt(encryptedData);
        // snippet.end

        assertArrayEquals(stringToBeEncrypted.getBytes(StandardCharsets.UTF_8), decryptedData);
        // the legacy module reads the data of the recommended one, as the docs say, and the other way round
        CryptoModule legacy = CryptoModule.createLegacyCryptoModule(CIPHER_KEY, true);
        assertEquals(stringToBeEncrypted, new String(legacy.decrypt(encryptedData), StandardCharsets.UTF_8));
        assertEquals(stringToBeEncrypted, new String(aesCbcCryptoModule.decrypt(legacy.encrypt(stringToBeEncrypted.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8));
    }

    @Test
    void decryptInputStream() throws Exception {
        // snippet.decryptInputStream
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true);
        String stringToBeEncrypted = "string to be encrypted";
        InputStream inputStream = new java.io.ByteArrayInputStream(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        InputStream encryptedStream = aesCbcCryptoModule.encryptStream(inputStream);
        InputStream decryptedStream = aesCbcCryptoModule.decryptStream(encryptedStream);
        // snippet.end

        assertEquals(stringToBeEncrypted, new String(readAll(decryptedStream), StandardCharsets.UTF_8));
    }

    @Test
    void createCryptoModuleBasic(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver =
                clients.kotlinClient(
                        OPEN,
                        "docs-config-receiver",
                        (java.util.function.Consumer<com.pubnub.api.v2.PNConfiguration.Builder>)
                                b -> {
                                    try {
                                        b.setCryptoModule(CryptoModule.createAesCbcCryptoModule(CIPHER_KEY, true));
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
        String text = Kit.probe("crypto-basic");
        Wait.Expectation arrived = Kit.armMessage(receiver, "docs-config-crypto", text);
        Kit.subscribeAndConnect(receiver, Collections.singletonList("docs-config-crypto"));
        Cleanup.record("history", "docs-config-crypto");

        // snippet.createCryptoModuleBasic
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        // publishKey from Admin Portal (only required if publishing)
        configBuilder.publishKey("PublishKey");
        configBuilder.cryptoModule(CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true));
        // all necessary config options
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
        clients.track(pubNub);

        assertNotNull(pubNub.getConfiguration().getCryptoModule());
        PNPublishResult published = pubNub.publish().channel("docs-config-crypto").message(text).sync();
        PNMessageResult received = (PNMessageResult) arrived.await();
        assertEquals(text, received.getMessage().getAsString());
        assertEquals("yourUserId", received.getPublisher());
        assertEquals(published.getTimetoken(), received.getTimetoken());
        // the documented client methods use the configured module too
        String encrypted = pubNub.encrypt(text);
        assertNotEquals(text, encrypted);
        assertEquals(text, pubNub.decrypt(encrypted));
    }

    @Test
    void encryptApnsBasic() throws Exception {
        // snippet.encryptApnsBasic
        JsonObject clearData = new JsonObject();
        clearData.addProperty("test_name", "pregnancy");
        clearData.addProperty("results", "positive");
        clearData.addProperty("notes", "You are having twins!");
        byte[]  clearBytes = clearData.toString().getBytes(StandardCharsets.UTF_8);

        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true);

        byte[] encryptedData = aesCbcCryptoModule.encrypt(clearBytes);
        // [80, 78, 69, 68, 1, 65, 67, 82, 72, 16, 83, 67, -54, -1, 98, -51, 91, 120, 31, 121, 100, 95, 75, 54, -95, 60, -74, 32, 26, 108, 77, 107, -47, 50, -45, -8, 86, -67, 72, 30, -106, -9, 45, -92, -111, 118, 50, -55, -48, -103, -90, -115, -70, 120, -47, -107, 41, 7, -61, 52, -37, -61, 83, -20, 34, -30, -64, 61, 104, 24, 3, 25, 41, -122, -36, 60, 98, -16, 34, 81, -41, 46, 102, 7, -97, 64, -37, -10, 124, 67, -41, 101, 35, -80, -103, -27, -26, -34, -50, -86, -2, -84, 105, 16, -84, 4]
        // snippet.end

        // the commented bytes of the sample are one result of many: the IV is random, so the bytes differ on every run, the
        // header and the length (the 9 byte header, a 16 byte IV and the padded 66 byte text) stay the same
        assertTrue(startsWithHeader(encryptedData), "the encrypted data does not carry the AES-CBC header");
        assertEquals(clearBytes.length + HEADER.length + 1 + 16 + (16 - clearBytes.length % 16), encryptedData.length);
        assertEquals(clearData, JsonParser.parseString(new String(aesCbcCryptoModule.decrypt(encryptedData), StandardCharsets.UTF_8)));
    }

    @Test
    void decryptingMessage() throws Exception {
        // The bytes of the docs do not decrypt with the cipher key of the sample: the sample ends in a PubNubException
        // (docs finding: the bytes come from another cipher key than the one the docs show).
        PubNubException error = assertThrows(PubNubException.class, () -> decryptingMessageSample());

        assertEquals(PubNubError.CRYPTO_ERROR, error.getPubnubError());
        assertEquals("Decryption failed", error.getErrorMessage());
        // the same bytes are well formed: they carry the AES-CBC header of the encrypt sample
        assertTrue(startsWithHeader(new byte[]{80, 78, 69, 68, 1, 65, 67, 82, 72, 16}));
    }

    private static void decryptingMessageSample() throws Exception {
        // snippet.decryptingMessage
        // parse the received message and pass the encrypted parts to decrypt API.
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true);
        byte[] encryptedData = new byte[]{80, 78, 69, 68, 1, 65, 67, 82, 72, 16, 83, 67, -54, -1, 98, -51, 91, 120, 31, 121, 100, 95, 75, 54, -95, 60, -74, 32, 26, 108, 77, 107, -47, 50, -45, -8, 86, -67, 72, 30, -106, -9, 45, -92, -111, 118, 50, -55, -48, -103, -90, -115, -70, 120, -47, -107, 41, 7, -61, 52, -37, -61, 83, -20, 34, -30, -64, 61, 104, 24, 3, 25, 41, -122, -36, 60, 98, -16, 34, 81, -41, 46, 102, 7, -97, 64, -37, -10, 124, 67, -41, 101, 35, -80, -103, -27, -26, -34, -50, -86, -2, -84, 105, 16, -84, 4};

        byte[] decryptedBytes = aesCbcCryptoModule.decrypt(encryptedData);
        JsonObject decryptedJson = JsonParser.parseString(new String(decryptedBytes, StandardCharsets.UTF_8)).getAsJsonObject();
        assert decryptedJson.get("test_name").getAsString().equals("pregnancy");
        assert decryptedJson.get("results").getAsString().equals("positive");
        assert decryptedJson.get("notes").getAsString().equals("You are having twins!");
        // snippet.end
    }

    @Test
    void documentedMethodsUseTheConfiguredModule(LiveClients clients) throws Exception {
        // The page documents pubnub.encrypt(inputString) and pubnub.decrypt(inputString) next to the crypto module samples.
        PubNub client = clients.javaClient(OPEN, "docs-config-sender", (java.util.function.Consumer<Object>) builder -> {
            try {
                ((PNConfiguration.Builder) builder).cryptoModule(CryptoModule.createAesCbcCryptoModule(CIPHER_KEY, true));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        String encrypted = client.encrypt("string to be encrypted");
        assertNotEquals("string to be encrypted", encrypted);
        assertEquals("string to be encrypted", client.decrypt(encrypted));
        PubNub withoutModule = clients.javaClient(OPEN, "docs-config-receiver");
        assertThrows(PubNubException.class, () -> withoutModule.encrypt("string to be encrypted"));
    }
}
