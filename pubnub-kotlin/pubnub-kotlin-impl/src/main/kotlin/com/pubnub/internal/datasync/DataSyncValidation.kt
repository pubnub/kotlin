package com.pubnub.internal.datasync

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField

/**
 * Rejects a sort list containing a blank [PNDataSyncSortField.property]. Without this a lone blank property is
 * silently dropped (no `sort` param is sent, so the caller gets the default order), while a blank among others
 * produces a malformed `sort=name,,x`.
 */
internal fun List<PNDataSyncSortField>.requireNoBlankSortProperty() {
    if (any { it.property.isBlank() }) {
        throw PubNubException(PubNubError.INVALID_ARGUMENTS, "DataSync sort property must not be blank.")
    }
}

/**
 * Rejects a class name that is set but blank. `null` stays valid: on the User/Channel APIs it means "the built-in
 * class" (create) or "the whole family" (list), which `""` must not silently stand in for.
 */
internal fun String?.requireClassNameNotBlankIfSet() {
    if (this != null && isBlank()) {
        throw PubNubException(PubNubError.ENTITY_CLASS_MISSING)
    }
}
