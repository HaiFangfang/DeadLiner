package com.example.deadliner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskBlock {
    private String tn;
    private String ddl;
    private String id;
    private String sttime;
    private boolean open;
    int color;
    String next;
    String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getNext() {
        return next;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public String getSttime() {
        return sttime;
    }

    public void setSttime(String sttime) {
        this.sttime = sttime;
    }

    public String getDDL() {
        return ddl;
    }

    public String getTN() {
        return tn;
    }

    public String getId() {
        return id;
    }

    public void setDDL(String ddl) {
        this.ddl = ddl;
    }

    public void setTN(String tn) {
        this.tn = tn;
    }

    public void setId(String id) {
        this.id = id;
    }



}