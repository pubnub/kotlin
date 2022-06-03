package com.pubnub.api.endpoints.objects.internal

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.SortBase

data class ReturningCollectionOfMembership (
    private val limit: Int? = null,
    private val page: PNPage? = null,
    private val filter: String? = null,
    private val sort: Collection<SortBase> = listOf(),
    private val includeCount: Boolean = false
) : ReturningCollectionBase(limit, page, filter, sort, includeCount)
