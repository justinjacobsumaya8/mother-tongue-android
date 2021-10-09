package com.example.mothertongue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ReadingDrillsActivity extends AppCompatActivity {

    private Button btnBack;
    private CardView cardWords, cardParagraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_drills);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ReadingDrillsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        cardWords = (CardView) findViewById(R.id.cardWords);
        cardWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ReadingDrillsActivity.this, WordsListActivity.class);
                startActivity(myIntent);
            }
        });

        cardParagraph = (CardView) findViewById(R.id.cardParagraph);
        cardParagraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ReadingDrillsActivity.this, ParagraphListActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
