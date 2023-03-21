package com.gals.prayertimes.repository.remote.model

import com.google.gson.annotations.SerializedName


data class PrayerResponse(
    @SerializedName("id")
    val objectId: String,
    @SerializedName("sDate")
    val sDate: String,
    @SerializedName("mDate")
    val mDate: String,
    @SerializedName("fajer")
    val fajer: String,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("duhr")
    val duhr: String,
    @SerializedName("asr")
    val asr: String,
    @SerializedName("maghrib")
    val maghrib: String,
    @SerializedName("isha")
    val isha: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updateAt: String
)

data class PrayersResponse(
    @SerializedName("sDate") val sDate: String,
    @SerializedName("mDate") val mDate: String,
    @SerializedName("prayers") val prayers: List<SinglePrayer>
)

data class SinglePrayer(
    val name: PrayerType,
    val time: String
)

enum class PrayerType(val value: String){
    FAJER("fajer"),
    SUNRISE("sunrise"),
    DUHR("duhr"),
    ASR("asr"),
    MAGHRIB("maghrib"),
    ISHA("isha"),
    UNKNOWN("unknown");

    companion object{
        infix fun fromValue(value: String): PrayerType {
            val map = PrayerType.values().associateBy { it.value }
            return map[value] ?: UNKNOWN
        }
    }
}
