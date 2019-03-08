package com.example.multi_notes;

import java.io.Serializable;

public class Note implements Serializable {
    private int id;
    private String title;
    private String body;
    private String date;

    public Note(int id, String title, String body, String date){
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getBody() { return body; }

    public String getDate() { return date; }

    public void setTitle(String title) { this.title = title; }

    public void setBody(String body) { this.body = body; }

    public void setDate(String date) { this.date = date; }
}
