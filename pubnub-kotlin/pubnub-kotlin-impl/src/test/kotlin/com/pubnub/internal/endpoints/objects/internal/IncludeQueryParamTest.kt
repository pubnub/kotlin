package com.pubnub.internal.endpoints.objects.internal

import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_KEY_INCLUDE
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_CHANNEL
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_CHANNEL_CUSTOM
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_CHANNEL_STATUS
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_CHANNEL_TYPE
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_CUSTOM
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_STATUS
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_TYPE
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_UUID
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_UUID_CUSTOM
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_UUID_STATUS
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam.Companion.QUERY_PARAM_UUID_TYPE
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IncludeQueryParamTest {
    private lateinit var objectUnderTest: IncludeQueryParam

    @Test
    fun createIncludeQueryParamsShouldReturnParamsThatHasTrue() {
        objectUnderTest = IncludeQueryParam(
            includeCustom = true,
            includeType = true,
            includeStatus = true,
            includeChannel = true,
            includeChannelCustom = true,
            includeChannelStatus = true,
            includeChannelType = true,
            includeUser = true,
            includeUserCustom = true,
            includeUserStatus = true,
            includeUserType = true
        )

        val createIncludeQueryParams = objectUnderTest.createIncludeQueryParams()

        val stringWithQueryParams: String = createIncludeQueryParams[QUERY_KEY_INCLUDE]!!
        val set = stringWithQueryParams.split(",").toSet()

        assertTrue(set.contains(QUERY_PARAM_CUSTOM))
        assertTrue(set.contains(QUERY_PARAM_TYPE))
        assertTrue(set.contains(QUERY_PARAM_STATUS))
        assertTrue(set.contains(QUERY_PARAM_CHANNEL))
        assertTrue(set.contains(QUERY_PARAM_CHANNEL_CUSTOM))
        assertTrue(set.contains(QUERY_PARAM_CHANNEL_STATUS))
        assertTrue(set.contains(QUERY_PARAM_CHANNEL_TYPE))
        assertTrue(set.contains(QUERY_PARAM_UUID))
        assertTrue(set.contains(QUERY_PARAM_UUID_CUSTOM))
        assertTrue(set.contains(QUERY_PARAM_UUID_STATUS))
        assertTrue(set.contains(QUERY_PARAM_UUID_TYPE))
    }

    @Test
    fun createIncludeQueryParamsShouldNotReturnParamsWhenAllAreFalse() {
        objectUnderTest = IncludeQueryParam(includeType = false, includeStatus = false)

        val queryParamsMap = objectUnderTest.createIncludeQueryParams()

        assertTrue(queryParamsMap.isEmpty())
    }
}
