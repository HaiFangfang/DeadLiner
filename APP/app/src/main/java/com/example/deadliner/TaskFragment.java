package com.example.deadliner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class TaskFragment extends Fragment {
    List<TaskBlock> TaskBlockList= new ArrayList<>();
    List<TaskCata> TaskCataList= new ArrayList<>();
    RecyclerView taskBlocks;
    BlockAdapter blockAdapter;
    RecyclerView taskCata;
    CataAdapter cataAdapter;
    String cataSelected;
    public enum Blocks {
        TaskBlock,
        TaskBlockNew
    }
    public enum Catas {
        TaskCata,
        TaskCataNew
    }
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
            TaskCata tc=new TaskCata();
            tc.setCataName(String.valueOf(i));
            tc.setTaskNum(i);
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
    public interface BlockClickListener {
        public void onBlockClick(RecyclerView parent , View view , String fileName );

    }




    public class BlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
        private BlockClickListener BlockClickListener = null;
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType==Blocks.TaskBlock.ordinal()){
                View viewt= LayoutInflater.from(getContext()).inflate(R.layout.module_task_block,parent,false);
                viewt.setOnClickListener(this);
                return new TBlockViewHolder(viewt);
            }else if(viewType==Blocks.TaskBlockNew.ordinal()){
                View viewn= LayoutInflater.from(getContext()).inflate(R.layout.module_task_new,parent,false);
                return new NBlockViewHolder(viewn);
            }
            return null;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof TBlockViewHolder){
                TaskBlock tb=TaskBlockList.get(position);
                ((TBlockViewHolder)holder).textView1.setText(tb.getTN());
                ((TBlockViewHolder)holder).textView2.setText(tb.getDDL());
                ((TBlockViewHolder)holder).textView3.setText(String.valueOf(TaskBlockList.size()));
            }else if(holder instanceof  NBlockViewHolder){
                TaskBlockNew tn=new TaskBlockNew();
            }
        }
        @Override
        public int getItemCount() {
            return TaskBlockList.size()+1;
        }
        @Override
        public int getItemViewType(int position) {
            return position <TaskBlockList.size() ? Blocks.TaskBlock.ordinal() : Blocks.TaskBlockNew.ordinal();
        }
        public void setBlockClickListener(BlockClickListener clickListener){
            this.BlockClickListener = clickListener;
        }
        @Override
        public void onClick(View view) {
            jump();
        }
        public void jump(){
            FragmentManager fm=getParentFragmentManager();
            fm.beginTransaction()
              .addToBackStack(null) //将当前fragment加入到返回栈中
              .replace(R.id.tasks, new TaskInfo()).commit();
        }

    }

    public static class TBlockViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ConstraintLayout constraintLayout;
        public TBlockViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            constraintLayout=itemView.findViewById(R.id.rootView);
        }
    }
    public static class NBlockViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout newBlock;
        public NBlockViewHolder(@NonNull View itemView) {
            super(itemView);
            newBlock=itemView.findViewById(R.id.newBlock);
        }
    }
    public class CataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType==Catas.TaskCata.ordinal()){
                View view= LayoutInflater.from(getContext()).inflate(R.layout.module_task_catalog,parent,false);
                return new CataViewHolder(view);
            }else if(viewType==Catas.TaskCataNew.ordinal()){
                View view= LayoutInflater.from(getContext()).inflate(R.layout.module_cata_new,parent,false);
                return new NCataViewHolder(view);
            }
           return null;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof CataViewHolder){
                TaskCata tc=TaskCataList.get(position);
                ((CataViewHolder)holder).tv.setText(tc.getCataName());
                ((CataViewHolder)holder).num.setText(String.valueOf(tc.getTaskNum()));
            }

        }
        @Override
        public int getItemCount() {
            return TaskCataList.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            return position <TaskCataList.size() ? Catas.TaskCata.ordinal() : Catas.TaskCataNew.ordinal();
        }
    }

    public static class CataViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        TextView num;
        public CataViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textview);
            num=itemView.findViewById(R.id.num);
        }
    }
    public static class NCataViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        TextView num;
        public NCataViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textview);
            num=itemView.findViewById(R.id.num);
        }
    }

}