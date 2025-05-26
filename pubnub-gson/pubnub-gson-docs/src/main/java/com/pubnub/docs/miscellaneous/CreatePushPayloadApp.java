package com.pubnub.docs.miscellaneous;
// https://www.pubnub.com/docs/sdks/java/api-reference/misc#create-push-payload-1

// snippet.createPushPayloadApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNPushEnvironment;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CreatePushPayloadApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Create an instance of PushPayloadHelper
        PushPayloadHelper pushPayloadHelper = new PushPayloadHelper();

        // Setup FCM parameters (FCMPayload)
        PushPayloadHelper.FCMPayloadV2 fcmPayload = new PushPayloadHelper.FCMPayloadV2();
        PushPayloadHelper.FCMPayloadV2.Notification fcmNotification = new PushPayloadHelper.FCMPayloadV2.Notification();
        fcmNotification.setTitle("Notification title");
        fcmNotification.setBody("Notification body text");
        fcmNotification.setImage("http://example.com/image.png");
        fcmPayload.setNotification(fcmNotification);

        Map<String, String> fcmData = new HashMap<>();
        fcmData.put("city", "sf");
        fcmData.put("count", "71");
        fcmData.put("is_private", "true");
        fcmData.put("token", "bk3RNwTe3H0:CI2k_HvvDMExdFQ3P1...");
        fcmData.put("topic", "/topic/news/");
        fcmPayload.setData(fcmData);

        // Set FCM payload
        pushPayloadHelper.setFcmPayloadV2(fcmPayload);

        // Setup APNs parameters
        PushPayloadHelper.APNSPayload apnsPayload = new PushPayloadHelper.APNSPayload();
        PushPayloadHelper.APNSPayload.APS aps = new PushPayloadHelper.APNSPayload.APS();
        aps.setAlert("Alert");
        aps.setBadge(1);
        aps.setSound("Ding");
        apnsPayload.setAps(aps);


        PushPayloadHelper.APNSPayload.APNS2Configuration.Target target = new PushPayloadHelper.APNSPayload.APNS2Configuration.Target();
        target.setEnvironment(PNPushEnvironment.DEVELOPMENT);
        target.setTopic("com.meetings.chat.app");
        target.setExcludeDevices(Arrays.asList("device1", "device2"));

        PushPayloadHelper.APNSPayload.APNS2Configuration apns2Configuration = new PushPayloadHelper.APNSPayload.APNS2Configuration();
        apns2Configuration.setCollapseId("invitations");
        apns2Configuration.setExpiration("2019-12-13T22:06:09Z");
        apns2Configuration.setVersion("v1");
        apns2Configuration.setTargets(Arrays.asList(target));

        // Set APNs2 Configurations
        apnsPayload.setApns2Configurations(Arrays.asList(apns2Configuration));

        Map<String, Object> apnsCustom = new HashMap<>();
        apnsCustom.put("apns_key_1", "value_1");
        apnsCustom.put("apns_key_2", "value_2");
        apnsPayload.setCustom(apnsCustom);

        // Set APNS payload
        pushPayloadHelper.setApnsPayload(apnsPayload);

        // Common payload for native PubNub subscribers
        Map<String, Object> commonPayload = new HashMap<>();
        commonPayload.put("message", "Hello");
        commonPayload.put("such", "object");
        commonPayload.put("type", 7);
        pushPayloadHelper.setCommonPayload(commonPayload);

        // Build the payload and publish
        Map<String, Object> payload = pushPayloadHelper.build();

        Channel channel = pubnub.channel("foo");
        channel.publish(payload)
                .async(result -> {
                    result.onSuccess(res -> {
                        System.out.println("Message published successfully.");
                    }).onFailure(exception -> {
                        System.out.println("Error publishing message: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
