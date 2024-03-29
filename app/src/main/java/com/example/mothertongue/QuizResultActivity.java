package com.example.mothertongue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mothertongue.Models.UserLessonQuiz;
import com.example.mothertongue.Services.BackgroundMusicService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class QuizResultActivity extends AppCompatActivity {

    private TextView txtScore, txtTotal;
    private ImageView bgText, bgImage;
    private RelativeLayout bgLayout;
    private Button btnBackHome;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // stop bg music to hear pass or fail sound
        stopService(new Intent(this, BackgroundMusicService.class));

        bgLayout = (RelativeLayout) findViewById(R.id.resultLayout);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        bgText = (ImageView) findViewById(R.id.bgText);
        bgImage = (ImageView) findViewById(R.id.bgImage);

        Intent intent = getIntent();

        Integer total = intent.getIntExtra("total", 0) - 1; // had to put minus 1 to get exact total
        Integer correct = intent.getIntExtra("correct", 0);
        Integer wrong = intent.getIntExtra("incorrect", 0);
        Integer lessonId = intent.getIntExtra("lessonId", 0);
        Log.d("Correct", correct.toString());

        txtTotal.setText("out of " + total);
        txtScore.setText(correct.toString());

        final MediaPlayer win_sound = MediaPlayer.create(this, R.raw.win_sound);
        final MediaPlayer fail_sound = MediaPlayer.create(this, R.raw.fail_sound);

        String uri;

        double passing = total * .75;
        Log.i("Passing", String.valueOf(passing));
        if (correct >= passing) {
            win_sound.start();
            bgLayout.setBackground(getDrawable(R.drawable.success_bg));
            bgText.setBackground(getDrawable(R.drawable.success_bg_for_text));

            uri = "@drawable/success_image";
        } else {
            fail_sound.start();
            bgLayout.setBackground(getDrawable(R.drawable.fail_bg));
            bgText.setBackground(getDrawable(R.drawable.fail_bg_for_text));

            uri = "@drawable/fail_image";
        }

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        bgImage.setImageDrawable(getResources().getDrawable(imageResource));

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.btn_sound);

        btnBackHome = (Button) findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent intent = new Intent(QuizResultActivity.this, MainActivity.class);
                intent.putExtra("startMusic", "true");
                startActivity(intent);
            }
        });

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("user_lesson_quizzes");
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // 0 quiz_id means it is General Quiz
        UserLessonQuiz userQuiz = new UserLessonQuiz(lessonId, correct, total, androidId.toString(), getTimeDate());

        String uniqueID = UUID.randomUUID().toString();
        reference.child(uniqueID).setValue(userQuiz);
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
}
