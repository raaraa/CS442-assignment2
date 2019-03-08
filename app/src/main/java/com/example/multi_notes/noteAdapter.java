package com.example.multi_notes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class noteAdapter extends RecyclerView.Adapter<viewHolder> {

    private List<Note> note_list;
    private MainActivity main_activity;

    public noteAdapter(List<Note> note_list, MainActivity main_activity){
        this.note_list = note_list;
        this.main_activity = main_activity;
    }

    @Override
    public viewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        view.setOnClickListener(main_activity);
        view.setOnLongClickListener(main_activity);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Note note = note_list.get(position);
        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDate());
        String body = note.getBody();

        if (body.length() > 80){ body = body.substring(0, 80)+"..."; }
        holder.body.setText(body);
    }

    @Override
    public int getItemCount() {
        return note_list.size();
    }
}
