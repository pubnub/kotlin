package com.pubnub.docs.support

import java.io.File

/**
 * Key loader. Fixed env names grouped in roles. Real env vars win over the optional env file. The file is
 * `LIVE_ENV_FILE`, or `.env.local` next to the tier README when that is not set. There is no fallback to demo keys and
 * only "set" or "missing" is ever printed per role.
 */
object Env {
    /** Env names per role. */
    val roles: Map<String, List<String>> =
        linkedMapOf(
            "open" to listOf("PUBLISH_KEY", "SUBSCRIBE_KEY"),
            "pam" to listOf("PAM_PUBLISH_KEY", "PAM_SUBSCRIBE_KEY", "PAM_SECRET_KEY"),
            "datasync" to listOf("DS_PUBLISH_KEY", "DS_SUBSCRIBE_KEY", "DS_SECRET_KEY"),
            // Read only Admin API access for the preflight. No test uses it.
            "admin" to listOf("PUBNUB_ADMIN_API_KEY"),
        )

    /** The roles that are keysets (the others only carry credentials for tooling). */
    val keysetRoles: List<String> = listOf("open", "pam", "datasync")

    /** Not secret: keyset ids for the Admin API feature read of the preflight. */
    val keysetIds: Map<String, String> =
        mapOf(
            "open" to "DOCS_SNIPPETS_OPEN_KEYSET_ID",
            "pam" to "DOCS_SNIPPETS_PAM_KEYSET_ID",
            "datasync" to "DOCS_SNIPPETS_DS_KEYSET_ID",
        )

    val switches: List<String> = listOf("LIVE_JANITOR", "LIVE_PREFLIGHT", "LIVE_GROUP")

    /** Other names the tier reads from the environment, never from the file. */
    val toolNames: List<String> = listOf("LIVE_ENV_FILE", "DOCS_REPO", "LIVE_MANIFEST_WRITE", "LIVE_EQUIVALENCE_STRICT")

    private val subscribeKeyNames = mapOf("open" to "SUBSCRIBE_KEY", "pam" to "PAM_SUBSCRIBE_KEY", "datasync" to "DS_SUBSCRIBE_KEY")
    private val publishKeyNames = mapOf("open" to "PUBLISH_KEY", "pam" to "PAM_PUBLISH_KEY", "datasync" to "DS_PUBLISH_KEY")
    private val secretKeyNames = mapOf("pam" to "PAM_SECRET_KEY", "datasync" to "DS_SECRET_KEY")

    /**
     * The SDK whose keysets this JVM uses: `kotlin` (default) or `java`, from the system property `docs.sdk` that the
     * docsSnippetsTest task of each module sets. The Java tests have their own keysets, so the Kotlin and Java runs can
     * go in parallel. For `java` every key and keyset id name gets the prefix `JAVA_` (`JAVA_PUBLISH_KEY`,
     * `DOCS_SNIPPETS_JAVA_OPEN_KEYSET_ID`). The Admin API key, the switches and the tool names are shared.
     */
    @JvmStatic
    val sdk: String = System.getProperty("docs.sdk", "kotlin").trim().lowercase().ifEmpty { "kotlin" }

    private val sdkPrefix: String =
        if (sdk == "kotlin") {
            ""
        } else {
            sdk.uppercase() + "_"
        }

    /** The env name that holds [name] for this JVM's SDK. */
    @JvmStatic
    fun actualName(name: String): String =
        when {
            sdkPrefix.isEmpty() || name == "PUBNUB_ADMIN_API_KEY" || name in switches || name in toolNames -> name
            name.startsWith("DOCS_SNIPPETS_") -> "DOCS_SNIPPETS_" + sdkPrefix + name.removePrefix("DOCS_SNIPPETS_")
            else -> sdkPrefix + name
        }

    private var fileValues: Map<String, String>? = null

    private fun knownNames(): Set<String> = (roles.values.flatten() + keysetIds.values + switches).map { actualName(it) }.toSet()

