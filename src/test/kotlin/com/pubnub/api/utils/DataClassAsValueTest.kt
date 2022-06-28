package com.pubnub.api.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.JsonAdapter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class DataClassAsValueTest {
    private val gson = GsonBuilder().create()
    private val expectedValue = "5150"
    val json = """{"field":"$expectedValue"}"""

    @Test
    fun deserializationHappyPath() {
        val result = gson.fromJson(json, CorrectSingleValueNoJacoco.Wrapper::class.java)
        assertEquals(CorrectSingleValueNoJacoco.Wrapper(CorrectSingleValueNoJacoco(expectedValue)), result)
    }

    @Test
    fun exceptionWhenDeserializingToClassWithMoreFields() {
        assertThrows<JsonSyntaxException> {
            gson.fromJson(json, IncorrectTwoFieldsNoJacoco.Wrapper::class.java)
        }
    }

    @Test
    fun exceptionWhenDeserializingToClassWithMoreConstructors() {
        assertThrows<JsonSyntaxException> {
            gson.fromJson(json, IncorrectTwoConstructorsNoJacoco.Wrapper::class.java)
        }
    }

    @Test
    fun serializationHappyPath() {
        val result = gson.toJson(CorrectSingleValueNoJacoco.Wrapper(CorrectSingleValueNoJacoco(expectedValue)))
        assertEquals(json, result)
    }

    @Test
    fun exceptionWhenSerializingClassWithMoreFields() {
        assertThrows<IllegalStateException> {
            gson.toJson(IncorrectTwoFieldsNoJacoco.Wrapper(IncorrectTwoFieldsNoJacoco(expectedValue, expectedValue)))
        }
    }
}

@JsonAdapter(DataClassAsValue::class)
data class CorrectSingleValueNoJacoco(val value: String) {
    data class Wrapper(val field: CorrectSingleValueNoJacoco)
}

@JsonAdapter(DataClassAsValue::class)
data class IncorrectTwoFieldsNoJacoco(val val1: String, val val2: String) {
    data class Wrapper(val field: IncorrectTwoFieldsNoJacoco)
}

@JsonAdapter(DataClassAsValue::class)
data class IncorrectTwoConstructorsNoJacoco(val value: String) {
    constructor(value: Int) : this(value.toString())

    data class Wrapper(val field: IncorrectTwoConstructorsNoJacoco)
}
