package com.nanb.wallpaper.fonts;

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

import com.nanb.wallpaper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class fontssimmiliarAdapter extends RecyclerView.Adapter<simmiliarfontviewholder> {
    Context mcontext;
    ArrayList<fontsmodelclass> data;

    public fontssimmiliarAdapter(Context mcontext, ArrayList<fontsmodelclass> data) {
        this.mcontext = mcontext;
        this.data = data;
    }

    @NonNull
    @Override
    public simmiliarfontviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        simmiliarfontviewholder simmiliarfontviewholder = new simmiliarfontviewholder(v);
        return simmiliarfontviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull simmiliarfontviewholder holder, int position) {
        fontsmodelclass fontsmodelclass = data.get(position);
        //Toast.makeText(mcontext,fontsmodelclass.getBackgroundurl(),Toast.LENGTH_LONG).show();
        Picasso.get().load(fontsmodelclass.getBackgroundurl()).into(holder.imageView);
        holder.textView.setText(fontsmodelclass.getDisplayName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,fontfiledetail.class);
                intent.putExtra("downloadurl",fontsmodelclass.getDownloadurl());
                intent.putExtra("backgroundurl",fontsmodelclass.getBackgroundurl());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class simmiliarfontviewholder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView textView;

    public simmiliarfontviewholder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.name);
    }
}