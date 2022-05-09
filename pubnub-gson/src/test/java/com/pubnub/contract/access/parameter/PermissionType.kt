package com.pubnub.contract.access.parameter

import io.cucumber.java.ParameterType

enum class PermissionType {
    READ,
    WRITE,
    GET,
    MANAGE,
    UPDATE,
    JOIN,
    DELETE,
}


@ParameterType(".*")
fun permissionType(name: String): PermissionType {
    return PermissionType.valueOf(name)
}
