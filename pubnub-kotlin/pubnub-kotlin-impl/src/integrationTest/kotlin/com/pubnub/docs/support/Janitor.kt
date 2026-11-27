package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Janitor: cleans exactly the names of [Resources], revokes the application level Access Manager (v2) grant and can wait
 * until a client without a token is refused again.
 *
 * It runs before and after a live run (see [LiveExtension]) and alone (`scripts/dev/docs-snippets.sh janitor`). It never
 * sweeps by name pattern and never lists a keyset to delete what it finds: nothing that is not listed is touched. Every
 * step is idempotent, a second pass removes nothing.
 */
object Janitor {
    private const val CLOSED_ROUNDS = 3
    private const val WORKERS = 8
    private const val PRESENCE_BATCH = 20

    class Report {
        val removed = ConcurrentHashMap<String, Int>()
        val failed = CopyOnWriteArrayList<String>()
        val critical = CopyOnWriteArrayList<String>()
        val skipped = CopyOnWriteArrayList<String>()

        fun count(what: String) {
            removed.merge(what, 1, Int::plus)
        }

        fun fail(
            label: String,
            error: Exception,
        ) {
            failed.add(Redact.scrub("$label: ${error.javaClass.simpleName}: ${error.message}"))
        }

        /** Runs one removal. A 404 is not a failure. Returns true when the call went through. */
        fun attempt(
            label: String,
            fn: () -> Unit,
        ): Boolean {
            return try {
                fn()
                true
            } catch (error: Exception) {
                if (!isMissing(error)) {
                    fail(label, error)
                }
                false
            }
        }

        /** Runs a removal and counts it under [what] when it went through. */
        fun remove(
            what: String,
            label: String,
            fn: () -> Unit,
        ) {
            if (attempt(label, fn)) {
                count(what)
            }
        }
    }

    internal fun isMissing(error: Throwable): Boolean {
        return (error is PubNubException && error.statusCode == 404) || error.message?.contains("404") == true
    }

    /** The names of [Resources] that belong on a keyset of [role]. DataSync objects are handled by [JanitorDataSync]. */
    private fun onlyOffDataSync(
        role: String,
        names: List<String>,
    ): List<String> {
        if (role == "datasync") {
            return emptyList()
        }
        return names
    }

    // A name with a star is a wildcard subscription pattern, never a channel that has history, files or occupants.
    private fun channelsFor(role: String): List<String> = onlyOffDataSync(role, Resources.channels()).filter { '*' !in it }

    private fun usersFor(role: String): List<String> = onlyOffDataSync(role, Resources.users())

    private fun groupsFor(role: String): List<String> = onlyOffDataSync(role, Resources.channelGroups())

    /** Closes the application level v2 grant (no channel, no auth key): every flag false, ttl 1. */
    @JvmStatic
    fun revokeApplicationGrant(client: PubNub) {
        client.grant(read = false, write = false, manage = false, delete = false, ttl = 1).sync()
    }

    /** One probe round: the calls that are still allowed, and the calls that failed with something other than 403. */
    private fun probeRound(
        role: String,
        attempt: Int,
    ): Pair<List<String>, List<String>> {
        val open = mutableListOf<String>()
        val unclear = mutableListOf<String>()
        LiveClients().use { clients ->
            val probe = clients.kotlinClient(role, "janitor-probe-$attempt") { secretKey = "" }
            val channel = "janitor-probe-${System.currentTimeMillis()}-$attempt"
            val calls =
                listOf<Pair<String, () -> Any?>>(
                    "publish" to { probe.publish(channel, mapOf("probe" to true)).sync() },
                    "read" to { probe.fetchMessages(listOf(channel)).sync() },
                )
            for ((label, call) in calls) {
                try {
                    call()
                    open.add(label)
                } catch (error: PubNubException) {
                    if (error.statusCode != 403) {
                        unclear.add("$label: ${error.statusCode}")
                    }
                }
            }
        }
        return open to unclear
    }

