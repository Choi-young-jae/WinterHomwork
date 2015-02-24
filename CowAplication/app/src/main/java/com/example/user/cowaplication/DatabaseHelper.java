package com.example.user.cowaplication;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by user on 2015-02-15.
 */
//소 목록을 관리하기 위한 데이터베이스 헬퍼 클래스 구성
public class DatabaseHelper {

    public static SQLiteDatabase db;
    public static String dbname = "/storage/sdcard0/CowHandler.db";
    public static String tablename = "cowlist"; //소의 목록을 저장 하기 위한 테이블
    public static String detailname = "detaillist"; //클릭 하였을 경우 세부정보를 보여줄 테이블

    public DatabaseHelper()
    {

    }
    public static void openDatabase(String dbName)
    {
        try
        {
            //db= SQLiteDatabase.openOrCreateDatabase(dbname,Activity.MODE_MULTI_PROCESS,null);
            db = SQLiteDatabase.openDatabase(dbname,null,SQLiteDatabase.OPEN_READWRITE + +SQLiteDatabase.CREATE_IF_NECESSARY);
            Log.d("DataBase open ","open");
        }
        catch (SQLiteException ex)
        {
            //Toast.makeText(this, ex.getMessage(), 1).show();
            Log.d("DataBase open fail","fail");
        }
    }
    public static void closeDatabase() {
        try {
            // close database
            db.close();
        } catch(Exception ext) {
            ext.printStackTrace();
            Log.d("closeDatabase","Exception in closing database : " + ext.toString());
        }
    }
    public static void createDetailTable()
    {
        try{
            Log.d("Create detail table ","table make");

            db.execSQL("create table if not exists " + detailname + "("
                    + " _id integer PRIMARY KEY autoincrement, "
                    + " number text,"
                    + " detail text, "
                    + " memo text);");
        }
        catch(Exception ext)
        {
            Log.d("Create table fail","table make fail");
        }
    }
    public static void createCowListTable()
    {
        try{
            Log.d("Create cowlist table ","table make");
            db.execSQL("create table if not exists " + tablename + "("
                    + " _id integer PRIMARY KEY autoincrement, "
                    + " location text,"
                    + " number text, "
                    + " sex text,"
                    + " birthday text);");
        }
        catch(Exception ext)
        {
            Log.d("Create detail fail","table make fail");
        }
    }
    public static boolean insertData(listdata insertdata)
    {
        String none = "내용을 입력해 주세요.";
        db.execSQL("insert into " + tablename + "(location, number, sex, birthday) values (" +
                "'" + insertdata.location + "'," +
                "'" + insertdata.number + "'," +
                "'" + insertdata.sex + "'," +
                "'" + insertdata.birthday + "');");
        db.execSQL("insert into " + detailname + "(number, detail, memo) values (" +
                "'" + insertdata.number + "'," +
                "'" + none + "'," +
                "'" +  none + "');");
        return true;
    }
    public static boolean deleteData(listdata deletedata)
    {
        String aSQL = "select location, number, sex, birthday"
                + " from " + tablename
                + " where number like ?";
        String delnum = deletedata.number;
        db.delete(tablename,"number=?",new String[]{delnum});
        return true;
    }
    public static void modifyTable(listdata defaultdata, listdata changeName)
    {
        Log.d(null,"location table update");
        db.execSQL("update " + tablename + " set location = '" + changeName.location +
                "' where number = '" + defaultdata.number + "';" );
        Log.d(null,"sex table update");
        db.execSQL("update " + tablename + " set sex = '" + changeName.sex +
                "' where number = '" + defaultdata.number + "';" );
        Log.d(null,"birthday table update");
        db.execSQL("update " + tablename + " set birthday = '" + changeName.birthday +
                "' where number = '" + defaultdata.number + "';" );
        Log.d(null,"number table update");
        db.execSQL("update " + tablename + " set number = '" + changeName.number +
                "' where number = '" + defaultdata.number + "';" );

        Log.d(null,"Update Db " + tablename);
    }
    public static  void modifyDetail(String defualtnumber,String modifynumber,String modifydetail,String modifymemo)
    {
        db.execSQL("update " + detailname + " set detail = '" + modifydetail +
                "' where number = '" + defualtnumber + "';");
        db.execSQL("update " + detailname + " set memo = '" + modifymemo +
                "' where number = '" + defualtnumber + "';");
        db.execSQL("update " + detailname + " set number = '" + modifynumber +
                "' where number = '" + defualtnumber + "';");
    }
}
