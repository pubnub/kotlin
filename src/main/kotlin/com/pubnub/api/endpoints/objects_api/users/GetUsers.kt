package com.pubnub.api.endpoints.objects_api.users

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNUserFields
import com.pubnub.api.models.consumer.objects_api.user.PNGetUsersResult
import com.pubnub.api.models.consumer.objects_api.user.PNUser
import com.pubnub.api.models.consumer.objects_api.util.FilteringParamsProvider
import com.pubnub.api.models.consumer.objects_api.util.InclusionParamsProvider
import com.pubnub.api.models.consumer.objects_api.util.ListingParamsProvider
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class GetUsers(pubnub: PubNub) : Endpoint<EntityArrayEnvelope<PNUser>, PNGetUsersResult>(pubnub),
    InclusionParamsProvider<PNUserFields>,
    ListingParamsProvider,
    FilteringParamsProvider {

    lateinit var userId: String

    override var includeFields: List<PNUserFields> = emptyList()
    override var limit: Int? = null
    override var start: String? = null
    override var end: String? = null
    override var withTotalCount: Boolean? = null
    override var filter: String? = null

    override fun validateParams() {
        super.validateParams()
        if (!::userId.isInitialized || userId.isBlank()) {
            throw PubNubException(PubNubError.USER_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNUser>> {
        includeFields.run { if (isNotEmpty()) queryParams["include"] = toCsv() }
        limit?.let { queryParams["limit"] = it.toString() }
        start?.let { queryParams["start"] = it }
        end?.let { queryParams["end"] = it }
        withTotalCount?.let { queryParams["count"] = it.toString() }
        filter?.let { queryParams["filter"] = it.pnUrlEncode() }

        return pubnub.retrofitManager.userService
            .getUsers(
                subKey = pubnub.configuration.subscribeKey,
                options = queryParams.encodeAuth()
            )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNUser>>): PNGetUsersResult? {
        return PNGetUsersResult().create(input.body()!!)
    }

    override fun operationType() = PNOperationType.PNGetUsersOperation

}
