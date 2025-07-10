package com.pubnub.docs.miscellaneous;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.util.TimetokenUtil;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper;
import com.pubnub.docs.SnippetBase;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiscellaneousOthers extends SnippetBase {
    private void createPushPayloadBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#methods

        // snippet.createPushPayloadBasic
        PushPayloadHelper helper = new PushPayloadHelper();
        helper.setApnsPayload(new PushPayloadHelper.APNSPayload());
        helper.setFcmPayloadV2(new PushPayloadHelper.FCMPayloadV2());
        helper.setCommonPayload(new HashMap<String, Object>()); // or your actual payload

        Map<String, Object> payload = helper.build();
        // snippet.end
    }

    private void destroyPubNubBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-1

        PubNub pubNub = createPubNub();
        // snippet.destroyPubNubBasic
        pubNub.destroy();
        // snippet.end
    }

    private void encryptString() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#encrypt-part-of-message

        // snippet.encryptString
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey01", true);
        String stringToBeEncrypted = "string to be encrypted";
        byte[] encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        // snippet.end
    }

    private void encryptInputStream() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-3

        // snippet.encryptInputStream
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey01", true);
        String stringToBeEncrypted = "string to be encrypted";
        InputStream inputStream = new java.io.ByteArrayInputStream(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        InputStream encryptedStream = aesCbcCryptoModule.encryptStream(inputStream);
        // snippet.end
    }

    private void decryptString() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-4

        // snippet.decryptString
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey01", true);
        String stringToBeEncrypted = "string to be encrypted";

        byte[] encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        byte[] decryptedData = aesCbcCryptoModule.decrypt(encryptedData);
        // snippet.end
    }

    private void decryptInputStream() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-5

        // snippet.decryptInputStream
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey01", true);
        String stringToBeEncrypted = "string to be encrypted";
        InputStream inputStream = new java.io.ByteArrayInputStream(stringToBeEncrypted.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        InputStream encryptedStream = aesCbcCryptoModule.encryptStream(inputStream);
        InputStream decryptedStream = aesCbcCryptoModule.decryptStream(encryptedStream);
        // snippet.end
    }

    private void getSubscribedChannelGroupsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#get-subscribed-channel-groups-1

        PubNub pubNub = createPubNub();

        // snippet.getSubscribedChannelGroupsBasic
        List<String> groups = pubNub.getSubscribedChannelGroups();
        // snippet.end
    }

    private void getSubscribedChannelsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#get-subscribed-channels-1

        PubNub pubNub = createPubNub();

        // snippet.getSubscribedChannelsBasic
        List<String> channels = pubNub.getSubscribedChannels();
        // snippet.end
    }

    private void disconnectBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-8

        PubNub pubNub = createPubNub();

        // snippet.disconnectBasic
        pubNub.disconnect();
        // snippet.end
    }


    private void reconnectBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-9

        PubNub pubNub = createPubNub();

        // snippet.reconnectBasic
        pubNub.reconnect();
        // or
        Long timetoken = 17276954606232118L; // Example timetoken received in publish/signal response
        pubNub.reconnect(timetoken);
        // snippet.end
    }



    private void timetokenToDateTimeBasic() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-10

        // snippet.timetokenToDateTimeBasic
        long timetoken = 17276954606232118L;
        Instant instant = TimetokenUtil.timetokenToInstant(timetoken);
        LocalDateTime localDateTimeInUTC = LocalDateTime.ofInstant(instant, java.time.ZoneId.of("UTC"));

        System.out.println("PubNub timetoken: " + timetoken);
        System.out.println("Current date: " + localDateTimeInUTC.toLocalDate());
        System.out.println("Current time: " + localDateTimeInUTC.toLocalTime());
        // snippet.end
    }

    private void dateTimeToTimetokenBasic() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-11

        // snippet.dateTimeToTimetokenBasic
        LocalDateTime localDateTime = LocalDateTime.of(2024, 9, 30, 12, 12, 44, 123456789); // Example date and time
        Instant instant = localDateTime.atOffset(java.time.ZoneOffset.UTC).toInstant();
        long timetoken = TimetokenUtil.instantToTimetoken(instant);

        System.out.println("Current date: " + localDateTime.toLocalDate());
        System.out.println("Current time: " + localDateTime.toLocalTime());
        System.out.println("PubNub timetoken: " + timetoken);
        // snippet.end
    }

    private void unixTimeToTimetokenBasic() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-12

        // snippet.unixTimeToTimetokenBasic
        long unixTime = 1727866935316L;
        long timetoken = TimetokenUtil.unixToTimetoken(unixTime);
        Instant instant = TimetokenUtil.timetokenToInstant(timetoken);
        LocalDateTime localDateTimeInUTC = LocalDateTime.ofInstant(instant, java.time.ZoneId.of("UTC"));

        System.out.println("PubNub timetoken: " + timetoken);
        System.out.println("Current date: " + localDateTimeInUTC.toLocalDate());
        System.out.println("Current time: " + localDateTimeInUTC.toLocalTime());
        // snippet.end
    }

    private void timetokenToUnixTimeBasic() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-13

        // snippet.timetokenToUnixTimeBasic
        long timetoken = 17276954606232118L;
        long unixTime = TimetokenUtil.timetokenToUnix(timetoken);
        Instant instant = Instant.ofEpochMilli(unixTime);
        LocalDateTime localDateTime = instant.atZone(java.time.ZoneOffset.UTC).toLocalDateTime();

        System.out.println("Current date: " + localDateTime.toLocalDate());
        System.out.println("Current time: " + localDateTime.toLocalTime());
        System.out.println("PubNub timetoken: " + timetoken);
        // snippet.end
    }

    private void createCryptoModuleBasic() throws PubNubException {
        // https://www.pubnub.com/docs/general/setup/data-security#encrypting-messages

        // snippet.createCryptoModuleBasic
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        // publishKey from Admin Portal (only required if publishing)
        configBuilder.publishKey("PublishKey");
        configBuilder.cryptoModule(CryptoModule.createAesCbcCryptoModule("enigma", true));
        // all necessary config options
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
    }

    private void encryptApnsBasic() {
        // https://www.pubnub.com/docs/general/setup/data-security#apns-example

        // snippet.encryptApnsBasic
        JsonObject clearData = new JsonObject();
        clearData.addProperty("test_name", "pregnancy");
        clearData.addProperty("results", "positive");
        clearData.addProperty("notes", "You are having twins!");
        byte[]  clearBytes = clearData.toString().getBytes(StandardCharsets.UTF_8);

        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey01", true);

        byte[] encryptedData = aesCbcCryptoModule.encrypt(clearBytes);
        // [80, 78, 69, 68, 1, 65, 67, 82, 72, 16, 83, 67, -54, -1, 98, -51, 91, 120, 31, 121, 100, 95, 75, 54, -95, 60, -74, 32, 26, 108, 77, 107, -47, 50, -45, -8, 86, -67, 72, 30, -106, -9, 45, -92, -111, 118, 50, -55, -48, -103, -90, -115, -70, 120, -47, -107, 41, 7, -61, 52, -37, -61, 83, -20, 34, -30, -64, 61, 104, 24, 3, 25, 41, -122, -36, 60, 98, -16, 34, 81, -41, 46, 102, 7, -97, 64, -37, -10, 124, 67, -41, 101, 35, -80, -103, -27, -26, -34, -50, -86, -2, -84, 105, 16, -84, 4]
        // snippet.end
    }

    private void decryptingMessage() {
        // https://www.pubnub.com/docs/general/setup/data-security#decrypting-messages

        // snippet.decryptingMessage
        // parse the received message and pass the encrypted parts to decrypt API.
        CryptoModule aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule("myCipherKey01", true);
        byte[] encryptedData = new byte[]{80, 78, 69, 68, 1, 65, 67, 82, 72, 16, 83, 67, -54, -1, 98, -51, 91, 120, 31, 121, 100, 95, 75, 54, -95, 60, -74, 32, 26, 108, 77, 107, -47, 50, -45, -8, 86, -67, 72, 30, -106, -9, 45, -92, -111, 118, 50, -55, -48, -103, -90, -115, -70, 120, -47, -107, 41, 7, -61, 52, -37, -61, 83, -20, 34, -30, -64, 61, 104, 24, 3, 25, 41, -122, -36, 60, 98, -16, 34, 81, -41, 46, 102, 7, -97, 64, -37, -10, 124, 67, -41, 101, 35, -80, -103, -27, -26, -34, -50, -86, -2, -84, 105, 16, -84, 4};

        byte[] decryptedBytes = aesCbcCryptoModule.decrypt(encryptedData);
        JsonObject decryptedJson = JsonParser.parseString(new String(decryptedBytes, StandardCharsets.UTF_8)).getAsJsonObject();
        assert decryptedJson.get("test_name").getAsString().equals("pregnancy");
        assert decryptedJson.get("results").getAsString().equals("positive");
        assert decryptedJson.get("notes").getAsString().equals("You are having twins!");
        // snippet.end
    }
}
