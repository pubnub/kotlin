package com.pubnub.docs.accessManager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class GrantTokenDataSync extends SnippetBase {
    private void grantTokenFlatList() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-token-flat-list

        // snippet.grantTokenFlatList
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        PubNub pubnub = PubNub.create(configBuilder.build());

        pubnub.grantToken(15)
                .authorizedUserId(new UserId("user-alice"))
                .grants(Arrays.asList(
                        ChannelGrant.name("channel-summer-sale").read().write(),
                        ChannelGrant.pattern("^announcements-.*$").read()))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Token: " + value.getToken());
                    }).onFailure(exception -> {
                        System.out.println("Failed to grant token: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void grantTokenDataSync() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-token-datasync

        // snippet.grantTokenDataSync
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        PubNub pubnub = PubNub.create(configBuilder.build());

        pubnub.grantToken(15)
                .authorizedUserId(new UserId("user-alice"))
                .grants(Arrays.asList(
                        DataSyncGrant.entity("product-sneaker-42").get().update(),
                        DataSyncGrant.relationship("rel-bob-owns-sneaker-42").get(),
                        DataSyncGrant.membership("membership-alice-summer-sale").get().update(),
                        // User and channel grants share the regular users and channels buckets of the token
                        DataSyncGrant.user("user-alice").get().update(),
                        DataSyncGrant.channel("channel-summer-sale").get(),
                        // Real-time events are delivered on the resource id, so grant read access to it
                        DataSyncGrant.subscribe("product-sneaker-42"),
                        DataSyncGrant.subscribe("user-alice")))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Token: " + value.getToken());
                    }).onFailure(exception -> {
                        System.out.println("Failed to grant token: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void grantTokenDataSyncProjection() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-token-datasync-projection

        // snippet.grantTokenDataSyncProjection
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        PubNub pubnub = PubNub.create(configBuilder.build());

        pubnub.grantToken(15)
                .authorizedUserId(new UserId("user-alice"))
                .grants(Arrays.asList(
                        DataSyncGrant.entity("product-sneaker-42").get().projection("admin"),
                        DataSyncGrant.subscribe("product-sneaker-42", "admin")))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Token: " + value.getToken());
                    }).onFailure(exception -> {
                        System.out.println("Failed to grant token: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void grantTokenDataSyncPattern() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-token-datasync-pattern

        // snippet.grantTokenDataSyncPattern
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        PubNub pubnub = PubNub.create(configBuilder.build());

        pubnub.grantToken(15)
                .authorizedUserId(new UserId("user-alice"))
                .grants(Arrays.asList(
                        DataSyncGrant.entityPattern("^product-.*$").get().update(),
                        DataSyncGrant.subscribePattern("^product-.*$")))
                .async(result -> {
                    result.onSuccess(value -> {
                        System.out.println("Token: " + value.getToken());
                    }).onFailure(exception -> {
                        System.out.println("Failed to grant token: " + exception.getMessage());
                    });
                });
        // snippet.end
    }

    private void parseTokenDataSync() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#parse-token-datasync

        PubNub pubnub = createPubNub();

        // snippet.parseTokenDataSync
        PNToken token = pubnub.parseToken(
                "qGF2AmF0GmqXSuBjdHRsD2R1dWlkanVzZXItYWxpY2VjcmVzpWRjaGFuoXNjaGFubmVsLXN1bW1lci1zYWxlAWNncnCgZHV1aWSgY3VzcqFqdXNlci1hbGljZRhgcWRhdGFzeW5jOmVudGl0aWVzoXJwcm9kdWN0LXNuZWFrZXItNDIYcGNwYXSjZGNoYW6gY2dycKBkdXVpZKBkbWV0YaFucG4tcHJvamVjdGlvbnOhY3Jlc6F4JGRhdGFzeW5jOmVudGl0aWVzOnByb2R1Y3Qtc25lYWtlci00MmVhZG1pbmNzaWdYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        PNToken.PNResourcePermissions entityPermissions = token.getResources().getDatasyncEntities().get("product-sneaker-42");
        if (entityPermissions != null) {
            System.out.println("Can create entities: " + entityPermissions.getCreate());
            System.out.println("Can update entities: " + entityPermissions.getUpdate());
        }
        System.out.println("User permissions: " + token.getResources().getUsers().get("user-alice"));
        // Projections are only present when the token was granted with a non-default projection
        if (token.getProjections() != null) {
            System.out.println("Projection: " + token.getProjections().getResources().getEntities().get("product-sneaker-42"));
        }
        // snippet.end
    }
}
