package com.pubnub.docs.support

import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap

/** Masks key values, key shapes and Access Manager tokens in any text the tier prints. */
object Redact {
    const val MASK = "***"

    private val secrets: MutableSet<String> = ConcurrentHashMap.newKeySet()
    private val shapes =
        listOf(
            Regex("""\b(?:pub|sub|sec)-c-[0-9a-fA-F-]{20,}"""),
            // Access Manager (v3) tokens are CBOR in base64url, so a real one starts with "qEF2".
            Regex("""\bqEF2[A-Za-z0-9_\-%=]{40,}"""),
        )
    private val query = Regex("""([?&](?:auth|signature|token|pnsdk_secret)=)[^&\s'"]+""", RegexOption.IGNORE_CASE)

    /** Remembers a value (key, granted token) so that [scrub] masks it and its URL encoded form. */
    @JvmStatic
    fun register(value: String?) {
        if (value == null || value.length < 6) {
            return
        }
        secrets.add(value)
        secrets.add(URLEncoder.encode(value, "UTF-8"))
    }

    @JvmStatic
    fun scrub(text: String?): String {
        if (text.isNullOrEmpty()) {
            return text.orEmpty()
        }
        var out: String = text
        for (secret in secrets.sortedByDescending { it.length }) {
            out = out.replace(secret, MASK)
        }
        for (shape in shapes) {
            out = shape.replace(out, MASK)
        }
        return query.replace(out) { it.groupValues[1] + MASK }
    }

    /** The number of registered secrets that still appear in [text]. Used by the self test, never prints a value. */
    @JvmStatic
    fun leakCount(text: String): Int = secrets.count { text.contains(it) }

    @JvmStatic
    fun registeredCount(): Int = secrets.size
}
