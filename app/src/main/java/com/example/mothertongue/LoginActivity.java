package com.example.mothertongue;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mothertongue.Models.User;
import com.example.mothertongue.Services.BackgroundMusicService;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private Button btnBack, btnStart;
    private ImageView imageView;
    private TextInputLayout txtFirstName, txtLastName;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = (ImageView) findViewById(R.id.imageGif);
        Glide.with(this).asGif().load(R.drawable.login).into(imageView);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.btn_sound);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent myIntent = new Intent(LoginActivity.this, StartActivity.class);
                LoginActivity.this.startActivity(myIntent);
            }
        });

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);

        btnStart = (Button) findViewById(R.id.btnStart);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "Loading. Please wait...", true);
        reference.orderByKey().equalTo(androidId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dialog.hide();
                    btnStart.setEnabled(false);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    dialog.hide();
                    btnStart.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();

                String firstName = txtFirstName.getEditText().getText().toString();
                String lastName = txtLastName.getEditText().getText().toString();
                if (firstName.matches("")) {
                    Toast.makeText(LoginActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lastName.matches("")) {
                    Toast.makeText(LoginActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(firstName, lastName, androidId, getTimeDate());
                reference.child(androidId).setValue(user);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public static String getTimeDate() { // without parameter argument
        try{
            Date netDate = new Date(); // current time from here
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sfd.format(netDate);
        } catch(Exception e) {
            return "date";
        }
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
