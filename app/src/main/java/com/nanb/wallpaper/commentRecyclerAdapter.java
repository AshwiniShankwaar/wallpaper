package com.nanb.wallpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentRecyclerAdapter extends RecyclerView.Adapter<viewholder> {
    Context mcontext;
    ArrayList<commentmodelclass> data;

    public commentRecyclerAdapter(Context mcontext, ArrayList<commentmodelclass> data) {
        this.mcontext = mcontext;
        this.data = data;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentview, parent, false);
        viewholder viewholder = new viewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        //Toast.makeText(mcontext,String.valueOf(data.size()),Toast.LENGTH_SHORT).show();
        commentmodelclass commentmodelclass = data.get(position);
        String userid = commentmodelclass.getUser_id();
        String commenttext = commentmodelclass.getComment();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("image")){
                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(holder.profiledp);
                }else{
                    holder.profiledp.setImageResource(R.drawable.profiledp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.textView.setText(commenttext);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
class viewholder extends RecyclerView.ViewHolder{
    CircleImageView profiledp;
    TextView textView;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        profiledp = itemView.findViewById(R.id.userprofileimage);
        textView = itemView.findViewById(R.id.commenttextview);
    }
}