package com.pubnub.api.endpoints.objects_api.users

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects_api.user.PNDeleteUserResult
import retrofit2.Call
import retrofit2.Response
import java.util.*

class DeleteUser(pubnub: PubNub) : Endpoint<Void, PNDeleteUserResult>(pubnub) {

    lateinit var userId: String

    override fun validateParams() {
        super.validateParams()
        if (!::userId.isInitialized || userId.isBlank()) {
            throw PubNubException(PubNubError.USER_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {

        return pubnub.retrofitManager.userService
            .deleteUser(
                subKey = pubnub.configuration.subscribeKey,
                userId = userId,
                options = queryParams.encodeAuth()
            )
    }

    override fun createResponse(input: Response<Void>): PNDeleteUserResult? {
        return PNDeleteUserResult()
    }

    override fun operationType() = PNOperationType.PNDeleteUserOperation
}
