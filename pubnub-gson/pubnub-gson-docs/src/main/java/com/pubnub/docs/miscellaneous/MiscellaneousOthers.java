package com.pubnub.docs.miscellaneous;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper;
import com.pubnub.docs.SnippetBase;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.util.TimetokenUtil;

import java.io.InputStream;
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


    private void reconnectBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-8

        PubNub pubNub = createPubNub();

        // snippet.reconnectBasic
        pubNub.reconnect();
        // snippet.end
    }

    private void disconnectBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/misc#basic-usage-9

        PubNub pubNub = createPubNub();

        // snippet.disconnectBasic
        pubNub.disconnect();
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

    private void createCryptoModuleBasic() {
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
}
