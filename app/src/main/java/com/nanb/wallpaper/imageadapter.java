package com.nanb.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class imageadapter extends BaseAdapter {
    Context mcontext;
    ArrayList<itemclass> imageurl;

    public imageadapter(Context mcontext, ArrayList<itemclass> imageurl) {
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
            convertView=layoutInflater.inflate(R.layout.grid_item,null,true);
        }
        //sliderclass listdata= (sliderclass) getItem(position);
        ImageView img=(ImageView)convertView.findViewById(R.id.imageView);
        TextView txt = (TextView)convertView.findViewById(R.id.name);
        itemclass itemclass = imageurl.get(position);
        txt.setVisibility(View.VISIBLE);
        txt.setText(itemclass.getDisplayName());
        Picasso.get().load(itemclass.getDownloadurl()).into(img);

        return convertView;
    }
}
