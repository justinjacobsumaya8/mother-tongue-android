package com.example.mothertongue;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mothertongue.Models.LessonDetail;
import com.example.mothertongue.Services.BackgroundMusicService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LessonsResultActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView lessonBody;
    private String lessonName;

    private DatabaseReference lessonDetailDatabase;

    private Integer lessonId = 0;

    FirebaseRecyclerAdapter<LessonDetail, LessonsResultViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_result);

        lessonDetailDatabase = FirebaseDatabase.getInstance().getReference("lesson_details");

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.btn_sound);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent myIntent = new Intent(LessonsResultActivity.this, LessonsActivity.class);
                LessonsResultActivity.this.startActivity(myIntent);
            }
        });

        lessonBody = (RecyclerView) findViewById(R.id.lessonBody);
        lessonBody.setHasFixedSize(true);
        lessonBody.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        lessonName = intent.getStringExtra("lessonName");
        lessonId = intent.getIntExtra("lessonId", 0);

        firebaseLessonBody(lessonId);
    }

    private void firebaseLessonBody(Integer lessonId)
    {
        ProgressDialog dialog = ProgressDialog.show(LessonsResultActivity.this, "", "Loading. Please wait...", true);

        Query query = lessonDetailDatabase.orderByChild("lesson_id").equalTo(lessonId);

        FirebaseRecyclerOptions<LessonDetail> options = new FirebaseRecyclerOptions.Builder<LessonDetail>()
                .setQuery(query, LessonDetail.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LessonDetail, LessonsResultViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LessonsResultViewHolder holder, int position, @NonNull LessonDetail model) {

                holder.setDetails(model);
            }

            @NonNull
            @Override
            public LessonsResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.lesson_result_layout, parent, false);
                return new LessonsResultViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(DatabaseError e) {
                Toast.makeText(LessonsResultActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };

        firebaseRecyclerAdapter.startListening();
        lessonBody.setAdapter(firebaseRecyclerAdapter);
    }

    public class LessonsResultViewHolder extends RecyclerView.ViewHolder {
        View view;

        public LessonsResultViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setDetails(LessonDetail lessonDetail) {

            // Start Detail 1
            TextView txtLesson = (TextView) view.findViewById(R.id.txtLesson);
            TextView txtBodyTitle1 = (TextView) view.findViewById(R.id.txtBodyTitle1);
            TextView txtBodyDescription1 = (TextView) view.findViewById(R.id.txtBodyDescription1);
            ImageView body1Image = (ImageView) view.findViewById(R.id.body1Image);

            txtLesson.setText(lessonName);
            txtBodyTitle1.setText(lessonDetail.getD1_title());
            txtBodyDescription1.setText(lessonDetail.getD1_description_1());
            Glide.with(view.getContext()).load(lessonDetail.getD1_image_1()).into(body1Image);
            // End Detail 1

            // Start Detail 2
            TextView txtBodyTitle2 = (TextView) view.findViewById(R.id.txtBodyTitle2);
            TextView txtBodyDescription2 = (TextView) view.findViewById(R.id.txtBodyDescription2);
            ImageView body2Image1 = (ImageView) view.findViewById(R.id.body2Image1);
            ImageView body2Image2 = (ImageView) view.findViewById(R.id.body2Image2);
            ImageView body2Image3 = (ImageView) view.findViewById(R.id.body2Image3);
            ImageView body2Image4 = (ImageView) view.findViewById(R.id.body2Image4);

            txtBodyTitle2.setText(lessonDetail.getD2_title());
            txtBodyDescription2.setText(lessonDetail.getD2_description_1());
            Glide.with(view.getContext()).asGif().load(lessonDetail.getD2_image_1()).into(body2Image1);
            Glide.with(view.getContext()).asGif().load(lessonDetail.getD2_image_2()).into(body2Image2);
            Glide.with(view.getContext()).asGif().load(lessonDetail.getD2_image_3()).into(body2Image3);
            Glide.with(view.getContext()).asGif().load(lessonDetail.getD2_image_4()).into(body2Image4);
            // End Detail 2

            // Start Detail 3
            TextView txtBody3Title3 = (TextView) view.findViewById(R.id.txtBody3Title3);
            TextView txtBody3Card1Title = (TextView) view.findViewById(R.id.txtBody3Card1Title);
            TextView txtBody3Card1Description = (TextView) view.findViewById(R.id.txtBody3Card1Description);
            TextView txtBody3Card1Example = (TextView) view.findViewById(R.id.txtBody3Card1Example);
            TextView txtBody3Card2Title = (TextView) view.findViewById(R.id.txtBody3Card2Title);
            TextView txtBody3Card2Description = (TextView) view.findViewById(R.id.txtBody3Card2Description);
            TextView txtBody3Card2Example = (TextView) view.findViewById(R.id.txtBody3Card2Example);
            TextView txtBody3Card3Title = (TextView) view.findViewById(R.id.txtBody3Card3Title);
            TextView txtBody3Card3Description = (TextView) view.findViewById(R.id.txtBody3Card3Description);
            TextView txtBody3Card3Example = (TextView) view.findViewById(R.id.txtBody3Card3Example);

            txtBody3Title3.setText(lessonDetail.getD3_title());
            txtBody3Card1Title.setText(lessonDetail.getD3_sub_title_1());
            txtBody3Card1Description.setText(lessonDetail.getD3_description_1());
            txtBody3Card1Example.setText(lessonDetail.getD3_example_1());

            txtBody3Card2Title.setText(lessonDetail.getD3_sub_title_2());
            txtBody3Card2Description.setText(lessonDetail.getD3_description_2());
            txtBody3Card2Example.setText(lessonDetail.getD3_example_2());

            txtBody3Card3Title.setText(lessonDetail.getD3_sub_title_3());
            txtBody3Card3Description.setText(lessonDetail.getD3_description_3());
            txtBody3Card3Example.setText(lessonDetail.getD3_example_3());
            // End Detail 3

            final MediaPlayer mp = MediaPlayer.create(view.getContext(), R.raw.btn_sound);
            Button btnTakeQuiz = (Button) view.findViewById(R.id.btnTakeQuiz);
            btnTakeQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mp.start();
                    Intent intent = new Intent(LessonsResultActivity.this, QuizActivity.class);
                    intent.putExtra("lessonId", lessonId);
                    intent.putExtra("lessonName", lessonName);
                    startActivity(intent);
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
