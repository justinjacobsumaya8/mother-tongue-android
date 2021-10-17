package com.example.mothertongue.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mothertongue.R;

public class BackgroundMusicService extends Service {
    private MediaPlayer mp;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mp = MediaPlayer.create(this, R.raw.bg_music);
        mp.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        Log.i("ACTION", action);
        if(action.equals("ACTION_PLAY")) {
            mp.start();
        } else if(action.equals("ACTION_PAUSE")) {
            mp.pause();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mp.stop();
    }
}
