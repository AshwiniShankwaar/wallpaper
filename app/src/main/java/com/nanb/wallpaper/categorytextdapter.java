package com.nanb.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class categorytextdapter extends BaseAdapter {
    Context mcontext;
    ArrayList<String> imageurl;

    public categorytextdapter(Context mcontext, ArrayList<String> imageurl) {
        this.mcontext = mcontext;
        this.imageurl = imageurl;
    }

    @Override
    public int getCount() {
        return imageurl.size();
    }

    @Override
    public Object getItem(int position) {
        return imageurl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)mcontext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.grit_category_items,null,true);
        }
        Button btn = (Button) convertView.findViewById(R.id.button);
        btn.setText(imageurl.get(position));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,displaydataAccoudingtokeyword.class);
                intent.putExtra("keyword",imageurl.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
        return convertView;
    }
}
