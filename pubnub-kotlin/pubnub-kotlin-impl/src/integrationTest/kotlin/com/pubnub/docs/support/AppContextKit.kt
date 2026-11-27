package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Arrange and read-back helpers for the App Context tests (user metadata, channel metadata, memberships, members), on Kotlin
 * clients. The Java tests use them for their second client too. App Context reads lag behind writes, so every read that
 * must be exact goes through [Wait.settled], and a removal is only done when a read answers 404 for three passes in a row.
 * Nothing here is part of a sample.
 */
object AppContextKit {
    private fun isNotFound(error: Throwable): Boolean = Retry.statusCode(error) == 404

    /** [fallback] for the answer 404, any other failure goes on. */
    private fun <T> whenNotFound(
        error: PubNubException,
        fallback: T,
    ): T {
        if (!isNotFound(error)) {
            throw error
        }
        return fallback
    }

    // ---- reads -------------------------------------------------------------------------------------------------------

    /** The user metadata of [id] with custom, or null when the service answers 404. */
    @JvmStatic
    fun readUser(
        client: PubNub,
        id: String,
    ): PNUUIDMetadata? =
        try {
            Retry.retryTransient(label = "read user $id") { client.getUUIDMetadata(id, includeCustom = true).sync().data }
        } catch (error: PubNubException) {
            whenNotFound(error, null)
        }

    /** The channel metadata of [id] with custom, or null when the service answers 404. */
    @JvmStatic
    fun readChannel(
        client: PubNub,
        id: String,
    ): PNChannelMetadata? =
        try {
            Retry.retryTransient(label = "read channel $id") { client.getChannelMetadata(id, includeCustom = true).sync().data }
        } catch (error: PubNubException) {
            whenNotFound(error, null)
        }

    /** The memberships of [user], with channel details, custom, status and type. Empty when the user has none. */
    @JvmStatic
    fun readMemberships(
        client: PubNub,
        user: String,
    ): List<PNChannelMembership> =
        try {
            Retry.retryTransient(label = "read memberships of $user") {
                client.getMemberships(
                    userId = user,
                    limit = 100,
                    include =
                        MembershipInclude(
                            includeCustom = true,
                            includeStatus = true,
                            includeType = true,
                            includeChannel = true,
                            includeChannelCustom = true,
                        ),
                ).sync().data.toList()
            }
        } catch (error: PubNubException) {
            whenNotFound(error, emptyList())
        }

    /** The members of [channel], with user details, custom, status and type. Empty when the channel has none. */
    @JvmStatic
    fun readMembers(
        client: PubNub,
        channel: String,
    ): List<PNMember> =
        try {
            Retry.retryTransient(label = "read members of $channel") {
                client.getChannelMembers(
                    channel = channel,
                    limit = 100,
                    include =
                        MemberInclude(
                            includeCustom = true,
                            includeStatus = true,
                            includeType = true,
                            includeUser = true,
                            includeUserCustom = true,
                        ),
                ).sync().data.toList()
            }
        } catch (error: PubNubException) {
            whenNotFound(error, emptyList())
        }

    // ---- waits (one-shot exact reads of App Context lag, so they settle first) ----------------------------------------

    /** Waits until the user metadata of [id] answers 404 three times in a row. */
    @JvmStatic
    fun awaitUserGone(
        client: PubNub,
        id: String,
    ) = Wait.settled("the user metadata of $id is gone") {
        assertTrue(readUser(client, id) == null, "the user metadata of $id still answers")
    }

    /** Waits until the channel metadata of [id] answers 404 three times in a row. */
    @JvmStatic
    fun awaitChannelGone(
        client: PubNub,
        id: String,
    ) = Wait.settled("the channel metadata of $id is gone") {
        assertTrue(readChannel(client, id) == null, "the channel metadata of $id still answers")
    }

    /** Waits until the user metadata of [id] answers and has the name [name]. Returns it. */
    @JvmStatic
    fun awaitUser(
        client: PubNub,
        id: String,
        name: String,
    ): PNUUIDMetadata =
        Wait.settled("the user metadata of $id has the name $name") {
            val found = readUser(client, id)
            assertTrue(found != null, "the user metadata of $id does not answer")
            assertEquals(name, found!!.name?.value, "the name of user $id")
            found
        }

    /** Waits until the channel metadata of [id] answers and has the name [name]. Returns it. */
    @JvmStatic
    fun awaitChannel(
        client: PubNub,
        id: String,
        name: String,
    ): PNChannelMetadata =
        Wait.settled("the channel metadata of $id has the name $name") {
            val found = readChannel(client, id)
            assertTrue(found != null, "the channel metadata of $id does not answer")
            assertEquals(name, found!!.name?.value, "the name of channel $id")
            found
        }

