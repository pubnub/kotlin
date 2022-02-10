package com.pubnub.api.network

interface HttpCallDescr {
    val id: String
}

interface HttpCall {
    val id: String
}

class NetworkModule {

    fun call(httpCall: HttpCallDescr)  {

    }

    fun cancel(id: String) {

    }



}