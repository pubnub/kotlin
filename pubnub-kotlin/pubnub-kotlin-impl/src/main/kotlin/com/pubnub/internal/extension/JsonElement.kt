package com.pubnub.internal.extension

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.internal.crypto.decryptString
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.managers.MapperManager

private const val PN_OTHER = "pn_other"

internal fun JsonElement.tryDecryptMessage(
    cryptoModule: CryptoModule?,
    mapper: MapperManager,
    logger: PNLogger,
): Pair<JsonElement, PubNubError?> {
    cryptoModule ?: return this to null
    val inputText =
        if (mapper.isJsonObject(this)) {
            // property pn_other is used when we want to send encrypted Push Notification, not whole JSON object is encrypted but only value of pn_other property
            if (mapper.hasField(this, PN_OTHER)) {
                // JSON with pn_other property indicates that this is encrypted Push Notification
                mapper.elementToString(this, PN_OTHER)
            } else {
                // plain JSON object indicates that this is not encrypted message
                return this to logAndReturnDecryptionError(logger)
            }
        } else if (isJsonPrimitive && asJsonPrimitive.isString) {
            // String may represent not encrypted string or encrypted data. We will check this when decrypting.
            mapper.elementToString(this)
        } else {
            // Input represents some other Json structure, such as JsonArray
            return this to logAndReturnDecryptionError(logger)
        }

    val outputText =
        try {
            cryptoModule.decryptString(inputText!!)
        } catch (e: Exception) {
            return this to logAndReturnDecryptionError(logger)
        }

    var outputObject = mapper.fromJson(outputText, JsonElement::class.java)

    mapper.getField(this, PN_OTHER)?.let {
        val objectNode = mapper.getAsObject(this)
        mapper.putOnObject(objectNode, PN_OTHER, outputObject)
        outputObject = objectNode
    }

    return outputObject to null
}

private fun logAndReturnDecryptionError(logger: PNLogger): PubNubError {
    val pnError = PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED
    logger.warn(
        LogMessage(
            location = "JsonElement.tryDecryptMessage",
            message = LogMessageContent.Text(pnError.message),
        )
    )
    return pnError
}
