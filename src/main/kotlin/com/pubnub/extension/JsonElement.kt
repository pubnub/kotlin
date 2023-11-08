package com.pubnub.extension

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.decryptString
import com.pubnub.api.managers.MapperManager
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("JsonElement")

private const val PN_OTHER = "pn_other"

internal fun JsonElement.processHistoryMessage(
    cryptoModule: CryptoModule?,
    mapper: MapperManager
): Pair<JsonElement, PubNubError?> {

    cryptoModule ?: return Pair(this, null)

    val inputText = if (mapper.isJsonObject(this)) {
        // property pn_other is used when we want to send encrypted Push Notification, not whole JSON object is encrypted but only value of pn_other property
        if (mapper.hasField(this, PN_OTHER)) {
            // JSON with pn_other property indicates that this is encrypted Push Notification
            mapper.elementToString(this, PN_OTHER)
        } else {
            // plain JSON object indicates that this is not encrypted message
            val error = logAndReturnDecryptionError()
            return Pair(this, error)
        }
    } else {
        // String may represent not encrypted string or encrypted data. We will check this when decrypting.
        mapper.elementToString(this)
    }

    val outputText = try {
        cryptoModule.decryptString(inputText!!)
    } catch (e: Exception) {
        val error = logAndReturnDecryptionError()
        return Pair(this, error)
    }

    var outputObject = mapper.fromJson(outputText, JsonElement::class.java)

    mapper.getField(this, PN_OTHER)?.let {
        val objectNode = mapper.getAsObject(this)
        mapper.putOnObject(objectNode, PN_OTHER, outputObject)
        outputObject = objectNode
    }

    return Pair(outputObject, null)
}

private fun logAndReturnDecryptionError(): PubNubError {
    val pnError = PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED
    log.warn(pnError.message)
    return pnError
}
