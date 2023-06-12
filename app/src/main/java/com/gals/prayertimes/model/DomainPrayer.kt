package com.gals.prayertimes.model

data class DomainPrayer(
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
        val EMPTY: DomainPrayer by lazy {
            DomainPrayer(
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
    }
}