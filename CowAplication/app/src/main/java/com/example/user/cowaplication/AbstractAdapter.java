package com.example.user.cowaplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015-02-13.
 */
//리스트뷰 출력을 도와줄 어뎁터이다.

public class AbstractAdapter extends BaseAdapter{
    private Context mContext;

    private List<listdata> mItems = new ArrayList<listdata>();
    public AbstractAdapter(Context context) {
        mContext = context;
    }
    public int getCount() {
        return mItems.size();
    }
    public void addItem(listdata newdata)
    {
        mItems.add(newdata);
    }
    public void deleteAllcontent()
    {
        mItems = new ArrayList<>();
        notifyDataSetChanged();
    }
    public long getItemId(int position) {
        return position;
    }
    public Object getItem(int position) {
        return mItems.get(position);
    }
    public View getView(int positoin,View convertView ,ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layoutId = R.layout.listitem;
        View view = inflater.inflate(layoutId,null);
        /*LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listitem,null);*/
        TextView location = (TextView)view.findViewById(R.id.listlocation);
        TextView number = (TextView)view.findViewById(R.id.listnumber);
        TextView sex = (TextView)view.findViewById(R.id.listsex);
        TextView birthday = (TextView)view.findViewById(R.id.listbirthday);

        Log.d("getView",mItems.get(positoin).location + " , " + mItems.get(positoin).number + " "
        + mItems.get(positoin).sex + " " + mItems.get(positoin).birthday);
        
        location.setText(mItems.get(positoin).location);
        number.setText(mItems.get(positoin).number);
        sex.setText(mItems.get(positoin).sex);
        birthday.setText(mItems.get(positoin).birthday);

        return view;
    }
}
