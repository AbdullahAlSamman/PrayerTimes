package com.gals.prayertimes.handlers.notification

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.gals.prayertimes.R

/**
 * Glance-based content for Alarm notifications.
 * Migrated from notification_service_alarm_remote_view.xml
 */
@Composable
@SuppressLint("RestrictedApi")
fun AlarmNotificationContent(
    bannerText: String,
    prayerText: String
) {
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = prayerText,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorProvider(R.color.text_color_primary_black),
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = bannerText,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorProvider(R.color.text_color_primary_black)
                ),
                modifier = GlanceModifier.padding(start = 4.dp)
            )
        }
    }
}

/**
 * Glance-based content for Permanent notifications.
 * Migrated from notification_service_permanent_remote_view.xml
 */
@Composable
@SuppressLint("RestrictedApi")
fun PermanentNotificationContent(
    bannerText: String,
    prayerText: String,
    prayerTime: String
) {
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = prayerTime,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = ColorProvider(R.color.text_color_primary_black)
                )
            )
            Text(
                text = prayerText,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = ColorProvider(R.color.text_color_primary_black)
                ),
                modifier = GlanceModifier.padding(start = 4.dp)
            )
            Text(
                text = bannerText,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = ColorProvider(R.color.text_color_primary_black)
                ),
                modifier = GlanceModifier.padding(start = 4.dp)
            )
        }
    }
}

