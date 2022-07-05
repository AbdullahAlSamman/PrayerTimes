package com.gals.prayertimes.model

import com.gals.prayertimes.EntityPrayer

data class Prayer(
    var objectId: String?,
    var sDate: String?,
    var mDate: String?,
    var fajer: String?,
    var sunrise: String?,
    var duhr: String?,
    var asr: String?,
    var maghrib: String?,
    var isha: String?
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

        fun Prayer.toEntity(): EntityPrayer =
            EntityPrayer(
                objectId = this.objectId.toString(),
                sDate = this.sDate.toString(),
                mDate = this.mDate.toString(),
                fajer = this.fajer.toString(),
                sunrise = this.sunrise.toString(),
                duhr = this.duhr.toString(),
                asr = this.asr.toString(),
                maghrib = this.maghrib.toString(),
                isha = this.isha.toString()
            )

        fun Prayer.isValid(): Boolean = objectId != null && sDate != null
    }
}