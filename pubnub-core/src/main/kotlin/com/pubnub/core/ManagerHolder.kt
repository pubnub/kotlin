package com.pubnub.core

interface ManagerHolder<Configuration, TokenManager, TelemetryManager, RetrofitManager : com.pubnub.core.RetrofitManager, Mapper> :
    Instance {
    val configuration: Configuration
    val tokenManager: TokenManager
    val telemetryManager: TelemetryManager
    val retrofitManager: RetrofitManager
    val mapper: Mapper
}
