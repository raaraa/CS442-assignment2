package com.example.multi_notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private EditText title_txt;
    private EditText body_txt;
    private String title;
    private String body;
    private Note note;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        intent = getIntent();
        title_txt = findViewById(R.id.title_txt);
        body_txt = findViewById(R.id.body_txt);
        note = (Note) intent.getSerializableExtra("note");
        title_txt.setText(note.getTitle());
        body_txt.setText(note.getBody());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                title = title_txt.getText().toString();
                body = body_txt.getText().toString();
                missingTitle();

                note.setTitle(title);
                note.setBody(body);
                note.setDate(time_today());

                intent.putExtra("note", note);
                setResult(RESULT_OK,intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (note.getBody().equals(body_txt.getText().toString()) && note.getTitle().equals(title_txt.getText().toString())){
            super.onBackPressed();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to save?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title = title_txt.getText().toString();
                body = body_txt.getText().toString();
                missingTitle();

                note.setTitle(title);
                note.setBody(body);
                note.setDate(time_today());
                intent.putExtra("note", note);
                setResult(RESULT_OK,intent);
                EditActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void missingTitle(){
        title = title_txt.getText().toString();
        if (title.length() == 0){
            Context context = getApplicationContext();
            CharSequence toast_msg = "Cant't save without a title";
            Toast toast = Toast.makeText(context, toast_msg, Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }

    public String time_today(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat("E MMM dd hh:mm a");
        return date_format.format(cal.getTime());
    }

}
