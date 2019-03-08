package com.example.multi_notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Note> note_list = new ArrayList<>();
    private RecyclerView recycleView;
    private noteAdapter note_adapter;
    private static final int E_REQ = 1;
    private String file_name = "db.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleView = findViewById(R.id.my_recyler);
        note_adapter = new noteAdapter(note_list, this);

        recycleView.setAdapter(note_adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        new AsyncRead().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_add:
                intent = new Intent(this, EditActivity.class);
                Note note = new Note(note_list.size(), "", "", "");
                intent.putExtra("note", note);
                startActivityForResult(intent, E_REQ);
                return true;
            case R.id.menu_info:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == E_REQ){
            if (resultCode == RESULT_OK){
                Note note = (Note) data.getSerializableExtra("note");
                if (note.getId()<note_list.size())
                {
                    int i = 0;
                    for(Iterator iter = note_list.iterator(); iter.hasNext();){
                        Note tmp = (Note) iter.next();
                        if (tmp.getId() == note.getId()) break;
                        i++;
                    }
                    note_list.remove(i);
                }
                note_list.add(0, note);
                note_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recycleView.getChildAdapterPosition(v);
        Note note = note_list.get(pos);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("note", note);
        startActivityForResult(intent, E_REQ);
        Log.d("onclick", "123");
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            FileOutputStream fos = openFileOutput(file_name, 0);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            writer.setIndent("    ");
            writer.beginArray();
            for(Iterator iter = note_list.iterator(); iter.hasNext();){
                Note note = (Note) iter.next();
                writer.beginObject();
                writer.name("id").value(note.getId());
                writer.name("title").value(note.getTitle());
                writer.name("body").value(note.getBody());
                writer.name("date").value(note.getDate());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(final View v) {
        final int pos = recycleView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Delete this note?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                note_list.remove(pos);
                note_adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return false;
    }

    class AsyncRead extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                FileInputStream f_input = openFileInput(file_name);
                JsonReader reader = new JsonReader(new InputStreamReader( f_input, "UTF-8"));
                reader.beginArray();
                while(reader.hasNext()) {
                    reader.beginObject();
                    String title = "";
                    String body = "";
                    String date = "";
                    int i = 0;
                    while(reader.hasNext()) {
                        String name = reader.nextName();
                        if (name.equals("id")) {
                            i = reader.nextInt(); }
                        else if(name.equals("title")) {
                            title = reader.nextString(); }
                        else if (name.equals("body")){
                            body = reader.nextString(); }
                        else if (name.equals("date")) {
                            date = reader.nextString(); }
                            else {
                            reader.skipValue();
                        }
                    }
                    Note note = new Note(i,title,body,date);
                    note_list.add(note);
                    reader.endObject();
                }
                reader.endArray();
                reader.close();
                note_adapter.notifyDataSetChanged();
            }
            catch (FileNotFoundException e) {
                try {
                    FileOutputStream f_output = openFileOutput(file_name,0);
                    f_output.close(); }
                catch (FileNotFoundException e1) {
                    e1.printStackTrace(); }
                catch (IOException e1) {
                    e1.printStackTrace(); }
                e.printStackTrace(); }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace(); }
            catch (IOException e) {
                e.printStackTrace(); }
            return null;
        }
    }

}


