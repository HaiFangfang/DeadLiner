package com.example.deadliner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TaskFragment extends Fragment {
    List<TaskBlock> TaskBlockList = new ArrayList<>();
    List<TaskCata> TaskCataList = new ArrayList<>();
    RecyclerView taskBlocks;
    BlockAdapter blockAdapter;
    RecyclerView taskCata;
    CataAdapter cataAdapter;
    String cataSelectedID;
    MyDBhelper db;
    public TaskInfo page;
    private boolean mHandledPress;



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
        // Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        // fragment.setArguments(args);
        return new TaskFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MainActivity.getdb();



//        Cursor task_default=db.getWritableDatabase().query("task",null,null,null,"ddl",null,null);
//        while(task_default.moveToNext()){
//            TaskBlock tb = new TaskBlock();
////            tb.setDDL(String.valueOf(i));
////            tb.setTN(String.valueOf(i + 3));
//            TaskBlockList.add(tb);
//            Toast.makeText(getContext(), task_default.getString(0)+task_default.getString(1)+task_default.getString(2), Toast.LENGTH_SHORT).show();
//        }


        //        for (int i = 0; i < 10; ++i) {
//            TaskCata tc = new TaskCata();
//            tc.setCataName(String.valueOf(i));
//            tc.setTaskNum(i);
//            TaskCataList.add(tc);
//        }

    }
    @Override
    public void onResume() {
        super.onResume();

        //当前Fragment用户可见了
        //可以启动网络加载或绘图工作了。
    }
    public void refreshTaskCata(){
        Cursor cata_c = db.getWritableDatabase().query("cata",null,null,null,null,null,null);
        String sql = "select count(*) from task";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        String count = cursor.getString(0);

        TaskCata tc = new TaskCata();
        tc.setId("");
        tc.setCataName("所有任务");
//        tc.setOpenNum(Integer.parseInt(cata_c.getString(2)));
        tc.setTaskNum(Integer.parseInt(count));
        TaskCataList.add(tc);
        while(cata_c.moveToNext()){
            tc = new TaskCata();
            tc.setId(cata_c.getString(0));
            tc.setCataName(cata_c.getString(1));
            tc.setOpenNum(Integer.parseInt(cata_c.getString(2)));
            tc.setTaskNum(Integer.parseInt(cata_c.getString(3)));
            TaskCataList.add(tc);
//            Toast.makeText(getContext(), cata_c.getString(0)+cata_c.getString(1)+cata_c.getString(2), Toast.LENGTH_SHORT).show();
        }
        cataAdapter.notifyDataSetChanged();
    }

    public void refreshTaskBlock(){
        Cursor cata_tasks;
        if(cataSelectedID.isEmpty()){
            cata_tasks=db.getWritableDatabase().query("task",null,null,null,null,null,null);

        }else{
           cata_tasks=db.getWritableDatabase().query("task",null,"cata=?",new String[]{cataSelectedID},null,null,null);
        }

        TaskBlockList.clear();
        while(cata_tasks.moveToNext()){
            TaskBlock tb=new TaskBlock();
            tb.setId(cata_tasks.getString(0));
            tb.setTN(cata_tasks.getString(1));
            tb.setDDL(cata_tasks.getString(2));
            TaskBlockList.add(tb);
        }
        Comparator comp = new TaskComparator();
        Collections.sort(TaskBlockList,comp);
        blockAdapter.notifyDataSetChanged();
    }
    private String addCata(String name){
        try{
            db.getWritableDatabase().execSQL("insert into cata(name) values (?)", new Object[] { name});
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        String sql="select last_insert_rowid() from task";
        Cursor c=db.getWritableDatabase().rawQuery(sql, null);
        if(c.moveToFirst()){
            return String.valueOf(c.getInt(0));
        }
        return "";
    }
    private void renameCata(String name,String id){
        ContentValues c=new ContentValues();
        c.put("name",name);
        try{
            db.getWritableDatabase().update("cata",c,"id=?",new String[]{id});
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void refreshTaskCata(String name,String id) {Toast.makeText(getContext(), "freahed"+name, Toast.LENGTH_SHORT).show();
        if(id.isEmpty()){
           addCata(name);
        }else{
            renameCata(name,id);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d("?","oncreateview");
        View tbs = inflater.inflate(R.layout.fragment_task, container, false);
        taskCata = tbs.findViewById(R.id.task_catalogs);
        cataAdapter = new CataAdapter();
        taskCata.setAdapter(cataAdapter);
        taskCata.setLayoutManager(new LinearLayoutManager(getContext()));
        taskBlocks = tbs.findViewById(R.id.task_blocks);
        blockAdapter = new BlockAdapter();
        taskBlocks.setAdapter(blockAdapter);
        taskBlocks.setLayoutManager(new LinearLayoutManager(getContext()));
        taskBlocks.addItemDecoration(getBlockDecoration());

        refreshTaskCata();
        cataSelectedID=TaskCataList.get(0).getId();
        refreshTaskBlock();
        return tbs;
    }
    private RecyclerView.ItemDecoration getBlockDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 15;
                outRect.bottom=5;
                outRect.left=20;
                outRect.right=25;

            }
        };
    }


    public class BlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        private BlockClickListener BlockClickListener = null;
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == Blocks.TaskBlock.ordinal()) {
                View viewt = LayoutInflater.from(getContext()).inflate(R.layout.module_task_block, parent, false);
                return new TBlockViewHolder(viewt);
            } else if (viewType == Blocks.TaskBlockNew.ordinal()) {
                View viewn = LayoutInflater.from(getContext()).inflate(R.layout.module_task_new, parent, false);
                return new NBlockViewHolder(viewn);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TBlockViewHolder) {
                TaskBlock tb = TaskBlockList.get(position);
                ((TBlockViewHolder) holder).taskName.setText(tb.getTN());
                ((TBlockViewHolder) holder).daysRemain.setText("剩余"+tb.getDDL()+"天");
                ((TBlockViewHolder) holder).DDL.setText(tb.getDDL());
//                ((TBlockViewHolder) holder).textView3.setText(String.valueOf(TaskBlockList.size()));
                holder.itemView.setOnClickListener(view-> {
                    int pos = holder.getAbsoluteAdapterPosition();
                    jump(TaskBlockList.get(pos).getId());
                });
                holder.itemView.setOnLongClickListener(view->{
                    int pos = holder.getAbsoluteAdapterPosition();
                    Vibrator v=(Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(30);
                    initBlockPop(view,TaskBlockList.get(pos).getId());
                    return true;
                });
            } else if (holder instanceof NBlockViewHolder) {
                TaskBlockNew tn = new TaskBlockNew();
                holder.itemView.setOnClickListener(view-> {
                    jump(null);
                });
            }
        }

        @Override
        public int getItemCount() {
            return cataSelectedID.isEmpty()?TaskBlockList.size():TaskBlockList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position < TaskBlockList.size() ? Blocks.TaskBlock.ordinal() : Blocks.TaskBlockNew.ordinal();
        }



        }

        public void jump(String id) {
            FragmentManager fm = getParentFragmentManager();
            page=new TaskInfo(id,cataSelectedID);
            fm.beginTransaction()
                    .addToBackStack(null) // 将当前fragment加入到返回栈中
                    .replace(R.id.tasks, page).commit();
        }
        public void openNamingPop(String id){
            TaskCataNamingFragment dialog = new TaskCataNamingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id",id);
            dialog.setArguments(bundle);
            dialog.show(getParentFragmentManager(), "dialog_fragment");
        }
        private void showDialog(String id) {
            stdDialog dialog = new stdDialog(getContext(), R.style.mdialog,id);
            Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
            final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.y=-150;
            dialog.getWindow().setAttributes(params);
            dialog.show();
        }


    private void initCataPop(View v,String id) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.module_task_cata_op_pop, null, false);
        Button cata_del = (Button) view.findViewById(R.id.cata_op_del);
        Button cata_edit = (Button) view.findViewById(R.id.cata_op_edit);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view, v.getWidth(), v.getHeight(), true);

        if(id.equals("1")){
            cata_del.setVisibility(View.INVISIBLE);
        }
