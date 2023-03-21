package com.gals.prayertimes.repository.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayers")
data class PrayerEntity(
    @PrimaryKey val objectId: String,
    val sDate: String,
    val mDate: String,
    val fajer: String,
    val sunrise: String,
    val duhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
) {

    companion object {
        val EMPTY: PrayerEntity by lazy {
            PrayerEntity(
                objectId = "",
                sDate = "",
                mDate = "",
                fajer = "",
                sunrise = "",
                duhr = "",
                asr = "",
                maghrib = "",
                isha = ""
            )
        }

        fun PrayerEntity.isValid(): Boolean = this.objectId != "" && this.sDate != ""
    }
}