    @JvmStatic
    fun envFile(): File {
        val given = System.getenv("LIVE_ENV_FILE")
        if (given.isNullOrBlank()) {
            return Tier.file(".env.local")
        }
        return File(given)
    }

    private fun readEnvFile(file: File): Map<String, String> {
        if (!file.isFile) {
            return emptyMap()
        }
        val values = linkedMapOf<String, String>()
        for (raw in file.readLines()) {
            var line = raw.trim()
            if (line.startsWith("export ")) {
                line = line.removePrefix("export ").trim()
            }
            if (line.isEmpty() || line.startsWith("#") || !line.contains('=')) {
                continue
            }
            val name = line.substringBefore('=').trim()
            val value = line.substringAfter('=').trim().trim('"').trim('\'')
            values[name] = value
        }
        return values
    }

    /** Reads the env file once and registers every key value with [Redact]. */
    @Synchronized
    @JvmStatic
    fun load() {
        if (fileValues != null) {
            return
        }
        fileValues = readEnvFile(envFile()).filterKeys { it in knownNames() }
        roles.values.flatten().forEach { Redact.register(value(it)) }
    }

    /**
     * The value of a known name for this JVM's SDK (see [actualName]): the real environment first, then the env file.
     * Empty when unset.
     */
    @JvmStatic
    fun value(name: String): String {
        val actual = actualName(name)
        val real = System.getenv(actual)
        if (!real.isNullOrEmpty()) {
            return real
        }
        return fileValues?.get(actual).orEmpty()
    }

    /** The env names of [role] that are unset, as they are spelled for this JVM's SDK. */
    @JvmStatic
    fun missing(role: String): List<String> {
        load()
        val names = roles[role] ?: error("unknown role '$role', known roles are ${roles.keys}")
        return names.filter { value(it).isEmpty() }.map { actualName(it) }
    }

    @JvmStatic
    fun has(role: String): Boolean = missing(role).isEmpty()

    /** One line per role: "set" or "missing: NAME, NAME". Never a value. */
    @JvmStatic
    fun status(): List<String> {
        load()
        return roles.keys.map { role ->
            val gone = missing(role)
            val state =
                if (gone.isEmpty()) {
                    "set"
                } else {
                    "missing: " + gone.joinToString(", ")
                }
            "$sdk $role: $state"
        }
    }

    /** Returns the values of a role. A missing key fails with the exact names, there is no fallback. */
    @JvmStatic
    fun require(role: String): Map<String, String> {
        val gone = missing(role)
        check(gone.isEmpty()) {
            "role '$role' is missing env: ${gone.joinToString(", ")}. Set them in the environment or in the env file (see .env.example)."
        }
        return roles.getValue(role).associateWith { value(it) }
    }

    @JvmStatic
    fun keysetId(role: String): String {
        load()
        return keysetIds[role]?.let { value(it).trim() }.orEmpty()
    }

    /** LIVE_JANITOR and LIVE_PREFLIGHT are on unless set to off. */
    @JvmStatic
    fun switchOn(name: String): Boolean {
        load()
        return value(name).trim().lowercase() != "off"
    }

    /** The selected group, or null for a full run. */
    @JvmStatic
    fun group(): String? {
        load()
        return value("LIVE_GROUP").trim().takeIf { it.isNotEmpty() && it != "all" }
    }

    fun subscribeKey(role: String): String = require(role).getValue(subscribeKeyNames.getValue(role))

    fun publishKey(role: String): String = require(role).getValue(publishKeyNames.getValue(role))

    /** The secret key of a role, or null for a role without one (open). */
    fun secretKey(role: String): String? = secretKeyNames[role]?.let { require(role).getValue(it) }

    @JvmStatic
    fun hasSecret(role: String): Boolean = secretKeyNames.containsKey(role)

    /** For the Java tree and for tools: prints [status] with a prefix. Never prints a value. */
    @JvmStatic
    fun printStatus() {
        status().forEach { println("live roles: $it") }
    }
}
