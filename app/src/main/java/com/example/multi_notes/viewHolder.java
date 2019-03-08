package com.example.multi_notes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class viewHolder  extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView body;
    public TextView date;

    public viewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.noteTitle);
        body =  view.findViewById(R.id.noteBody);
        date =  view.findViewById(R.id.noteDate);
    }

}
