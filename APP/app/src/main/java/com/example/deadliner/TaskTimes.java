package com.example.deadliner;

public class TaskTimes {
    private String tn;
    private int seconds;
    private String id;
    int color;

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }


    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }


    public String getTN() {
        return tn;
    }

    public String getId() {
        return id;
    }


    public void setTN(String tn) {
        this.tn = tn;
    }

    public void setId(String id) {
        this.id = id;
    }



}