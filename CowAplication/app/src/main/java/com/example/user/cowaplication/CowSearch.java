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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 2015-02-24.
 */

public class CowSearch extends Activity{

    ArrayList<listdata> datalist;
    AbstractAdapter adapter;
    ListView list; //소의 목록을 출력해줄 리스트
    EditText searchText;
    Button searchButton;
    RadioGroup searchType;
    int dataType;

    public static final int REQUEST_CODE_DETAIL = 1005;
    public static final int REQUEST_CODE_CALENDAR = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cowsearch);

        datalist = new  ArrayList<listdata>();
        list = (ListView)findViewById(R.id.showcowlist);
        searchText = (EditText)findViewById(R.id.searchcontent);
        adapter = new AbstractAdapter(this);
        searchButton = (Button)findViewById(R.id.searchbutton);
        searchType = (RadioGroup)findViewById(R.id.typeRadio);

        //DatabaseHelper클래스에서 DB를 열고 테이블을 만드는 과정이다.

        //리스트에 있는 아이템을 클릭 했을 경우 발생되는 이벤트 이다.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                String str_1 = ((TextView)v.findViewById(R.id.listlocation)).getText().toString();
                String str_2 = ((TextView)v.findViewById(R.id.listnumber)).getText().toString();
                String str_3 = ((TextView)v.findViewById(R.id.listbirthday)).getText().toString();
                String str_4 = ((TextView)v.findViewById(R.id.listsex)).getText().toString();

                Log.d("Detail mode! ","detail show");
                Bundle bundle = new Bundle();
                bundle.putString("data0",str_1);
                bundle.putString("data1",str_2);
                bundle.putString("data2",str_3);
                bundle.putString("data3",str_4);

                Log.d("SetOnItemClickListenr", str_1 + " / "+ str_2);
                Intent intnet = new Intent(getApplicationContext(), CowDetailView.class);
                intnet.putExtras(bundle);
                DatabaseHelper.closeDatabase();
                startActivityForResult(intnet,REQUEST_CODE_DETAIL);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saerch = searchText.getText().toString();
                switch(searchType.getCheckedRadioButtonId())
                {
                    case R.id.locationRadio:
                        dataType = 0;
                        break;
                    case R.id.numberRadio:
                        dataType = 1;
                        break;
                    case R.id.sexRadio:
                        dataType = 2;
                        break;
                    case R.id.dateRadio:
                        dataType = 3;
                        break;
                }
                printSeachString(saerch,dataType);
            }
        });
    }

    public void printSeachString(String search,int dataType)
    {
        adapter.deleteAllcontent();

        Log.d(null,search + " / " + dataType);

        DatabaseHelper.openDatabase(DatabaseHelper.tablename);
        Cursor findCursor = DatabaseHelper.SearchData(DatabaseHelper.tablename,search,dataType);
        int locationCol = findCursor.getColumnIndex("location");
        int numberCol = findCursor.getColumnIndex("number");
        int sexCol = findCursor.getColumnIndex("sex");
        int dateCol = findCursor.getColumnIndex("birthday");

        int dataCount = findCursor.getCount();
        Log.d("SearchContent",dataCount+"");
        Toast.makeText(this,"검색 결과 : "+dataCount,Toast.LENGTH_LONG).show();

        findCursor.moveToFirst();
        for(int i=0; i<dataCount; i++)
        {
            adapter.addItem(new listdata(findCursor.getString(locationCol), findCursor.getString(numberCol),findCursor.getString(sexCol), findCursor.getString(dateCol)));
            list.setAdapter(adapter);
            findCursor.moveToNext();
        }
        DatabaseHelper.closeDatabase();
        findCursor.close();
    }
    public void onDateSearch(View v)
    {
        Intent intent = new Intent(getBaseContext(), ShowCalendar.class);
        startActivityForResult(intent, REQUEST_CODE_CALENDAR);

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

        if (requestCode == REQUEST_CODE_CALENDAR) {
            Toast toast = Toast.makeText(getBaseContext(), "onActivityResult() 메소드가 호출됨. 요청코드 : " + requestCode + ", 결과코드 : " + resultCode, Toast.LENGTH_LONG);
            toast.show();
            if (resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                Log.d(null,"REQUEST_CODE_CALENDAR()");
                String day_str = bundle.getString("returnday");
                String[] datesplit = day_str.split("//");
                searchText.setText(datesplit[0]+ " / " + datesplit[1] + " / " + datesplit[2]);
            }
        }
        else if(requestCode == REQUEST_CODE_DETAIL)
        {
            Toast toast = Toast.makeText(getBaseContext(), "onActivityResult() 메소드가 호출됨. 요청코드 : " + requestCode + ", 결과코드 : " + resultCode, Toast.LENGTH_LONG);
            toast.show();
            Log.d(null,"detail return");
        }
    }
}
