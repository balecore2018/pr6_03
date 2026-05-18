package com.example.pr6_03.presentations;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pr6_03.R;
import com.example.pr6_03.datas.DbContext;
import com.example.pr6_03.datas.NotesContext;
import com.example.pr6_03.domains.Note;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class NotesActivity extends AppCompatActivity {

    GridLayout itemsParent;
    View bthAddNotes;
    EditText etSearch;

    DbContext dbContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        bthAddNotes = findViewById(R.id.bth_add_notes);
        itemsParent = findViewById(R.id.gl_notes);
        etSearch = findViewById(R.id.et_search);

        bthAddNotes.setOnClickListener(v -> {
            Intent intentActivityNote = new Intent(this, NoteActivity.class);
            startActivity(intentActivityNote);
        });

        etSearch.setOnKeyListener(SearchListner);

        dbContext = new DbContext(this);
        LoadNotes(NotesContext.AllNotes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadNotes(NotesContext.AllNotes());
    }

    public void LoadNotes(ArrayList<Note> notes){
        itemsParent.removeAllViews();

        for(int i = 0; i < notes.size(); i ++){
            View item_notes = LayoutInflater.from(this).inflate(R.layout.item_note, itemsParent, false);
            TextView tvTitle = item_notes.findViewById(R.id.tv_title);
            TextView tvText = item_notes.findViewById(R.id.tv_text);
            TextView tvDate = item_notes.findViewById(R.id.tv_date);

            Note note = notes.get(i);

            tvTitle.setText(note.title);
            tvDate.setText(note.date);
            tvText.setText(note.text);

            item_notes.setOnClickListener(v -> {
                Intent intentActivityNote = new Intent(this, NoteActivity.class);
                intentActivityNote.putExtra("id", note.id);
                intentActivityNote.putExtra("title", note.title);
                intentActivityNote.putExtra("text", note.text);
                intentActivityNote.putExtra("date", note.date);
                intentActivityNote.putExtra("color", note.color);
                startActivity(intentActivityNote);
            });
            itemsParent.addView(item_notes);
        }
    }

    View.OnKeyListener SearchListner = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event){
            String Search = etSearch.getText().toString();
            ArrayList<Note> FindNotes = NotesContext.AllNotes().stream().filter(
                    item -> item.text.contains(Search)
            ).collect(Collectors.toCollection(ArrayList::new));

            LoadNotes(FindNotes);

            return false;
        }
    };
}

// !!!
