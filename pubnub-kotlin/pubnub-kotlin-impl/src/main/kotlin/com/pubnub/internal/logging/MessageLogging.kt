package com.pubnub.internal.logging

import com.google.gson.JsonPrimitive
import com.pubnub.api.logging.LogContentConfig
import com.pubnub.internal.managers.MapperManager
import java.nio.ByteBuffer
import java.nio.charset.CodingErrorAction

/**
 * Mirrors the server's `message_fingerprint` input rule:
 *
 * ```js
 * let msg = typeof input !== "string" ? JSON.stringify(input) : input;
 * ```
 *
 * String values (raw [String] or string-typed [JsonPrimitive]) are returned **unquoted**. All
 * other values are serialized via [mapper] so the fingerprint matches the server's
 * `JSON.stringify(input)` branch.
 *
 * Callers that already serialized [value] for the request itself SHOULD pass the result via
 * [preComputedJson] — the helper will reuse it for non-string values instead of running
 * `mapper.toJson` a second time on multi-MB payloads.
 *
 */
internal fun getMessageFingerprintInput(
    value: Any?,
    mapper: MapperManager,
    preComputedJson: String? = null,
): String =
    when (value) {
        is String -> value
        is JsonPrimitive -> if (value.isString) {
            value.asString
        } else {
            (preComputedJson ?: mapper.toJson(value))
        }
        else -> preComputedJson ?: mapper.toJson(value)
    }

internal data class MessageLogContent(
    val display: Any,
    val pnMfp: String,
    val totalBytes: Int,
)

private const val ZERO_CAP_MARKER = "[...]"

/**
 * Builds the message-content fields for a publish/signal/subscribe debug log entry.
 *
 * `cap` is in UTF-8 bytes. [fingerprintInput] is the exact string fed into [pnMfp]; callers MUST
 * derive it using the server's `message_fingerprint` rule (string values hashed unquoted, all
 * other values hashed as canonical JSON). The helper never calls `cryptoModule.encrypt(...)` —
 * random-IV cryptors would produce different ciphertexts than the request builder, breaking
 * publisher/subscriber correlation.
 *
 * Callers that already serialize the plaintext for the request itself (Publish, Signal) SHOULD
 * pass that string via [preComputedPlaintextJson] to avoid a second `mapper.toJson(plaintext)`
 * call — non-trivial for large messages on the 2 MiB ceiling.
 *
 * Cap semantics ([cap] is always `>= 0`; [LogContentConfig] coerces negatives to `0`):
 *  - `cap == 0`              → zero-cap marker (content suppressed; pn_mfp + totalBytes still emit).
 *  - `cap == Int.MAX_VALUE`  → effectively unlimited; full plaintext is rendered without truncation.
 *  - `cap > 0`               → UTF-8 byte budget; oversize plaintext is truncated. The truncation
 *                              suffix (`… [truncated at N bytes, M total — raise … to see more]`)
 *                              is appended only when [cap] equals the SDK default
 *                              ([LogContentConfig.DEFAULT_LOGGED_MESSAGE_CONTENT_MAX_BYTES]) — for
 *                              caller-customized caps the user has already chosen the budget, so
 *                              the "raise the property" hint is noise.
 */
internal fun prepareMessageLogContent(
    plaintext: Any,
    cap: Int,
    mapper: MapperManager,
    fingerprintInput: String,
    preComputedPlaintextJson: String? = null,
): MessageLogContent {
    val plaintextJson = preComputedPlaintextJson ?: mapper.toJson(plaintext)
    val plaintextBytes = plaintextJson.toByteArray(Charsets.UTF_8)
    val totalBytes = plaintextBytes.size
    val mfp = pnMfp(fingerprintInput)

    if (cap == 0) {
        return MessageLogContent(
            display = ZERO_CAP_MARKER,
            pnMfp = mfp,
            totalBytes = totalBytes,
        )
    }

    return if (totalBytes <= cap) {
        MessageLogContent(display = plaintext, pnMfp = mfp, totalBytes = totalBytes)
    } else {
        val truncated = truncateUtf8(plaintextBytes, cap)
        val display = if (cap == LogContentConfig.DEFAULT_LOGGED_MESSAGE_CONTENT_MAX_BYTES) {
            "$truncated… [truncated at $cap bytes, $totalBytes total — set loggedMessageContentMaxBytes (bytes) to a higher value to see more, or 0 to disable message logging]"
        } else {
            "$truncated…"
        }
        MessageLogContent(display = display, pnMfp = mfp, totalBytes = totalBytes)
    }
}

private fun truncateUtf8(bytes: ByteArray, byteCap: Int): String {
    val decoder = Charsets.UTF_8.newDecoder()
        .onMalformedInput(CodingErrorAction.IGNORE)
        .onUnmappableCharacter(CodingErrorAction.IGNORE)
    return decoder.decode(ByteBuffer.wrap(bytes, 0, byteCap)).toString()
}
