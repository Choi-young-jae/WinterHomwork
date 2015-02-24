package com.example.user.cowaplication;

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
    TextView txtMsg1; //상세 설명을 저장할 공간이다.
    TextView txtMsg2; //메모를 기록할 공간이다.
    TextView numberMsg; //list로 부터 받아온 번호
    TextView locationMsg; //list로 부터 받아온 위치
    TextView sexMsg; //list로 부터 받아온 성별
    TextView birthdayMsg; //list로 부터 받아온 출생일
    TextView setwork; //일정을 등록할 부분이다.
    private CaldroidFragment dialogCaldroidFragment; //달력을 위한 오픈 소스 사용
    private CaldroidFragment caldroidFragment;
    public static final int REQUEST_CODE_DETAIL = 1003;

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

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMM dd");
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

                String[] Token = formatter.format(date).split(" ");
                String dat_str = Token[0] + "년 " + Token[1] + " " + Token[2] + "일";
                Log.d("longClick split ", Token[0] + " " + Token[1] + " / " + Token[2]);

                dialogCaldroidFragment.dismiss();

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
    }


    public void detailInput()
    {
        String aSQL = "select number, detail, memo"
                + " from " + DatabaseHelper.detailname
                + " where number = ?";


        String[] args = {numberMsg.getText().toString()};
        Log.d("detail number = ",numberMsg.getText().toString());

        Cursor outCoursor = DatabaseHelper.db.rawQuery(aSQL,args);

        int detailCol = outCoursor.getColumnIndex("detail");
        Log.d("detail col",detailCol + "");
        int memoCol = outCoursor.getColumnIndex("memo");
        Log.d("memo col",memoCol + "");
        outCoursor.moveToNext();
        String details = outCoursor.getString(detailCol);
        Log.d("detail String  ",details);
        String memos = outCoursor.getString(memoCol);
        Log.d("detail memos  ",memos);

        txtMsg1.setText("\n");
        txtMsg1.append(details);
        txtMsg2.setText(memos);
        outCoursor.close();

    }

    public void println(String msg) {
        Log.d(TAG, msg);
    }

    public void onModifibuttonClicked(View v)
    {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Log.d(null,"Return DB changed");

        if (requestCode == REQUEST_CODE_DETAIL) {

            if (resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
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
