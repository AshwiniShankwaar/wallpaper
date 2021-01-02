package com.nanb.wallpaper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
public class sliderAdapter extends PagerAdapter
{
    Context context;
    LayoutInflater layoutInflater;
    public String[] text = { "First", "second", "Third" };

    public sliderAdapter(Context paramContext)
    {
        this.context = paramContext;
    }

    public int[] slider_icon={
            //image icon
    };

    public String[] slider_heading = {
        //topic header
    };



    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slidelayout, container,false);
        TextView textView = view.findViewById(R.id.text);
        container.addView(view);
        return view;

    };

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
