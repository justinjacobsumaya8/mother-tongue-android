package com.example.mothertongue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.mothertongue.Services.BackgroundMusicService;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction("ACTION_PLAY");
        startService(intent);

        imageView = (ImageView) findViewById(R.id.splashScreenImage);
        Glide.with(this).asGif().load(R.drawable.splash).into(imageView);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, StartActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction("ACTION_PAUSE");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction("ACTION_PLAY");
        startService(intent);
    }
}
