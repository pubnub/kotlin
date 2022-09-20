package com.pubnub.contract

import com.google.gson.Gson
import java.io.File
import java.util.Locale

val gson = Gson()

fun readPersonaFile(personaName: String) = File(
    "src/test/resources/features/data/${
    personaName.lowercase(
        Locale.getDefault()
    )
    }.json"
).readText(Charsets.UTF_8)

inline fun <reified T> loadPersona(personaName: String): T = gson.fromJson(readPersonaFile(personaName), T::class.java)
