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

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private CaldroidFragment dialogCaldroidFragment; //달력을 위한 오픈 소스 사용
    private CaldroidFragment caldroidFragment;
    WorkSetDialog SetDateDialog;
    Bundle findbundle;


    public static final int REQUEST_CODE_DETAIL = 1003;
    public static final int REQUEST_CODE_FAMILY = 1008;

    //달력 출력 관련 부분
    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -18);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 16);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.blue,
                    blueDate);
            caldroidFragment.setBackgroundResourceForDate(R.color.green,
                    greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cowdetail);


        DatabaseHelper.openDatabase(DatabaseHelper.dbname);

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy//MM//dd//");
        caldroidFragment = new CaldroidFragment();

        txtMsg1 = (TextView)findViewById(R.id.txtMsg1);
        txtMsg2 = (TextView)findViewById(R.id.txtMsg2);
        locationMsg = (TextView)findViewById(R.id.locationspace);
        numberMsg = (TextView)findViewById(R.id.numberspace);
        sexMsg = (TextView)findViewById(R.id.sexspace);
        birthdayMsg = (TextView)findViewById(R.id.birthdayspace);
        setwork = (TextView)findViewById(R.id.setWork);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        printString(bundle);

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            //달력의 날짜를 길게 누를 경우 날짜 정보를 String으로 전달한다.
            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();

                //String[] Token = formatter.format(date).split(" ");
                //String dat_str = Token[0] + "/" + Token[1] + "/" + Token[2] + "/";
                String dat_str = formatter.format(date);
                Log.d("longClick split ", dat_str);

                dialogCaldroidFragment.dismiss();

                SetDateDialog = new WorkSetDialog(CowDetailView.this,dat_str);
                SetDateDialog.setTitle("시간을 정해주세요");
                SetDateDialog.show();


                //다이얼로그에서 빠져나왔을 경우 호출되는 함수
                SetDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        String DaySplit[] = SetDateDialog.retunContent().split("//");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set( Integer.valueOf(DaySplit[0]), Integer.valueOf(DaySplit[1]) - 1,
                                Integer.valueOf(DaySplit[2]),  Integer.valueOf(DaySplit[3]),  Integer.valueOf(DaySplit[4]));

                        Log.d("Alram Set",Integer.valueOf(DaySplit[0]) + " / " + (Integer.valueOf(DaySplit[1]) - 1) + " / " +
                                Integer.valueOf(DaySplit[2]) + " / "  + Integer.valueOf(DaySplit[3])+ " / " +  Integer.valueOf(DaySplit[4]));

                        setAlarm(CowDetailView.this, calendar,DaySplit);
                        setwork.setText(SetDateDialog.retunContent());
                    }
                });

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null)
                {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };
        setCustomResourceForDates();


        final Bundle state = savedInstanceState;

        Button daysetbutton = (Button)findViewById(R.id.dayset_btn);

        //날짜 설정 버튼을 눌렀을 경우.
        daysetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup caldroid to use as dialog
                final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                Log.d(dialogTag,"setup day");

                dialogCaldroidFragment = new CaldroidFragment();
                Log.d(dialogTag,"Caldroid Created");
                dialogCaldroidFragment.setCaldroidListener(listener);
                Log.d(dialogTag,"Listener Setting");

                // If activity is recovered from rotation

                if (state != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getSupportFragmentManager(), state,
                            "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }
                dialogCaldroidFragment.show(getSupportFragmentManager(),
                        dialogTag);
            }
        });
    }
    // 알람 등록
    private void setAlarm(Context context, Calendar calendar,String daysplit[])
    {
        Log.i(TAG, "setAlarm()");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CowDetailView.this,
                AlarmReceive.class);
        Bundle bundle = new Bundle();
        bundle.putString("data1",numberMsg.getText().toString());
        bundle.putString("data2",sexMsg.getText().toString());
        bundle.putString("data3",birthdayMsg.getText().toString());
        bundle.putString("data0",locationMsg.getText().toString());
        bundle.putInt("alarmnum",ALARMCOUNT);

        Log.d("Alram input",numberMsg.getText().toString() + " " + sexMsg.getText().toString() + " " +birthdayMsg.getText().toString()
         + " " + locationMsg.getText().toString());

        DatabaseHelper.setWorkDB(daysplit,numberMsg.getText().toString());

        intent.putExtras(bundle);

        PendingIntent pIntent = PendingIntent.getBroadcast(context, ALARMCOUNT, intent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pIntent);
        ALARMCOUNT++;

    }

    public void printString(Bundle bundle)
    {
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
    }
    public void detailInput()
    {
        String aSQL = "select number, detail, memo"
                + " from " + DatabaseHelper.detailname
                + " where number = ?";


        String[] args = {numberMsg.getText().toString()};
        Log.d("detail number = ",numberMsg.getText().toString());

        Cursor outCoursor = DatabaseHelper.db.rawQuery(aSQL,args);

        outCoursor.moveToFirst();

        int detailCol = outCoursor.getColumnIndex("detail");
        Log.d("detail col",detailCol + "");
        int memoCol = outCoursor.getColumnIndex("memo");
        Log.d("memo col",memoCol + "");

        outCoursor.moveToFirst();
        String details = outCoursor.getString(detailCol);
        Log.d("detail String  ",details);
        String memos = outCoursor.getString(memoCol);
        Log.d("detail memos  ",memos);

        txtMsg1.setText("\n");
        txtMsg1.append(details);
        txtMsg2.setText(memos);
        outCoursor.close();
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

        Log.d(null,"Return DB changed");

        if (requestCode == REQUEST_CODE_DETAIL) {

            if (resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                Log.d(null,"onActivityResult()");

                printString(bundle);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

}
