package com.pubnub.entities.objects.endpoint.internal


data class ReturningCustom(
    private val includeCustom: Boolean = false
) {

    internal fun createIncludeQueryParams(): Map<String, String> {
        return if (includeCustom) {
            mapOf("include" to "custom")
        } else {
            mapOf()
        }
    }
}

