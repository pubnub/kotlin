package com.pubnub.docs.accessManager;

import static com.pubnub.docs.support.AccessManagerKit.DATASYNC;
import static com.pubnub.docs.support.AccessManagerKit.PAM;
import static com.pubnub.docs.support.AccessManagerKit.UNTOUCHED;
import static com.pubnub.docs.support.AccessManagerKit.UNTOUCHED_PRODUCT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import com.pubnub.docs.support.AccessManagerKit;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Redact;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs pages access-manager and access-manager-v2 (docs/sdks/java/api-reference/access-manager.md and
 * access-manager-v2.md). One test per snippet. The text between a start marker and {@code snippet.end} is the published
 * sample, the arrangement is above it and the assertions below it.
 *
 * <p>Grants need the secret key, so the samples run on Java clients that hold it (role {@code pam} or {@code datasync}). The
 * proof that a token works uses Kotlin clients WITHOUT the secret key ({@code AccessManagerKit.tokenClient}): allowed on the
 * granted resource, refused (403) on a fresh name that no grant touches and without the token. Tokens are registered with
 * {@code Redact} and never appear in a message. The v2 grant is closed by a cleanup that is registered before the sample runs.
 */
@ExtendWith(LiveExtension.class)
class AccessManagerDocsTest {
    // The token that the parseToken and setToken samples show. It is not a live token: the setToken test swaps it for one.
    private static final String SAMPLE_TOKEN =
            "qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo=";

    private static Set<String> set(String... names) {
        return new HashSet<>(Arrays.asList(names));
    }

    /** A Java client with the secret key of {@code role}, the client of a sample that grants. */
    private static PubNub javaServer(LiveClients clients, String role) {
        return clients.javaClient(role, "docs-java-server");
    }

    /** A Kotlin client with the secret key, for the arrangement and the parse of the tests. */
    private static com.pubnub.api.PubNub server(LiveClients clients, String role) {
        return clients.server(role, "docs-server");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Access Manager v2
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void grantWithGetAllMetadataAccess(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        PubNub pubnub = recorder.wrap(javaServer(clients, PAM), PubNub.class);
        com.pubnub.api.PubNub lister = AccessManagerKit.authKeyClient(clients, PAM, "docs-am-lister", "my-auth-key");
        // The close of the grant runs after the test, also when an assertion below fails.
        Cleanup.cleanupAfterTest("close the grant of my-auth-key", () -> AccessManagerKit.closeAuthKeyGrant(server, "my-auth-key"));

        // snippet.grantWithGetAllMetadataAccess
        // Allow the auth keys to list all channel and uuid metadata on the keyset.
        // The `get` permission is sent automatically, so no other permission can be set.
        pubnub.grant()
                .authKeys(Collections.singletonList("my-auth-key"))
                .getAllChannels(true)
                .getAllUUIDs(true)
                .async(result -> { /* check result */ });
        // snippet.end

        PNAccessManagerGrantResult result = (PNAccessManagerGrantResult) recorder.awaitOk("grant");
        assertEquals(set("channels", "uuids"), result.getCategories().keySet());
        assertTrue(result.getCategories().get("channels").get("my-auth-key").isGetEnabled(), "the auth key may list all channel metadata");
        assertTrue(result.getCategories().get("uuids").get("my-auth-key").isGetEnabled(), "the auth key may list all uuid metadata");
        // The grant takes a moment to reach every node.
        // The keyset disallows listing all metadata for requests without a signature: the secret key client may list,
        // a client with the auth key but without the secret key and a client without both are refused with 403.
        AccessManagerKit.assertListChannelsAllowed(server);
        AccessManagerKit.assertListUuidsAllowed(server);
        AccessManagerKit.assertListAllRefused(lister);
        AccessManagerKit.assertListAllRefused(AccessManagerKit.plainClient(clients, PAM, "docs-am-lister"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Grant token
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void basicGrantToken(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        AccessManagerKit.ScrubbedStdout out = AccessManagerKit.ScrubbedStdout.captureForTest();

        com.pubnub.docs.accessManager.samples.GrantTokenApp.main(new String[0]);

        // The sample answers from a callback, so the print is awaited.
        String token = out.printedToken("Token: ");
        out.assertNotPrinted("Error granting token");
        PNToken parsed = server.parseToken(token);
        assertEquals("my-authorized-uuid", parsed.getAuthorizedUUID());
        assertEquals(15L, parsed.getTtl());
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getResources().getChannels().get("channel-a")));
        assertEquals(set("read", "write"), AccessManagerKit.granted(parsed.getResources().getChannels().get("channel-b")));

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, PAM, "my-authorized-uuid", token);
        AccessManagerKit.assertPublishAllowed(holder, "channel-b");
        AccessManagerKit.assertPublishRefused(holder, "channel-a");
        AccessManagerKit.assertPublishRefused(holder, UNTOUCHED);
        AccessManagerKit.assertPublishRefused(AccessManagerKit.plainClient(clients, PAM, "my-authorized-uuid"), "channel-b");
    }

    @Test
    void grantTokenDifferentAccessLevels(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        PubNub pubNub = recorder.wrap(javaServer(clients, PAM), PubNub.class);

        // snippet.grantTokenDifferentAccessLevels
        PNGrantTokenResult pnGrantTokenResult = pubNub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.name("channel-a").read(),
                        ChannelGrant.name("channel-b").read().write(),
                        ChannelGrant.name("channel-c").read().write(),
                        ChannelGrant.name("channel-d").read().write()))
                .channelGroups(Arrays.asList(
                        ChannelGroupGrant.id("channel-group-b").read()))
                .uuids(Arrays.asList(
                        UUIDGrant.id("uuid-c").get(),
                        UUIDGrant.id("uuid-d").get().update()))
                .sync();

