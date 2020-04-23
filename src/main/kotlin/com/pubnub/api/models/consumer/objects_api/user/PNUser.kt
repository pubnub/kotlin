package com.pubnub.api.models.consumer.objects_api.user

import com.pubnub.api.models.consumer.objects_api.PNObject

class PNUser(id: String, var name: String) : PNObject(id) {

    var externalId: String? = null
    var profileUrl: String? = null
    var email: String? = null

    override fun toString(): String {
        return "PNUser(" +
                "name='$name', " +
                "externalId=$externalId, " +
                "profileUrl=$profileUrl, " +
                "email=$email" +
                ")" +
                " ${super.toString()}"
    }

}