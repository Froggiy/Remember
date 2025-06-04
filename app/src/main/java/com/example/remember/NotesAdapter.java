package com.example.remember;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private Context context;
    List<Note> notes;

    public NotesAdapter(Context context, List<Note> notes) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.textView.setText(notes.get(position).getTittle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteView.class);
            intent.putExtra("noteTitle", notes.get(position).getTittle());
            intent.putExtra("noteText", notes.get(position).getText());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.note_text);
        }
    }
}
