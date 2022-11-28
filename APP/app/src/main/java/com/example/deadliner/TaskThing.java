package com.example.deadliner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskThing extends Fragment {
    private LinearLayout show;
    public TaskThing() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        show=(LinearLayout)getView().findViewById(R.id.showthing);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_thing, container, false);
    }
    private void addView(final LinearLayout ll){
        final TaskBlock t=TaskBlock.newInstance("1","2");
        ll.addView(t.getView());

    }
}