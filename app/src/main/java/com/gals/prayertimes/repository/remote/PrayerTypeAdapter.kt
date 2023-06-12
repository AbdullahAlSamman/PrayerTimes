package com.gals.prayertimes.repository.remote

import com.gals.prayertimes.repository.remote.model.PrayerType
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import com.gals.prayertimes.repository.remote.model.SinglePrayer
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class PrayerTypeAdapter : JsonDeserializer<PrayersResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PrayersResponse {
        val prayers: MutableList<SinglePrayer> = mutableListOf()
        val element = json?.asJsonObject

        val listKeys = element?.getAsJsonObject(PRAYERS_TAG)?.keySet()
        val list = element?.getAsJsonObject(PRAYERS_TAG)

        listKeys?.forEach { key ->
            prayers.add(
                SinglePrayer(
                    name = PrayerType.fromValue(key),
                    time = jsonElementToString(list, key)
                )
            )
        }

        return PrayersResponse(
            sDate = jsonElementToString(element, S_DATE_TAG),
            mDate = jsonElementToString(element, M_DATE_TAG),
            prayers = prayers
        )
    }

    private fun jsonElementToString(element: JsonObject?, elementName: String): String =
        if (element?.has(elementName) == true) {
            element.get(elementName).asString
        } else {
            ""
        }

    companion object {
        private const val S_DATE_TAG = "sDate"
        private const val M_DATE_TAG = "mDate"
        private const val PRAYERS_TAG = "prayers"
    }
}