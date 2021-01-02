package com.nanb.wallpaper.fonts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanb.wallpaper.R;
import com.nanb.wallpaper.itemclass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class fontsimageadapter extends BaseAdapter {
    Context mcontext;
    ArrayList<fontsmodelclass> imageurl;

    public fontsimageadapter(Context mcontext, ArrayList<fontsmodelclass> imageurl) {
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
        fontsmodelclass fontsmodelclass = imageurl.get(position);
        txt.setVisibility(View.VISIBLE);
        txt.setText(fontsmodelclass.getDisplayName());
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.get().load(fontsmodelclass.getBackgroundurl()).into(img);

        return convertView;
    }
}
