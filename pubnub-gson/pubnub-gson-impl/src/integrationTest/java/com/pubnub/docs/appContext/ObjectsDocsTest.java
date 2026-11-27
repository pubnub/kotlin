package com.pubnub.docs.appContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNRemoveChannelMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNGetChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNManageChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNMembers;
import com.pubnub.api.java.models.consumer.objects_api.member.PNRemoveChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNSetChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUser;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNManageMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNSetMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNGetUUIDMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNRemoveUUIDMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.utils.PatchValue;
import com.pubnub.docs.support.AppContextKit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page objects (docs/sdks/java/api-reference/objects.md): App Context user metadata, channel
 * metadata, memberships and members. One test per snippet. The text between a start marker and {@code snippet.end} is the
 * published sample, the arrangement is above it and the assertions below it. Every object is reset to a known state before
 * the sample writes it, reads that must be exact wait for the service to settle (App Context reads lag behind writes), and
 * {@code custom} appears in a result only when the sample asks for it.
 *
 * <p>The sample's own client is a Java client, the second client of a test (the reader) is a Kotlin client, so the Kotlin
 * helpers of {@code AppContextKit} work on it.
 */
@ExtendWith(LiveExtension.class)
class ObjectsDocsTest {
    private static final String OPEN = "open";

    // The user id of the sample's own client: the samples without a user id act on it.
    private static final String ME = "docs-appctx-java";

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub reader(LiveClients clients) {
        return clients.kotlinClient(OPEN, "docs-reader");
    }

    private static <T> T valueOf(PatchValue<T> patch) {
        return patch == null ? null : patch.getValue();
    }

    private static Object customOf(PatchValue<Map<String, Object>> custom, String key) {
        Map<String, Object> map = valueOf(custom);
        return map == null ? null : map.get(key);
    }

    private static List<String> ids(Collection<PNMembership> memberships) {
        return memberships.stream().map(membership -> membership.getChannel().getId()).collect(Collectors.toList());
    }

    private static List<String> userIds(Collection<PNMembers> members) {
        return members.stream().map(member -> member.getUuid().getId()).collect(Collectors.toList());
    }

