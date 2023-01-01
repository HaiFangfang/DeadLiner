package com.example.deadliner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class MyDBhelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME= "demo";//数据库名称
    private final static int VERSION= 1;//当前版本

    public static final String CREATE_TASKS = "create table task(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "name text NOT NULL," +
            "start_time text NOT NULL," +
            "ddl text NOT NULL," +
            "status boolean NOT NULL," +
            "color integer NOT NULL," +
            "cata integer NOT NULL)";
    public static final String CREATE_CATAS = "create table cata(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "name text NOT NULL," +
            "num_open integer default 0," +
            "num_all integer default 0)";
    public static final String CREATE_PROGRESS = "create table progress(" +
            //primary key 将id列设为主键    autoincrement表示id列是自增长的
            "id integer primary key autoincrement," +
            "name text NOT NULL," +
            "seq integer NOT NULL,"+
            "status boolean NOT NULL," +
            "task integer NOT NULL)";

    private SQLiteDatabase db;

    private Context mContext;

    //构造方法：第一个参数Context，第二个参数数据库名，第三个参数cursor允许我们在查询数据的时候返回一个自定义的光标位置，一般传入的都是null，第四个参数表示目前库的版本号（用于对库进行升级）
    public  MyDBhelper(Context context){
        super(context,DATABASE_NAME ,null,VERSION);
        mContext=context;
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //调用SQLiteDatabase中的execSQL（）执行建表语句。
        try{
            db.execSQL(CREATE_CATAS);
            db.execSQL(CREATE_TASKS);
            db.execSQL(CREATE_PROGRESS);
        } catch (Exception e){
            Log.e("exp",e.toString());
        }

        //创建成功
        this.db = db;
        db.execSQL("insert into cata(name) values (?)", new Object[] { "默认分类" });
//        Toast.makeText(mContext, "数据库成功创建", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}