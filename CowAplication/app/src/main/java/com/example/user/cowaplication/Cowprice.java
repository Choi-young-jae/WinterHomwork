package com.example.user.cowaplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2015-02-24.
 */
public class Cowprice extends Activity{

    public static final String TAG = "Parsing";
    private EditText edittext;
    private static Thread thread = null;
    private Source source;
    private ArrayList<String> array;
    private String inputStr = "http://www.ekapepia.com/user/priceStat/realTimeCowBody.do";
    TextView tabletext;
    int curtableid = R.id.table1;
    public static final int REQUEST_CODE_PRICEDIF = 1004;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cowprice);

        Log.d(TAG, " onCreate");
        Runnable task = new Runnable() {
            @Override
            public void run()
            {
                getData(inputStr);
            }
        };
        Log.d(null,"treadstart");
        thread = new Thread(task);
        thread.start();

        try{
            thread.join();
        }
        catch(Exception e)
        {
            Log.d(TAG,e.toString());
        }

        for(int i=0; i<array.size(); i++)
        {
            tabletext = (TextView)findViewById(curtableid); //해당하는 칸에 얻어온 숫자를 하나씩 기입한다.
            tabletext.setText(array.get(i));
            curtableid++;
        }
    }
    public ArrayList<String> getData(String strURL)
    {
        URL nURL;
        array = new ArrayList();
        try
        {
            nURL = new URL(strURL);
            source = new Source(nURL);
            Element table = (Element) source.getAllElements(HTMLElementName.TABLE).get(2); //사이트 에서 TABLE태그를 가진것중 세번째 것을 불러온다.
            int tr_count = table.getAllElements(HTMLElementName.TR).size(); //얻어온 테이블 에서 전체 TR의 갯수를 센다
            Element tr = null;
            ArrayList<String> hm = null;
            //사이트 소스 코드를 분석했을때 내가 필요한 tr은 세번째 부터 시작한다.
            for(int i=2; i<tr_count; i++)
            {
                tr = (Element) table.getAllElements(HTMLElementName.TR).get(i); //i번째 tr을 불러온다.
                int td_count = tr.getAllElements(HTMLElementName.TD).size(); //불러온 tr의 전체 td를 센다.
                hm = new ArrayList<String>();
                for(int j=0; j<td_count; j++)
                {
                    //td의 원소를 hm에 저장한다.
                    hm.add(((Element) tr.getAllElements(HTMLElementName.TD).get(j)).getContent().toString());
                }
                println(hm);
            }
        }
        catch(Exception e)
        {
            Toast viewtoast = Toast.makeText(this,"Exceptoin Error... " ,Toast.LENGTH_LONG);
            viewtoast.show();
        }
        return array;
    }

    public void onButtonPeridClicked(View v)
    {
        // 웹 파싱을 통해 정보를 얻어오는 새로운 인텐트를 생성
        Intent intent = new Intent(getBaseContext(), Cowprice_dif.class);
        startActivityForResult(intent, REQUEST_CODE_PRICEDIF);
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
    private void println(ArrayList<String> hm) {

        int nuumber = hm.size();
        String returnString = "";
        for(int i=1; i<nuumber; i++)
        {
            //얻어온 원소들을 하나씩 저장한다.
            array.add(hm.get(i));
            returnString = returnString + hm.get(i) + " / ";
        }
        Log.d(TAG, returnString);
    }
}
