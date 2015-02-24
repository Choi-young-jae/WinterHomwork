package com.example.user.cowaplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        //DatabaseHelper클래스에서 DB를 열고 테이블을 만드는 과정이다.
        try {
            DatabaseHelper.openDatabase(DatabaseHelper.dbname); //dbopen
            DatabaseHelper.createCowListTable(); //createTable
            DatabaseHelper.createDetailTable(); //createDetail
            showAllcontent(); //show content in table
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast toast2 = Toast.makeText(this,"database is not created.",Toast.LENGTH_LONG);
            toast2.show();
        }

        //리스트에 있는 아이템을 클릭 했을 경우 발생되는 이벤트 이다.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                String str_1 = ((TextView)v.findViewById(R.id.listlocation)).getText().toString();
                String str_2 = ((TextView)v.findViewById(R.id.listnumber)).getText().toString();
                String str_3 = ((TextView)v.findViewById(R.id.listbirthday)).getText().toString();
                String str_4 = ((TextView)v.findViewById(R.id.listsex)).getText().toString();

                Log.d("tab! ",deleteitem + "");

                if(deleteitem)
                {
                    Log.d("Delete mode! ","delete now");
                    listdata deletedata = new listdata(str_1,str_2,str_3,str_4);
                    DatabaseHelper.deleteData(deletedata);
                    adapter.deleteAllcontent();
                    showAllcontent();
                    hiddentext.setVisibility(View.INVISIBLE);
                    deleteitem = false;
                }
                else
                {
                    Log.d("Detail mode! ","detail show");
                    Bundle bundle = new Bundle();
                    bundle.putString("data0",str_1);
                    bundle.putString("data1",str_2);
                    bundle.putString("data2",str_3);
                    bundle.putString("data3",str_4);

                    Log.d("SetOnItemClickListenr", str_1 + " / "+ str_2);
                    Intent intnet = new Intent(getApplicationContext(), CowDetailView.class);
                    intnet.putExtras(bundle);
                    startActivityForResult(intnet,REQUEST_CODE_DETAIL);
                }
            }
        });
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
                String send_location = intent.getExtras().getString("location");
                String send_number = intent.getExtras().getString("number");
                String send_sex = intent.getExtras().getString("sex");
                String send_birthday = intent.getExtras().getString("birthday");
                DatabaseHelper.insertData(new listdata(send_location,send_number,send_birthday,send_sex));
                adapter.addItem(new listdata(send_location, send_number, send_birthday, send_sex));

                adapter.notifyDataSetChanged(); //adapter의 내용이 변한다면 이를 적용시켜준다.
            }
        }
        else if(requestCode == REQUEST_CODE_DETAIL)
        {
            Toast toast = Toast.makeText(getBaseContext(), "onActivityResult() 메소드가 호출됨. 요청코드 : " + requestCode + ", 결과코드 : " + resultCode, Toast.LENGTH_LONG);
            toast.show();
            Log.d(null,"detail return");
            adapter.deleteAllcontent();
            showAllcontent();
        }
    }

    //DB의 내용을 전부 출력시켜주는 함수이다.
    public void showAllcontent()
    {
        Log.d("SelectName" , "name");
        String aSQL = "select location, number,sex,birthday  from " + DatabaseHelper.tablename;
        Cursor cursor = DatabaseHelper.db.query(DatabaseHelper.tablename,null,null,null,null,null,null);
        int db_count = cursor.getCount();
        Toast toast1 = Toast.makeText(this, "database is created. have " + db_count, Toast.LENGTH_LONG);
        toast1.show();
        cursor.moveToFirst();
        for(int i=0; i<db_count; i++)
        {
            adapter.addItem(new listdata(cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4)));
            list.setAdapter(adapter);
            cursor.moveToNext();
        }
    }
}