    /**
     * A client of [role] without the secret key and without a token must get 403 on publish and on a history read,
     * three times in a row. Returns the seconds it took.
     */
    @JvmStatic
    @JvmOverloads
    fun verifyApplicationGrantClosed(
        role: String = "pam",
        timeoutSeconds: Int = 120,
    ): Double {
        val started = System.nanoTime()
        var rounds = 0
        var attempt = 0
        while (true) {
            attempt++
            val (open, unclear) = probeRound(role, attempt)
            val elapsed = (System.nanoTime() - started) / 1e9
            if (open.isEmpty() && unclear.isEmpty()) {
                rounds++
                if (rounds >= CLOSED_ROUNDS) {
                    return Math.round(elapsed * 10) / 10.0
                }
                Thread.sleep(1000)
                continue
            }
            rounds = 0
            if (elapsed + 3 > timeoutSeconds) {
                val seen = (open + unclear).joinToString(", ")
                throw IllegalStateException(
                    Redact.scrub("the $role keyset still lets a client without a token through after ${timeoutSeconds}s ($seen)"),
                )
            }
            Thread.sleep(3000)
        }
    }

    private fun cleanChannel(
        client: PubNub,
        name: String,
        users: Set<String>,
        report: Report,
    ) {
        report.remove("history", "delete history of $name") { client.deleteMessages(listOf(name)).sync() }
        try {
            for (file in client.listFiles(name).sync().data) {
                report.remove("files", "delete file ${file.name} on $name") { client.deleteFile(name, file.name, file.id).sync() }
            }
        } catch (error: Exception) {
            if (!isMissing(error)) {
                report.fail("list files of $name", error)
            }
        }
        try {
            val members = client.getChannelMembers(name, limit = 100).sync().data.map { it.uuid.id }.filter { it in users }
            if (members.isNotEmpty()) {
                client.removeChannelMembers(name, members).sync()
                report.count("members")
            }
        } catch (error: Exception) {
            if (!isMissing(error)) {
                report.fail("members of $name", error)
            }
        }
        report.remove("channel_metadata", "remove channel metadata $name") { client.removeChannelMetadata(name).sync() }
    }

    private fun cleanUser(
        client: PubNub,
        name: String,
        channels: Set<String>,
        report: Report,
    ) {
        try {
            val mine = client.getMemberships(name, limit = 100).sync().data.map { it.channel.id }.filter { it in channels }
            if (mine.isNotEmpty()) {
                client.removeMemberships(mine, name).sync()
                report.count("memberships")
            }
        } catch (error: Exception) {
            if (!isMissing(error)) {
                report.fail("memberships of $name", error)
            }
        }
        report.remove("uuid_metadata", "remove user metadata $name") { client.removeUUIDMetadata(name).sync() }
    }

    /** The listed user ids that are still occupants of a listed channel, with their channels. */
    private fun occupants(
        role: String,
        client: PubNub,
        channels: List<String>,
        users: Set<String>,
        report: Report,
    ): Map<String, Set<String>> {
        val byUser = sortedMapOf<String, MutableSet<String>>()
        for (batch in channels.sorted().chunked(PRESENCE_BATCH)) {
            try {
                val result = client.hereNow(channels = batch, includeUUIDs = true).sync()
                for (entry in result.channels.values) {
                    for (occupant in entry.occupants.filter { it.uuid in users }) {
                        byUser.getOrPut(occupant.uuid) { sortedSetOf() }.add(entry.channelName)
                    }
                }
            } catch (error: Exception) {
                if (!isMissing(error)) {
                    report.fail("list occupants ($role)", error)
                }
                return byUser
            }
        }
        return byUser
    }

    /**
     * Makes every listed user id that is still an occupant of a listed channel leave it. Occupants stay for the presence
     * timeout when a client stopped without a leave, and a later here now would count them. Only user ids that are in
     * [Resources] are touched.
     */
    private fun cleanPresence(
        role: String,
        client: PubNub,
        channels: List<String>,
        users: Set<String>,
        report: Report,
    ) {
        for ((user, userChannels) in occupants(role, client, channels, users, report)) {
            LiveClients().use { clients ->
                val leaver = clients.kotlinClient(role, user)
                report.remove("presence_occupants", "leave $user") {
                    val connected = Wait.connected(leaver, userChannels)
                    leaver.subscribe(channels = userChannels.toList())
                    connected.await()
                    leaver.unsubscribeAll()
                }
            }
        }
    }

