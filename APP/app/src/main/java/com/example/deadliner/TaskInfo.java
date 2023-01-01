package com.example.deadliner;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskInfo extends Fragment {
    private String cata;
    private String id;
    private TextView name_text;
    private TextView times;
    private EditText name_edit;
    private ImageView edit;
    private ImageView save;
    private TextView DDL;
    private TextView ST;
    private TextView TR;
    private Button add_pro;
    MyDBhelper db;
    List<detail> detailList =new ArrayList<>();
    detailAdapter detailAdapter=new detailAdapter();
    RecyclerView details;


    public TaskInfo() {
        // Required empty public constructor
    }
    public TaskInfo(String n,String cata) {
        this.cata=cata;
        id=n;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment TaskInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskInfo newInstance() {
        TaskInfo fragment = new TaskInfo();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tinfo = inflater.inflate(R.layout.fragment_task_info, container, false);
        ImageView back = tinfo.findViewById(R.id.back);
        back.setOnClickListener(view -> {
            ((TaskFragment)(((MainActivity)getActivity()).getFragmentList().get(1))).refreshTaskBlock();
            ((HomeFragment)(((MainActivity)getActivity()).getFragmentList().get(0))).refreshData();
            ((TaskFragment)(((MainActivity)getActivity()).getFragmentList().get(1))).page=null;
            FragmentManager fm=getParentFragmentManager();
            fm.popBackStack();
        });
        edit=tinfo.findViewById(R.id.edit);
        edit.setOnClickListener(view -> {
            ChangeToEditMode(id);
                }
        );
        save=tinfo.findViewById(R.id.save);
        save.setOnClickListener(view -> {
            SaveInfo();
            QuitEditMode();
        });
        name_edit=tinfo.findViewById(R.id.task_name_edit);
        name_text=tinfo.findViewById(R.id.task_name);

        times=tinfo.findViewById(R.id.task_times);
        times.setOnLongClickListener(view -> {
            Vibrator v=(Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(30);
            initDatePicker();
            return true;
        });
        times.setOnClickListener(view -> {
            selectColor();
        });
        DDL=tinfo.findViewById(R.id.task_ddl);
        ST=tinfo.findViewById(R.id.task_start_time);
        TR=tinfo.findViewById(R.id.task_days_remain);
        if(id==null){
            ChangeToEditMode(null);
            SaveInfo();
        }else{
            initData(id);

        }
        Cursor c=MainActivity.getdb().getWritableDatabase().query("progress",null,"task=?",new String[]{id},null,null,"seq");
        while (c.moveToNext()){
            detail d=new detail();
            d.setId(c.getString(0));
            d.setName(c.getString(1));
            d.setSeq(c.getInt(2));
            if(c.getInt(3)==0){
                d.setComplete(false);
            }else{
                d.setComplete(true);
            }
            detailList.add(d);
        }
        details=tinfo.findViewById(R.id.task_progress_detail);
        details.setAdapter(detailAdapter);
        details.setLayoutManager(new LinearLayoutManager(getContext()));
        add_pro=tinfo.findViewById(R.id.new_progress);
        add_pro.setOnClickListener(view -> {
            detail nd=new detail();
            nd.setComplete(false);
            nd.setName("");
            nd.setSeq(detailList.size());
            detailList.add(nd);
            MainActivity.getdb().getWritableDatabase().execSQL("insert into progress(name,status,seq,task) values (?,?,?,?)", new Object[] {"",0,nd.getSeq(),id});
            String sql="select last_insert_rowid() from progress";
            Cursor c1=db.getWritableDatabase().rawQuery(sql, null);
            if(c1.moveToFirst()){
               nd.setId(c1.getString(0));
            }
            detailAdapter.notifyDataSetChanged();
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new  MyItemTouchHelperCallBack());
        itemTouchHelper.attachToRecyclerView(details);
        return tinfo;

    }
    public class MyItemTouchHelperCallBack extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 有6个值来控制方向
            //控制拖拽的方向（一般是上下左右）
            int dragFlags= ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            //控制快速滑动的方向（一般是左右）
            int swipeFlags = 0;
//还有两个flag 分别是 ItemTouchHelper.START 和 ItemTouchHelper.END ,原文的解释是：
//Horizontal direction. Resolved to LEFT or RIGHT depending on RecyclerView's layout direction. Used for swipe & drag control.
// 横向方向,取决于 RecyclerView 的方向，与 LinearLayoutManager 的 layoutReverse 有关（暂时没有验证）
            return makeMovementFlags(dragFlags, swipeFlags);//计算movement flag值
        }
        int startPosition;
        int endPosition;
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
        }
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
//
//            if (viewHolder != null && actionState != ACTION_STATE_IDLE) {
//                // 非闲置状态下，记录下起始 position
//                startPosition = viewHolder.getAbsoluteAdapterPosition();
//            }
//            if(actionState == ACTION_STATE_IDLE){
//                // 当手势抬起时刷新，endPosition 是在 onMove() 回调中记录下来的
//                detailAdapter adapter = detailAdapter;
//                if (adapter != null) {
//                    adapter.notifyItemRangeChanged(Math.min(startPosition, endPosition), Math.abs(startPosition - endPosition) + 1);
//                }
//            }
            for(int i=0;i<detailList.size();i++){
//                Toast.makeText(getContext(), detailList.get(i).getName(), Toast.LENGTH_SHORT).show();
                changePos(detailList.get(i),i);
            }
        }
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // 拖拽时，每移动一个位置就会调用一次。
            // 在此改变 dataList 被移动 item 的位置，并且刷新adapter
            if (recyclerView == null) return false;
            detailAdapter adapter = detailAdapter;
            if (adapter == null) return false;
            if (detailList != null) {
                int from = viewHolder.getAbsoluteAdapterPosition();
                int endPosition = target.getAbsoluteAdapterPosition();//在这里我一直在刷新最后移动到的位置，以便接下来做其他操作
                Toast.makeText(getContext(), from+" "+endPosition, Toast.LENGTH_SHORT).show();

                String n1=detailList.get(from).getName();
                String n2=detailList.get(endPosition).getName();
                Boolean c1=detailList.get(from).isComplete();
                Boolean c2=detailList.get(endPosition).isComplete();

                String i1=detailList.get(from).getId();
                String i2=detailList.get(endPosition).getId();
                detailList.get(from).setName(n2);
                detailList.get(from).setId(i2);
                detailList.get(from).setComplete(c2);
                detailList.get(endPosition).setName(n1);
                detailList.get(endPosition).setId(i1);
                detailList.get(endPosition).setComplete(c1);
//                Collections.swap(detailList, from, endPosition);//数据交换位置
                // 使用notifyItemMoved可以表现得更平滑，问题是 from ~ endPosition 间的item position 不会更新，并引发一系列角标混乱的问题，
                //这个问题可以在后面的 onSelectedChanged()方法中解决。
                // 在此做notifyItemMoved操作就足够了，notifyDataSetChanged() 和 notifyItemRangeChanged() 会打断 drag 操作。
                adapter.notifyItemMoved(from, endPosition);
                //其他操作
//                if (onMovedListener != null) {
//                    onMovedListener.onMoved(dataList, from, endPosition);
//                }
            }
            return true; // true 可以拖拽，false 不能。
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //滑动处理
        }
    }
    private void ChangeToEditMode(String id){
        name_text.setVisibility(View.INVISIBLE);
        name_edit.setVisibility(View.VISIBLE);
        edit.setVisibility(View.INVISIBLE);
        save.setVisibility(View.VISIBLE);
        if(id!=null){


        }

    }
    private void initData(String id){
        db=MainActivity.getdb();
        Cursor tinfo=db.getWritableDatabase().query("task",null,"id=?",new String[]{id},null,null,null);
        if(tinfo.moveToNext()){
            name_text.setText(tinfo.getString(1));
            name_edit.setText(tinfo.getString(1));
            ST.setText(tinfo.getString(2).isEmpty()?"-":tinfo.getString(2));
            DDL.setText(tinfo.getString(3).isEmpty()?"-":tinfo.getString(3));
            if(tinfo.getString(3).isEmpty()){
                TR.setText("余 -");
            }else{
                setTR(tinfo.getString(3));
            }

            Toast.makeText(getContext(), tinfo.getString(1), Toast.LENGTH_SHORT).show();
        }
    }
    public void setDDL(String D){
      DDL.setText(D);

    }
    public void setStart(String S){
      ST.setText(S);

    }
    private void initDatePicker() {
        TaskDatePickerFragment dialog = new TaskDatePickerFragment();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "dialog_fragment");
    }
    private void QuitEditMode(){
        name_text.setVisibility(View.VISIBLE);
        name_edit.setVisibility(View.INVISIBLE);
        edit.setVisibility(View.VISIBLE);
        save.setVisibility(View.INVISIBLE);
        SaveInfo();
    }
    private boolean SaveInfo(){
        MyDBhelper db=MainActivity.getdb();
                if(id!=null){
                    ContentValues c=new ContentValues();
                    c.put("name",name_edit.getText().toString());
                    try{
                        db.getWritableDatabase().update("task",c,"id=?",new String[]{id});
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    try{
                        db.getWritableDatabase().execSQL("insert into task(name,start_time,ddl,status,color,cata) values (?,?,?,?,?,?)", new Object[] { name_edit.getText().toString(),"","",1,0,cata });
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    String sql="select last_insert_rowid() from task";
                    Cursor c=db.getWritableDatabase().rawQuery(sql, null);
                    if(c.moveToFirst()){
                        id=c.getString(0);
                    }
                }
                name_text.setText(name_edit.getText().toString());
//        Toast.makeText(getContext(), "saved"+id+name_edit.getText().toString(), Toast.LENGTH_SHORT).show();

        return true;
    }


    public void refreshTimes(String DDL, String time, String sttime) {
        setDDL(DDL+" "+time);

        setStart(sttime);
        setTR(DDL+" "+time);
        MyDBhelper db=MainActivity.getdb();
        if(id!=null){
            ContentValues c=new ContentValues();
            c.put("DDL",DDL+" "+time);
            c.put("start_time",sttime);
            try{
                db.getWritableDatabase().update("task",c,"id=?",new String[]{id});
            }catch (Exception e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            ((TaskFragment)(((MainActivity)getActivity()).getFragmentList().get(1))).refreshTaskBlock();
            ((HomeFragment)(((MainActivity)getActivity()).getFragmentList().get(0))).refreshData();
        }

    }

    public void selectColor() {
            TaskColorSelectFragment dialog = new TaskColorSelectFragment();
            Bundle bundle = new Bundle();
            dialog.setArguments(bundle);
            dialog.show(getParentFragmentManager(), "dialog_fragment");
    }
    public void refreshColor(int color){
        MyDBhelper db=MainActivity.getdb();
        ContentValues c=new ContentValues();
        c.put("color",color);
        if(id!=null){
            try{
                db.getWritableDatabase().update("task",c,"id=?",new String[]{id});
            }catch (Exception e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            ((TaskFragment)(((MainActivity)getActivity()).getFragmentList().get(1))).refreshTaskBlock();
            ((HomeFragment)(((MainActivity)getActivity()).getFragmentList().get(0))).refreshData();
        }
    }
    public void setTR(String DDLtime){
        Date date;
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            date=df.parse(DDLtime);
            TR.setText(ShowTimeInterval(date));
        } catch (ParseException e) {
            e.printStackTrace();
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
            if(day<0||hour<0||min<0||sec<0){
                return "超时 "+String.valueOf(-day)+"D "+String.valueOf(-hour)+"H";
            }
            return "余 "+String.valueOf(day)+"D "+String.valueOf(hour)+"H";
        }
    public class detailAdapter extends RecyclerView.Adapter<detailViewHolder> {
        @NonNull
        @Override
        public detailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewt = LayoutInflater.from(getContext()).inflate(R.layout.module_detail_block, parent, false);

            return new detailViewHolder(viewt);
        }


        @Override
        public void onBindViewHolder(@NonNull detailViewHolder holder, int position) {



            holder.cb=holder.itemView.findViewById(R.id.detail_check);
            holder.name=holder.itemView.findViewById(R.id.detail_name);
            holder.name_edit=holder.itemView.findViewById(R.id.detail_name_edit);
            holder.save=holder.itemView.findViewById(R.id.detail_save);
            holder.name.setOnClickListener(view -> {
                holder.name_edit.setText(holder.name.getText());
                holder.name.setVisibility((View.INVISIBLE));
                holder.name_edit.setVisibility(View.VISIBLE);
                holder.save.setVisibility(View.VISIBLE);
            });
            holder.save.setOnClickListener(view -> {
                int pos = holder.getAbsoluteAdapterPosition();
                holder.name.setText(holder.name_edit.getText().toString());
                holder.name.setVisibility((View.VISIBLE));
                holder.name_edit.setVisibility(View.INVISIBLE);
                holder.save.setVisibility(View.INVISIBLE);
                ContentValues c=new ContentValues();
                c.put("name",holder.name_edit.getText().toString());
                detailList.get(pos).setName(holder.name_edit.getText().toString());
                MainActivity.getdb().getWritableDatabase().update("progress",c,"id="+ detailList.get(pos).getId(),null);
            });
            holder.del=holder.itemView.findViewById(R.id.detail_del);
            holder.del.setOnClickListener(view -> {
                int pos = holder.getAbsoluteAdapterPosition();
                MainActivity.getdb().getWritableDatabase().delete("progress","id="+ detailList.get(pos).getId(),null);
                detailList.remove(pos);
                for(int i=pos;i<detailList.size();i++){
                    changePos(detailList.get(i),i);
                }
                detailAdapter.notifyDataSetChanged();
            });
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = holder.getAbsoluteAdapterPosition();
                    ContentValues c=new ContentValues();
                    c.put("status",isChecked);
                    MainActivity.getdb().getWritableDatabase().update("progress",c,"id="+ detailList.get(pos).getId(),null);
                }
            });
            detail d = detailList.get(position);


            holder.name.setText(d.getName());
            if(d.isComplete()){
                holder.cb.setChecked(true);
            }
        }


        @Override
        public int getItemCount() {
            return  detailList.size();
        }

    }
    public void changePos(detail d,int i){
        ContentValues c=new ContentValues();
        c.put("seq",i);
        MainActivity.getdb().getWritableDatabase().update("progress",c,"id="+ d.getId(),null);
    }
    public static class detailViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;
        TextView name;
        ImageView del;
        ImageView save;
        EditText name_edit;

        public detailViewHolder(@NonNull View itemView) {
            super(itemView);

        }

    }

}