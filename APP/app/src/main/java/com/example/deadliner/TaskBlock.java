package com.example.deadliner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskBlock  {

    private String tn;
    private String ddl;

    public String getDDL() {
        return ddl;
    }

    public String getTN() {
        return tn;
    }

    public void setDDL(String ddl) {
        this.ddl = ddl;
    }

    public void setTN(String tn) {
        this.tn = tn;
    }
}