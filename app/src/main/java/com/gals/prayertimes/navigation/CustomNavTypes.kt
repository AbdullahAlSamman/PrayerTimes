package com.gals.prayertimes.navigation

import android.net.Uri
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.gals.prayertimes.navigation.PrayerTimesNavHost.NavDestination
import kotlinx.serialization.json.Json


object CustomNavTypes {
    val NavDestination = object : NavType<NavDestination>(isNullableAllowed = false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: NavDestination
        ) = bundle.putString(key, Json.encodeToString(value))

        override fun get(
            bundle: SavedState,
            key: String
        ): NavDestination? = Json.decodeFromString(bundle.getString(key, null))

        override fun parseValue(value: String): NavDestination =
            Json.decodeFromString(Uri.decode(value))

        override fun serializeAsValue(value: NavDestination): String =
            Uri.encode(Json.encodeToString(value))
    }
}