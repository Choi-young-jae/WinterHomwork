package com.example.user.cowaplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by user on 2015-02-15.
 */
//소 목록을 관리하기 위한 데이터베이스 헬퍼 클래스 구성
public class DatabaseHelper {

    public static SQLiteDatabase db;
    public static String dbname = "/storage/sdcard0/Handler.db";
    public static String tablename = "cowlist"; //소의 목록을 저장 하기 위한 테이블
    public static String detailname = "detaillist"; //클릭 하였을 경우 세부정보를 보여줄 테이블
    public static String workname = "worklist"; //일정을 저장할 테이블

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
            Log.d("DB close","Close");
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
    public static void createWorktable()
    {
        try{
            Log.d("Create work table ","table make");
            db.execSQL("create table if not exists " + workname + "("
                    + " _id integer PRIMARY KEY autoincrement, "
                    + " cownumber text,"
                    + " year text,"
                    + " month text, "
                    + " day text, "
                    + " hour text, "
                    + " min text, "
                    + " simplememo text, "
                    + " resetNum text);");
        }
        catch(Exception ext)
        {
            Log.d("Create table fail","table make fail");
        }
    }
    public static boolean insertData(listdata insertdata)
    {
        openDatabase(tablename);
        openDatabase(detailname);
        openDatabase(workname);

        String none = "내용을 입력해 주세요.";
        String nowork = "지금은 일정이 없어요.";
        String test = "---";
        db.execSQL("insert into " + tablename + "(location, number, sex, birthday) values (" +
                "'" + insertdata.location + "'," +
                "'" + insertdata.number + "'," +
                "'" + insertdata.sex + "'," +
                "'" + insertdata.birthday + "');");
        db.execSQL("insert into " + detailname + "(number, detail, memo) values (" +
                "'" + insertdata.number + "'," +
                "'" + none + "'," +
                "'" +  none + "');");
        db.execSQL("insert into " + workname + "(cownumber, year, month, day, hour, min,simplememo,resetNum) values (" +
                "'" + insertdata.number + "'," +
                "'" + test + "'," +
                "'" + test + "'," +
                "'" + test + "'," +
                "'" + test + "'," +
                "'" + test + "'," +
                "'" + nowork + "'," +
                "'NO');");

        return true;
    }
    public static boolean deleteData(listdata deletedata)
    {
        String delnum = deletedata.number;
        openDatabase(tablename);
        openDatabase(detailname);
        openDatabase(workname);

        db.delete(tablename,"number=?",new String[]{delnum});
        db.delete(detailname,"number=?",new String[]{delnum});
        db.delete(workname,"cownumber=?",new String[]{delnum});
        closeDatabase();

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
    public static boolean setWorkDB(String daySplit[],String cownum)
    {
        Log.d("workname table",daySplit[0] + " / " + daySplit[1] + " / " + daySplit[2] + " / " + daySplit[3] + " / " + daySplit[4] + " / " + daySplit[5]);

        db.execSQL("update " + workname + " set year = '" + daySplit[0] +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname  + " set month = '" + daySplit[1] +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set day = '" + daySplit[2] +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set hour = '" + daySplit[3] +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set min = '" + daySplit[4] +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set simplememo = '" + daySplit[5] +
                "' where cownumber = '" + cownum + "';" );

        Log.d(null,"Update Db " + workname);
        return true;
    }
    public static void resetWork(String cownum)
    {
        Log.d(null,cownum + " reset Start!");

        String reset = "--";
        db.execSQL("update " + workname + " set year = '" + reset +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname  + " set month = '" +reset +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set day = '" + reset +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set hour = '" + reset +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set min = '" +reset +
                "' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set simplememo = '지금은 일정이 없어요.' where cownumber = '" + cownum + "';" );
        db.execSQL("update " + workname + " set resetNum = 'NO' where cownumber = '" + cownum + "';" );

        Log.d(null,"Update Db " + workname);
    }

    public static Cursor SearchData(String tablename,String searchContent)
    {
        Log.d("SearchData","찾기 모드");

        Cursor returnCursor = null;

        if(tablename.compareTo(workname)==0)
        {
            Log.d("SearchData","커서 찾기");
            String aSQL = "select cownumber, year, month, day, hour, min, simplememo, resetNum"
                    + " from " + tablename
                    + " where cownumber like ?";
            String[] args = {searchContent};

            returnCursor = db.rawQuery(aSQL, args);
            return returnCursor;
        }
        else if(tablename.compareTo(tablename)==0)
        {
            Log.d("SearchData","커서 찾기");
            String aSQL = "select location, number, sex, birthday"
                    + " from " + tablename
                    + " where number like ?";
            String[] args = {searchContent};

            returnCursor = db.rawQuery(aSQL, args);
            return returnCursor;
        }

        Log.d("SearchData","리턴 오류");
        return returnCursor;
    }
    public static Cursor SearchData(String tablename,String searchContent, int searchtype)
    {
        Log.d("SearchData","찾기 모드");

        Cursor returnCursor = null;
        String[] args = {searchContent};
        if(searchtype ==0)
        {
            String aSQL = "select location, number, sex, birthday"
                    + " from " + tablename
                    + " where location like ?";
            returnCursor = db.rawQuery(aSQL, args);
        }
        else if(searchtype==1)
        {
            String aSQL = "select location, number, sex, birthday"
                    + " from " + tablename
                    + " where number like ?";
            returnCursor = db.rawQuery(aSQL, args);
        }
        else if(searchtype==2)
        {
            String aSQL = "select location, number, sex, birthday"
                    + " from " + tablename
                    + " where sex like ?";
            returnCursor = db.rawQuery(aSQL, args);
        }
        else
        {
            String aSQL = "select location, number, sex, birthday"
                    + " from " + tablename
                    + " where birthday like ?";
            returnCursor = db.rawQuery(aSQL, args);
        }

        Log.d("SearchData","커서 찾기");
        return returnCursor;
    }
}
