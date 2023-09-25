package com.pubnub.extension

import com.google.gson.JsonElement
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.decryptString
import com.pubnub.api.managers.MapperManager

internal fun JsonElement.processHistoryMessage(cryptoModule: CryptoModule?, mapper: MapperManager): JsonElement {
    cryptoModule ?: return this

    val inputText =
        if (mapper.isJsonObject(this) && mapper.hasField(
                this,
                "pn_other"
            )
        ) {
            mapper.elementToString(this, "pn_other")
        } else {
            mapper.elementToString(this)
        }

    val outputText = cryptoModule.decryptString(inputText!!)

    var outputObject = mapper.fromJson(outputText, JsonElement::class.java)

    mapper.getField(this, "pn_other")?.let {
        val objectNode = mapper.getAsObject(this)
        mapper.putOnObject(objectNode, "pn_other", outputObject)
        outputObject = objectNode
    }

    return outputObject
}
