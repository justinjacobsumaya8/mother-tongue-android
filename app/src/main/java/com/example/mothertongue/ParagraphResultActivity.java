package com.example.mothertongue;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ParagraphResultActivity extends AppCompatActivity {

    private Button btnBack;
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
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.paragraph_result_layout, parent, false);
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

            Button btnBack = (Button) view.findViewById(R.id.btnBack);
            btnBack.setTransformationMethod(null);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(ParagraphResultActivity.this, ParagraphListActivity.class);
                    startActivity(myIntent);
                }
            });

            Button btnChangeLanguage = (Button) view.findViewById(R.id.btnChangeLanguage);

            setDetailsCebuano(view, paragraph);
            btnChangeLanguage.setText("English");

            btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button)v;
                    String buttonText = b.getText().toString();

                    if (buttonText.equals("English")) {
                        setDetailsEnglish(view, paragraph);
                        Toast.makeText(view.getContext(), "Translated to English", Toast.LENGTH_SHORT).show();
                        btnChangeLanguage.setText("Cebuano");
                    } else {
                        setDetailsCebuano(view, paragraph);
                        btnChangeLanguage.setText("English");
                        Toast.makeText(view.getContext(), "Translated to Cebuano", Toast.LENGTH_SHORT).show();
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
    }
}
