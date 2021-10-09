package com.example.mothertongue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private CardView btnReadingDrills, btnLessons, btnQuiz;
    private ImageView imageView;
    private TextView txtUser;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageGif);
        Glide.with(this).asGif().load(R.drawable.splash).into(imageView);

        btnReadingDrills = (CardView) findViewById(R.id.cardReadingDrills);
        btnReadingDrills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ReadingDrillsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btnLessons = (CardView) findViewById(R.id.cardLessons);
        btnLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, LessonsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btnQuiz = (CardView) findViewById(R.id.cardQuiz);
        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, QuizActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        reference.orderByKey().equalTo(androidId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String firstName = "";
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        firstName = postSnapshot.child("first_name").getValue().toString();
                    }
                    txtUser = (TextView) findViewById(R.id.txtUser);
                    txtUser.setText(firstName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
