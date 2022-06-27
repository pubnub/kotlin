package com.pubnub.api.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.JsonAdapter
import org.junit.Assert.*
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class DataClassAsValueTest {
    private val gson = GsonBuilder().create()
    private val expectedValue = "5150"
    val json = """{"field":"$expectedValue"}"""

    @Test
    fun deserializationHappyPath() {
        val result = gson.fromJson(json, CorrectSingleValue.Wrapper::class.java)
        assertEquals(CorrectSingleValue.Wrapper(CorrectSingleValue(expectedValue)), result)
    }

    @Test
    fun exceptionWhenDeserializingToClassWithMoreFields() {
        assertThrows<JsonSyntaxException> {
            gson.fromJson(json, IncorrectTwoFields.Wrapper::class.java)
        }
    }

    @Test
    fun exceptionWhenDeserializingToClassWithMoreCtors() {
        assertThrows<JsonSyntaxException> {
            gson.fromJson(json, IncorrectTwoCtors.Wrapper::class.java)
        }
    }

    @Test
    fun serializationHappyPath() {
        val result = gson.toJson(CorrectSingleValue.Wrapper(CorrectSingleValue(expectedValue)))
        assertEquals(json, result)
    }

    @Test
    fun exceptionWhenSerializingClassWithMoreFields() {
        assertThrows<IllegalStateException> {
            gson.toJson(IncorrectTwoFields.Wrapper(IncorrectTwoFields(expectedValue, expectedValue)))
        }
    }
}

@JsonAdapter(DataClassAsValue::class)
data class CorrectSingleValue(val value: String) {
    data class Wrapper (val field: CorrectSingleValue)
}

@JsonAdapter(DataClassAsValue::class)
data class IncorrectTwoFields(val val1: String, val val2: String) {
    data class Wrapper (val field: IncorrectTwoFields)
}

@JsonAdapter(DataClassAsValue::class)
data class IncorrectTwoCtors(val value: String) {
    constructor(value: Int) : this(value.toString())

    data class Wrapper (val field: IncorrectTwoCtors)
}
