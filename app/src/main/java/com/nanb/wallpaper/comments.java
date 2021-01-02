package com.nanb.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class comments extends AppCompatActivity {

    String filename,fileid;
    ImageButton backbutton;
    CircleImageView send;
    ImageView pd;
    EditText commentedittext;
    RecyclerView commentrecycler;
    FirebaseAuth mAuth;
    DatabaseReference rootdata;
    TextView header;
    ArrayList<commentmodelclass> commentdata;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        filename = getIntent().getStringExtra("filename").toString();
        //Toast.makeText(getApplicationContext(),filename,Toast.LENGTH_SHORT).show();
        fileid = getIntent().getStringExtra("fileid").toString();
        mAuth = FirebaseAuth.getInstance();
        backbutton = findViewById(R.id.backbutton);
        pd = findViewById(R.id.dp);
        send = findViewById(R.id.send);
        header = findViewById(R.id.name);
        commentedittext = findViewById(R.id.comment);
        commentrecycler = findViewById(R.id.commentdisplay);
        commentdata = new ArrayList<>();
        rootdata = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        header.setText(filename);
        layoutManager = new LinearLayoutManager(this);
        commentrecycler.setLayoutManager(layoutManager);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setdp(pd);
        getcommentdata();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commenttext = commentedittext.getText().toString();
                sendhttprequesttoaddcomment(commenttext);
            }
        });

    }

    private void getcommentdata() {
        String url = "http://192.168.225.213:8080/NANB_wallpaper/getcomment.php?id="+fileid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                commentdata.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        commentmodelclass commentmodelclass = new commentmodelclass(jsonObject.getString("userid"),jsonObject.getString("comment"));
                        commentdata.add(commentmodelclass);
                    }
                    adapter = new commentRecyclerAdapter(getApplicationContext(),commentdata);
                    commentrecycler.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void sendhttprequesttoaddcomment(String commenttext) {
        commentbackgroundhelper commentbackgroundhelper = new commentbackgroundhelper(comments.this);
        commentbackgroundhelper.execute(commenttext,fileid,filename,mAuth.getUid());
        commentedittext.setText("");
    }

    private void setdp(ImageView pd) {

        //Toast.makeText(getApplicationContext(),mAuth.getUid(),Toast.LENGTH_SHORT).show();
        rootdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("image")){
                    String userdp = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(userdp).into(pd);
                }else{
                    pd.setImageResource(R.drawable.profiledp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
