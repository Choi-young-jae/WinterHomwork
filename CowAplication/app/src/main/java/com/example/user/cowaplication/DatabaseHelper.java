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
    public static String dbname = "/storage/sdcard0/CowHandler.db"; //DB파일이 저장될 위치
    public static String tablename = "cowlist"; //생성될 테이블의 이름

    public DatabaseHelper()
    {

    }
    public static void openDatabase(String dbName)
    {
        //데이터페이스를 열기
        try
        {
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
        //데이터베이스를 닫음
        try {
            // close database
            db.close();
        } catch(Exception ext) {
            ext.printStackTrace();
            Log.d("closeDatabase","Exception in closing database : " + ext.toString());
        }
    }

   public static void createCowListTable()
   {
       //정보를 저장할 Table을 생성
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
       //DB에 데이터를 추가할 경우
       db.execSQL("insert into " + tablename + "(location, number, sex, birthday) values (" +
               "'" + insertdata.location + "'," +
               "'" + insertdata.number + "'," +
               "'" + insertdata.sex + "'," +
               "'" + insertdata.birthday + "');");
       return true;
   }
    public static boolean deleteData(listdata deletedata)
    {
        //DB에서 데이터를 삭제할 경우
        String aSQL = "select location, number, sex, birthday"
                + " from " + tablename
                + " where number like ?";
        String delnum = deletedata.number;
        db.delete(tablename,"number=?",new String[]{delnum});
        return true;
    }
    public static void modifyTable(listdata defaultdata, listdata changeName)
    {
        //DB에서 데이터를 수정할 경우
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
}
