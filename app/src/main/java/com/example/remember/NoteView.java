package com.example.remember;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;

public class NoteView extends AppCompatActivity {
    Button saveb, deleteb, cancelb;
    EditText titleed, texted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        saveb = findViewById(R.id.saveb);
        deleteb = findViewById(R.id.button6);
        cancelb = findViewById(R.id.cancelb);
        titleed = findViewById(R.id.titleed);
        texted = findViewById(R.id.texted);

        String notetitle = getIntent().getStringExtra("noteTitle");
        String notetext = getIntent().getStringExtra("noteText");

        titleed.setText(notetitle);
        texted.setText(notetext);

        cancelb.setOnClickListener(v -> {
            finish();
        });

        deleteb.setOnClickListener(v -> {
            getSharedPreferences("notes", MODE_PRIVATE)
                    .edit()
                    .remove(titleed.getText().toString())
                    .apply();
            HomeFragment.notesAdapter.notifyDataSetChanged();
            finish();
        });

        saveb.setOnClickListener(v -> {
            String title = titleed.getText().toString();
            String text = texted.getText().toString();
            if (title.isEmpty() || text.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }
            Note note = new Note(title, text);

            SharedPreferences preferences = getSharedPreferences("notes", MODE_PRIVATE);
            if (preferences.contains(title)) {
                ;
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(title, text);
                editor.apply();


            }
            finish();
        });
    }
}