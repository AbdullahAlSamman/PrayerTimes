package com.gals.prayertimes;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicPlayer extends Service {

    private MediaPlayer musicPlayer;
    private int soundID;

    public MusicPlayer() {

    }

    public int getSoundID() {
        return soundID;
    }

    public void setSoundID(int soundID) {
        this.soundID = soundID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Music", "Music Service Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO: how to check if the file is ended to kill the service and change the button
        try {
            Log.i("Music", "Music started");
            setSoundID(intent.getIntExtra("athanType", 0));
            musicPlayer = MediaPlayer.create(getApplicationContext(), getSoundID());
            musicPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        Log.i("Music", "Service Stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
