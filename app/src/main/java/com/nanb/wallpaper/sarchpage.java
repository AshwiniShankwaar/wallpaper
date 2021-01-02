package com.nanb.wallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nanb.wallpaper.ringtone.ringtoneAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class sarchpage extends AppCompatActivity {

    private ImageButton back;
    private EditText searchbar;
    private ImageButton searchbutton;
    private RelativeLayout hotwords,searchitems,ringtone,fonts;
    private GridView hotwordsgrid,searchgrid,fontsgrid;
    private RecyclerView rintonegrid;
    private TextView searcheader,ringtoneheader,fontheader;
    ArrayList<itemclass> data,ringtonedata,fontsdata;
    ArrayList<String> keywords;
    ArrayList<itemclass> dataitem;
    private String searchurl = "http://192.168.225.213:8080/NANB_wallpaper/getitembykeyword.php?keyword=";
    private String hotwordsurl = "http://192.168.225.213:8080/NANB_wallpaper/gethotwords.php";
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sarchpage);
        back = findViewById(R.id.back);
        searchbar = findViewById(R.id.searchbar);
        searchbutton = findViewById(R.id.searchbutton);
        hotwords = findViewById(R.id.hotwords);
        searchitems = findViewById(R.id.searchdata);
        hotwordsgrid = findViewById(R.id.hotwordsitems);
        searchgrid =findViewById(R.id.searchitems);
        searcheader = findViewById(R.id.wallpaperheader);
        ringtone = findViewById(R.id.rintonesearch);
        fonts = findViewById(R.id.fontsearchdata);
        rintonegrid = findViewById(R.id.rintoneitems);
        fontsgrid = findViewById(R.id.fontsearchitems);
        ringtoneheader = findViewById(R.id.ringtoneheader);
        fontheader = findViewById(R.id.fontsheader);
        layoutManager = new LinearLayoutManager(this);
        rintonegrid.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        ringtonedata = new ArrayList<>();
        fontsdata = new ArrayList<>();
        keywords = new ArrayList<>();
        dataitem = new ArrayList<>();
        setlatestdata();
        setlatestringtonedata();
        gethotwords();
        getsearchdata();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setlatestringtonedata() {
        String finalurl = "http://192.168.225.213:8080/NANB_wallpaper/items.php?Type=Ringtones&&data=Latest" ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                if(response.isEmpty()){
                    Toast.makeText(getApplicationContext(),"empty",Toast.LENGTH_SHORT).show();
                }else{
                    dataitem.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject imagedata = jsonArray.getJSONObject(i);

                            itemclass itemclass = new itemclass(imagedata.getString("Type"),imagedata.getString("DisplayName"),imagedata.getString("downloadurl"));
                            //Toast.makeText(getApplicationContext(),itemclass.getType()+itemclass.getDisplayName()+itemclass.getDownloadurl(),Toast.LENGTH_SHORT).show();
                            dataitem.add(itemclass);
                        }
                        //Toast.makeText(getApplicationContext(),String.valueOf(dataitem.size()),Toast.LENGTH_SHORT).show();
                        adapter = new ringtoneAdapter(sarchpage.this,dataitem);
                        rintonegrid.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void setlatestdata() {

        String finalurl = "http://192.168.225.213:8080/NANB_wallpaper/items.php?Type=Wallpapers&&data=Latest" ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //Toast.makeText(getApplicationContext(),String.valueOf(jsonArray.length()),Toast.LENGTH_SHORT).show();
                    if(jsonArray.length() == 0){
                        searchitems.setVisibility(View.GONE);
                    }else{
                        searchitems.setVisibility(View.VISIBLE);
                        data.clear();
                        for(int i= 0; i<jsonArray.length();i++){
                            JSONObject itemdata = jsonArray.getJSONObject(i);

                            itemclass modeldata = new itemclass(itemdata.getString("Type"),itemdata.getString("DisplayName"),itemdata.getString("downloadurl"));
                            data.add(modeldata);

                        }
                        searchgrid.setAdapter(new displaydataclass(getApplicationContext(),data));
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;
                        RelativeLayout.LayoutParams paramsss = new RelativeLayout.LayoutParams(width,height);
                        paramsss.addRule(RelativeLayout.BELOW,R.id.wallpaperheader);
                        searchgrid.setLayoutParams(paramsss);
                        searchgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                itemclass itemclass = data.get(position);
                                Intent intent = new Intent(getApplicationContext(),itemdisplay.class);
                                intent.putExtra("imageurl",itemclass.getDownloadurl());
                                //intent.putExtra("Type",Datarequested);
                                startActivity(intent);
                            }
                        });
                    }

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

    private void gethotwords() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, hotwordsurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    keywords.clear();
                    for(int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(),jsonObject.getString("keyword"),Toast.LENGTH_SHORT).show();
                        keywords.add(jsonObject.getString("keyword"));
                    }
                    hotwordsgrid.setAdapter(new categorytextdapter(getApplicationContext(),keywords));

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

    private void getsearchdata() {
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searcheader.setText("Search Result");
                String keyword = searchbar.getText().toString();
                String finalurl = searchurl+keyword;
                sendstringrequestforsearch(finalurl);
            }
        });
    }

    private void sendstringrequestforsearch(String finalurl) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //Toast.makeText(getApplicationContext(),String.valueOf(jsonArray.length()),Toast.LENGTH_SHORT).show();
                    if(jsonArray.length() == 0){
                        searchitems.setVisibility(View.GONE);
                        ringtone.setVisibility(View.GONE);
                        fonts.setVisibility(View.GONE);
                    }else{
                        searchitems.setVisibility(View.VISIBLE);
                        data.clear();
                        for(int i= 0; i<jsonArray.length();i++){
                            JSONObject itemdata = jsonArray.getJSONObject(i);

                            itemclass modeldata = new itemclass(itemdata.getString("Type"),itemdata.getString("DisplayName"),itemdata.getString("downloadurl"));

                            if(itemdata.getString("Type").equals("Wallpapers")){
                                data.add(modeldata);
                            }else if(itemdata.getString("Type").equals("Ringtones")){
                                ringtonedata.add(modeldata);
                            }else if(itemdata.getString("Type").equals("Fonts")){
                                fontsdata.add(modeldata);
                            }

                        }
                        if(data.size() == 0){
                            searchitems.setVisibility(View.GONE);
                        }else{
                            searchgrid.setAdapter(new displaydataclass(getApplicationContext(),data));
                            searchgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    itemclass itemclass = data.get(position);
                                    Intent intent = new Intent(getApplicationContext(),itemdisplay.class);
                                    intent.putExtra("imageurl",itemclass.getDownloadurl());
                                    //intent.putExtra("Type",Datarequested);
                                    startActivity(intent);
                                }
                            });
                        }
                        if(ringtonedata.size() == 0){
                            ringtone.setVisibility(View.GONE);
                        }else{
                            adapter = new ringtoneAdapter(sarchpage.this,ringtonedata);
                            rintonegrid.setAdapter(adapter);
                        }
                        if(fontsdata.size() == 0){
                            fonts.setVisibility(View.GONE);
                        }else{

                        }
                    }

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
