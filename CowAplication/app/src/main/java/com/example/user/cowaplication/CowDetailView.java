package com.example.user.cowaplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by user on 2015-02-15.
 */
public class CowDetailView extends FragmentActivity{
    public static final String TAG = "CowDetailShowActivity";
    public static int ALARMCOUNT = 0;
    TextView txtMsg1; //상세 설명을 저장할 공간이다.
    TextView txtMsg2; //메모를 기록할 공간이다.
    TextView numberMsg; //list로 부터 받아온 번호
    TextView locationMsg; //list로 부터 받아온 위치
    TextView sexMsg; //list로 부터 받아온 성별
    TextView birthdayMsg; //list로 부터 받아온 출생일
    TextView setwork; //일정을 등록할 부분이다.
    Button cancelbtn;
    WorkSetDialog SetDateDialog;
    Bundle findbundle;

    public static final int REQUEST_CODE_DETAIL = 1003;
    public static final int REQUEST_CODE_FAMILY = 1008;
    public static final int REQUEST_CODE_CALENDAR = 1000;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cowdetail);
        txtMsg1 = (TextView)findViewById(R.id.txtMsg1);
        txtMsg2 = (TextView)findViewById(R.id.txtMsg2);
        locationMsg = (TextView)findViewById(R.id.locationspace);
        numberMsg = (TextView)findViewById(R.id.numberspace);
        sexMsg = (TextView)findViewById(R.id.sexspace);
        birthdayMsg = (TextView)findViewById(R.id.birthdayspace);
        setwork = (TextView)findViewById(R.id.setWork);
        cancelbtn = (Button)findViewById(R.id.daycancel_btn);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        printString(bundle);

        Button daysetbutton = (Button)findViewById(R.id.dayset_btn);
        //일정 등록 버튼을 눌렀을 경우.
        daysetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calintent = new Intent(getBaseContext(), ShowCalendar.class);
                startActivityForResult(calintent, REQUEST_CODE_CALENDAR);
            }
        });
    }
    // 알람 등록
    private void setAlarm(Context context, Calendar calendar,String daysplit[])
    {
        DatabaseHelper.openDatabase(DatabaseHelper.workname);
        Log.i(TAG, "setAlarm()");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CowDetailView.this,
                AlarmReceive.class);
        Bundle bundle = new Bundle();
        bundle.putString("data0",locationMsg.getText().toString());
        bundle.putString("data1",numberMsg.getText().toString());
        bundle.putString("data2",sexMsg.getText().toString());
        bundle.putString("data3",birthdayMsg.getText().toString());
        bundle.putInt("alarmnum",ALARMCOUNT);

        Log.d("Alram input",numberMsg.getText().toString() + " " + sexMsg.getText().toString() + " " +birthdayMsg.getText().toString()
         + " " + locationMsg.getText().toString());

        DatabaseHelper.setWorkDB(daysplit,numberMsg.getText().toString());
        intent.putExtras(bundle);

        PendingIntent pIntent = PendingIntent.getBroadcast(context, ALARMCOUNT, intent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pIntent);
        ALARMCOUNT++;
        DatabaseHelper.closeDatabase();
    }
    //알람 취소
    public void cancelAlram()
    {
        Calendar calendar = Calendar.getInstance();
        ALARMCOUNT--;

        Log.i(TAG, "cancelAlarm()");
        AlarmManager alarmManager = (AlarmManager)CowDetailView.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CowDetailView.this, AlarmReceive.class);
        Bundle bundle = new Bundle();
        bundle.putString("data0",locationMsg.getText().toString());
        bundle.putString("data1",numberMsg.getText().toString());
        bundle.putString("data2",sexMsg.getText().toString());
        bundle.putString("data3",birthdayMsg.getText().toString());
        bundle.putInt("alarmnum",ALARMCOUNT);

        Log.d("Alram cancel",numberMsg.getText().toString() + " " + sexMsg.getText().toString() + " " +birthdayMsg.getText().toString()
                + " " + locationMsg.getText().toString());
        DatabaseHelper.resetWork(numberMsg.getText().toString());
        intent.putExtras(bundle);

        PendingIntent pIntent = PendingIntent.getBroadcast(CowDetailView.this, ALARMCOUNT, intent, 0);
        alarmManager.cancel(pIntent);

    }

    public void printString(Bundle bundle)
    {
        try
        {
            DatabaseHelper.openDatabase(DatabaseHelper.dbname);
            DatabaseHelper.openDatabase(DatabaseHelper.detailname);
            DatabaseHelper.openDatabase(DatabaseHelper.workname);
            Log.d(null,"printString Come");

            String locationString = bundle.getString("data0");
            String numberString = bundle.getString("data1");
            String sexString = bundle.getString("data2");
            String birthdayString = bundle.getString("data3");

            println(locationString + " // " + numberString + " // " + sexString + " // " + birthdayString);
            locationMsg.setText(locationString);
            numberMsg.setText(numberString);
            sexMsg.setText(sexString);
            birthdayMsg.setText(birthdayString);
            detailInput();
            workinput();
            showCancelbtn();
        }
        catch(Exception e)
        {
            Log.d("DB열기 에러",e.toString());
        }
        finally {
            DatabaseHelper.closeDatabase();
        }

    }
    public void detailInput()
    {
        String aSQL = "select number, detail, memo"
                + " from " + DatabaseHelper.detailname
                + " where number = ?";

        String[] args = {numberMsg.getText().toString()};
        Log.d("detail number = ",numberMsg.getText().toString());
        Cursor outCoursor = DatabaseHelper.db.rawQuery(aSQL,args);

        try
        {    outCoursor.moveToFirst();

            int detailCol = outCoursor.getColumnIndex("detail");
            Log.d("detail col",detailCol + "");
            int memoCol = outCoursor.getColumnIndex("memo");
            Log.d("memo col",memoCol + "");

            outCoursor.moveToFirst();
            String details = outCoursor.getString(detailCol);
            Log.d("detail String  ",details);
            String memos = outCoursor.getString(memoCol);
            Log.d("detail memos  ",memos);

            txtMsg1.setText(details);
            txtMsg2.setText(memos);
        }
        catch(Exception e)
        {
            Log.d("detailInput cursor error...",e.toString());
        }
        finally {
            outCoursor.close();
        }

    }
    public void workinput()
    {
        String aSQL = "select cownumber, year, month, day, hour, min, simplememo, resetNum"
                + " from " + DatabaseHelper.workname
                + " where cownumber = ?";


        String[] args = {numberMsg.getText().toString()};
        Log.d("work number = ",numberMsg.getText().toString());

        Cursor workCoursor = DatabaseHelper.db.rawQuery(aSQL,args);

        int numberCol = workCoursor.getColumnIndex("cownumber");
        Log.d("detail col",numberCol + "");
        int yearCol = workCoursor.getColumnIndex("year");
        Log.d("detail col",yearCol + "");
        int monthCol = workCoursor.getColumnIndex("month");
        Log.d("detail col",monthCol + "");
        int dayCol = workCoursor.getColumnIndex("day");
        Log.d("detail col",dayCol + "");
        int hourCol = workCoursor.getColumnIndex("hour");
        Log.d("detail col",hourCol + "");
        int minCol = workCoursor.getColumnIndex("min");
        Log.d("detail col",minCol + "");
        int simplememoCol = workCoursor.getColumnIndex("simplememo");
        Log.d("detail col",simplememoCol + "");
        int resetCol = workCoursor.getColumnIndex("resetNum");
        Log.d("detail col",resetCol + "");


        workCoursor.moveToFirst();
        String time = workCoursor.getString(yearCol) + " / " + workCoursor.getString(monthCol) + " / "
        + " / " + workCoursor.getString(dayCol) + " / " + workCoursor.getString(hourCol)
                + " / " + workCoursor.getString(minCol);

        Log.d("timecol",time);

       setwork.setText("\n");
       setwork.append(time + "\n" + workCoursor.getString(simplememoCol));

        Log.d("compare",workCoursor.getString(resetCol).compareTo("NO") + "");

        if(workCoursor.getString(resetCol).compareTo("YES") == 0)
        {
            Log.d("reset 확인",workCoursor.getString(resetCol));
            DatabaseHelper.resetWork(workCoursor.getString(numberCol));
        }

        workCoursor.close();
    }

    public void println(String msg) {
        Log.d(TAG, msg);
    }

    public void onModifibuttonClicked(View v)
    {
        //수정 버튼을 눌렀을 경우 현재 있는 정보를 다른 인텐트로 넘겨 준다.
        Intent intent = new Intent(getBaseContext(),CowDetailInput.class);

        intent.putExtra("location",locationMsg.getText().toString());
        intent.putExtra("number",numberMsg.getText().toString());
        intent.putExtra("sex",sexMsg.getText().toString());
        intent.putExtra("birthday",birthdayMsg.getText().toString());
        intent.putExtra("detail",txtMsg1.getText().toString());
        intent.putExtra("memo",txtMsg2.getText().toString());

        Log.d("input intent start"," input start");
        startActivityForResult(intent, REQUEST_CODE_DETAIL);
    }

    public void onNumberClicked(View v)
    {
        //소의 번호를 눌럿을 경우 이의 혈통을 보여준다.
        Log.d("onNumberClicked : ",numberMsg.getText().toString());
        Intent intent = new Intent(getBaseContext(),FindFamily.class);
        intent.putExtra("cownum",numberMsg.getText().toString());
        startActivityForResult(intent, REQUEST_CODE_FAMILY);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Log.d("Return DB changed / ",requestCode + "");

        if (requestCode == REQUEST_CODE_DETAIL) {

            if (resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                Log.d(null,"onActivityResult()");
                printString(bundle);
            }
        }

        if(requestCode == REQUEST_CODE_CALENDAR)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle bundle = intent.getExtras();
                Log.d(null,"REQUEST_CODE_CALENDAR()");
                String dat_str = bundle.getString("returnday");

                SetDateDialog = new WorkSetDialog(CowDetailView.this,dat_str);
                SetDateDialog.setTitle("시간을 정해주세요");
                SetDateDialog.show();

                SetDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                    public void onDismiss(DialogInterface dialog) {
                    try{
                        String DaySplit[] = SetDateDialog.retunContent().split("//");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set( Integer.valueOf(DaySplit[0]), Integer.valueOf(DaySplit[1]) - 1,
                                Integer.valueOf(DaySplit[2]),  Integer.valueOf(DaySplit[3]),  Integer.valueOf(DaySplit[4]));

                        Log.d("Alram Set",Integer.valueOf(DaySplit[0]) + " / " + (Integer.valueOf(DaySplit[1]) - 1) + " / " +
                                Integer.valueOf(DaySplit[2]) + " / "  + Integer.valueOf(DaySplit[3])+ " / " +  Integer.valueOf(DaySplit[4]));

                        setAlarm(CowDetailView.this, calendar,DaySplit);
                        Log.d(null,"알람 설정 완료");
                        setwork.setText(SetDateDialog.retunContent());
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(CowDetailView.this,"취소하셧습니다.",Toast.LENGTH_LONG).show();
                        //SetDateDialog.show();
                    }

                    }
                });

            }
        }
    }

    public void showCancelbtn()
    {
        //일정 삭제 버튼을 보여줄지 안보여줄지를 결정하는 함수이다.
        Log.d(null,"취소 버튼 보여주기");
        Cursor cursor = DatabaseHelper.SearchData(DatabaseHelper.workname,numberMsg.getText().toString());

        int yearocol = cursor.getColumnIndex("year");
        Log.d(null,yearocol+"");
        cursor.moveToFirst();
        String year_str = cursor.getString(yearocol);
        Log.d(null,year_str);

        if(year_str.compareTo("--")==0)
        {
            cancelbtn.setVisibility(View.GONE);
        }
        else
            cancelbtn.setVisibility(View.VISIBLE);

        cursor.close();
    }

    public void onAlramCancel(View v)
    {
        DatabaseHelper.openDatabase(DatabaseHelper.workname);
        cancelAlram();
        workinput();
        cancelbtn.setVisibility(View.GONE);
        DatabaseHelper.closeDatabase();
    }
    @Override
    public void onBackPressed()
    {
        DatabaseHelper.closeDatabase();
        Log.d(null,"BackButtonPressed");
        setResult(RESULT_OK);
        finish();
    }


}
