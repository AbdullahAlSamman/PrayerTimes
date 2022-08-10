package com.gals.prayertimes.model

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
    }
}