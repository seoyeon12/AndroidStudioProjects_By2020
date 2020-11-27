package com.codes.sampletodo.data;

public class ToDoRecord {
    private int id;
    private String contents;
    private String time;

    public ToDoRecord(){ }
    public ToDoRecord(String contents, String time) {
        this.contents = contents;
        this.time = time;
    }
    public ToDoRecord(int id, String contents, String time) {
        this.id = id;
        this.contents = contents;
        this.time = time;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
