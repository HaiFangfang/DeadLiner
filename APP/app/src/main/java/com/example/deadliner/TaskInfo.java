package com.example.deadliner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private TextView back;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskInfo newInstance(String param1, String param2) {
        TaskInfo fragment = new TaskInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tinfo = inflater.inflate(R.layout.fragment_task_info, container, false);
        this.back=tinfo.findViewById(R.id.back);
        this.back.setOnClickListener(v -> { Toast.makeText(getContext(), "qwe", Toast.LENGTH_SHORT).show();
            FragmentManager fm=getParentFragmentManager();
            fm.popBackStack();
        });

        return tinfo;

    }

}