    /** Waits until [user] has exactly the memberships in [channels] (sorted by id). Returns them. */
    @JvmStatic
    fun awaitMemberships(
        client: PubNub,
        user: String,
        channels: List<String>,
    ): List<PNChannelMembership> =
        Wait.settled("the memberships of $user are $channels") {
            val found = readMemberships(client, user)
            assertEquals(channels.sorted(), found.map { it.channel.id }.sorted(), "the memberships of $user")
            found
        }

    /** Waits until [channel] has exactly the members in [users] (sorted by id). Returns them. */
    @JvmStatic
    fun awaitMembers(
        client: PubNub,
        channel: String,
        users: List<String>,
    ): List<PNMember> =
        Wait.settled("the members of $channel are $users") {
            val found = readMembers(client, channel)
            assertEquals(users.sorted(), found.map { it.uuid.id }.sorted(), "the members of $channel")
            found
        }

    // ---- arrange ------------------------------------------------------------------------------------------------------

    /** Removes the memberships and the metadata of [id] and waits until the metadata answers 404 and no membership is left. */
    @JvmStatic
    fun clearUser(
        client: PubNub,
        id: String,
    ) {
        val channels = readMemberships(client, id).map { it.channel.id }
        if (channels.isNotEmpty()) {
            Retry.retryTransient(label = "remove memberships of $id") { client.removeMemberships(channels, userId = id).sync() }
        }
        removeIgnoringNotFound { client.removeUUIDMetadata(id).sync() }
        Wait.settled("the user $id has no metadata and no membership") {
            assertTrue(readUser(client, id) == null, "the user metadata of $id still answers")
            assertEquals(emptyList<String>(), readMemberships(client, id).map { it.channel.id }, "the memberships of $id")
        }
    }

    /** Removes the members and the metadata of [id] and waits until the metadata answers 404 and no member is left. */
    @JvmStatic
    fun clearChannel(
        client: PubNub,
        id: String,
    ) {
        val users = readMembers(client, id).map { it.uuid.id }
        if (users.isNotEmpty()) {
            Retry.retryTransient(label = "remove members of $id") { client.removeChannelMembers(id, users).sync() }
        }
        removeIgnoringNotFound { client.removeChannelMetadata(id).sync() }
        Wait.settled("the channel $id has no metadata and no member") {
            assertTrue(readChannel(client, id) == null, "the channel metadata of $id still answers")
            assertEquals(emptyList<String>(), readMembers(client, id).map { it.uuid.id }, "the members of $id")
        }
    }

    private fun removeIgnoringNotFound(call: () -> Any?) {
        try {
            Retry.retryTransient(label = "remove") { call() }
        } catch (error: PubNubException) {
            if (!isNotFound(error)) {
                throw error
            }
        }
    }

    /**
     * Registers the cleanup of a test: after the test the users and channels are removed with their memberships. Call it
     * first, so a failing arrangement is cleaned too. [client] must stay alive until the cleanup, a client of the test does.
     */
    @JvmStatic
    fun cleanupAfterTest(
        client: PubNub,
        users: List<String>,
        channels: List<String>,
    ) {
        Cleanup.cleanupAfterTest("clear app context of $users and $channels") {
            channels.forEach { clearChannel(client, it) }
            users.forEach { clearUser(client, it) }
        }
    }

    /** Stores a user with a name, an email and custom data, and waits until a read shows it. */
    @JvmStatic
    @JvmOverloads
    fun seedUser(
        client: PubNub,
        id: String,
        name: String,
        custom: Map<String, Any?> = mapOf("role" to "tester"),
    ): PNUUIDMetadata {
        Retry.retryTransient(label = "seed user $id") {
            client.setUUIDMetadata(
                uuid = id,
                name = name,
                email = "${name.lowercase()}@example.com",
                custom = custom,
                type = "docs",
                status = "active",
            ).sync()
        }
        return awaitUser(client, id, name)
    }

    /** Stores a channel with a name, a description and custom data, and waits until a read shows it. */
    @JvmStatic
    @JvmOverloads
    fun seedChannel(
        client: PubNub,
        id: String,
        name: String,
        custom: Map<String, Any?> = mapOf("topic" to "docs"),
    ): PNChannelMetadata {
        Retry.retryTransient(label = "seed channel $id") {
            client.setChannelMetadata(
                channel = id,
                name = name,
                description = "Channel of the docs tests",
                custom = custom,
                type = "docs",
                status = "active",
            ).sync()
        }
        return awaitChannel(client, id, name)
    }

    /** Stores memberships of [user] in [channels] (status `active`, type `docs`) and waits until a read lists exactly them. */
    @JvmStatic
    fun seedMemberships(
        client: PubNub,
        user: String,
        channels: List<String>,
    ): List<PNChannelMembership> {
        val inputs: List<ChannelMembershipInput> =
            channels.map { PNChannelMembership.Partial(channelId = it, status = "active", type = "docs") }
        Retry.retryTransient(label = "seed memberships of $user") { client.setMemberships(inputs, userId = user).sync() }
        return awaitMemberships(client, user, channels)
    }

