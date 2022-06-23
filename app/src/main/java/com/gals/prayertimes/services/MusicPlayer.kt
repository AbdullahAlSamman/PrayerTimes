package com.gals.prayertimes.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicPlayer : Service() {
    private lateinit var musicPlayer: MediaPlayer
    private var soundID = 0
    override fun onCreate() {
        super.onCreate()
        Log.i(
            "Music",
            "Music Service Started"
        )
    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        //TODO: how to check if the file is ended to kill the service and change the button
        try {
            Log.i(
                "Music",
                "Music started"
            )
            soundID = intent.getIntExtra(
                "athanType",
                0
            )
            musicPlayer = MediaPlayer.create(
                applicationContext,
                soundID
            )
            musicPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.onStartCommand(
            intent,
            flags,
            startId
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.stop()
        Log.i(
            "Music",
            "Service Stopped"
        )
    }

    override fun onBind(intent: Intent): IBinder? {
        //Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }
}