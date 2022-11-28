package com.example.deadliner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class TaskFragment extends Fragment {
    List<TaskBlock> TaskBlockList= new ArrayList<>();
    List<TextView> TaskCataList= new ArrayList<>();
    RecyclerView taskBlocks;
    BlockAdapter blockAdapter;
    RecyclerView taskCata;
    CataAdapter cataAdapter;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance() {
        //        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return new TaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
        for(int i=0;i<10;++i){
            TaskBlock tb=new TaskBlock();
            tb.setDDL(String.valueOf(i));
            tb.setTN(String.valueOf(i+3));
            TaskBlockList.add(tb);
        }
        for(int i=0;i<10;++i){
            TextView tc=new TextView(getContext());
            tc.setText(String.valueOf(i));
            TaskCataList.add(tc);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tbs = inflater.inflate(R.layout.fragment_task, container, false);
        taskCata=tbs.findViewById(R.id.task_catalog);
        cataAdapter=new CataAdapter();
        taskCata.setAdapter(cataAdapter);
        taskCata.setLayoutManager(new LinearLayoutManager(getContext()));
        taskBlocks=tbs.findViewById(R.id.task_blocks);
        blockAdapter=new BlockAdapter();
        taskBlocks.setAdapter(blockAdapter);
        taskBlocks.setLayoutManager(new LinearLayoutManager(getContext()));
        return tbs;
    }

    public class BlockAdapter extends RecyclerView.Adapter<BlockViewHolder>{
        @NonNull
        @Override
        public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getContext()).inflate(R.layout.module_task_block,parent,false);
            return new BlockViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {

            TaskBlock tb=TaskBlockList.get(position);
            holder.textView1.setText(tb.getTN());
            holder.textView2.setText(tb.getDDL());
            holder.textView3.setText(String.valueOf(TaskBlockList.size()));
        }
        @Override
        public int getItemCount() {
            return TaskBlockList.size();
        }
    }

    public static class BlockViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ConstraintLayout constraintLayout;
        public BlockViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            constraintLayout=itemView.findViewById(R.id.rootView);
        }
    }

    public class CataAdapter extends RecyclerView.Adapter<CataViewHolder>{
        @NonNull
        @Override
        public CataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getContext()).inflate(R.layout.module_task_catalog,parent,false);
            return new CataViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull CataViewHolder holder, int position) {

            TextView tc=TaskCataList.get(position);
            holder.tv.setText(tc.getText());
        }
        @Override
        public int getItemCount() {
            return TaskCataList.size();
        }
    }

    public static class CataViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public CataViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textview);
        }
    }

}