    /** Stores memberships of [user] with the type per channel in [typeByChannel] (status `active`) and waits until a read lists exactly them. */
    @JvmStatic
    fun seedTypedMemberships(
        client: PubNub,
        user: String,
        typeByChannel: Map<String, String>,
    ): List<PNChannelMembership> {
        val inputs: List<ChannelMembershipInput> =
            typeByChannel.map { (channel, type) -> PNChannelMembership.Partial(channelId = channel, status = "active", type = type) }
        Retry.retryTransient(label = "seed memberships of $user") { client.setMemberships(inputs, userId = user).sync() }
        return awaitMemberships(client, user, typeByChannel.keys.toList())
    }

    /** Stores members of [channel] (status `active`, type `docs`) and waits until a read lists exactly them. */
    @JvmStatic
    fun seedMembers(
        client: PubNub,
        channel: String,
        users: List<String>,
    ): List<PNMember> {
        val inputs = users.map { PNMember.Partial(uuidId = it, status = "active", type = "docs") }
        Retry.retryTransient(label = "seed members of $channel") { client.setChannelMembers(channel, inputs).sync() }
        return awaitMembers(client, channel, users)
    }

    /** Stores members of [channel] with the status per user in [statusByUser] (type `docs`) and waits until a read lists exactly them. */
    @JvmStatic
    fun seedStatusMembers(
        client: PubNub,
        channel: String,
        statusByUser: Map<String, String>,
    ): List<PNMember> {
        val inputs = statusByUser.map { (user, status) -> PNMember.Partial(uuidId = user, status = status, type = "docs") }
        Retry.retryTransient(label = "seed members of $channel") { client.setChannelMembers(channel, inputs).sync() }
        return awaitMembers(client, channel, statusByUser.keys.toList())
    }

    /** Waits until the list of all user metadata shows every id in [ids]: a one-shot list read needs it before the sample runs. */
    @JvmStatic
    fun awaitUsersListed(
        client: PubNub,
        ids: List<String>,
    ) = Wait.settled("the list of all users shows $ids") {
        val listed = Retry.retryTransient(label = "list users") { client.getAllUUIDMetadata(limit = 100).sync().data.map { it.id } }
        assertTrue(listed.containsAll(ids), "the list of all users does not show every seeded user")
    }

    /** Waits until the list of all channel metadata shows every id in [ids]. */
    @JvmStatic
    fun awaitChannelsListed(
        client: PubNub,
        ids: List<String>,
    ) = Wait.settled("the list of all channels shows $ids") {
        val listed = Retry.retryTransient(label = "list channels") { client.getAllChannelMetadata(limit = 100).sync().data.map { it.id } }
        assertTrue(listed.containsAll(ids), "the list of all channels does not show every seeded channel")
    }

    // ---- a sample that ends the JVM ------------------------------------------------------------------------------------

    /** What a sample printed in its own JVM, redacted, and its exit code. */
    class ChildResult(val exitCode: Int, val output: String)

    /**
     * Runs the `main` of [mainClass] in a JVM of its own, with the classpath of the test JVM and the environment of the test
     * run, and waits for it. For samples whose text ends in `System.exit(0)` or `exitProcess(0)`: the region cannot change, and
     * in the test JVM the exit would stop the whole run. A failing assertion of the sample ends the child with a non-zero exit.
     */
    @JvmStatic
    @JvmOverloads
    fun runInChildJvm(
        mainClass: String,
        timeoutSeconds: Long = 120,
    ): ChildResult {
        val java = File(File(System.getProperty("java.home"), "bin"), "java").path
        val classpath = System.getProperty("java.class.path")
        val process =
            // docs.sdk selects the keysets of the SDK under test (Java and Kotlin have their own), the child must use the same.
            ProcessBuilder(java, "-Ddocs.sdk=${Env.sdk}", "-cp", classpath, mainClass)
                .redirectErrorStream(true)
                .start()
        val output = StringBuilder()
        val reader =
            Thread {
                process.inputStream.bufferedReader().forEachLine { output.append(it).append('\n') }
            }
        reader.isDaemon = true
        reader.start()
        if (!process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
            process.destroyForcibly()
            reader.join(2000)
            val seen = output.toString().take(1500)
            throw AssertionError(Redact.scrub("the sample $mainClass did not end within ${timeoutSeconds}s. Output: $seen"))
        }
        reader.join(5000)
        assertFalse(output.isEmpty() && process.exitValue() != 0, "the sample process failed without output")
        return ChildResult(process.exitValue(), Redact.scrub(output.toString()))
    }
}
