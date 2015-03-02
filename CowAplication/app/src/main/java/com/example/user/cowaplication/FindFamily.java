package com.example.user.cowaplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.URL;
import java.util.ArrayList;


public class FindFamily extends Activity {

    public static final String TAG = "Parsing";
    private EditText edittext;
    private static Thread thread = null;
    private Source source;
    private ArrayList<String> array;
    TextView tabletext;
    public static final int REQUEST_CODE_PRICEDIF = 1004;
    LinearLayout addlayout;
    LinearLayout layoutfirst;
    TableLayout table1;
    ArrayList<TextView> viewArray;
    String inputStr;
    Toast findtoast;
    Boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.familydialog);
        tabletext = (TextView)findViewById(R.id.textView1);
        layoutfirst = (LinearLayout)findViewById(R.id.first);
        table1 = (TableLayout)findViewById(R.id.tablelayout);
        viewArray = new ArrayList<TextView>();
        findtoast = Toast.makeText(getApplicationContext(), "해당하는 검색결과가 없습니다.",
                Toast.LENGTH_SHORT);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String cownum = bundle.getString("cownum");

        inputStr = "http://www.limc.co.kr/mobile/sub2_2.asp?kpnnum=" + cownum;

        Log.d("Main", " onCreate");
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

            for(int i=0; i<array.size(); i++)
            {
                layoutfirst = (LinearLayout)findViewById(R.id.first+ i);
                String[] splitstr = array.get(i).split(" , ");

                Log.d("Split num",splitstr.length + "");

                for(int j=0; j<splitstr.length; j++)
                {
                    if(j%10==0)
                    {
                        Log.d(null,j + " new layout create!");
                        addlayout  =  new LinearLayout(FindFamily.this);
                        addlayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        addlayout.setOrientation(LinearLayout.HORIZONTAL);
                        layoutfirst.addView(addlayout);
                    }
                    setTexttotable(splitstr[j],addlayout);
                }
                Log.d(null,"add layoutend");
                tabletext.append(array.get(i) + "\n");
            }
        }
        catch(Exception e)
        {
            Log.d(TAG,e.toString());
        }
    }
    public void setTexttotable(String str,LinearLayout addlayout)
    {
        TextView veiw1 = new TextView(FindFamily.this);
        LinearLayout.LayoutParams editlinear =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editlinear.setMargins(1,0,0,1);
        veiw1.setLayoutParams(editlinear);
        veiw1.setBackgroundColor(Color.parseColor("#ffc8fbf3"));
        veiw1.setText(str);
        veiw1.setPadding(3,3,3,3);
        addlayout.addView(veiw1);
        viewArray.add(veiw1);
    }

    public ArrayList<String> getData(String strURL)
    {
        URL nURL;
        array = new ArrayList();
        try
        {
            nURL = new URL(strURL);
            source = new Source(nURL);
            Element table = (Element) source.getAllElements(HTMLElementName.TABLE).get(0); //사이트 에서 TABLE태그를 가진것중 세번째 것을 불러온다.
            int tr_count = table.getAllElements(HTMLElementName.TR).size(); //얻어온 테이블 에서 전체 TR의 갯수를 센다
            Element tr = null;
            ArrayList<String> hm = null;
            //사이트 소스 코드를 분석했을때 내가 필요한 tr은 두번째 부터 시작한다.
            for(int i=1; i<tr_count; i++)
            {
                tr = (Element) table.getAllElements(HTMLElementName.TR).get(i); //i번째 tr을 불러온다.
                int td_count = tr.getAllElements(HTMLElementName.TD).size(); //불러온 tr의 전체 td를 센다.
                hm = new ArrayList<String>();
                for(int j=0; j<td_count; j++)
                {
                    //td의 원소를 hm에 저장한다.
                    hm.add(((Element) tr.getAllElements(HTMLElementName.TD).get(j)).getContent().toString());
                }
                array.add(println(hm));
            }
        }
        catch(Exception e)
        {
            findtoast.show();
            finish();
        }
        return array;
    }
    private String println(ArrayList<String> hm) {

        int nuumber = hm.size();
        String returnString = "";
        Log.d("hmnumber : ",nuumber+"");
        String returnString1 = hm.get(0).substring(22,hm.get(0).length()-7);
        for(int i=0; i<nuumber; i++)
        {
            //얻어온 원소들을 하나씩 저장한다.
            //array.add(hm.get(i));
            returnString = returnString + hm.get(i);
        }
        Log.d(TAG, returnString1);
        return returnString1;
    }

}
