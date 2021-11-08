package com.example.mothertongue;

import android.annotation.SuppressLint;
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
import com.example.mothertongue.Models.Paragraph;
import com.example.mothertongue.Services.BackgroundMusicService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ParagraphResultActivity extends AppCompatActivity {

    private RecyclerView paragraphDetail;

    private DatabaseReference paragraphDatabase;
    FirebaseRecyclerAdapter<Paragraph, ParagraphResultActivity.ParagraphResultViewHolder> firebaseRecyclerAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph_result);

        paragraphDatabase = FirebaseDatabase.getInstance().getReference("rd_paragraphs");

        paragraphDetail = (RecyclerView) findViewById(R.id.paragraphResult);
        paragraphDetail.setHasFixedSize(true);
        paragraphDetail.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        Integer paragraphId = intent.getIntExtra("paragraphId", 0);
        firebaseDetail(paragraphId);
    }

    private void firebaseDetail(Integer paragraphId)
    {
        ProgressDialog dialog = ProgressDialog.show(ParagraphResultActivity.this, "", "Loading. Please wait...", true);

        Query query = paragraphDatabase.orderByChild("id").equalTo(paragraphId);

        FirebaseRecyclerOptions<Paragraph> options = new FirebaseRecyclerOptions.Builder<Paragraph>()
                .setQuery(query, Paragraph.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Paragraph, ParagraphResultActivity.ParagraphResultViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ParagraphResultActivity.ParagraphResultViewHolder holder, int position, @NonNull Paragraph model) {
                holder.setDetails(model);
            }

            @NonNull
            @Override
            public ParagraphResultActivity.ParagraphResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = null;
                if (paragraphId.equals(1)) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.paragraph_result_layout, parent, false);
                } else if (paragraphId.equals(2)) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.paragraph_result_layout_2, parent, false);
                } else if (paragraphId.equals(3)) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.paragraph_result_layout_3, parent, false);
                } else if (paragraphId.equals(4)) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.paragraph_result_layout_4, parent, false);
                } else if (paragraphId.equals(5)) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.paragraph_result_layout_5, parent, false);
                }
                else {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.paragraph_result_layout, parent, false);
                }
                return new ParagraphResultActivity.ParagraphResultViewHolder(view);
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
                Toast.makeText(ParagraphResultActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };

        firebaseRecyclerAdapter.startListening();
        paragraphDetail.setAdapter(firebaseRecyclerAdapter);
    }

    public class ParagraphResultViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ParagraphResultViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void setDetails(Paragraph paragraph) {

            final MediaPlayer mp = MediaPlayer.create(view.getContext(), R.raw.btn_sound);

            Button btnBack = (Button) view.findViewById(R.id.btnBack);
            btnBack.setTransformationMethod(null);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mp.start();
                    Intent myIntent = new Intent(ParagraphResultActivity.this, ParagraphListActivity.class);
                    startActivity(myIntent);
                }
            });

            Button btnChangeLanguage = (Button) view.findViewById(R.id.btnChangeLanguage);

            if (paragraph.getId().equals(1)) {
                setDetailsCebuano(view, paragraph);
            } else if (paragraph.getId().equals(2)) {
                setDetailsCebuano2(view, paragraph);
            } else if (paragraph.getId().equals(3)) {
                setDetailsCebuano3(view, paragraph);
            } else if (paragraph.getId().equals(4)) {
                setDetailsCebuano4(view, paragraph);
            } else if (paragraph.getId().equals(5)) {
                setDetailsCebuano5(view, paragraph);
            }

            btnChangeLanguage.setText("English");
            btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    Button b = (Button)v;
                    String buttonText = b.getText().toString();

                    if (buttonText.equals("English")) {
                        Toast.makeText(view.getContext(), "Translated to English", Toast.LENGTH_SHORT).show();
                        btnChangeLanguage.setText("Cebuano");

                        if (paragraph.getId().equals(1)) {
                            setDetailsEnglish(view, paragraph);
                        } else if (paragraph.getId().equals(2)) {
                            setDetailsEnglish2(view, paragraph);
                        } else if (paragraph.getId().equals(3)) {
                            setDetailsEnglish3(view, paragraph);
                        } else if (paragraph.getId().equals(4)) {
                            setDetailsEnglish4(view, paragraph);
                        } else if (paragraph.getId().equals(5)) {
                            setDetailsEnglish5(view, paragraph);
                        }
                    } else {
                        btnChangeLanguage.setText("English");
                        Toast.makeText(view.getContext(), "Translated to Cebuano", Toast.LENGTH_SHORT).show();

                        if (paragraph.getId().equals(1)) {
                            setDetailsCebuano(view, paragraph);
                        } else if (paragraph.getId().equals(2)) {
                            setDetailsCebuano2(view, paragraph);
                        } else if (paragraph.getId().equals(3)) {
                            setDetailsCebuano3(view, paragraph);
                        } else if (paragraph.getId().equals(4)) {
                            setDetailsCebuano4(view, paragraph);
                        } else if (paragraph.getId().equals(5)) {
                            setDetailsCebuano5(view, paragraph);
                        }
                    }
                }
            });
        }

        public void setDetailsCebuano(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtAuthor = (TextView) view.findViewById(R.id.txtAuthor);
            TextView txtTranslatedBy = (TextView) view.findViewById(R.id.txtTranslatedBy);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            TextView txtDetail1 = (TextView) view.findViewById(R.id.txtDetail1);
            TextView txtDetail2 = (TextView) view.findViewById(R.id.txtDetail2);
            TextView txtDetail3 = (TextView) view.findViewById(R.id.txtDetail3);
            TextView txtDetail4 = (TextView) view.findViewById(R.id.txtDetail4);
            TextView txtDetail5 = (TextView) view.findViewById(R.id.txtDetail5);
            TextView txtDetail6 = (TextView) view.findViewById(R.id.txtDetail6);
            TextView txtDiscussTitle = (TextView) view.findViewById(R.id.txtDiscussTitle);
            TextView txtDiscussSubTitle = (TextView) view.findViewById(R.id.txtDiscussSubTitle);
            TextView txtDiscussSentence1 = (TextView) view.findViewById(R.id.txtDiscussSentence1);
            TextView txtDiscussSentence2 = (TextView) view.findViewById(R.id.txtDiscussSentence2);
            TextView txtDiscussSentence3 = (TextView) view.findViewById(R.id.txtDiscussSentence3);
            TextView txtDiscussSentence4 = (TextView) view.findViewById(R.id.txtDiscussSentence4);
            TextView txtDiscussSentence5 = (TextView) view.findViewById(R.id.txtDiscussSentence5);

            txtTitle.setText(paragraph.getTitle());
            txtAuthor.setText(paragraph.getAuthor());
            txtTranslatedBy.setText(paragraph.getTranslated_by());
            Glide.with(view.getContext()).load(paragraph.getImage()).into(image);
            txtDetail1.setText(paragraph.getDetail_1());
            txtDetail2.setText(paragraph.getDetail_2());
            txtDetail3.setText(paragraph.getDetail_3());
            txtDetail4.setText(paragraph.getDetail_4());
            txtDetail5.setText(paragraph.getDetail_5());
            txtDetail6.setText(paragraph.getDetail_6());
            txtDiscussTitle.setText(paragraph.getDiscuss_title());
            txtDiscussSubTitle.setText(paragraph.getDiscuss_sub_title());
            txtDiscussSentence1.setText("1. " + paragraph.getDiscuss_sentence_1());
            txtDiscussSentence2.setText("2. " +paragraph.getDiscuss_sentence_2());
            txtDiscussSentence3.setText("3. " +paragraph.getDiscuss_sentence_3());
            txtDiscussSentence4.setText("4. " +paragraph.getDiscuss_sentence_4());
            txtDiscussSentence5.setText("5. " +paragraph.getDiscuss_sentence_5());
        }

        public void setDetailsEnglish(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtAuthor = (TextView) view.findViewById(R.id.txtAuthor);
            TextView txtTranslatedBy = (TextView) view.findViewById(R.id.txtTranslatedBy);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            TextView txtDetail1 = (TextView) view.findViewById(R.id.txtDetail1);
            TextView txtDetail2 = (TextView) view.findViewById(R.id.txtDetail2);
            TextView txtDetail3 = (TextView) view.findViewById(R.id.txtDetail3);
            TextView txtDetail4 = (TextView) view.findViewById(R.id.txtDetail4);
            TextView txtDetail5 = (TextView) view.findViewById(R.id.txtDetail5);
            TextView txtDetail6 = (TextView) view.findViewById(R.id.txtDetail6);
            TextView txtDiscussTitle = (TextView) view.findViewById(R.id.txtDiscussTitle);
            TextView txtDiscussSubTitle = (TextView) view.findViewById(R.id.txtDiscussSubTitle);
            TextView txtDiscussSentence1 = (TextView) view.findViewById(R.id.txtDiscussSentence1);
            TextView txtDiscussSentence2 = (TextView) view.findViewById(R.id.txtDiscussSentence2);
            TextView txtDiscussSentence3 = (TextView) view.findViewById(R.id.txtDiscussSentence3);
            TextView txtDiscussSentence4 = (TextView) view.findViewById(R.id.txtDiscussSentence4);
            TextView txtDiscussSentence5 = (TextView) view.findViewById(R.id.txtDiscussSentence5);

            txtTitle.setText(paragraph.getTitle());
            txtAuthor.setText(paragraph.getAuthor());
            txtTranslatedBy.setText(paragraph.getTranslated_by());
            Glide.with(view.getContext()).load(paragraph.getImage()).into(image);
            txtDetail1.setText(paragraph.getDetail_1_english());
            txtDetail2.setText(paragraph.getDetail_2_english());
            txtDetail3.setText(paragraph.getDetail_3_english());
            txtDetail4.setText(paragraph.getDetail_4_english());
            txtDetail5.setText(paragraph.getDetail_5_english());
            txtDetail6.setText(paragraph.getDetail_6_english());
            txtDiscussTitle.setText(paragraph.getDiscuss_title_english());
            txtDiscussSubTitle.setText(paragraph.getDiscuss_sub_title_english());
            txtDiscussSentence1.setText("1. " + paragraph.getDiscuss_sentence_1_english());
            txtDiscussSentence2.setText("2. " + paragraph.getDiscuss_sentence_2_english());
            txtDiscussSentence3.setText("3. " + paragraph.getDiscuss_sentence_3_english());
            txtDiscussSentence4.setText("4. " + paragraph.getDiscuss_sentence_4_english());
            txtDiscussSentence5.setText("5. " + paragraph.getDiscuss_sentence_5_english());
        }

        public void setDetailsCebuano2(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);
            TextView txtDesc4 = (TextView) view.findViewById(R.id.txtDesc4);
            TextView txtDesc5 = (TextView) view.findViewById(R.id.txtDesc5);
            TextView txtDesc6 = (TextView) view.findViewById(R.id.txtDesc6);
            TextView txtDesc7 = (TextView) view.findViewById(R.id.txtDesc7);
            TextView txtDesc8 = (TextView) view.findViewById(R.id.txtDesc8);
            TextView txtDesc9 = (TextView) view.findViewById(R.id.txtDesc9);
            TextView txtDesc10 = (TextView) view.findViewById(R.id.txtDesc10);

            txtTitle.setText(getResources().getString(R.string.title_rd_2));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_2));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_2));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_2));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_2));
            txtDesc4.setText(getResources().getString(R.string.detail_4_rd_2));
            txtDesc5.setText(getResources().getString(R.string.detail_5_rd_2));
            txtDesc6.setText(getResources().getString(R.string.detail_6_rd_2));
            txtDesc7.setText(getResources().getString(R.string.detail_7_rd_2));
            txtDesc8.setText(getResources().getString(R.string.detail_8_rd_2));
            txtDesc9.setText(getResources().getString(R.string.detail_9_rd_2));
            txtDesc10.setText(getResources().getString(R.string.detail_10_rd_2));
        }

        public void setDetailsEnglish2(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);
            TextView txtDesc4 = (TextView) view.findViewById(R.id.txtDesc4);
            TextView txtDesc5 = (TextView) view.findViewById(R.id.txtDesc5);
            TextView txtDesc6 = (TextView) view.findViewById(R.id.txtDesc6);
            TextView txtDesc7 = (TextView) view.findViewById(R.id.txtDesc7);
            TextView txtDesc8 = (TextView) view.findViewById(R.id.txtDesc8);
            TextView txtDesc9 = (TextView) view.findViewById(R.id.txtDesc9);
            TextView txtDesc10 = (TextView) view.findViewById(R.id.txtDesc10);

            txtTitle.setText(getResources().getString(R.string.title_rd_2_english));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_2_english));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_2_english));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_2_english));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_2_english));
            txtDesc4.setText(getResources().getString(R.string.detail_4_rd_2_english));
            txtDesc5.setText(getResources().getString(R.string.detail_5_rd_2_english));
            txtDesc6.setText(getResources().getString(R.string.detail_6_rd_2_english));
            txtDesc7.setText(getResources().getString(R.string.detail_7_rd_2_english));
            txtDesc8.setText(getResources().getString(R.string.detail_8_rd_2_english));
            txtDesc9.setText(getResources().getString(R.string.detail_9_rd_2_english));
            txtDesc10.setText(getResources().getString(R.string.detail_10_rd_2_english));
        }

        public void setDetailsCebuano3(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);
            TextView txtDesc4 = (TextView) view.findViewById(R.id.txtDesc4);
            TextView txtDesc5 = (TextView) view.findViewById(R.id.txtDesc5);
            TextView txtDesc6 = (TextView) view.findViewById(R.id.txtDesc6);

            txtTitle.setText(getResources().getString(R.string.title_rd_3));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_3));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_3));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_3));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_3));
            txtDesc4.setText(getResources().getString(R.string.detail_4_rd_3));
            txtDesc5.setText(getResources().getString(R.string.detail_5_rd_3));
            txtDesc6.setText(getResources().getString(R.string.detail_6_rd_3));
        }

        public void setDetailsEnglish3(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);
            TextView txtDesc4 = (TextView) view.findViewById(R.id.txtDesc4);
            TextView txtDesc5 = (TextView) view.findViewById(R.id.txtDesc5);
            TextView txtDesc6 = (TextView) view.findViewById(R.id.txtDesc6);

            txtTitle.setText(getResources().getString(R.string.title_rd_3_english));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_3_english));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_3_english));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_3_english));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_3_english));
            txtDesc4.setText(getResources().getString(R.string.detail_4_rd_3_english));
            txtDesc5.setText(getResources().getString(R.string.detail_5_rd_3_english));
            txtDesc6.setText(getResources().getString(R.string.detail_6_rd_3_english));
        }

        public void setDetailsCebuano4(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);

            txtTitle.setText(getResources().getString(R.string.title_rd_4));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_4));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_4));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_4));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_4));
        }

        public void setDetailsEnglish4(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);

            txtTitle.setText(getResources().getString(R.string.title_rd_4_english));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_4_english));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_4_english));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_4_english));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_4_english));
        }

        public void setDetailsCebuano5(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);
            TextView txtDesc4 = (TextView) view.findViewById(R.id.txtDesc4);
            TextView txtDesc5 = (TextView) view.findViewById(R.id.txtDesc5);
            TextView txtDesc6 = (TextView) view.findViewById(R.id.txtDesc6);
            TextView txtDesc7 = (TextView) view.findViewById(R.id.txtDesc7);
            TextView txtDesc8 = (TextView) view.findViewById(R.id.txtDesc8);

            txtTitle.setText(getResources().getString(R.string.title_rd_5));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_5));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_5));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_5));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_5));
            txtDesc4.setText(getResources().getString(R.string.detail_4_rd_5));
            txtDesc5.setText(getResources().getString(R.string.detail_5_rd_5));
            txtDesc6.setText(getResources().getString(R.string.detail_6_rd_5));
            txtDesc7.setText(getResources().getString(R.string.detail_7_rd_5));
            txtDesc8.setText(getResources().getString(R.string.detail_8_rd_5));
        }

        public void setDetailsEnglish5(View view, Paragraph paragraph) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtSubTitle = (TextView) view.findViewById(R.id.txtSubTitle);
            TextView txtDesc1 = (TextView) view.findViewById(R.id.txtDesc1);
            TextView txtDesc2 = (TextView) view.findViewById(R.id.txtDesc2);
            TextView txtDesc3 = (TextView) view.findViewById(R.id.txtDesc3);
            TextView txtDesc4 = (TextView) view.findViewById(R.id.txtDesc4);
            TextView txtDesc5 = (TextView) view.findViewById(R.id.txtDesc5);
            TextView txtDesc6 = (TextView) view.findViewById(R.id.txtDesc6);
            TextView txtDesc7 = (TextView) view.findViewById(R.id.txtDesc7);
            TextView txtDesc8 = (TextView) view.findViewById(R.id.txtDesc8);

            txtTitle.setText(getResources().getString(R.string.title_rd_5_english));
            txtSubTitle.setText(getResources().getString(R.string.subtitle_rd_5_english));
            txtDesc1.setText(getResources().getString(R.string.detail_1_rd_5_english));
            txtDesc2.setText(getResources().getString(R.string.detail_2_rd_5_english));
            txtDesc3.setText(getResources().getString(R.string.detail_3_rd_5_english));
            txtDesc4.setText(getResources().getString(R.string.detail_4_rd_5_english));
            txtDesc5.setText(getResources().getString(R.string.detail_5_rd_5_english));
            txtDesc6.setText(getResources().getString(R.string.detail_6_rd_5_english));
            txtDesc7.setText(getResources().getString(R.string.detail_7_rd_5_english));
            txtDesc8.setText(getResources().getString(R.string.detail_8_rd_5_english));
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
