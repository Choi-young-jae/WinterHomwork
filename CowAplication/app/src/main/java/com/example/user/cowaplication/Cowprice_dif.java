package com.example.user.cowaplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
public class Cowprice_dif extends Activity{

    TextView textView;
    public static final String TAG = "ParsingTest";
    private String rssUrl;
    private EditText edittext;
    private static Thread thread = null;
    private Source source_dif;
    private ArrayList<String> array;
    TextView tabletext;
    int curtableid = R.id.tabledif1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cowprice_dif);
        edittext = (EditText)findViewById(R.id.edit_dif);
        edittext.setText("http://www.ekapepia.com/user/priceStat/periodCowAuctionPrice.do");
        Log.d(TAG, " onCreate");
        Button show_btn = (Button) findViewById(R.id.show_btn);
        show_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Runnable task = new Runnable() {
                    String inputStr = edittext.getText().toString();
                    @Override
                    public void run()
                    {
                        getData(inputStr);
                    }
                };
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
                    tabletext = (TextView)findViewById(curtableid);
                    tabletext.setText(array.get(i));
                    curtableid++;
                }
            }

        });
    }
    public ArrayList<String> getData(String strURL)
    {
        URL nURL;
        array = new ArrayList();
        try
        {
            Log.d(TAG,  " Start..");
            nURL = new URL(strURL);
            Log.d(TAG,  " URL update");
            source_dif = new Source(nURL);
            Log.d(TAG,  " Source Create");
            Element table_dif = (Element) source_dif.getAllElements(HTMLElementName.TABLE).get(0);
            Log.d(TAG,  " Get table");
            int tr_count_dif = table_dif.getAllElements(HTMLElementName.TR).size();
            Log.d(TAG,  " Get tr" + tr_count_dif);
            Element tr_dif = null;

            ArrayList<String> hm = null;
            for(int i=2; i<tr_count_dif; i++)
            {
                Log.d(TAG,  i + " tr loop");
                tr_dif = (Element) table_dif.getAllElements(HTMLElementName.TR).get(i);
                //Log.d(TAG,  " Get tr");
                int td_count_dif = tr_dif.getAllElements(HTMLElementName.TD).size();
                Log.d(TAG,  td_count_dif + " Get td");
                hm = new ArrayList<String>();
                for(int j=0; j<td_count_dif; j++)
                {
                    hm.add(((Element) tr_dif.getAllElements(HTMLElementName.TD).get(j)).getContent().toString());
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

        Log.d(null, "Println");
        int nuumber = hm.size();
        String returnString = "";
        for (int i = 0; i < nuumber; i++) {
            String addstr = "";
            if (hm.get(i).compareTo("") != 0) {
                String split[] = hm.get(i).split("<br/>");

                for (int j = 0; j < split.length; j++) {
                    addstr = addstr + split[j];
                }
            }
            array.add(addstr);
            Log.d("array size count", array.size() + "");
            returnString = returnString + addstr + " / ";
        }
    }
}
