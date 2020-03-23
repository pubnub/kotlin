package com.pubnub.api.managers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.internal.bind.TypeAdapters.BOOLEAN
import com.google.gson.internal.bind.TypeAdapters.NUMBER
import com.google.gson.internal.bind.TypeAdapters.STRING
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

class MapperManager {

    private val objectMapper: Gson
    private val converterFactory: Converter.Factory

    init {
        val booleanAsIntAdapter = object : TypeAdapter<Boolean>() {
            override fun write(out: JsonWriter?, value: Boolean?) {
                if (value == null) {
                    out?.nullValue()
                } else {
                    out?.value(value)
                }
            }

            override fun read(_in: JsonReader?): Boolean? {
                return when (_in?.peek()) {
                    BOOLEAN -> _in?.nextBoolean()
                    NUMBER -> _in?.nextInt() != 0
                    STRING -> _in?.nextString()?.toBoolean()
                    else -> throw IllegalStateException("Expected BOOLEAN or NUMBER")
                }
            }
        }

        objectMapper = GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, booleanAsIntAdapter)
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, booleanAsIntAdapter)
            .create()
        converterFactory = GsonConverterFactory.create(objectMapper)
    }

    fun hasField(element: JsonElement, field: String) = element.asJsonObject.has(field)

    fun getField(element: JsonElement?, field: String): JsonElement? {
        if (element?.isJsonObject!!) {
            return element.asJsonObject.get(field)
        }
        return null
    }

    fun getArrayIterator(element: JsonElement?) = element?.asJsonArray?.iterator()

    fun getArrayIterator(element: JsonElement, field: String) = element.asJsonObject.get(field).asJsonArray.iterator()

    fun getObjectIterator(element: JsonElement) = element.asJsonObject.entrySet().iterator()

    fun getObjectIterator(element: JsonElement, field: String) =
        element.asJsonObject.get(field).asJsonObject.entrySet().iterator()

    fun elementToString(element: JsonElement?) = element?.asString

    fun elementToString(element: JsonElement?, field: String) = element?.asJsonObject?.get(field)?.asString

    fun elementToInt(element: JsonElement, field: String) = element.asJsonObject.get(field).asInt

    fun isJsonObject(element: JsonElement) = element.isJsonObject

    fun getAsObject(element: JsonElement) = element.asJsonObject

    fun getAsBoolean(element: JsonElement, field: String) = element.asJsonObject.get(field).asBoolean

    fun putOnObject(element: JsonObject, key: String, value: JsonElement) = element.add(key, value)

    fun getArrayElement(element: JsonElement, index: Int) = element.asJsonArray.get(index)

    fun elementToLong(element: JsonElement) = element.asLong

    fun elementToLong(element: JsonElement, field: String) = element.asJsonObject.get(field).asLong

    fun getAsArray(element: JsonElement) = element.asJsonArray

    @Throws(PubNubException::class)
    fun <T> fromJson(input: String?, clazz: Class<T>): T {
        return try {
            this.objectMapper.fromJson(input, clazz)
        } catch (e: JsonParseException) {
            throw PubNubException(PubNubError.PARSING_ERROR).apply {
                errorMessage = e.message
            }
        }
    }

    fun <T> convertValue(input: JsonElement?, clazz: Class<T>): T {
        return this.objectMapper.fromJson(input, clazz) as T
    }

    /*@Throws(PubNubException::class)
    fun <T> convertValue(obj: Any, clazz: Class<T>): T {
        return fromJson(toJson(obj), clazz)
    }*/

    @Throws(PubNubException::class)
    fun toJson(input: Any): String {
        return try {
            this.objectMapper.toJson(input)
        } catch (e: JsonParseException) {
            throw PubNubException(PubNubError.JSON_ERROR).apply {
                errorMessage = e.message
            }
        }
    }
}
