package com.example.user.cowaplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2015-02-18.
 */
public class WorkSetDialog extends Dialog implements View.OnTouchListener{


    private EditText hour,min;
    private Button hourup,hourdown;
    private Button minup,mindown;
    private TextView yearText;
    private EditText Memo;
    String showyear;
    String year;
    Button ok_btn;
    Toast toast;

    public WorkSetDialog(Context context)
    {
        super(context);
    }
    public WorkSetDialog(Context context, String str_yearday)
    {
        super(context);
        String split[]=str_yearday.split("//");
        showyear = split[0]+"/"+split[1]+"/"+split[2];
        year = str_yearday;
    }
    public String retunContent()
    {
        Log.d(null,"메모 확인");
        if( Memo.getText().toString().compareTo("")==0)
        {
            Log.d(null,"메모가 비었음");
            Memo.setText("없음");
        }

        String return_str = year + hour.getText().toString() + "//" + min.getText().toString() + "//" + Memo.getText().toString();
        Log.d("retunContent",return_str);

        return return_str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worksetdialog);
        ok_btn = (Button)findViewById(R.id.Ok_btn);
        hour = (EditText) findViewById(R.id.hour);
        min = (EditText) findViewById(R.id.min);
        yearText = (TextView)findViewById(R.id.setupdate);
        Memo = (EditText)findViewById(R.id.memo);

        hourup = (Button)findViewById(R.id.hourplus);
        hourdown = (Button)findViewById(R.id.houtmius);

        minup = (Button)findViewById(R.id.minplus);
        mindown = (Button)findViewById(R.id.mintmius);

        hourup.setOnTouchListener(this);
        hourdown.setOnTouchListener(this);

        minup.setOnTouchListener(this);
        mindown.setOnTouchListener(this);
        yearText.setText(showyear);
        Memo.setText("");

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(v == hourup || v == hourdown)
        {
            Integer numhour = Integer.parseInt(hour.getText().toString());
            Log.d("hour plus minus ", numhour.toString());
            if(v == hourup)
            {
                numhour++;
                Log.d("hour plus ",numhour.toString());
            }
            else if(v == hourdown)
            {
                numhour--;
                Log.d("hour minus ",numhour.toString());
            }

            if(numhour>=24) {numhour = 0;}

            if(numhour<0) {numhour = 23;}

            hour.setText(numhour.toString());
        }
        else if(v == minup || v == mindown)
        {
            Integer nummin = Integer.parseInt(min.getText().toString());
            Log.d("min plus minus ", nummin.toString());
            if(v == minup)
            {
                nummin++;
                Log.d("min plus ",nummin.toString());
            }
            else if(v == mindown)
            {
                nummin--;
                Log.d("min minus ",nummin.toString());
            }

            if(nummin>=60) {nummin = 0;}

            if(nummin<0) {nummin = 59;}

            min.setText(nummin.toString());
        }

        return false;
    }

}
