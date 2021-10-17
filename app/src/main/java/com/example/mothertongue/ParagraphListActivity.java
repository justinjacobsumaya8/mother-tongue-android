package com.example.mothertongue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mothertongue.Models.Paragraph;
import com.example.mothertongue.Models.Word;
import com.example.mothertongue.Services.BackgroundMusicService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ParagraphListActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView paragraphList;
    private DatabaseReference paragraphDatabase;

    FirebaseRecyclerAdapter<Paragraph, ParagraphListActivity.ParagraphsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph_list);

        paragraphDatabase = FirebaseDatabase.getInstance().getReference("rd_paragraphs");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        paragraphList = (RecyclerView) findViewById(R.id.paragraphList);
        paragraphList.setHasFixedSize(true);
        paragraphList.setLayoutManager(layoutManager);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.btn_sound);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent myIntent = new Intent(ParagraphListActivity.this, ReadingDrillsActivity.class);
                startActivity(myIntent);
            }
        });

        firebaseParagraphSearch();

    }

    private void firebaseParagraphSearch() {

        ProgressDialog dialog = ProgressDialog.show(ParagraphListActivity.this, "", "Loading. Please wait...", true);

        FirebaseRecyclerOptions<Paragraph> options = new FirebaseRecyclerOptions.Builder<Paragraph>()
                .setQuery(paragraphDatabase, Paragraph.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Paragraph, ParagraphListActivity.ParagraphsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ParagraphListActivity.ParagraphsViewHolder holder, int position, @NonNull Paragraph model) {
                holder.setDetails(model.getTitle(), model.getImage());
                holder.itemView.setTag(R.id.paragraphId, model.getId());
            }

            @NonNull
            @Override
            public ParagraphListActivity.ParagraphsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.paragraph_list_layout, parent, false);
                return new ParagraphListActivity.ParagraphsViewHolder(view);
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
                Toast.makeText(ParagraphListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };

        firebaseRecyclerAdapter.startListening();
        paragraphList.setAdapter(firebaseRecyclerAdapter);
    }

    public class ParagraphsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        final MediaPlayer mp = MediaPlayer.create(ParagraphListActivity.this, R.raw.btn_sound);

        public ParagraphsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
        }

        public void setDetails(String title, String image) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);

            txtTitle.setText(title);
            Glide.with(view.getContext()).load(image).into(imageView);
        }

        @Override
        public void onClick(View view) {
            mp.start();
            Intent intent = new Intent(ParagraphListActivity.this, ParagraphResultActivity.class);
            intent.putExtra("paragraphId", (Integer) view.getTag(R.id.paragraphId));
            startActivity(intent);
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