    private static Map<String, String> typed(String... pairs) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put(pairs[i], pairs[i + 1]);
        }
        return map;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // User metadata
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void getAllUUIDMetadataApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = reader(clients);
        List<String> users = Arrays.asList("user-1", "user-2");
        AppContextKit.cleanupAfterTest(reader, users, Collections.emptyList());
        for (String user : users) {
            AppContextKit.clearUser(reader, user);
        }
        AppContextKit.seedUser(reader, "user-1", "Alice", Collections.singletonMap("role", "Developer"));
        AppContextKit.seedUser(reader, "user-2", "Bob", Collections.singletonMap("role", "Manager"));
        AppContextKit.awaitUsersListed(reader, users);
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.appContext.samples.GetAllUUIDMetadataApp.main(new String[0]);

        Wait.eventuallyRun("the sample printed the list", () -> out.assertPrinted("Total Count: "));
        assertTrue(out.lines("Error retrieving UUID metadata").isEmpty(), "the sample printed an error");
        String text = out.text();
        for (String user : users) {
            assertTrue(text.contains("UUID: " + user), "the list does not show " + user);
        }
        out.assertPrinted("Name: Alice");
        out.assertPrinted("Email: alice@example.com");
        out.assertPrinted("Custom: {role=Developer}");
        out.assertPrinted("Name: Bob");
        out.assertPrinted("Custom: {role=Manager}");
        // sorted by id ascending: user-1 before user-2
        assertTrue(text.indexOf("UUID: user-1") < text.indexOf("UUID: user-2"), "the list is not sorted by id");
    }

    @Test
    void getUUIDMetadataBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList(ME), Collections.emptyList());
        AppContextKit.clearUser(reader, ME);
        AppContextKit.seedUser(reader, ME, "Foo", Collections.singletonMap("role", "tester"));

        // snippet.getUUIDMetadataBasic
        pubNub.getUUIDMetadata().async(result -> { /* check result */ });
        // snippet.end

        PNGetUUIDMetadataResult result = (PNGetUUIDMetadataResult) recorder.awaitOk("getUUIDMetadata");
        assertEquals(200, result.getStatus());
        assertEquals(ME, result.getData().getId());
        assertEquals("Foo", valueOf(result.getData().getName()));
        assertEquals("foo@example.com", valueOf(result.getData().getEmail()));
        assertNull(result.getData().getCustom(), "custom is returned although the sample did not ask for it");
    }

    @Test
    void setUUIDMetadataBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList(ME), Collections.emptyList());
        AppContextKit.clearUser(reader, ME);

        // snippet.setUUIDMetadataBasic
        PNSetUUIDMetadataResult pnSetUUIDMetadataResult = pubNub.setUUIDMetadata()
                .name("Foo")
                .profileUrl("http://example.com")
                .email("foo@example.com")
                .includeCustom(true)
                .sync();
        // snippet.end

        assertEquals(200, pnSetUUIDMetadataResult.getStatus());
        PNUUIDMetadata data = pnSetUUIDMetadataResult.getData();
        assertEquals(ME, data.getId());
        assertEquals("Foo", valueOf(data.getName()));
        assertEquals("http://example.com", valueOf(data.getProfileUrl()));
        assertEquals("foo@example.com", valueOf(data.getEmail()));
        com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata stored = AppContextKit.awaitUser(reader, ME, "Foo");
        assertEquals("http://example.com", stored.getProfileUrl().getValue());
        assertEquals("foo@example.com", stored.getEmail().getValue());
    }

    @Test
    void removeUUIDMetadataBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList(ME), Collections.emptyList());
        AppContextKit.clearUser(reader, ME);
        AppContextKit.seedUser(reader, ME, "Foo");

        // snippet.removeUUIDMetadataBasic
        pubNub.removeUUIDMetadata().async(result -> { /* check result */ });
        // snippet.end

        PNRemoveUUIDMetadataResult result = (PNRemoveUUIDMetadataResult) recorder.awaitOk("removeUUIDMetadata");
        assertEquals(200, result.getStatus());
        AppContextKit.awaitUserGone(reader, ME);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Channel metadata
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void getAllChannelsMetadataBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.emptyList(), Collections.singletonList("myChannel"));
        AppContextKit.clearChannel(reader, "myChannel");
        AppContextKit.seedChannel(reader, "myChannel", "Some Name");
        AppContextKit.awaitChannelsListed(reader, Collections.singletonList("myChannel"));

        // snippet.getAllChannelsMetadataBasic
        PNGetAllChannelsMetadataResult pnGetAllChannelsMetadataResult = pubNub.getAllChannelsMetadata().sync();
        // snippet.end

        assertEquals(200, pnGetAllChannelsMetadataResult.getStatus());
        PNChannelMetadata mine = pnGetAllChannelsMetadataResult.getData().stream()
                .filter(channel -> "myChannel".equals(channel.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(mine, "the list of all channel metadata does not show myChannel");
        assertEquals("Some Name", valueOf(mine.getName()));
        assertNull(mine.getCustom(), "custom is returned although the sample did not ask for it");
        assertNull(pnGetAllChannelsMetadataResult.getTotalCount(), "a total count is returned although the sample did not ask for it");
    }

    @Test
    void getChannelMetadataBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.emptyList(), Collections.singletonList("myChannel"));
        AppContextKit.clearChannel(reader, "myChannel");
        AppContextKit.seedChannel(reader, "myChannel", "Some Name", Collections.singletonMap("topic", "docs"));

        // snippet.getChannelMetadataBasic
        PNGetChannelMetadataResult pnGetChannelMetadataResult = pubNub.getChannelMetadata()
                .channel("myChannel")
                .sync();
        // snippet.end

        assertEquals(200, pnGetChannelMetadataResult.getStatus());
        assertEquals("myChannel", pnGetChannelMetadataResult.getData().getId());
        assertEquals("Some Name", valueOf(pnGetChannelMetadataResult.getData().getName()));
        assertEquals("Channel of the docs tests", valueOf(pnGetChannelMetadataResult.getData().getDescription()));
        assertNull(pnGetChannelMetadataResult.getData().getCustom(), "custom is returned although the sample did not ask for it");
    }

    @Test
    void setChannelMetadataBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.emptyList(), Collections.singletonList("myChannel"));
        AppContextKit.clearChannel(reader, "myChannel");
        AppContextKit.seedChannel(reader, "myChannel", "Old Name", Collections.singletonMap("topic", "docs"));

        // snippet.setChannelMetadataBasic
        pubNub.setChannelMetadata()
                .channel("myChannel")
                .name("Some Name")
                .includeCustom(true)
                .async(result -> { /* check result */ });
        // snippet.end

        // The recorder names a call after the step that returns the endpoint: this builder chain records under "channel".
        PNSetChannelMetadataResult result = (PNSetChannelMetadataResult) recorder.awaitOk("channel");
        assertEquals(200, result.getStatus());
        assertEquals("myChannel", result.getData().getId());
        assertEquals("Some Name", valueOf(result.getData().getName()));
        // includeCustom(true): the stored custom data comes back, the sample did not change it
        assertEquals("docs", customOf(result.getData().getCustom(), "topic"));
        com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata stored = AppContextKit.awaitChannel(reader, "myChannel", "Some Name");
        assertEquals("docs", stored.getCustom().getValue().get("topic"));
    }

    @Test
    void iterativelyUpdateExistingMetadataMain(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.emptyList(), Collections.singletonList("channelId"));
        AppContextKit.clearChannel(reader, "channelId");

        // The sample ends with System.exit(0), so it runs in a JVM of its own. Its own assertions end it with a failure.
        AppContextKit.ChildResult result = AppContextKit.runInChildJvm("com.pubnub.docs.appContext.samples.UpdateCustomChannelMetadataApp");

        assertEquals(0, result.getExitCode(), "the sample failed. Output: " + result.getOutput().substring(0, Math.min(1500, result.getOutput().length())));
        // The sample removes the channel metadata at its end.
        AppContextKit.awaitChannelGone(reader, "channelId");
    }

    @Test
    void removeChannelMetadataBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.emptyList(), Collections.singletonList("myChannel"));
        AppContextKit.clearChannel(reader, "myChannel");
        AppContextKit.seedChannel(reader, "myChannel", "Some Name");

        // snippet.removeChannelMetadataBasic
        pubNub.removeChannelMetadata()
                .channel("myChannel")
                .async(result -> { /* check result */ });
        // snippet.end

        // The recorder names a call after the step that returns the endpoint: this builder chain records under "channel".
        PNRemoveChannelMetadataResult result = (PNRemoveChannelMetadataResult) recorder.awaitOk("channel");
        assertEquals(200, result.getStatus());
        AppContextKit.awaitChannelGone(reader, "myChannel");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Memberships
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void getMembershipsBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList("userId01"), Arrays.asList("channelId01", "channelId02"));
        AppContextKit.clearUser(reader, "userId01");
        AppContextKit.clearChannel(reader, "channelId01");
        AppContextKit.clearChannel(reader, "channelId02");
        AppContextKit.seedChannel(reader, "channelId01", "First Channel", Collections.singletonMap("topic", "docs"));
        AppContextKit.seedChannel(reader, "channelId02", "Second Channel");
        AppContextKit.seedTypedMemberships(reader, "userId01", typed("channelId01", "typeB", "channelId02", "typeA"));

        // snippet.getMembershipsBasic
        PNGetMembershipsResult pnGetMembershipsResult = pubNub.getMemberships()
                .userId("userId01")
                .limit(10)
                .include(MembershipInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .includeChannelType(true)
                        .includeChannelStatus(true)
                        .build())
                .sort(Arrays.asList(PNSortKey.asc(PNSortKey.Key.TYPE)))
                .sync();
        // snippet.end

        assertEquals(200, pnGetMembershipsResult.getStatus());
        assertEquals(Integer.valueOf(2), pnGetMembershipsResult.getTotalCount());
        // sorted by the type of the membership, ascending: typeA (channelId02) before typeB (channelId01)
        assertEquals(Arrays.asList("channelId02", "channelId01"), ids(pnGetMembershipsResult.getData()));
        PNMembership second = pnGetMembershipsResult.getData().get(1);
        assertEquals("typeB", valueOf(second.getType()));
        assertEquals("active", valueOf(second.getStatus()));
        assertEquals("First Channel", valueOf(second.getChannel().getName()));
        assertEquals("docs", valueOf(second.getChannel().getType()));
        assertEquals("active", valueOf(second.getChannel().getStatus()));
        assertEquals("docs", customOf(second.getChannel().getCustom(), "topic"));
    }

    @Test
    void getMembershipsWithPagination(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        List<String> channels = Arrays.asList("channelId01", "channelId02", "myChannel", "myChannelId", "myChannelName");
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList(ME), channels);
        AppContextKit.clearUser(reader, ME);
        for (String channel : channels) {
            AppContextKit.clearChannel(reader, channel);
        }
        // five memberships, so that a page of three is followed by a page of two
        AppContextKit.seedMemberships(reader, ME, channels);
        Stdout out = Stdout.captureForTest();

        // snippet.getMembershipsWithPagination
        final PNGetMembershipsResult getMembershipsResult = pubNub.getMemberships()
                .limit(3)
                .include(MembershipInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeChannel(true)
                        .build())
                .sync();

        if (getMembershipsResult.getNext() != null) {
            final PNGetMembershipsResult getMembershipsNextPageResult = pubNub.getMemberships()
                    .page(getMembershipsResult.nextPage())
                    .limit(3)
                    .include(MembershipInclude.builder()
                            .includeCustom(true)
                            .includeStatus(true)
                            .includeType(true)
                            .includeTotalCount(true)
                            .includeChannel(true)
                            .build())
                    .sync();
            System.out.println(getMembershipsNextPageResult);
        }
        // snippet.end

        assertEquals(Integer.valueOf(5), getMembershipsResult.getTotalCount());
        assertEquals(3, getMembershipsResult.getData().size());
        assertNotNull(getMembershipsResult.getNext(), "the first page of three of five memberships has no next page");
        List<Object> reads = recorder.records("getMemberships").stream().map(record -> record.getValue()).collect(Collectors.toList());
        assertEquals(2, reads.size(), "the sample did not read two pages");
        PNGetMembershipsResult nextPage = (PNGetMembershipsResult) reads.get(1);
        assertEquals(2, nextPage.getData().size());
        // the two pages together list every membership once
        List<String> all = new ArrayList<>(ids(getMembershipsResult.getData()));
        all.addAll(ids(nextPage.getData()));
        List<String> expected = new ArrayList<>(channels);
        Collections.sort(all);
        Collections.sort(expected);
        assertEquals(expected, all);
        // the sample printed the second page
        out.assertPrinted("PNGetMembershipsResult");
    }

    @Test
    void setMembershipsBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList(ME), Collections.singletonList("myChannelId"));
        AppContextKit.clearUser(reader, ME);
        AppContextKit.clearChannel(reader, "myChannelId");
        AppContextKit.seedChannel(reader, "myChannelId", "Some Name");

        // snippet.setMembershipsBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("membership_param1", "val1");
        customMap.putIfAbsent("membership_param2", "val2");


        PNChannelMembership pnChannelMembership = PNChannelMembership.builder("myChannelId")
                .custom(customMap)
                .status("inactive")
                .type("member")
                .build();

        pubNub.setMemberships(Arrays.asList(pnChannelMembership))
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end

        PNSetMembershipResult result = (PNSetMembershipResult) recorder.awaitOk("setMemberships");
        assertEquals(200, result.getStatus());
        assertEquals(Integer.valueOf(1), result.getTotalCount());
        PNMembership membership = result.getData().get(0);
        assertEquals("myChannelId", membership.getChannel().getId());
        assertEquals("val1", customOf(membership.getCustom(), "membership_param1"));
        assertEquals("val2", customOf(membership.getCustom(), "membership_param2"));
        assertEquals("Some Name", valueOf(membership.getChannel().getName()));
        // the sample asks neither for the status nor for the type, so the result carries none
        assertNull(membership.getStatus(), "status is returned although the sample did not ask for it");
        assertNull(membership.getType(), "type is returned although the sample did not ask for it");
        com.pubnub.api.models.consumer.objects.membership.PNChannelMembership stored =
                AppContextKit.awaitMemberships(reader, ME, Collections.singletonList("myChannelId")).get(0);
        assertEquals("inactive", stored.getStatus().getValue());
        assertEquals("member", stored.getType().getValue());
    }

    @Test
    void removeMembershipsBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList(ME), Arrays.asList("myChannelName", "myChannel"));
        AppContextKit.clearUser(reader, ME);
        AppContextKit.seedMemberships(reader, ME, Arrays.asList("myChannelName", "myChannel"));

        // snippet.removeMembershipsBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("membership_param1", "val1");
        customMap.putIfAbsent("membership_param2", "val2");

        PNRemoveMembershipResult pnRemoveMembershipResult = pubNub.removeMemberships(Arrays.asList("myChannelName"))
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeType(true)
                        .includeStatus(true)
                        .build())
                .sync();
        // snippet.end

        assertEquals(200, pnRemoveMembershipResult.getStatus());
        // the result lists what is left: only the named membership is removed
        assertEquals(Collections.singletonList("myChannel"), ids(pnRemoveMembershipResult.getData()));
        assertEquals(Integer.valueOf(1), pnRemoveMembershipResult.getTotalCount());
        AppContextKit.awaitMemberships(reader, ME, Collections.singletonList("myChannel"));
    }

    @Test
    void manageMembershipsBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Collections.singletonList("userId01"), Arrays.asList("channelId01", "channelId02"));
        AppContextKit.clearUser(reader, "userId01");
        AppContextKit.clearChannel(reader, "channelId01");
        AppContextKit.clearChannel(reader, "channelId02");
        AppContextKit.seedChannel(reader, "channelId02", "Second Channel");
        AppContextKit.seedMemberships(reader, "userId01", Collections.singletonList("channelId01"));

        // snippet.manageMembershipsBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("membership_param1", "val1");
        customMap.putIfAbsent("membership_param2", "val2");

        final List<PNChannelMembership> channelMembershipsToSet = Collections.singletonList(
                PNChannelMembership.builder("channelId02")
                        .custom(customMap)
                        .status("inactive")
                        .type("member")
                        .build());
        final List<String> channelIdsToRemove = Collections.singletonList("channelId01");

        pubNub.manageMemberships(channelMembershipsToSet, channelIdsToRemove)
                .userId("userId01")
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end

        PNManageMembershipResult result = (PNManageMembershipResult) recorder.awaitOk("manageMemberships");
        assertEquals(200, result.getStatus());
        assertEquals(Collections.singletonList("channelId02"), ids(result.getData()));
        assertEquals(Integer.valueOf(1), result.getTotalCount());
        PNMembership membership = result.getData().get(0);
        assertEquals("inactive", valueOf(membership.getStatus()));
        assertEquals("member", valueOf(membership.getType()));
        assertEquals("val1", customOf(membership.getCustom(), "membership_param1"));
        assertEquals("Second Channel", valueOf(membership.getChannel().getName()));
        AppContextKit.awaitMemberships(reader, "userId01", Collections.singletonList("channelId02"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Members
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void getChannelMembersBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Arrays.asList("userId1", "userId2"), Collections.singletonList("testChannelId"));
        AppContextKit.clearChannel(reader, "testChannelId");
        AppContextKit.clearUser(reader, "userId1");
        AppContextKit.clearUser(reader, "userId2");
        AppContextKit.seedUser(reader, "userId1", "User One", Collections.singletonMap("role", "first"));
        AppContextKit.seedUser(reader, "userId2", "User Two", Collections.singletonMap("role", "second"));
        AppContextKit.seedStatusMembers(reader, "testChannelId", typed("userId1", "statusA", "userId2", "statusB"));

        // snippet.getChannelMembersBasic
        PNGetChannelMembersResult pnGetChannelMembersResult = pubNub.getChannelMembers("testChannelId")
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .sort(Arrays.asList(PNSortKey.desc(PNSortKey.Key.STATUS)))
                .sync();
        // snippet.end

        assertEquals(200, pnGetChannelMembersResult.getStatus());
        assertEquals(Integer.valueOf(2), pnGetChannelMembersResult.getTotalCount());
        // sorted by the status of the member, descending: statusB (userId2) before statusA (userId1)
        assertEquals(Arrays.asList("userId2", "userId1"), userIds(pnGetChannelMembersResult.getData()));
        PNMembers first = pnGetChannelMembersResult.getData().get(0);
        assertEquals("statusB", valueOf(first.getStatus()));
        assertEquals("docs", valueOf(first.getType()));
        assertEquals("User Two", valueOf(first.getUuid().getName()));
        assertEquals("second", customOf(first.getUuid().getCustom(), "role"));
    }

    @Test
    void setChannelMembersBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Arrays.asList("userId1", "userId2"), Collections.singletonList("testChannelId"));
        AppContextKit.clearChannel(reader, "testChannelId");
        AppContextKit.clearUser(reader, "userId1");
        AppContextKit.clearUser(reader, "userId2");
        AppContextKit.seedUser(reader, "userId1", "User One");
        AppContextKit.seedUser(reader, "userId2", "User Two");

        // snippet.setChannelMembersBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("members_param1", "val1");
        customMap.putIfAbsent("members_param2", "val2");

        final Collection<PNUser> channelMembers = Arrays.asList(
                PNUser.builder("userId1").status("status01").type("typeR").build(),
                PNUser.builder("userId2").custom(customMap).status("status02").type("typeS").build());

        pubNub.setChannelMembers("testChannelId", channelMembers)
                .include(MemberInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .includeUserStatus(true)
                        .includeUserType(true)
                        .build())
                .sort(Arrays.asList(PNSortKey.asc(PNSortKey.Key.STATUS)))
                .async(result -> { /* check result */ });
        // snippet.end

        PNSetChannelMembersResult result = (PNSetChannelMembersResult) recorder.awaitOk("setChannelMembers");
        assertEquals(200, result.getStatus());
        assertEquals(Integer.valueOf(2), result.getTotalCount());
        // sorted by the status of the member, ascending: status01 (userId1) before status02 (userId2)
        assertEquals(Arrays.asList("userId1", "userId2"), userIds(result.getData()));
        PNMembers one = result.getData().get(0);
        PNMembers two = result.getData().get(1);
        assertEquals("status01", valueOf(one.getStatus()));
        assertEquals("typeR", valueOf(one.getType()));
        assertEquals("User One", valueOf(one.getUuid().getName()));
        assertEquals("status02", valueOf(two.getStatus()));
        assertEquals("typeS", valueOf(two.getType()));
        assertEquals("val1", customOf(two.getCustom(), "members_param1"));
        assertEquals("val2", customOf(two.getCustom(), "members_param2"));
        List<com.pubnub.api.models.consumer.objects.member.PNMember> stored =
                AppContextKit.awaitMembers(reader, "testChannelId", Arrays.asList("userId1", "userId2"));
        assertEquals("typeS", stored.stream().filter(member -> "userId2".equals(member.getUuid().getId())).findFirst().get().getType().getValue());
    }

    @Test
    void removeChannelMembersBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Arrays.asList("userId", "userId1"), Collections.singletonList("channelId"));
        AppContextKit.clearChannel(reader, "channelId");
        AppContextKit.clearUser(reader, "userId");
        AppContextKit.clearUser(reader, "userId1");
        AppContextKit.seedMembers(reader, "channelId", Arrays.asList("userId", "userId1"));

        // snippet.removeChannelMembersBasic
        pubNub.removeChannelMembers("channelId", Collections.singletonList("userId"))
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end

        PNRemoveChannelMembersResult result = (PNRemoveChannelMembersResult) recorder.awaitOk("removeChannelMembers");
        assertEquals(200, result.getStatus());
        // the result lists what is left: only the named member is removed
        assertEquals(Collections.singletonList("userId1"), userIds(result.getData()));
        assertEquals(Integer.valueOf(1), result.getTotalCount());
        AppContextKit.awaitMembers(reader, "channelId", Collections.singletonList("userId1"));
    }

    @Test
    void manageChannelMembersBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, ME);
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        com.pubnub.api.PubNub reader = reader(clients);
        AppContextKit.cleanupAfterTest(reader, Arrays.asList("userId01", "userId02"), Collections.singletonList("channelId"));
        AppContextKit.clearChannel(reader, "channelId");
        AppContextKit.clearUser(reader, "userId01");
        AppContextKit.clearUser(reader, "userId02");
        AppContextKit.seedUser(reader, "userId02", "User Two");
        AppContextKit.seedMembers(reader, "channelId", Collections.singletonList("userId01"));

        // snippet.manageChannelMembersBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("members_param1", "val1");
        customMap.putIfAbsent("members_param2", "val2");

        final List<PNUser> channelMembersToSet = Collections.singletonList(
                PNUser.builder("userId02")
                        .custom(customMap)
                        .status("status02")
                        .type("type02")
                        .build());
        final List<String> channelMembersIdsToRemove = Collections.singletonList("userId01");

        pubNub.manageChannelMembers("channelId", channelMembersToSet, channelMembersIdsToRemove)
                .include(MemberInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .includeUserStatus(true)
                        .includeUserType(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end

        PNManageChannelMembersResult result = (PNManageChannelMembersResult) recorder.awaitOk("manageChannelMembers");
        assertEquals(200, result.getStatus());
        assertEquals(Collections.singletonList("userId02"), userIds(result.getData()));
        assertEquals(Integer.valueOf(1), result.getTotalCount());
        PNMembers member = result.getData().get(0);
        assertEquals("status02", valueOf(member.getStatus()));
        assertEquals("type02", valueOf(member.getType()));
        assertEquals("val1", customOf(member.getCustom(), "members_param1"));
        assertEquals("User Two", valueOf(member.getUuid().getName()));
        AppContextKit.awaitMembers(reader, "channelId", Collections.singletonList("userId02"));
    }
}
