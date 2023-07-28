package com.gals.prayertimes.model

import com.gals.prayertimes.repository.remote.model.SinglePrayer

data class Prayer(
    var sDate: String = "",
    var mDate: String = "",
    var prayers: List<SinglePrayer> = emptyList()
)
