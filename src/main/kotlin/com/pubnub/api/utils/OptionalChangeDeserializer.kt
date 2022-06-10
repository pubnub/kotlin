package com.pubnub.api.utils

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class OptionalChangeDeserializer(private val gson: Gson, private val type: Type) :
    TypeAdapter<OptionalChange<Any>>() {

    override fun write(out: JsonWriter, value: OptionalChange<Any>) {
        out.jsonValue(gson.toJson(value.value))
    }

    override fun read(_in: JsonReader): OptionalChange<Any> {
        return when (_in.peek()) {
            JsonToken.NULL -> {
                _in.nextNull()
                OptionalChange.Removed()
            }
            else -> {
                OptionalChange.Changed(
                    gson.fromJson(
                        _in,
                        (type as ParameterizedType).actualTypeArguments[0]
                    )
                )
            }
        }
    }

    class Factory : TypeAdapterFactory {
        override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
            return if (type.rawType == OptionalChange::class.java) {
                OptionalChangeDeserializer(gson, type.type) as TypeAdapter<T>
            } else {
                null
            }
        }
    }
}
