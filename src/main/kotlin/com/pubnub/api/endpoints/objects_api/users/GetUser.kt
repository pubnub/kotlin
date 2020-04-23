package com.pubnub.api.endpoints.objects_api.users

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNUserFields
import com.pubnub.api.models.consumer.objects_api.user.PNGetUserResult
import com.pubnub.api.models.consumer.objects_api.user.PNUser
import com.pubnub.api.models.consumer.objects_api.util.InclusionParamsProvider
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class GetUser(pubnub: PubNub) : Endpoint<EntityEnvelope<PNUser>, PNGetUserResult>(pubnub),
    InclusionParamsProvider<PNUserFields> {

    lateinit var userId: String

    override var includeFields: List<PNUserFields> = emptyList()

    override fun validateParams() {
        super.validateParams()
        if (!::userId.isInitialized || userId.isBlank()) {
            throw PubNubException(PubNubError.USER_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNUser>> {
        includeFields.run { if (isNotEmpty()) queryParams["include"] = toCsv() }

        return pubnub.retrofitManager.userService
            .getUser(
                pubnub.configuration.subscribeKey,
                userId,
                queryParams.encodeAuth()
            )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNUser>>): PNGetUserResult? {
        return PNGetUserResult(input.body()!!.data!!)
    }

    override fun operationType() = PNOperationType.PNCreateUserOperation
}
