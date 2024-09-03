package com.gals.prayertimes.services

interface AlarmScheduler {
    fun scheduleAlarm(alarmItem: AlarmItem)
    fun cancelAlarm(alarmItem: AlarmItem)
}