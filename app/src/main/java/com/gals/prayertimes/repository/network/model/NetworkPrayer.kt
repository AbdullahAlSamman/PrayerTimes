package com.gals.prayertimes.repository.network.model

import com.google.gson.annotations.SerializedName


data class NetworkPrayer(
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
