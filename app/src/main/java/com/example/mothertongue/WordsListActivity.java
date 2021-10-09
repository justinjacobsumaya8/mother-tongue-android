package com.example.mothertongue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothertongue.Models.Word;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Locale;

public class WordsListActivity extends AppCompatActivity {

    private Button btnBack;
    private SearchView searchView;
    private RecyclerView wordList;
    private DatabaseReference wordDatabase;

    private TextToSpeech textToSpeech;

    FirebaseRecyclerAdapter<Word, WordsListActivity.WordsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        wordDatabase = FirebaseDatabase.getInstance().getReference("rd_words");

        wordList = (RecyclerView) findViewById(R.id.wordList);
        wordList.setHasFixedSize(true);
        wordList.setLayoutManager(new LinearLayoutManager(this));

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(WordsListActivity.this, ReadingDrillsActivity.class);
                startActivity(myIntent);
            }
        });

        searchView = (SearchView) findViewById(R.id.searchViewWords);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseWordSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    firebaseWordSearch("");
                }
                return false;
            }
        });

        firebaseWordSearch("");

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
    }

    private void firebaseWordSearch(String searchText) {
        Query query = wordDatabase.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        if (!searchText.isEmpty())
        {
            searchText = searchText.substring(0, 1).toUpperCase() + searchText.substring(1).toLowerCase();
            query = wordDatabase.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        }

        ProgressDialog dialog = ProgressDialog.show(WordsListActivity.this, "", "Loading. Please wait...", true);

        FirebaseRecyclerOptions<Word> options = new FirebaseRecyclerOptions.Builder<Word>()
                .setQuery(query, Word.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Word, WordsListActivity.WordsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WordsListActivity.WordsViewHolder holder, int position, @NonNull Word model) {
                holder.setDetails(model.getTitle(), model.getDescription());


                holder.itemView.setTag(R.id.wordId, model.getId());
                holder.itemView.setTag(R.id.wordTitle, model.getTitle());
            }

            @NonNull
            @Override
            public WordsListActivity.WordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.words_list_layout, parent, false);
                return new WordsListActivity.WordsViewHolder(view);
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
                Toast.makeText(WordsListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };

        firebaseRecyclerAdapter.startListening();
        wordList.setAdapter(firebaseRecyclerAdapter);
    }

    public class WordsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;

        public WordsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
        }

        public void setDetails(String title, String description) {
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtDescription = (TextView) view.findViewById(R.id.txtDescription);

            txtTitle.setText(title);
            txtDescription.setText(description);

            ImageButton btnSpeak = (ImageButton) view.findViewById(R.id.btnSpeak);
            btnSpeak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textToSpeech.speak(title, TextToSpeech.QUEUE_FLUSH, null, null);
                    utterListener.onDone(description);
                }
            });
        }

        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(WordsListActivity.this, WordResultActivity.class);
//            intent.putExtra("wordId", (Integer) view.getTag(R.id.wordId));
//            intent.putExtra("wordName", String.valueOf(view.getTag(R.id.wordTitle)));
//            startActivity(intent);
        }
    }

    private final UtteranceProgressListener utterListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
        }

        @Override
        public void onDone(String utteranceId) {
            textToSpeech.speak(utteranceId, TextToSpeech.QUEUE_FLUSH, null, null);
        }

        @Override
        public void onError(String utteranceId) {
            if (utteranceId.equals("utterID")) {

            }
        }
    };
}
