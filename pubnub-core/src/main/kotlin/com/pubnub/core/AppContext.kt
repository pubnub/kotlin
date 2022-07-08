package com.pubnub.core

import java.util.Collections

data class ServiceKey(val instance: Instance, val serviceName: String)

object AppContext {

    val services: MutableMap<ServiceKey, Any> = Collections.synchronizedMap(mutableMapOf())

    inline fun <reified T> ManagerHolder<*, *, *, *, *>.getService(): T {

        val res = services.getOrPut(ServiceKey(this, T::class.java.canonicalName)) {
            this.retrofitManager.transactionInstance.create(T::class.java) as Any
        }

        return res as T
    }
}
