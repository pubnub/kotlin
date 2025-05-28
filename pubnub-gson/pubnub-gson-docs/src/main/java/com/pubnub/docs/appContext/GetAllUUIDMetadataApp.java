package com.pubnub.docs.appContext;
// https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage

// snippet.getAllUUIDMetadataApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.java.v2.PNConfiguration;

import java.util.Arrays;
import java.util.List;

public class GetAllUUIDMetadataApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Get all UUID Metadata
        pubnub.getAllUUIDMetadata()
                .limit(20)
                .sort(Arrays.asList(PNSortKey.asc(PNSortKey.Key.ID), PNSortKey.desc(PNSortKey.Key.UPDATED)))
                .includeTotalCount(true)
                .includeCustom(true)
                .async(result -> {
                    result.onSuccess(res -> {
                        System.out.println("Total Count: " + res.getTotalCount());
                        List<PNUUIDMetadata> uuidMetadataList = res.getData();
                        for (PNUUIDMetadata metadata : uuidMetadataList) {
                            System.out.println("UUID: " + metadata.getId());
                            System.out.println("Name: " + metadata.getName().getValue());
                            System.out.println("Email: " + metadata.getEmail().getValue());
                            System.out.println("Custom: " + metadata.getCustom().getValue());
                        }
                    }).onFailure(exception -> {
                        System.out.println("Error retrieving UUID metadata: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
