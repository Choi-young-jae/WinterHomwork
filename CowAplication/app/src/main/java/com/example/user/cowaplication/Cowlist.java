package com.example.user.cowaplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 2015-02-24.
 */

public class Cowlist extends Activity{

    ArrayList<listdata> datalist;
    AbstractAdapter adapter;
    EditText name; //텍스트 버튼에서 입력한 이름
    EditText number; //텍스트 버튼에서 적은 번호
    ListView list; //소의 목록을 출력해줄 리스트
    boolean deleteitem = false;
    TextView hiddentext;

    public static final int REQUEST_CODE_ANOTHER = 1002;
    public static final int REQUEST_CODE_DETAIL = 1005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cowlist);

        datalist = new  ArrayList<listdata>();
        list = (ListView)findViewById(R.id.showcowlist);
        hiddentext = (TextView)findViewById(R.id.hiddenid);
        adapter = new AbstractAdapter(this);
    }

    public void Onbutton2click(View v)
    {
        //삭제버튼이 눌려졌을 경우 시각적으로 삭제모드라는것을 표기하여 준다.
        hiddentext.setVisibility(View.VISIBLE);
        deleteitem = true;
    }
    public void Onbutton1click(View v)
    {
        // 추가 버튼을 눌렀을 경우 소를 추가하는 화면을 보여줌.
        Intent intent = new Intent(getBaseContext(), CowAdd.class);
        // 액티비티를 띄워주도록 startActivityForResult() 메소드를 호출합니다.
        startActivityForResult(intent, REQUEST_CODE_ANOTHER);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_ANOTHER) {
            Toast toast = Toast.makeText(getBaseContext(), "onActivityResult() 메소드가 호출됨. 요청코드 : " + requestCode + ", 결과코드 : " + resultCode, Toast.LENGTH_LONG);
            toast.show();

            if (resultCode == RESULT_OK) {
                //OK버튼이 호출되었을 경우 전해받은 intent로 부터 데이터틑 받은뒤 이를 어댑터에 셋팅하여 출력해줌
                Log.d("onActivityResult"," Call OK button");

                String send_location = intent.getExtras().getString("location");
                String send_number = intent.getExtras().getString("number");
                String send_sex = intent.getExtras().getString("sex");
                String send_birthday = intent.getExtras().getString("birthday");

                adapter.addItem(new listdata(send_location, send_number, send_birthday, send_sex));
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == REQUEST_CODE_DETAIL)
        {
            Toast toast = Toast.makeText(getBaseContext(), "onActivityResult() 메소드가 호출됨. 요청코드 : " + requestCode + ", 결과코드 : " + resultCode, Toast.LENGTH_LONG);
            toast.show();
            Log.d(null,"detail return");
            adapter.deleteAllcontent();
        }
    }
}
