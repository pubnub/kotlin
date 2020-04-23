package com.pubnub.api.endpoints.objects_api.users

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNUserFields
import com.pubnub.api.models.consumer.objects_api.user.PNUpdateUserResult
import com.pubnub.api.models.consumer.objects_api.user.PNUser
import com.pubnub.api.models.consumer.objects_api.util.InclusionParamsProvider
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.*

class UpdateUser(pubnub: PubNub) : Endpoint<EntityEnvelope<PNUser>, PNUpdateUserResult>(pubnub),
    InclusionParamsProvider<PNUserFields> {

    lateinit var user: PNUser

    override var includeFields: List<PNUserFields> = emptyList()

    override fun validateParams() {
        super.validateParams()
        if (!::user.isInitialized) {
            throw PubNubException(PubNubError.USER_MISSING)
        }
        if (user.id.isBlank()) {
            throw PubNubException(PubNubError.USER_ID_MISSING)
        }
        if (user.name.isBlank()) {
            throw PubNubException(PubNubError.USER_NAME_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNUser>> {
        includeFields.run { if (isNotEmpty()) queryParams["include"] = toCsv() }

        return pubnub.retrofitManager.userService
            .updateUser(
                pubnub.configuration.subscribeKey,
                user.id,
                user,
                queryParams.encodeAuth()
            )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNUser>>): PNUpdateUserResult? {
        return PNUpdateUserResult(input.body()!!.data!!)
    }

    override fun operationType() = PNOperationType.PNUpdateUserOperation
}
