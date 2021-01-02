package com.nanb.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class similaritemsAdapter extends RecyclerView.Adapter<similiarviewholder>{
    Context mcontext;
    ArrayList<itemclass> data;

    public similaritemsAdapter(Context mcontext, ArrayList<itemclass> data) {
        this.mcontext = mcontext;
        this.data = data;
    }


    @NonNull
    @Override
    public similiarviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        similiarviewholder similiarviewholder = new similiarviewholder(v);
        return similiarviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull similiarviewholder holder, int position) {
        itemclass itemclass = data.get(position);
        String downloadurl = itemclass.getDownloadurl();
        Picasso.get().load(downloadurl).into(holder.imageView);
        String Displayname = itemclass.getDisplayName();
        holder.textView.setText(Displayname);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,itemdisplay.class);
                intent.putExtra("imageurl",downloadurl);
                //intent.putExtra("Type",Datarequested);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class similiarviewholder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView textView;
    public similiarviewholder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.name);
    }
}