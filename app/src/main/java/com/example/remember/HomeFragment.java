package com.example.remember;

import static android.view.View.INVISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    static NotesAdapter notesAdapter;
    private TextView textView;
    private List<Note> notes = new ArrayList<>();
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            // Inflate layout
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            textView = view.findViewById(R.id.textView);
            recyclerView = view.findViewById(R.id.recycleView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            notesAdapter = new NotesAdapter(getContext(), notes);
            recyclerView.setAdapter(notesAdapter);

            loadNotes();

            return view;
        }

        private void loadNotes() {
            notes.clear(); // Clear the list that backs the adapter
            SharedPreferences prefs = requireActivity().getSharedPreferences("notes", Context.MODE_PRIVATE);
            Map<String, ?> allNotes = prefs.getAll();

            for (Map.Entry<String, ?> entry : allNotes.entrySet()) {
                String title = entry.getKey();
                String text = entry.getValue().toString();
                notes.add(new Note(title, text));
            }


            notesAdapter.notifyDataSetChanged(); // Refresh RecyclerView
        }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
        if (!notes.isEmpty()) {textView.setVisibility(View.INVISIBLE);}
    }
}