//        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, -v.getWidth(), -v.getHeight());

        //设置popupWindow里的按钮的事件
        cata_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                showDialog(id);
                popWindow.dismiss();
            }
        });
        cata_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNamingPop(id);
                popWindow.dismiss();
            }
        });
    }
    private void initBlockPop(View v,String id) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.module_task_block_op_pop, null, false);
        Button block_del = (Button) view.findViewById(R.id.block_op_del);
        Button block_comp = (Button) view.findViewById(R.id.block_op_comp);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view, v.getWidth(), v.getHeight(), true);


//        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 0, -180);

        //设置popupWindow里的按钮的事件
        block_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                showDialog(id);
                popWindow.dismiss();
            }
        });
        block_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNamingPop(id);
                popWindow.dismiss();
            }
        });
    }

    public static class TBlockViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView daysRemain;
        TextView textView3;
        TextView DDL;
        ConstraintLayout constraintLayout;

        public TBlockViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_block_name);
            daysRemain = itemView.findViewById(R.id.task_block_days_remain);
            DDL = itemView.findViewById(R.id.task_block_ddl);
            constraintLayout = itemView.findViewById(R.id.task_block);
        }
    }

    public static class NBlockViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout newBlock;

        public NBlockViewHolder(@NonNull View itemView) {
            super(itemView);
            newBlock = itemView.findViewById(R.id.newBlock);
        }
    }

    public class CataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == Catas.TaskCata.ordinal()) {
                View cataview = LayoutInflater.from(getContext()).inflate(R.layout.module_task_catalog, parent, false);

                return new CataViewHolder(cataview);
            } else if (viewType == Catas.TaskCataNew.ordinal()) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.module_cata_new, parent, false);
                view.setOnClickListener(view1 -> {openNamingPop("");});
                return new NCataViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CataViewHolder) {
                TaskCata tc = TaskCataList.get(position);
                ((CataViewHolder) holder).tv.setText(tc.getCataName());
                ((CataViewHolder) holder).num.setText(String.valueOf(tc.getTaskNum()));
                holder.itemView.setOnLongClickListener(view->{
                    Vibrator v=(Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(30);
                    int pos = holder.getAbsoluteAdapterPosition();
                    initCataPop(view,TaskCataList.get(pos).getId());
                    return true;
                });
                holder.itemView.setOnClickListener(view -> {
                    int pos = holder.getAbsoluteAdapterPosition();
                    cataSelectedID=TaskCataList.get(pos).getId();
                    refreshTaskBlock();
                });
            }

        }

        @Override
        public int getItemCount() {
            return TaskCataList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position < TaskCataList.size() ? Catas.TaskCata.ordinal() : Catas.TaskCataNew.ordinal();
        }


    }

    public static class CataViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView num;

        public CataViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textview);
            num = itemView.findViewById(R.id.num);
        }
    }

    public static class NCataViewHolder extends RecyclerView.ViewHolder {
//        TextView tv;
//        TextView num;
          TextView newcata;
        public NCataViewHolder(@NonNull View itemView) {
            super(itemView);
//            tv = itemView.findViewById(R.id.textview);
//            num = itemView.findViewById(R.id.num);
            newcata=itemView.findViewById(R.id.newcata)
;        }
    }

    private class TaskComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            TaskBlock a = (TaskBlock) lhs;
            TaskBlock b = (TaskBlock) rhs;
            if(a.getDDL()==null){
                return 1;
            }
            if(b.getDDL()==null){
                return -1;
            }
            return (Integer.parseInt(a.getDDL()) - Integer.parseInt(b.getDDL()));
        }

    }
    public static String ShowTimeInterval(Date date) {
        long lDate1 = date.getTime();
        long lDate2 =  new Date(System.currentTimeMillis()).getTime();
        long diff = lDate1 - lDate2;
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = diff / (60 * 60 * 1000) - day * 24;
        long min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
        long sec = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        return String.valueOf(day)+" "+String.valueOf(hour)+" "+String.valueOf(min);
    }
}