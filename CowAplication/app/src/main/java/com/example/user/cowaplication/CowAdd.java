package com.example.user.cowaplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView birthday;
    ShowCalendar simplecal;
    public static final int REQUEST_CODE_CALENDAR = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addinterface);

        locatoin = (EditText)  findViewById(R.id.location);
        number = (EditText) findViewById(R.id.cownumber);
        sex = (RadioGroup)findViewById(R.id.radioGroup);
        birthday = (TextView)findViewById(R.id.birthday);
        save_Btn = (Button) findViewById(R.id.save_btn);
        simplecal = new ShowCalendar();
    }
    public void onDataClicked(View v)
    {
        Intent intent = new Intent(getBaseContext(), ShowCalendar.class);
        startActivityForResult(intent, REQUEST_CODE_CALENDAR);

        Log.d(null,"return to ondate Clicked");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onSaveButtonClicked(View v)
    {
        DatabaseHelper.openDatabase(DatabaseHelper.tablename);
        Cursor searchcursor = DatabaseHelper.SearchData(DatabaseHelper.tablename,number.getText().toString());
        searchcursor.moveToFirst();
        try{
            //예외가 캐치 되는 경우는 데이터 베이스에 저장이 되지 않은 경우 이므로 예외일 경우 데이터 베이스에 저장한다.
            Log.d("Cursor in or not?",searchcursor.getString(1));
            Toast.makeText(this, "이미 저장되어있는 번호입니다.", Toast.LENGTH_LONG).show();
            Log.d("Cursor","있는 정보입니다.");
        }
        catch(IndexOutOfBoundsException e)
        {
            Log.d("Exception!",e.toString());
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
        finally {
            searchcursor.close();
            DatabaseHelper.closeDatabase();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Log.d(null,"Return DB changed");

        if (requestCode == REQUEST_CODE_CALENDAR) {

            if (resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                Log.d(null,"REQUEST_CODE_CALENDAR()");
                String day_str = bundle.getString("returnday");
                String[] datesplit = day_str.split("//");
                birthday.setText(datesplit[0]+ " / " + datesplit[1] + " / " + datesplit[2]);
            }
        }
    }
}
