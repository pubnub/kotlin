package com.pubnub.docs.publishAndSubscribe;

public class PublishOther {
//    private void publishWithMetadata() {
//        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#publish-with-metadata
//
//        // snippet.publishWithMetadata
//
//        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
//        configBuilder.publishKey("demo");
//
//        PNConfiguration pnConfiguration = configBuilder.build();
//        PubNub pubnub = PubNub.create(pnConfiguration);
//        Channel channel = pubnub.channel("myChannel");
//
//        channel.publish(Arrays.asList("hello", "there"))
//                .customMessageType("text-message")
//                .shouldStore(true)
//                .meta("meta") // optional meta data object which can be used with the filtering ability.
//                .usePOST(true)
//                .async(result -> { /* check result */ });
//
//        // snippet.end
//    }
}
