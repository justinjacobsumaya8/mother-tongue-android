package com.example.mothertongue;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.mothertongue.Services.BackgroundMusicService;

public class ReadingDrillsActivity extends AppCompatActivity {

    private Button btnBack;
    private CardView cardWords, cardParagraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_drills);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.btn_sound);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent myIntent = new Intent(ReadingDrillsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        cardWords = (CardView) findViewById(R.id.cardWords);
        cardWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent myIntent = new Intent(ReadingDrillsActivity.this, WordsListActivity.class);
                startActivity(myIntent);
            }
        });

        cardParagraph = (CardView) findViewById(R.id.cardParagraph);
        cardParagraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent myIntent = new Intent(ReadingDrillsActivity.this, ParagraphListActivity.class);
                startActivity(myIntent);
            }
        });
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
