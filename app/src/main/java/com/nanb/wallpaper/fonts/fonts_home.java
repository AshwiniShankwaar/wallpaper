package com.nanb.wallpaper.fonts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.nanb.wallpaper.R;
import com.nanb.wallpaper.categorytextdapter;
import com.nanb.wallpaper.home;
import com.nanb.wallpaper.imageadapter;
import com.nanb.wallpaper.itemclass;
import com.nanb.wallpaper.itemdisplay;
import com.nanb.wallpaper.profile.profile_home;
import com.nanb.wallpaper.ringtone.ringtone_Home;
import com.nanb.wallpaper.sarchpage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class fonts_home extends AppCompatActivity {
    public RelativeLayout searchbar;
    public TextView searchbartext,header;
    private ImageSlider imageSlider;
    List<String> slider;
    FirebaseAuth mAuth;
    String currentuserid;
    ArrayList<String> categorytext;
    ArrayList<fontsmodelclass> dataitem;
    private GridView items;
    private ProgressDialog mProgressDialog;
    private static String Datarequested = "Treading";
    private ImageButton top,category,favorite,latest;
    private static final String URL_items = "http://192.168.225.213:8080/NANB_wallpaper/slider.php?type=slider";
    private static final String URL = "http://192.168.225.213:8080/NANB_wallpaper/items.php?Type=Fonts&&data=";
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bacgroundnav);
        bottomNavigationView.setSelectedItemId(R.id.fonts);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Wallpaper:
                        startActivity(new Intent(getApplicationContext(), home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ringtone:
                        startActivity(new Intent(getApplicationContext(), ringtone_Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.fonts:
                        startActivity(new Intent(getApplicationContext(), fonts_home.class));
                        overridePendingTransition(0,0);
                        return true;
                    /**case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile_home.class));
                        overridePendingTransition(0,0);
                        return true;
                     **/
                }
                return false;
            }
        });
        searchbar = findViewById(R.id.searchbar);
        searchbartext = findViewById(R.id.searchbartext);

        mAuth = FirebaseAuth.getInstance();
        //checkcurrentuserexitornot();
        //checkappfolderexitornot();
        //currentuserid = mAuth.getCurrentUser().getUid();
        header = findViewById(R.id.header);
        items = findViewById(R.id.items);
        top = findViewById(R.id.top);
        category = findViewById(R.id.Category);
        favorite = findViewById(R.id.fav);
        latest = findViewById(R.id.latest);
        slider = new ArrayList<String>();
        dataitem = new ArrayList<>();
        categorytext = new ArrayList<>();
        loadslider();
        imageSlider = findViewById(R.id.image_slider);

        clicklistenermethod();

        defaultdisplaydata();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void defaultdisplaydata() {
        Datarequested = "Treading";
        header.setText(Datarequested);
        final String URL_Data = URL + Datarequested;
        //Toast.makeText(getApplicationContext(),URL_Data,Toast.LENGTH_SHORT).show();
        loadDataMethod(URL_Data);
    }

    private void clicklistenermethod() {
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), sarchpage.class);
                startActivity(intent);
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datarequested = "Treading";
                dataitem.clear();
                header.setText(Datarequested);
                final String URL_Data = URL + Datarequested;
                //Toast.makeText(getApplicationContext(),URL_Data,Toast.LENGTH_SHORT).show();
                loadDataMethod(URL_Data);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header.setText("Category");
                //Toast.makeText(getApplicationContext(),"send to category page",Toast.LENGTH_SHORT).show();
                getcategorymethod();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datarequested = "Favorite";
                dataitem.clear();
                header.setText(Datarequested);
                final String URL_Data = URL + Datarequested+"&&userid="+currentuserid;
                //Toast.makeText(getApplicationContext(),URL_Data,Toast.LENGTH_SHORT).show();
                loadDataMethod(URL_Data);
            }
        });

        latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datarequested = "Latest";
                dataitem.clear();
                header.setText(Datarequested);
                final String URL_Data = URL + Datarequested;
                //Toast.makeText(getApplicationContext(),URL_Data,Toast.LENGTH_SHORT).show();
                loadDataMethod(URL_Data);
            }
        });
    }

    private void getcategorymethod() {
        String url = "http://192.168.225.213:8080/NANB_wallpaper/getCatagorydata.php?Type=Fonts";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    categorytext.clear();
                    for(int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(),jsonObject.getString("Tag"),Toast.LENGTH_SHORT).show();
                        categorytext.add(jsonObject.getString("Tag"));
                    }
                    items.setAdapter(new categorytextdapter(getApplicationContext(),categorytext));

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void loadDataMethod(String url_data) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data, new Response.Listener<String>() {
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
                            fontsmodelclass fontsmodelclass = new fontsmodelclass(imagedata.getString("Type"),imagedata.getString("DisplayName"),imagedata.getString("downloadurl"),imagedata.getString("backgroundurl"));
                            dataitem.add(fontsmodelclass);
                           // Toast.makeText(getApplicationContext(),imagedata.getString("backgroundurl"),Toast.LENGTH_SHORT).show();
                        }
                        items.setAdapter(new fontsimageadapter(fonts_home.this,dataitem));
                        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                fontsmodelclass fontsmodelclass = dataitem.get(position);
                                Intent intent = new Intent(getApplicationContext(),fontfiledetail.class);
                                intent.putExtra("downloadurl",fontsmodelclass.getDownloadurl());
                                intent.putExtra("backgroundurl",fontsmodelclass.getBackgroundurl());
                                startActivity(intent);
                            }
                        });

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


    private void slidermethod() {
        List<SlideModel> slidermodel = new ArrayList<>();

        //Toast.makeText(getApplicationContext(),String.valueOf(slider.size()),Toast.LENGTH_LONG).show();
        for(String sliderdata: slider){
            String imageurl = sliderdata.toString();
            //Toast.makeText(getApplicationContext(),imageurl,Toast.LENGTH_LONG).show();
            slidermodel.add(new SlideModel(imageurl));
            imageSlider.setImageList(slidermodel,false);
        }
        /** slidermodel.add( new SlideModel(R.drawable.slider1));
         slidermodel.add( new SlideModel(R.drawable.slider2));
         slidermodel.add( new SlideModel(R.drawable.slider3));
         slidermodel.add( new SlideModel(R.drawable.slider4));
         imageSlider.setImageList(slidermodel,false);**/
    }

    private void loadslider() {
        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_items, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                try {

                    JSONArray array = new JSONArray(response);
                    //Toast.makeText(getApplicationContext(),String.valueOf(array.length()),Toast.LENGTH_SHORT).show();
                    for(int i = 0; i<array.length();i++){
                        JSONObject sliderdata = array.getJSONObject(i);
                        slider.add(sliderdata.getString("downloadurl"));
                        //Toast.makeText(getApplicationContext(),String.valueOf(slider.size()),Toast.LENGTH_SHORT).show();
                    }
                    slidermethod();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
}
