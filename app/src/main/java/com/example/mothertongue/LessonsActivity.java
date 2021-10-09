package com.example.mothertongue;

import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothertongue.Models.Lesson;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;

public class LessonsActivity extends AppCompatActivity {

    private Button btnBack;
    private SearchView searchView;
    private RecyclerView lessonList;
    private DatabaseReference lessonDatabase;

    FirebaseRecyclerAdapter<Lesson, LessonsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        lessonDatabase = FirebaseDatabase.getInstance().getReference("lessons");

        lessonList = (RecyclerView) findViewById(R.id.lessonList);
        lessonList.setHasFixedSize(true);
        lessonList.setLayoutManager(new LinearLayoutManager(this));

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTransformationMethod(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LessonsActivity.this, MainActivity.class);
                LessonsActivity.this.startActivity(myIntent);
            }
        });

        searchView = (SearchView) findViewById(R.id.searchViewLessons);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseLessonSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    firebaseLessonSearch("");
                }
                return false;
            }
        });

        firebaseLessonSearch("");
    }

    private void firebaseLessonSearch(String searchText) {
        Query query = lessonDatabase.orderByChild("lesson_number").startAt(searchText).endAt(searchText + "\uf8ff");;
        if (!searchText.isEmpty())
        {
            if (!isNumeric(searchText))
            {
                searchText = searchText.substring(0, 1).toUpperCase() + searchText.substring(1).toLowerCase();
                query = lessonDatabase.orderByChild("lesson").startAt(searchText).endAt(searchText + "\uf8ff");
            }
        }

        ProgressDialog dialog = ProgressDialog.show(LessonsActivity.this, "", "Loading. Please wait...", true);

        FirebaseRecyclerOptions<Lesson> options = new FirebaseRecyclerOptions.Builder<Lesson>()
                .setQuery(query, Lesson.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lesson, LessonsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LessonsViewHolder holder, int position, @NonNull Lesson model) {
                holder.setDetails(model.getLesson(), model.getTitle());


                holder.itemView.setTag(R.id.lessonId, model.getId());
                holder.itemView.setTag(R.id.lessonName, model.getLesson());
            }

            @NonNull
            @Override
            public LessonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.lesson_list_layout, parent, false);
                return new LessonsViewHolder(view);
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
                Toast.makeText(LessonsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };

        firebaseRecyclerAdapter.startListening();
        lessonList.setAdapter(firebaseRecyclerAdapter);
    }

    public class LessonsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;

        public LessonsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
        }

        public void setDetails(String lessonNumber, String title) {
            TextView txtLessonNumber = (TextView) view.findViewById(R.id.txtLessonNumber);
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);

            txtLessonNumber.setText(lessonNumber);
            txtTitle.setText(title);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LessonsActivity.this, LessonsResultActivity.class);
            intent.putExtra("lessonId", (Integer) view.getTag(R.id.lessonId));
            intent.putExtra("lessonName", String.valueOf(view.getTag(R.id.lessonName)));
            LessonsActivity.this.startActivity(intent);
        }
    }

    public static boolean isNumeric(String string) {
        int intValue;

        if(string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
