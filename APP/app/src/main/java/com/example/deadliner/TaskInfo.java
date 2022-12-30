package com.example.deadliner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.os.Vibrator;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskInfo extends Fragment {
    private String cata;
    private String id="";
    private TextView name_text;
    private TextView times;
    private EditText name_edit;
    private ImageView edit;
    private ImageView save;
    private TextView DDL;
    private TextView ST;
    private TextView TR;
    Date DDLts;
    Date STts;


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
        if(id==null){
            ChangeToEditMode(null);
            SaveInfo();
        }else{
            Cursor c=MainActivity.getdb().getWritableDatabase().query("task",null,"id=?",new String[]{id},null,null,null);
            while (c.moveToNext()){
                name_text.setText(c.getString(1));
                name_edit.setText(c.getString(1));
            }

        }
        times=tinfo.findViewById(R.id.task_times);
        times.setOnLongClickListener(view -> {
            Vibrator v=(Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(30);
            initDatePicker();
            return true;
        });
        DDL=tinfo.findViewById(R.id.task_ddl);
        ST=tinfo.findViewById(R.id.task_start_time);
        TR=tinfo.findViewById(R.id.task_days_remain);

        return tinfo;

    }
    private void ChangeToEditMode(String id){
        name_text.setVisibility(View.INVISIBLE);
        name_edit.setVisibility(View.VISIBLE);
        edit.setVisibility(View.INVISIBLE);
        save.setVisibility(View.VISIBLE);
        if(id!=null){


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
                        db.getWritableDatabase().execSQL("insert into task(name,start_time,ddl,status,cata) values (?,?,?,?,?)", new Object[] { name_edit.getText().toString(),null,null,1,cata });
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    String sql="select last_insert_rowid() from task";
                    Cursor c=db.getWritableDatabase().rawQuery(sql, null);
                    if(c.moveToFirst()){
                        id=String.valueOf(c.getInt(0));
                    }
                }
                name_text.setText(name_edit.getText().toString());
//        Toast.makeText(getContext(), "saved"+id+name_edit.getText().toString(), Toast.LENGTH_SHORT).show();

        return true;
    }


    public void refreshTimes(String DDL, String time, String sttime) {
        setDDL(DDL+time);

        setStart(sttime);
        Date date;
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            date=df.parse(DDL+" "+time);
            DDLts=date;
            STts=df.parse(sttime+" 00:00");
            setTR( ShowTimeInterval(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        }

    }
    public void setTR(String TM){
         TR.setText(TM);
    }

        public static String ShowTimeInterval(Date date) {
            long lDate1 = date.getTime();
            long lDate2 =  new Date(System.currentTimeMillis()).getTime();
            long diff = lDate1 - lDate2;
            long day = diff / (24 * 60 * 60 * 1000);
            long hour = diff / (60 * 60 * 1000) - day * 24;
            long min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
            long sec = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
            System.out.println("date1 与 date2 相差 " + day + "天" + hour + "小时" + min + "分" + sec + "秒");
            return String.valueOf(day)+" "+String.valueOf(hour)+" "+String.valueOf(min);
        }

}