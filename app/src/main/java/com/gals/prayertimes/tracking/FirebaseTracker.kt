package com.gals.prayertimes.tracking

import android.os.Bundle
import android.os.Parcelable
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Singleton

interface Tracker {
    fun logEvent(name: String, params: Map<String, Any>)
}


@Singleton
class FirebaseTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : Tracker {

    override fun logEvent(
        name: String,
        params: Map<String, Any>
    ) {
        firebaseAnalytics.logEvent(name, params.toBundle())
    }
}

private fun Map<String, Any?>.toBundle(): Bundle = Bundle().apply {
    for ((key, value) in this@toBundle) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is Double -> putDouble(key, value)
            is Boolean -> putBoolean(key, value)
            is Bundle -> putBundle(key, value)
            is Parcelable -> putParcelable(key, value)
            is Serializable -> putSerializable(key, value)
            null -> putString(key, null)
            else -> putString(key, "Unsupported type: $value")
        }
    }
}