        String token = pnGrantTokenResult.getToken();
        // snippet.end

        Redact.register(token);
        recorder.assertNoFailures();
        PNToken parsed = server.parseToken(token);
        assertEquals("my-authorized-uuid", parsed.getAuthorizedUUID());
        assertEquals(15L, parsed.getTtl());
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getResources().getChannels().get("channel-a")));
        for (String name : Arrays.asList("channel-b", "channel-c", "channel-d")) {
            assertEquals(set("read", "write"), AccessManagerKit.granted(parsed.getResources().getChannels().get(name)), "permissions of " + name);
        }
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getResources().getChannelGroups().get("channel-group-b")));
        assertEquals(set("get"), AccessManagerKit.granted(parsed.getResources().getUuids().get("uuid-c")));
        assertEquals(set("get", "update"), AccessManagerKit.granted(parsed.getResources().getUuids().get("uuid-d")));
        assertTrue(parsed.getPatterns().getChannels().isEmpty(), "the token has no channel pattern");

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, PAM, "my-authorized-uuid", token);
        AccessManagerKit.assertPublishAllowed(holder, "channel-b");
        AccessManagerKit.assertHistoryAllowed(holder, "channel-a");
        AccessManagerKit.assertPublishRefused(holder, "channel-a");
        AccessManagerKit.assertPublishRefused(holder, UNTOUCHED);
        AccessManagerKit.assertPublishRefused(AccessManagerKit.plainClient(clients, PAM, "my-authorized-uuid"), "channel-b");
    }

    @Test
    void grantTokenWithRegEx(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        PubNub pubNub = recorder.wrap(javaServer(clients, PAM), PubNub.class);

        // snippet.grantTokenWithRegEx
        pubNub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.pattern("^channel-[A-Za-z0-9]*$").read()))
                .async(result -> { /* check result */ });
        // snippet.end

        String token = ((PNGrantTokenResult) recorder.awaitOk("grantToken")).getToken();
        Redact.register(token);
        PNToken parsed = server.parseToken(token);
        assertEquals("my-authorized-uuid", parsed.getAuthorizedUUID());
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getPatterns().getChannels().get("^channel-[A-Za-z0-9]*$")));
        assertTrue(parsed.getResources().getChannels().isEmpty(), "the token names no single channel");

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, PAM, "my-authorized-uuid", token);
        AccessManagerKit.assertHistoryAllowed(holder, "channel-a");
        AccessManagerKit.assertHistoryAllowed(holder, "channel-e");
        AccessManagerKit.assertPublishRefused(holder, "channel-a");
        AccessManagerKit.assertHistoryRefused(holder, UNTOUCHED);
        AccessManagerKit.assertHistoryRefused(AccessManagerKit.plainClient(clients, PAM, "my-authorized-uuid"), "channel-a");
    }

    @Test
    void grantTokenDifferentAccessLevelsWithRegEx(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        PubNub pubNub = recorder.wrap(javaServer(clients, PAM), PubNub.class);

        // snippet.grantTokenDifferentAccessLevelsWithRegEx
        pubNub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.name("channel-a").read(),
                        ChannelGrant.name("channel-b").read().write(),
                        ChannelGrant.name("channel-c").read().write(),
                        ChannelGrant.name("channel-d").read().write(),
                        ChannelGrant.pattern("^channel-[A-Za-z0-9]*$").read()))
                .channelGroups(Collections.singletonList(
                        ChannelGroupGrant.id("channel-group-b").read()))
                .uuids(Arrays.asList(
                        UUIDGrant.id("uuid-c").get(),
                        UUIDGrant.id("uuid-d").get().update()))
                .async(result -> { /* check result */ });
        // snippet.end

        String token = ((PNGrantTokenResult) recorder.awaitOk("grantToken")).getToken();
        Redact.register(token);
        PNToken parsed = server.parseToken(token);
        assertEquals("my-authorized-uuid", parsed.getAuthorizedUUID());
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getResources().getChannels().get("channel-a")));
        assertEquals(set("read", "write"), AccessManagerKit.granted(parsed.getResources().getChannels().get("channel-b")));
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getResources().getChannelGroups().get("channel-group-b")));
        assertEquals(set("get", "update"), AccessManagerKit.granted(parsed.getResources().getUuids().get("uuid-d")));
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getPatterns().getChannels().get("^channel-[A-Za-z0-9]*$")));

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, PAM, "my-authorized-uuid", token);
        // A named channel with write access, and a channel that only the pattern covers (read only).
        AccessManagerKit.assertPublishAllowed(holder, "channel-b");
        AccessManagerKit.assertHistoryAllowed(holder, "channel-e");
        AccessManagerKit.assertPublishRefused(holder, "channel-e");
        AccessManagerKit.assertPublishRefused(holder, UNTOUCHED);
        AccessManagerKit.assertPublishRefused(AccessManagerKit.plainClient(clients, PAM, "my-authorized-uuid"), "channel-b");
    }

    @Test
    void grantTokenWithGetAllMetadataAccess(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        PubNub pubNub = recorder.wrap(javaServer(clients, PAM), PubNub.class);

        // snippet.grantTokenWithGetAllMetadataAccess
        // Allow listing all channel and uuid metadata on the keyset (getAllChannelsMetadata / getAllUUIDMetadata).
        // A `get` on a named channel or uuid doesn't imply this.
        pubNub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .getAllChannels(true)
                .getAllUUIDs(true)
                .async(result -> { /* check result */ });
        // snippet.end

        String token = ((PNGrantTokenResult) recorder.awaitOk("grantToken")).getToken();
        Redact.register(token);
        PNToken parsed = server.parseToken(token);
        assertEquals("my-authorized-uuid", parsed.getAuthorizedUUID());
        assertTrue(parsed.getAllChannels(), "the token may list all channel metadata");
        assertTrue(parsed.getAllUUIDs(), "the token may list all uuid metadata");

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, PAM, "my-authorized-uuid", token);
        // The keyset disallows listing all metadata for requests without a signature: the secret key client may list,
        // the token holder without the secret key and a client without a token are refused with 403.
        AccessManagerKit.assertListChannelsAllowed(server);
        AccessManagerKit.assertListUuidsAllowed(server);
        AccessManagerKit.assertListAllRefused(holder);
        AccessManagerKit.assertListAllRefused(AccessManagerKit.plainClient(clients, PAM, "my-authorized-uuid"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Grant token with DataSync (the datasync keyset)
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void grantTokenFlatList(LiveClients clients) throws Exception {
        AccessManagerKit.ScrubbedStdout out = AccessManagerKit.ScrubbedStdout.captureForTest();

        // snippet.grantTokenFlatList
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "datasync");
        // snippet.show
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
        clients.track(pubnub);

        String token = out.printedToken("Token: ");
        out.assertNotPrinted("Failed to grant token");
        PNToken parsed = pubnub.parseToken(token);
        assertEquals("user-alice", parsed.getAuthorizedUUID());
        assertEquals(15L, parsed.getTtl());
        assertEquals(set("read", "write"), AccessManagerKit.granted(parsed.getResources().getChannels().get("channel-summer-sale")));
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getPatterns().getChannels().get("^announcements-.*$")));

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, DATASYNC, "user-alice", token);
        AccessManagerKit.assertPublishAllowed(holder, "channel-summer-sale");
        AccessManagerKit.assertPublishRefused(holder, UNTOUCHED);
        AccessManagerKit.assertPublishRefused(AccessManagerKit.plainClient(clients, DATASYNC, "user-alice"), "channel-summer-sale");
    }

    @Test
    void grantTokenDataSync(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub admin = server(clients, DATASYNC);
        AccessManagerKit.createProduct(admin, "product-sneaker-42");
        AccessManagerKit.createProduct(admin, UNTOUCHED_PRODUCT);
        AccessManagerKit.ScrubbedStdout out = AccessManagerKit.ScrubbedStdout.captureForTest();

        // snippet.grantTokenDataSync
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "datasync");
        // snippet.show
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
        clients.track(pubnub);

        String token = out.printedToken("Token: ");
        out.assertNotPrinted("Failed to grant token");
        PNToken parsed = pubnub.parseToken(token);
        assertEquals("user-alice", parsed.getAuthorizedUUID());
        assertEquals(set("get", "update"), AccessManagerKit.granted(parsed.getResources().getDatasyncEntities().get("product-sneaker-42")));
        assertEquals(set("get"), AccessManagerKit.granted(parsed.getResources().getDatasyncRelationships().get("rel-bob-owns-sneaker-42")));
        assertEquals(set("get", "update"), AccessManagerKit.granted(parsed.getResources().getDatasyncMemberships().get("membership-alice-summer-sale")));
        assertEquals(set("get", "update"), AccessManagerKit.granted(parsed.getResources().getUsers().get("user-alice")));
        assertEquals(set("get"), AccessManagerKit.granted(parsed.getResources().getChannels().get("channel-summer-sale")));
        // The two subscribe grants are read grants on the ref channels of the product and of the user.
        boolean productRef = false;
        boolean userRef = false;
        for (java.util.Map.Entry<String, PNToken.PNResourcePermissions> entry : parsed.getResources().getChannels().entrySet()) {
            if (set("read").equals(AccessManagerKit.granted(entry.getValue()))) {
                productRef |= entry.getKey().contains("product-sneaker-42");
                userRef |= entry.getKey().contains("user-alice");
            }
        }
        assertTrue(productRef, "the token has a read grant on the ref channel of the product");
        assertTrue(userRef, "the token has a read grant on the ref channel of the user");

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, DATASYNC, "user-alice", token);
        AccessManagerKit.assertEntityAllowed(holder, "product-sneaker-42");
        AccessManagerKit.assertEntityRefused(holder, UNTOUCHED_PRODUCT);
        AccessManagerKit.assertEntityRefused(AccessManagerKit.plainClient(clients, DATASYNC, "user-alice"), "product-sneaker-42");
    }

    @Test
    void grantTokenDataSyncPattern(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub admin = server(clients, DATASYNC);
        AccessManagerKit.createProduct(admin, "product-sneaker-42");
        AccessManagerKit.createProduct(admin, UNTOUCHED_PRODUCT);
        AccessManagerKit.ScrubbedStdout out = AccessManagerKit.ScrubbedStdout.captureForTest();

        // snippet.grantTokenDataSyncPattern
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "datasync");
        // snippet.show
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
        clients.track(pubnub);

        String token = out.printedToken("Token: ");
        out.assertNotPrinted("Failed to grant token");
        PNToken parsed = pubnub.parseToken(token);
        assertEquals("user-alice", parsed.getAuthorizedUUID());
        assertEquals(set("get", "update"), AccessManagerKit.granted(parsed.getPatterns().getDatasyncEntities().get("^product-.*$")));
        boolean readPattern = false;
        for (java.util.Map.Entry<String, PNToken.PNResourcePermissions> entry : parsed.getPatterns().getChannels().entrySet()) {
            readPattern |= entry.getKey().contains("product-") && set("read").equals(AccessManagerKit.granted(entry.getValue()));
        }
        assertTrue(readPattern, "the token has a read pattern on the ref channels of the products");

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, DATASYNC, "user-alice", token);
        AccessManagerKit.assertEntityAllowed(holder, "product-sneaker-42");
        AccessManagerKit.assertEntityRefused(holder, UNTOUCHED_PRODUCT);
    }

    @Test
    void grantTokenDataSyncProjection(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub admin = server(clients, DATASYNC);
        AccessManagerKit.createProduct(admin, "product-sneaker-42");
        AccessManagerKit.ScrubbedStdout out = AccessManagerKit.ScrubbedStdout.captureForTest();

        // snippet.grantTokenDataSyncProjection
        // Only server-side applications should use the secret key, as it is required to grant tokens
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("myServerUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("mySecretKey");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "datasync");
        // snippet.show
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
        clients.track(pubnub);

        String token = out.printedToken("Token: ");
        out.assertNotPrinted("Failed to grant token");
        PNToken parsed = pubnub.parseToken(token);
        assertEquals("user-alice", parsed.getAuthorizedUUID());
        assertEquals(set("get"), AccessManagerKit.granted(parsed.getResources().getDatasyncEntities().get("product-sneaker-42")));
        assertEquals("admin", parsed.getProjections().getResources().getEntities().get("product-sneaker-42"));

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, DATASYNC, "user-alice", token);
        AccessManagerKit.assertEntityAllowed(holder, "product-sneaker-42");
        AccessManagerKit.assertEntityRefused(AccessManagerKit.plainClient(clients, DATASYNC, "user-alice"), "product-sneaker-42");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Revoke token
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void revokeToken(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        String token = AccessManagerKit.grantRoom(server, "docs-am-revokee", "docs-am-revoke-room");
        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, PAM, "docs-am-revokee", token);
        AccessManagerKit.assertPublishAllowed(holder, "docs-am-revoke-room");
        recorder.placeholder("p0thisAkFl043rhDdHRsCkNyZXisRGNoYW6hanNlY3JldAFDZ3Jwsample3KgQ3NwY6BDcGF0pERjaGFuoENnctokenVzcqBDc3BjoERtZXRhoENzaWdYIGOAeTyWGJI", token);
        PubNub pubnub = recorder.wrap(javaServer(clients, PAM), PubNub.class);

        // snippet.revokeToken
        pubnub.revokeToken()
            .token("p0thisAkFl043rhDdHRsCkNyZXisRGNoYW6hanNlY3JldAFDZ3Jwsample3KgQ3NwY6BDcGF0pERjaGFuoENnctokenVzcqBDc3BjoERtZXRhoENzaWdYIGOAeTyWGJI")
            .async(result -> { /* check result */ });
        // snippet.end

        recorder.awaitOk("revokeToken");
        // The docs say a revoke can take up to a minute, so the refusal is awaited. Then two fresh clients must be refused too,
        // because enforcement reaches the nodes unevenly.
        AccessManagerKit.assertPublishRefusedEventually(holder, "docs-am-revoke-room");
        for (int attempt = 1; attempt <= 2; attempt++) {
            AccessManagerKit.assertPublishRefusedEventually(
                    AccessManagerKit.tokenClient(clients, PAM, "docs-am-revokee", token), "docs-am-revoke-room", 60_000L);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Parse token
    // -----------------------------------------------------------------------------------------------------------------

    private static void assertSampleTokenParsed(PNToken parsed) {
        assertEquals(2, parsed.getVersion());
        assertEquals(1747117669L, parsed.getTimestamp());
        assertEquals(1337L, parsed.getTtl());
        assertEquals("authorizedUser", parsed.getAuthorizedUUID());
        assertEquals(set("delete"), AccessManagerKit.granted(parsed.getResources().getChannels().get("space01")));
        assertEquals(set("get"), AccessManagerKit.granted(parsed.getResources().getUuids().get("user01")));
        assertEquals(set("read"), AccessManagerKit.granted(parsed.getPatterns().getChannels().get("space.*")));
        assertEquals(set("get"), AccessManagerKit.granted(parsed.getPatterns().getUuids().get("user.*")));
        assertTrue(parsed.getResources().getChannelGroups().isEmpty() && parsed.getPatterns().getChannelGroups().isEmpty(),
                "the token has no channel groups");
    }

    @Test
    void parseToken(LiveClients clients) throws Exception {
        PubNub pubnub = javaServer(clients, PAM);

        // snippet.parseToken
        pubnub.parseToken("qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo=");
        // snippet.end

        // The sample drops the result, so the same token is parsed again. It decodes locally, no request is sent.
        assertSampleTokenParsed(pubnub.parseToken(SAMPLE_TOKEN));
    }

    @Test
    void parseTokenResult(LiveClients clients) throws Exception {
        PubNub pubnub = javaServer(clients, PAM);

        // snippet.parseTokenResult
          /*
          * {
          "version": 2,
          "timestamp": 1747117669,
          "ttl": 1337,
          "authorizedUUID": "authorizedUser",
          "resources": {
            "channels": {
              "space01": {
                "read": false,
                "write": false,
                "manage": false,
                "delete": true,
                "get": false,
                "update": false,
                "join": false
              }
            },
            "channelGroups": {},
            "uuids": {
              "user01": {
                "read": false,
                "write": false,
                "manage": false,
                "delete": false,
                "get": true,
                "update": false,
                "join": false
              }
            }
          },
          "patterns": {
            "channels": {
              "space.*": {
                "read": true,
                "write": false,
                "manage": false,
                "delete": false,
                "get": false,
                "update": false,
                "join": false
              }
            },
            "channelGroups": {},
            "uuids": {
              "user.*": {
                "read": false,
                "write": false,
                "manage": false,
                "delete": false,
                "get": true,
                "update": false,
                "join": false
              }
            }
          },
          "meta": {}
        }
          */
        // snippet.end

        // The comment is the result of parsing the token of the parseToken sample. Every value of it is checked against the real
        // parse, so the page cannot show a result that the SDK does not return.
        PNToken parsed = pubnub.parseToken(SAMPLE_TOKEN);
        assertSampleTokenParsed(parsed);
        assertTrue(parsed.getResources().getChannelGroups().isEmpty() && parsed.getPatterns().getChannelGroups().isEmpty(), "channelGroups is {}");
        assertTrue(parsed.getMeta() == null || parsed.getMeta().toString().equals("{}"), "meta is {}");
        assertFalse(parsed.getAllChannels(), "the token has no getAllChannels");
    }

    @Test
    void parseTokenDataSync(LiveClients clients) throws Exception {
        PubNub pubnub = javaServer(clients, PAM);
        AccessManagerKit.ScrubbedStdout out = AccessManagerKit.ScrubbedStdout.captureForTest();

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

        // The token of the sample grants create and update on the entity, get and update on the user, and the admin projection.
        out.assertPrinted("Can create entities: true");
        out.assertPrinted("Can update entities: true");
        out.assertPrinted("User permissions: PNResourcePermissions(read=false, write=false, manage=false, delete=false, get=true, update=true, join=false, create=false)");
        out.assertPrinted("Projection: admin");
        assertEquals(set("get", "update", "create"), AccessManagerKit.granted(entityPermissions));
        assertEquals("user-alice", token.getAuthorizedUUID());
    }

    @Test
    void parseTokenWithGetAllMetadataAccess(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        PubNub pubnub = recorder.wrap(javaServer(clients, PAM), PubNub.class);

        // snippet.parseTokenWithGetAllMetadataAccess
        PNGrantTokenResult grantTokenResult = pubnub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .getAllChannels(true)
                .sync();

        PNToken token = pubnub.parseToken(grantTokenResult.getToken());
        boolean canListAllChannels = token.getAllChannels(); // true
        boolean canListAllUuids = token.getAllUUIDs(); // false
        // snippet.end

        Redact.register(grantTokenResult.getToken());
        recorder.assertNoFailures();
        assertTrue(canListAllChannels, "the token may list all channel metadata");
        assertFalse(canListAllUuids, "the token may not list all uuid metadata");

        com.pubnub.api.PubNub holder = AccessManagerKit.tokenClient(clients, PAM, "my-authorized-uuid", grantTokenResult.getToken());
        // The keyset disallows listing all metadata for requests without a signature, so the token holder is refused
        // on both listings and the secret key client may list.
        AccessManagerKit.assertListChannelsAllowed(server);
        AccessManagerKit.assertListUuidsAllowed(server);
        AccessManagerKit.assertListAllRefused(holder);
        AccessManagerKit.assertListAllRefused(AccessManagerKit.plainClient(clients, PAM, "my-authorized-uuid"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Set token
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void setToken(LiveClients clients, Recorder recorder) throws Exception {
        com.pubnub.api.PubNub server = server(clients, PAM);
        String token = AccessManagerKit.grantRoom(server, "docs-am-alice", "docs-am-room");
        // The client of the sample has no secret key, so the keyset enforces the token.
        PubNub client = clients.javaClient(PAM, "docs-am-alice", AccessManagerKit::withoutSecret);
        // Without a token the keyset refuses the same user.
        AccessManagerKit.assertPublishRefused(AccessManagerKit.plainClient(clients, PAM, "docs-am-alice"), "docs-am-room");
        recorder.placeholder(SAMPLE_TOKEN, token);
        PubNub pubnub = recorder.wrap(client, PubNub.class);

        // snippet.setToken
        pubnub.setToken("qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo=");
        // snippet.end

        // The Java client has no getter for the token, so the effect is checked: it publishes to its room and not to another.
        publishRoom(client, "docs-am-room");
        assertRefusedPublish(client, UNTOUCHED);
    }

    private static void publishRoom(PubNub client, String channel) throws PubNubException {
        com.pubnub.docs.support.Kit.assertTimetoken(
                client.publish().channel(channel).message("docs-am-check").shouldStore(false).sync().getTimetoken());
    }

    private static void assertRefusedPublish(PubNub client, String channel) {
        AccessManagerKit.assertRefused(
                "publish to " + channel,
                () -> client.publish().channel(channel).message("docs-am-check").shouldStore(false).sync());
    }
}
