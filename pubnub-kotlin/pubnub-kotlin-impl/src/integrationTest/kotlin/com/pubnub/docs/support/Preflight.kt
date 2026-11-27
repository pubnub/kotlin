package com.pubnub.docs.support

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * Preflight: fails fast with the name of every missing keyset feature, before any test runs.
 *
 * 1. Admin API check (read only). With `PUBNUB_ADMIN_API_KEY` and a keyset id per role it reads `GET /v2/keysets/{id}` and
 *    `/v2/keysets/{id}/config`, confirms that the keyset of that id holds the role's subscribe key (a boolean, never the
 *    value) and names every feature the role lacks. Without the admin key or the id the role is skipped here (a note says
 *    so) and only the probes below run.
 * 2. Live probes on the open keyset: publish (keys and network), fetch messages (Message Persistence), delete messages on
 *    an empty range (Delete-From-History) and here now (Presence, only when the Admin API did not vouch for it).
 *
 * Only feature names and flags are printed, never a key value.
 */
object Preflight {
    private const val BASE = "https://admin-api.pubnub.com/v2/keysets"
    const val ADMIN_API_VERSION = "2026-11-23"

    private val http = OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build()

    /** One required feature: [name] is what the report prints, [path] is the place in the config. */
    class Feature(val name: String, val path: List<String>, val ok: (JsonElement?) -> Boolean)

    private fun flag(
        name: String,
        vararg path: String,
        expected: Boolean = true,
    ) = Feature(name, path.toList()) { (it?.takeIf { v -> v.isJsonPrimitive }?.asBoolean ?: false) == expected }

    private fun tracksEverything(rules: JsonElement?): Boolean =
        rules?.takeIf { it.isJsonArray }?.asJsonArray?.any {
            val rule = it.asJsonObject
            rule.get("pattern")?.asString == "*" && rule.getAsJsonObject("tracking")?.get("enabled")?.asBoolean == true
        } ?: false

    private val persistence =
        listOf(
            flag("Message Persistence", "messagePersistence", "enabled"),
            flag("Message Persistence: Delete-From-History", "messagePersistence", "deleteFromHistory"),
            // 0 means unlimited, so any number is a retention. Only an unset value fails.
            Feature("Message Persistence: retention set", listOf("messagePersistence", "retention")) {
                it != null && it.isJsonPrimitive
            },
        )
    private val presence =
        listOf(
            flag("Presence", "presence", "enabled"),
            Feature("Presence: tracking rule for *", listOf("presence", "rules")) { tracksEverything(it) },
        )
    private val appContext =
        listOf(
            flag("App Context", "appContext", "enabled"),
            flag("App Context: user metadata events", "appContext", "userMetadataEvents"),
            flag("App Context: channel metadata events", "appContext", "channelMetadataEvents"),
            flag("App Context: membership events", "appContext", "membershipEvents"),
            flag("App Context: referential integrity off", "appContext", "referentialIntegrity", expected = false),
        )

    /**
     * The get-all metadata calls: allowed on the open keyset (the App Context samples list everything), disallowed on the
     * secure keyset, so the Access Manager tests can assert a refusal without the get-all permission.
     */
    private fun getAll(disallowed: Boolean): List<Feature> {
        val state =
            if (disallowed) {
                "disallowed"
            } else {
                "allowed"
            }
        return listOf(
            flag("App Context: get all user metadata $state", "appContext", "disallowGetAllUserMetadata", expected = disallowed),
            flag("App Context: get all channel metadata $state", "appContext", "disallowGetAllChannelMetadata", expected = disallowed),
        )
    }

    private val files = listOf(flag("Files", "files", "enabled"))
    private val streamController =
        listOf(
            flag("Stream Controller", "streamController", "enabled"),
            flag("Stream Controller: wildcard subscribe", "streamController", "wildcardSubscribe"),
        )
    private val accessManager =
        listOf(
            flag("Access Manager", "accessManager", "enabled"),
            flag("Access Manager: token revocation", "accessManager", "tokenRevoke"),
        )

    /** Features per role. */
    val required: Map<String, List<Feature>> =
        mapOf(
            "open" to
                persistence + presence + appContext + getAll(disallowed = false) + files + streamController +
                flag("No Access Manager on the open keyset", "accessManager", "enabled", expected = false),
            "pam" to accessManager + persistence + presence + appContext + getAll(disallowed = true) + files + streamController,
            "datasync" to
                listOf(
                    flag("DataSync", "dataSync", "enabled"),
                    Feature("DataSync: region", listOf("dataSync", "region")) { it?.isJsonPrimitive == true && it.asString.isNotEmpty() },
                ) + accessManager + persistence + presence + streamController,
        )