    private fun cleanPush(
        client: PubNub,
        report: Report,
    ) {
        // Without the Push Notifications add-on on the keyset nothing can be registered, so there is nothing to remove. The push
        // tests themselves fail with the service text, the janitor only notes it once.
        var addOnOff = false
        val push: (String, () -> Unit) -> Unit = { label, call ->
            if (!addOnOff) {
                try {
                    call()
                } catch (error: Exception) {
                    if (error.message?.contains("Push Notifications which is not enabled") == true) {
                        addOnOff = true
                        println("janitor: the Push Notifications add-on is not enabled on this keyset, no push devices to remove")
                    } else if (!isMissing(error)) {
                        report.fail(label, error)
                    }
                }
            }
        }
        for (device in Resources.pushDevices()) {
            push("remove push device $device") {
                client.removeAllPushNotificationsFromDeviceWithPushToken(PNPushType.FCM, device).sync()
            }
            // The service accepts only a hex token (40 or 64 characters) for APNs, any other string is an FCM token.
            val apnsToken = Regex("[0-9a-fA-F]{40}|[0-9a-fA-F]{64}").matches(device)
            for (environment in PNPushEnvironment.values().filter { apnsToken }) {
                push("remove push device $device") {
                    client.removeAllPushNotificationsFromDeviceWithPushToken(
                        PNPushType.APNS2,
                        device,
                        Resources.PUSH_TOPIC,
                        environment,
                    ).sync()
                }
            }
            if (!addOnOff) {
                report.count("push_devices")
            }
        }
    }

    private fun cleanRole(
        role: String,
        report: Report,
    ) {
        val users = Resources.users().toSet()
        val allChannels = Resources.channels().toSet()
        LiveClients().use { clients ->
            val client = clients.kotlinClient(role, "janitor")
            if (Env.hasSecret(role)) {
                try {
                    revokeApplicationGrant(client)
                    report.count("application_grant_revoked")
                } catch (error: Exception) {
                    report.critical.add(Redact.scrub("revoke of the application level grant on $role failed: ${error.message}"))
                }
            }
            val tasks = mutableListOf<Runnable>()
            channelsFor(role).forEach { name -> tasks.add(Runnable { cleanChannel(client, name, users, report) }) }
            usersFor(role).forEach { name -> tasks.add(Runnable { cleanUser(client, name, allChannels, report) }) }
            groupsFor(role).forEach { name ->
                tasks.add(
                    Runnable {
                        report.remove("channel_groups", "delete channel group $name") { client.deleteChannelGroup(name).sync() }
                    },
                )
            }
            val pool = Executors.newFixedThreadPool(WORKERS)
            try {
                tasks.map { pool.submit(it) }.forEach { it.get() }
            } finally {
                pool.shutdown()
                pool.awaitTermination(30, TimeUnit.SECONDS)
            }
            if (role != "datasync") {
                cleanPresence(role, client, channelsFor(role), usersFor(role).toSet(), report)
            }
            if (role == "open") {
                cleanPush(client, report)
            }
            if (role == "datasync") {
                JanitorDataSync.clean(client, report)
            }
        }
    }

    /** Cleans every role that has keys (or only [roles]). Never throws, the caller reads the report. */
    @JvmStatic
    @JvmOverloads
    fun run(roles: List<String>? = null): Report {
        val report = Report()
        for (role in roles ?: Env.keysetRoles) {
            if (Env.missing(role).isNotEmpty()) {
                report.skipped.add("$role: no keys")
            } else {
                cleanRole(role, report)
            }
        }
        return report
    }

    @JvmStatic
    fun formatReport(report: Report): List<String> {
        val removed = report.removed.entries.sortedBy { it.key }.joinToString(", ") { "${it.key} ${it.value}" }
        val lines = mutableListOf("janitor: " + removed.ifEmpty { "nothing to do" })
        report.skipped.forEach { lines.add("janitor: skipped $it") }
        // One line per distinct error: 80 names that fail with the same 403 are one problem, not 80.
        val groups = linkedMapOf<String, MutableList<String>>()
        for (failure in report.failed) {
            groups.getOrPut(failure.substringAfter(": ")) { mutableListOf() }.add(failure.substringBefore(": "))
        }
        for ((error, labels) in groups) {
            lines.add("janitor: FAILED ${labels.size}x ${error.take(300)}")
            labels.take(3).forEach { lines.add("janitor:   $it") }
            if (labels.size > 3) {
                lines.add("janitor:   and ${labels.size - 3} more")
            }
        }
        report.critical.forEach { lines.add("janitor: CRITICAL $it") }
        return lines
    }

    /** Runs the janitor, prints the report and returns true when it was clean. */
    @JvmStatic
    @JvmOverloads
    fun runAndPrint(roles: List<String>? = null): Boolean {
        Env.load()
        val report = run(roles)
        formatReport(report).forEach { println(it) }
        return report.failed.isEmpty() && report.critical.isEmpty()
    }
}
