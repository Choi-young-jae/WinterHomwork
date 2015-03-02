package com.example.user.cowaplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2015-03-02.
 */
public class ShowCalendar extends FragmentActivity {
    private CaldroidFragment dialogCaldroidFragment; //달력을 위한 오픈 소스 사용
    private CaldroidFragment caldroidFragment;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy//MM//dd//");
    Bundle savebundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savebundle = savedInstanceState;

        ViewCalendar();
    }

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

    public void ViewCalendar()
    {
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
            }

            //달력의 날짜를 길게 누를 경우 날짜 정보를 String으로 전달한다.
            @Override
            public void onLongClickDate(Date date, View view) {

                String dat_str = formatter.format(date);
                Log.d("longClick split ", dat_str);
                dialogCaldroidFragment.dismiss();

                Intent resultIntent = new Intent();
                Bundle daybundle = new Bundle();
                daybundle.putString("returnday",dat_str);
                resultIntent.putExtras(daybundle);

                setResult(RESULT_OK, resultIntent);
                finish();
            }

            /*@Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null)
                {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }*/

        };

        setCustomResourceForDates();

        // Setup caldroid to use as dialog
        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
        Log.d(dialogTag,"setup day");

        dialogCaldroidFragment = new CaldroidFragment();
        Log.d(dialogTag, "Caldroid Created");
        dialogCaldroidFragment.setCaldroidListener(listener);
        Log.d(dialogTag,"Listener Setting");

        // If activity is recovered from rotation

        try{
            if (savebundle != null) {
                Log.d(dialogTag,"not null");
                dialogCaldroidFragment.restoreDialogStatesFromKey(
                        getSupportFragmentManager(), savebundle,
                        "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                Bundle args = dialogCaldroidFragment.getArguments();
                if (args == null) {
                    Log.d(dialogTag,"arge null");
                    args = new Bundle();
                    dialogCaldroidFragment.setArguments(args);
                }
            } else {
                Log.d(dialogTag,"else");
                // Setup arguments
                Bundle bundle = new Bundle();
                // Setup dialogTitle
                dialogCaldroidFragment.setArguments(bundle);
            }

            dialogCaldroidFragment.show(getSupportFragmentManager(),dialogTag);
            Log.d(dialogTag,"done");
        }
        catch(Exception e)
        {
            Log.d("Error...",e.toString());
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
