package com.pubnub.api.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataClassAsValue<T> : JsonDeserializer<T>, JsonSerializer<T> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T {
        if ((typeOfT as? Class<*>)?.declaredFields?.size != 1) error("Expected a class with only one declared field and found class with ${(typeOfT as Class<*>).declaredFields.size} fields")
        if ((typeOfT as? Class<*>)?.constructors?.size != 1) error("Expected a class with only one declared constructor and found class with ${typeOfT.constructors.size} constructors")
        val constructor = typeOfT.constructors[0]
        val typeOfField = constructor.parameters[0].type
        return constructor.newInstance(context.deserialize(json, TypeToken.get(typeOfField).type)) as T
    }

    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        if ((typeOfSrc as? Class<*>)?.declaredFields?.size != 1) error("Expected a class with only one declared field and found class with ${(typeOfSrc as Class<*>).declaredFields.size} fields")
        val value = typeOfSrc.getMethod("component1").invoke(src)
        return context.serialize(value)
    }
}
