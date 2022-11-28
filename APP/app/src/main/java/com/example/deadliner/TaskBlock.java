package com.example.deadliner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskBlock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskBlock extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TASK_NAME = "unknown";
    private static final String DDL = "unknown";

    // TODO: Rename and change types of parameters
    private String tn;
    private String ddl;

    public TaskBlock() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param task_name Parameter 1.
     * @param ddl Parameter 2.
     * @return A new instance of fragment task_block.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskBlock newInstance(String task_name, String ddl) {
        TaskBlock fragment = new TaskBlock();
        Bundle args = new Bundle();
        args.putString(TASK_NAME, task_name);
        args.putString(DDL, ddl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tn = getArguments().getString(TASK_NAME);
            ddl = getArguments().getString(DDL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_block, container, false);
    }
}