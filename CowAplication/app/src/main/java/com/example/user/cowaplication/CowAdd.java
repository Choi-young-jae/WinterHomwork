package com.example.user.cowaplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by user on 2015-02-15.
 */
public class CowAdd extends Activity {

    Button save_Btn;
    Button cancel_btn;
    EditText locatoin;
    EditText number;
    RadioGroup sex;
    String convert_sex;
    EditText birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addinterface);

        locatoin = (EditText)  findViewById(R.id.location);
        number = (EditText) findViewById(R.id.cownumber);
        sex = (RadioGroup)findViewById(R.id.radioGroup);
        birthday = (EditText)findViewById(R.id.birthday);
        save_Btn = (Button) findViewById(R.id.save_btn);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onSaveButtonClicked(View v)
    {
        Intent resultIntent = new Intent();

        if(sex.getCheckedRadioButtonId() == R.id.radioButton)
        {
            convert_sex = "암";
        }
        else
        {
            convert_sex = "수";
        }

        Log.d("Add intent save","save start");
        /*Log.d("Add intent save",locatoin.getText().toString() + " / " +number.getText().toString() +
                " / " + convert_sex + " / " +  birthday.getText().toString());*/

        resultIntent.putExtra("location", locatoin.getText().toString());
        Log.d("Add location save",locatoin.getText().toString());

        resultIntent.putExtra("number", number.getText().toString());
        Log.d("Add number save",number.getText().toString());


        resultIntent.putExtra("sex",convert_sex);
        Log.d("Add sex save",convert_sex);

        resultIntent.putExtra("birthday", birthday.getText().toString());
        Log.d("Add birhday save",birthday.getText().toString());
        //resultIntent.putStringArrayListExtra("data",)

        // 응답을 전달하고 이 액티비티를 종료합니다.
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}