    private fun get(url: String): JsonObject {
        val request =
            Request.Builder().url(url)
                .header("Authorization", Env.require("admin").getValue("PUBNUB_ADMIN_API_KEY"))
                .header("PubNub-Version", ADMIN_API_VERSION)
                .build()
        http.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "Admin API answered HTTP ${response.code} for ${url.substringAfter("/v2/")}" }
            return JsonParser.parseString(response.body.string()).asJsonObject
        }
    }

    /** The names of the features of [role] that [config] lacks. */
    @JvmStatic
    fun missingFeatures(
        role: String,
        config: JsonObject,
    ): List<String> =
        required.getValue(role).filterNot { feature ->
            var node: JsonElement? = config
            for (key in feature.path) {
                node = node?.takeIf { it.isJsonObject }?.asJsonObject?.get(key)
            }
            feature.ok(node)
        }.map { "$role: ${it.name} (${it.path.joinToString(".")})" }

    private class Read(val status: String, val detail: String = "", val config: JsonObject? = null)

    private fun readFeatures(role: String): Read {
        if (Env.missing("admin").isNotEmpty()) {
            return Read("skipped", "PUBNUB_ADMIN_API_KEY is not set")
        }
        val keysetId = Env.keysetId(role)
        if (keysetId.isEmpty()) {
            return Read("skipped", "${Env.keysetIds.getValue(role)} is not set")
        }
        return try {
            val keyset = get("$BASE/$keysetId")
            val found = (keyset.getAsJsonObject("keyset") ?: keyset).get("subscribeKey")?.asString
            if (found != Env.subscribeKey(role)) {
                Read("error", "keyset $keysetId does not hold the subscribe key of role $role")
            } else {
                Read("read", config = get("$BASE/$keysetId/config"))
            }
        } catch (error: Exception) {
            Read("error", Redact.scrub("${error.javaClass.simpleName}: ${error.message}"))
        }
    }

    /** True when the selected group (or, without one, any group) lists a mobile push test class. */
    private fun runIncludesMobilePush(): Boolean {
        val selected = Env.group()
        return com.pubnub.docs.support.tools.Groups.load()
            .filter { selected == null || it.name == selected }
            .any { group -> (group.kotlin + group.java).any { it.contains(".mobilePush.") } }
    }

    /** One add-channels call on a listed test device, then its removal. The service refuses both when the add-on is off. */
    private fun probePush(client: com.pubnub.api.PubNub) {
        val device = MessagingKit.GOOGLE_TOKEN
        try {
            client.addPushNotificationsOnChannels(com.pubnub.api.enums.PNPushType.FCM, listOf("preflight-push"), device).sync()
        } finally {
            client.removeAllPushNotificationsFromDeviceWithPushToken(com.pubnub.api.enums.PNPushType.FCM, device).sync()
        }
    }

    private fun probeOpenKeyset(presenceVouched: Boolean): List<String> {
        val problems = mutableListOf<String>()
        LiveClients().use { clients ->
            val client = clients.kotlinClient("open", "preflight")
            val channel = "preflight-${System.currentTimeMillis()}"
            val probes = mutableListOf<Pair<String, () -> Unit>>()
            probes.add("publish (keys and network)" to { client.publish(channel, "preflight").sync() })
            probes.add("Message Persistence (fetch messages)" to { client.fetchMessages(listOf(channel)).sync() })
            probes.add("Delete-From-History (empty range)" to { client.deleteMessages(listOf(channel), start = 1, end = 2).sync() })
            if (!presenceVouched) {
                probes.add("Presence (here now)" to { client.hereNow(listOf(channel)).sync() })
            }
            if (runIncludesMobilePush()) {
                probes.add("Mobile Push is off on the open keyset (add and remove a device channel)" to { probePush(client) })
            }
            for ((label, call) in probes) {
                try {
                    call()
                } catch (error: Exception) {
                    problems.add(Redact.scrub("open: probe $label failed: ${error.message}"))
                }
            }
        }
        return problems
    }

    /** Returns notes and problems. */
    @JvmStatic
    @JvmOverloads
    fun run(roles: List<String>? = null): Pair<List<String>, List<String>> {
        val notes = mutableListOf<String>()
        val problems = mutableListOf<String>()
        var vouched = false
        for (role in roles ?: Env.keysetRoles) {
            if (Env.missing(role).isNotEmpty()) {
                continue
            }
            val read = readFeatures(role)
            when (read.status) {
                "read" -> {
                    val missing = missingFeatures(role, read.config!!)
                    problems.addAll(missing)
                    if (missing.isEmpty()) {
                        notes.add("$role: keyset features ok")
                    }
                    vouched = vouched || (role == "open" && read.config.getAsJsonObject("presence")?.get("enabled")?.asBoolean == true)
                }
                "skipped" -> notes.add("$role: Admin API feature check skipped (${read.detail})")
                else -> problems.add("$role: Admin API feature check failed: ${read.detail}")
            }
        }
        if ((roles == null || "open" in roles) && Env.missing("open").isEmpty()) {
            problems.addAll(probeOpenKeyset(vouched))
        }
        if ((roles == null || "datasync" in roles) && Env.missing("datasync").isEmpty()) {
            // Event publishing per class is a switch of the Admin Portal that the Admin API cannot read, so it is probed live.
            problems.addAll(DataSyncKit.probeEvents())
        }
        return notes to problems
    }

    /** Runs the preflight, prints it and returns true when it found no problem. */
    @JvmStatic
    @JvmOverloads
    fun runAndPrint(roles: List<String>? = null): Boolean {
        Env.load()
        val (notes, problems) = run(roles)
        notes.forEach { println("preflight: $it") }
        problems.forEach { println("preflight: missing: $it") }
        if (problems.isEmpty()) {
            println("preflight: keyset features ok")
        } else {
            println("preflight: ${problems.size} problems")
        }
        return problems.isEmpty()
    }
}
