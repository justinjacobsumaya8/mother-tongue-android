package com.example.mothertongue;

import static android.os.Build.VERSION_CODES.N;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.mothertongue.Models.Quiz;
import com.example.mothertongue.Services.BackgroundMusicService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestion, txtTitle, txtSecondTitle, txtEmoji;
    private RadioButton btnChoice1, btnChoice2, btnChoice3, btnChoice4;
    private ImageView imageEmoji;
    private RelativeLayout bodyLayout;
    private LinearLayout emojiLayout;

    DatabaseReference reference;

    int total = 0;
    int correct = 0;
    int wrong = 0;
    int totalChild = 0;

    int arrIndex = 0;
    int max = 13;

    private Integer lessonId = 0;
    private String lessonName;

    List<Integer> indices;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null){
            lessonId = intent.getIntExtra("lessonId", 0);
            lessonName = intent.getStringExtra("lessonName");
        }

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.btn_sound);
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent intent;
                if (lessonId == 0) {
                    intent = new Intent(QuizActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(QuizActivity.this, LessonsResultActivity.class);
                    intent.putExtra("lessonId", lessonId);
                    intent.putExtra("lessonName", lessonName);
                }
                startActivity(intent);
            }
        });

        btnChoice1 = (RadioButton) findViewById(R.id.btnChoice1);
        btnChoice2 = (RadioButton) findViewById(R.id.btnChoice2);
        btnChoice3 = (RadioButton) findViewById(R.id.btnChoice3);
        btnChoice4 = (RadioButton) findViewById(R.id.btnChoice4);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSecondTitle = (TextView) findViewById(R.id.txtSecondTitle);

        txtEmoji = (TextView) findViewById(R.id.txtEmoji);
        imageEmoji = (ImageView) findViewById(R.id.imageEmoji);
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        emojiLayout = (LinearLayout) findViewById(R.id.emojiLayout);

        DatabaseReference ref;

        if (lessonId == 0) {
            ref = FirebaseDatabase.getInstance().getReference().child("quizzes");
        } else {
            ref = FirebaseDatabase.getInstance().getReference().child("lessons").child(String.valueOf(lessonId)).child("quizzes");
            txtTitle.setText("Pagsukod sa Kahanas - " + lessonName);
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalChild = (int) snapshot.getChildrenCount() + 1;
//                Log.i("Total", String.valueOf(snapshot.getChildrenCount() + 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.i("totalChild", String.valueOf(totalChild));
        // Can't get totalChild of quizzes table - will use static max for now
        indices = new ArrayList<Integer>(max);
        for(int c = 1; c < max; ++c)
        {
            indices.add(c);
        }

        updateQuestion();
    }

    private void updateQuestion()
    {
        ProgressDialog dialog = ProgressDialog.show(QuizActivity.this, "", "Loading. Please wait...", true);
        dialog.show();

        btnChoice1.setTextColor(Color.BLACK);
        btnChoice2.setTextColor(Color.BLACK);
        btnChoice3.setTextColor(Color.BLACK);
        btnChoice4.setTextColor(Color.BLACK);

        bodyLayout.setAlpha(1);
        emojiLayout.setAlpha(0);

        if (total > 0)
        {
            txtTitle.setVisibility(View.GONE);
            txtSecondTitle.setVisibility(View.GONE);
        }

        total++;
        if (total == totalChild)
        {
            Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
            intent.putExtra("total", total);
            intent.putExtra("correct", correct);
            intent.putExtra("incorrect", wrong);
            intent.putExtra("lessonId", lessonId);
            startActivity(intent);
        }
        else
        {
            int randomIndex = 0;
            if (indices.size() > 0) {
                arrIndex = (int)((double)indices.size() * Math.random());
                randomIndex = indices.get(arrIndex);
                indices.remove(arrIndex);
            }
            Log.i("randomIndex", String.valueOf(randomIndex));
            if (lessonId == 0) {
                reference = FirebaseDatabase.getInstance().getReference().child("quizzes").child(String.valueOf(randomIndex));
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child("lessons").child(String.valueOf(lessonId)).child("quizzes").child(String.valueOf(randomIndex));
            }

            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dialog.hide();
                    
                    if (snapshot.exists())
                    {
                        final Quiz quiz = snapshot.getValue(Quiz.class);
                        
                        String question = total + ". " + quiz.getQuestion();
                        txtQuestion.setText(Html.fromHtml(question));

                        btnChoice1.setText(quiz.getChoice_1());
                        btnChoice2.setText(quiz.getChoice_2());
                        btnChoice3.setText(quiz.getChoice_3());
                        btnChoice4.setText(quiz.getChoice_4());

                        final MediaPlayer mp = MediaPlayer.create(QuizActivity.this, R.raw.btn_sound);
                        btnChoice1.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void onClick(View view) {
                                mp.start();

                                btnChoice1.setEnabled(false);
                                btnChoice2.setEnabled(false);
                                btnChoice3.setEnabled(false);
                                btnChoice4.setEnabled(false);

                                if (btnChoice1.getText().toString().equals(quiz.getCorrect_answer()))
                                {
//                                    btnChoice1.setBackgroundColor(Color.parseColor("#57A35D"));
//                                    btnChoice1.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Great!");
                                    txtEmoji.setTextColor(Color.GREEN);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/happy";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            correct++;
                                            btnChoice1.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                                else
                                {
                                    wrong++;

//                                    btnChoice1.setBackgroundColor(Color.parseColor("#BA3F37"));
//                                    btnChoice1.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Sorry Wrong...");
                                    txtEmoji.setTextColor(Color.RED);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/sad";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    if (btnChoice2.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice2.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice2.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice3.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice3.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice3.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice4.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice4.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice4.setTextColor(Color.WHITE);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            btnChoice1.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice2.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice3.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice4.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                            }
                        });

                        btnChoice2.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void onClick(View view) {
                                mp.start();

                                btnChoice1.setEnabled(false);
                                btnChoice2.setEnabled(false);
                                btnChoice3.setEnabled(false);
                                btnChoice4.setEnabled(false);

                                if (btnChoice2.getText().toString().equals(quiz.getCorrect_answer()))
                                {
//                                    btnChoice2.setBackgroundColor(Color.parseColor("#57A35D"));
//                                    btnChoice2.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Great!");
                                    txtEmoji.setTextColor(Color.GREEN);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/happy";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            correct++;
                                            btnChoice2.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                                else
                                {
                                    wrong++;

                                    btnChoice2.setBackgroundColor(Color.parseColor("#BA3F37"));
                                    btnChoice2.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Sorry Wrong...");
                                    txtEmoji.setTextColor(Color.RED);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/sad";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    if (btnChoice1.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice1.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice1.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice3.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice3.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice3.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice4.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice4.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice4.setTextColor(Color.WHITE);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            btnChoice1.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice2.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice3.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice4.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                            }
                        });

                        btnChoice3.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void onClick(View view) {
                                mp.start();

                                btnChoice1.setEnabled(false);
                                btnChoice2.setEnabled(false);
                                btnChoice3.setEnabled(false);
                                btnChoice4.setEnabled(false);

                                if (btnChoice3.getText().toString().equals(quiz.getCorrect_answer()))
                                {
//                                    btnChoice3.setBackgroundColor(Color.parseColor("#57A35D"));
//                                    btnChoice3.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Great!");
                                    txtEmoji.setTextColor(Color.GREEN);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/happy";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            correct++;
                                            btnChoice3.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                                else
                                {
                                    wrong++;

//                                    btnChoice3.setBackgroundColor(Color.parseColor("#BA3F37"));
//                                    btnChoice3.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Sorry Wrong...");
                                    txtEmoji.setTextColor(Color.RED);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/sad";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    if (btnChoice1.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice1.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice1.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice2.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice2.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice2.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice4.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice4.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice4.setTextColor(Color.WHITE);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            btnChoice1.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice2.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice3.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice4.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                            }
                        });

                        btnChoice4.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void onClick(View view) {
                                mp.start();

                                btnChoice1.setEnabled(false);
                                btnChoice2.setEnabled(false);
                                btnChoice3.setEnabled(false);
                                btnChoice4.setEnabled(false);

                                if (btnChoice4.getText().toString().equals(quiz.getCorrect_answer()))
                                {
//                                    btnChoice4.setBackgroundColor(Color.parseColor("#57A35D"));
//                                    btnChoice4.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Great!");
                                    txtEmoji.setTextColor(Color.GREEN);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/happy";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            correct++;
                                            btnChoice4.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                                else
                                {
                                    wrong++;

//                                    btnChoice4.setBackgroundColor(Color.parseColor("#BA3F37"));
//                                    btnChoice4.setTextColor(Color.WHITE);

//                                    txtEmoji.setText("Sorry Wrong...");
                                    txtEmoji.setTextColor(Color.RED);
                                    bodyLayout.setAlpha((float) 0.3);
                                    emojiLayout.setAlpha(1);
                                    String uri = "@drawable/sad";
                                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                    imageEmoji.setImageDrawable(getResources().getDrawable(imageResource));

                                    if (btnChoice1.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice1.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice1.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice2.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice2.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice2.setTextColor(Color.WHITE);
                                    }
                                    else if (btnChoice3.getText().toString().equals(quiz.getCorrect_answer()))
                                    {
                                        btnChoice3.setBackgroundColor(Color.parseColor("#57A35D"));
                                        btnChoice3.setTextColor(Color.WHITE);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @SuppressLint("UseCompatLoadingForDrawables")
                                        @Override
                                        public void run() {
                                            btnChoice1.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice2.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice3.setBackground(getDrawable(R.drawable.custom_radio_button));
                                            btnChoice4.setBackground(getDrawable(R.drawable.custom_radio_button));

                                            btnChoice1.setEnabled(true);
                                            btnChoice2.setEnabled(true);
                                            btnChoice3.setEnabled(true);
                                            btnChoice4.setEnabled(true);

                                            updateQuestion();
                                        }
                                    }, 1500);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
