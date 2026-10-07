@file:JvmName("DataSyncErrors")

package com.pubnub.api.models.consumer.datasync

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.pubnub.api.PubNubException

private const val DATASYNC_ERROR_CODE_PREFIX = "DS-"

/**
 * Parses the DataSync error envelope (`{"errors":[{"errorCode":"DS-xxxx","message":...}]}`) carried in
 * [PubNubException.jso].
 *
 * Returns an empty list when the failure isn't a DataSync server error: no body, a non-JSON body, or a body without
 * `DS-` codes (e.g. a `403` produced in front of DataSync). Never throws.
 *
 * Java: `DataSyncErrors.from(exception)`.
 */
@JvmName("from")
fun PubNubException.dataSyncErrors(): List<PNDataSyncError> = parseDataSyncErrors(jso)

/**
 * Code of the first DataSync error item, e.g. `DS-0302`, or `null` when [dataSyncErrors] is empty.
 *
 * Java: `DataSyncErrors.firstCode(exception)`.
 */
@JvmName("firstCode")
fun PubNubException.dataSyncErrorCode(): String? = dataSyncErrors().firstOrNull()?.code

private fun parseDataSyncErrors(json: String?): List<PNDataSyncError> {
    if (json.isNullOrBlank()) {
        return emptyList()
    }
    val root =
        try {
            JsonParser.parseString(json)
        } catch (e: JsonParseException) {
            return emptyList()
        } catch (e: IllegalStateException) {
            return emptyList()
        }
    val errors = (root as? JsonObject)?.get("errors")?.takeIf { it.isJsonArray }?.asJsonArray ?: return emptyList()
    return errors.mapNotNull { item -> (item as? JsonObject)?.toDataSyncError() }
}

private fun JsonObject.toDataSyncError(): PNDataSyncError? {
    val code = stringOrNull("errorCode")?.takeIf { it.startsWith(DATASYNC_ERROR_CODE_PREFIX) } ?: return null
    return PNDataSyncError(
        code = code,
        message = stringOrNull("message").orEmpty(),
        path = stringOrNull("path"),
        location = (get("location") as? JsonObject)?.toDataSyncErrorLocation(),
    )
}

private fun JsonObject.toDataSyncErrorLocation(): PNDataSyncErrorLocation? {
    val offset = intOrNull("offset") ?: return null
    val length = intOrNull("length") ?: return null
    return PNDataSyncErrorLocation(offset = offset, length = length)
}

private fun JsonObject.stringOrNull(name: String): String? =
    get(name)?.primitiveOrNull()?.takeIf { it.isString }?.asString

private fun JsonObject.intOrNull(name: String): Int? =
    get(name)?.primitiveOrNull()?.takeIf { it.isNumber }?.asNumber?.toInt()

private fun JsonElement.primitiveOrNull() = takeIf { it.isJsonPrimitive }?.asJsonPrimitive
