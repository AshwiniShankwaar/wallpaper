package com.nanb.wallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class displaydataAccoudingtokeyword extends AppCompatActivity {

    private TextView name;
    private ImageButton back;
    private String keyword;
    private String url = "http://192.168.225.213:8080/NANB_wallpaper/getitembykeyword.php?keyword=";
    ArrayList<itemclass> data;
    private GridView items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaydata_accoudingtokeyword);

        name = findViewById(R.id.name);
        back = findViewById(R.id.backbutton);
        keyword = getIntent().getStringExtra("keyword");
        name.setText(keyword);
        items = findViewById(R.id.items);
        //Toast.makeText(getApplicationContext(),keyword,Toast.LENGTH_SHORT).show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        data = new ArrayList<>();
        String final_url = url+keyword;//+"Type=Wallpapers";
        sendrequestforthedata(final_url);
    }

    private void sendrequestforthedata(String final_url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, final_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject itemdata = jsonArray.getJSONObject(i);
                        itemclass modeldata = new itemclass(itemdata.getString("Type"),itemdata.getString("DisplayName"),itemdata.getString("downloadurl"));
                        data.add(modeldata);
                        //Toast.makeText(getApplicationContext(),modeldata.getType()+modeldata.getDisplayName()+modeldata.getDownloadurl(),Toast.LENGTH_SHORT).show();
                    }
                    items.setAdapter(new displaydataclass(getApplicationContext(),data));
                    //Toast.makeText(getApplicationContext(),String.valueOf(data.size()),Toast.LENGTH_SHORT).show();
                    items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(getApplicationContext(),"pass",Toast.LENGTH_SHORT).show();
                            itemclass itemclass = data.get(position);
                            Intent intent = new Intent(getApplicationContext(),itemdisplay.class);
                            intent.putExtra("imageurl",itemclass.getDownloadurl());
                            //intent.putExtra("Type",Datarequested);
                            startActivity(intent);
                        }
                    });
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
}
