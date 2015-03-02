package com.example.user.cowaplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by user on 2015-02-15.
 */
public class CowDetailInput extends Activity {

    public static final String TAG = "CowDetailInput";
    EditText txtMsg1_input;
    EditText txtMsg2_input;
    EditText numberMsg_input;
    EditText locationMsg_input;
    EditText sexMsg_input;
    EditText birthdayMsg_input;
    Button modifyok_btn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cowdetail_input);

        txtMsg1_input = (EditText)findViewById(R.id.txtMsg1_input);
        txtMsg2_input = (EditText)findViewById(R.id.txtMsg2_input);
        locationMsg_input = (EditText)findViewById(R.id.locationspace_input);
        numberMsg_input = (EditText)findViewById(R.id.numberspace_input);
        sexMsg_input = (EditText)findViewById(R.id.sexspace_input);
        birthdayMsg_input = (EditText)findViewById(R.id.birthdayspace_input);
        modifyok_btn = (Button)findViewById(R.id.modify_ok);

        Log.d("input text", "input ");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String locationString = bundle.getString("location");
        String numberString = bundle.getString("number");
        String sexString = bundle.getString("sex");
        String birthdayString = bundle.getString("birthday");
        String detailString = bundle.getString("detail");
        String memoString = bundle.getString("memo");

        println(locationString + " // " + numberString + " // " + sexString + " // " + birthdayString);
        locationMsg_input.setText(locationString);
        numberMsg_input.setText(numberString);
        sexMsg_input.setText(sexString);
        birthdayMsg_input.setText(birthdayString);
        txtMsg1_input.setText(detailString);
        txtMsg2_input.setText(memoString);

    }

    public void println(String msg) {
        Log.d(TAG, msg);
    }

    public void ModifyOkButtonClicked(View v)
    {
        Log.d(null,"ModifyOk Start");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String locationString = bundle.getString("location");
        String numberString = bundle.getString("number");
        String sexString = bundle.getString("sex");
        String birthdayString = bundle.getString("birthday");
        String detailString = bundle.getString("detail");
        String memoString = bundle.getString("memo");



        DatabaseHelper.modifyTable(new listdata(locationString,numberString,birthdayString,sexString),
                new listdata(locationMsg_input.getText().toString(),numberMsg_input.getText().toString(),birthdayMsg_input.getText().toString(),sexMsg_input.getText().toString()));

        DatabaseHelper.modifyDetail(numberString,numberMsg_input.getText().toString(),txtMsg1_input.getText().toString(),txtMsg2_input.getText().toString());

        println("DB update Finish");

        Intent resultIntent = new Intent(getApplicationContext(), CowDetailView.class);
        Bundle inputbundle = new Bundle();
        inputbundle.putString("data0",locationMsg_input.getText().toString());
        inputbundle.putString("data1",numberMsg_input.getText().toString());
        inputbundle.putString("data2",sexMsg_input.getText().toString());
        inputbundle.putString("data3",birthdayMsg_input.getText().toString());

        resultIntent.putExtras(inputbundle);

        setResult(RESULT_OK, resultIntent);
        finish();

    }
}
