package com.gals.prayertimes.handlers.notification

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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

/**
 * Glance-based content for Alarm notifications.
 * Migrated from notification_service_alarm_remote_view.xml
 */
@Composable
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
                text = bannerText,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorProvider(Color.Black)
                ),
                modifier = GlanceModifier.padding(end = 4.dp)
            )
            Text(
                text = prayerText,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorProvider(Color.Black),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

/**
 * Glance-based content for Permanent notifications.
 * Migrated from notification_service_permanent_remote_view.xml
 */
@Composable
fun PermanentNotificationContent(
    bannerText: String,
    prayerText: String,
    prayerTime: String
) {
    Row(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = bannerText,
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(Color.Black)
            ),
            modifier = GlanceModifier.padding(end = 4.dp)
        )
        Text(
            text = prayerText,
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(Color.Black)
            ),
            modifier = GlanceModifier.padding(end = 4.dp)
        )
        Text(
            text = prayerTime,
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(Color.Black)
            )
        )
    }
}
