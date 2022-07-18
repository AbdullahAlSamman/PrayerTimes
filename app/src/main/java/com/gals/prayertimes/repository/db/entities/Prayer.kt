package com.gals.prayertimes.repository.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayers")
data class Prayer(
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
        val EMPTY: Prayer by lazy {
            Prayer(
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

        fun Prayer.isValid(): Boolean = this.objectId != null && this.sDate != null
    }
}
