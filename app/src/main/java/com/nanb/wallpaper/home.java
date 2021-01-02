package com.nanb.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
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
import com.nanb.wallpaper.fonts.fonts_home;
import com.nanb.wallpaper.profile.profile_home;
import com.nanb.wallpaper.ringtone.ringtone_Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class home extends AppCompatActivity {

    public RelativeLayout searchbar;
    public TextView searchbartext,header;
    private ImageSlider imageSlider;
    List<String> slider;
    FirebaseAuth mAuth;
    String currentuserid;
    ArrayList<String> categorytext;
    ArrayList<itemclass> dataitem;
    private GridView items;
    private ProgressDialog mProgressDialog;
    private static String Datarequested = "Treading";
    private ImageButton top,category,favorite,latest;
    private static final String URL_items = "http://192.168.225.213:8080/NANB_wallpaper/slider.php?type=slider";
    private static final String URL = "http://192.168.225.213:8080/NANB_wallpaper/items.php?Type=Wallpapers&&data=";
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkforconnection();
        searchbar = findViewById(R.id.searchbar);
        searchbartext = findViewById(R.id.searchbartext);

        mAuth = FirebaseAuth.getInstance();
        //checkcurrentuserexitornot();
        checkappfolderexitornot();
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bacgroundnav);
        bottomNavigationView.setSelectedItemId(R.id.Wallpaper);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Wallpaper:
                        startActivity(new Intent(getApplicationContext(),home.class));
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
        loadslider();
        imageSlider = findViewById(R.id.image_slider);

        clicklistenermethod();

        defaultdisplaydata();
    }

    private void checkappfolderexitornot() {
        File file = new File(Environment.getExternalStorageDirectory()+"/Wallpaper");
        if(!file.exists()){
            sendusertomainactivity();
        }else{
            Log.d("Folder","Folder exit");
        }
    }

    private void sendusertomainactivity() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void checkforconnection() {
        mProgressDialog = new ProgressDialog(home.this);
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("Checking connection");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait... ");
        mProgressDialog.show();
        final String[] connectionstatus = {""};
        String url = "http://192.168.225.213:8080/NANB_wallpaper/connectioncheck.php";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    mProgressDialog.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void checkcurrentuserexitornot() {
        if(mAuth.getCurrentUser()==null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

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
                Intent intent = new Intent(getApplicationContext(),sarchpage.class);
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
        String url = "http://192.168.225.213:8080/NANB_wallpaper/getCatagorydata.php?Type=Wallpapers";
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

                           itemclass itemclass = new itemclass(imagedata.getString("Type"),imagedata.getString("DisplayName"),imagedata.getString("downloadurl"));
                            //Toast.makeText(getApplicationContext(),itemclass.getType()+itemclass.getDisplayName()+itemclass.getDownloadurl(),Toast.LENGTH_SHORT).show();
                           dataitem.add(itemclass);
                        }
                        //Toast.makeText(getApplicationContext(),String.valueOf(dataitem.size()),Toast.LENGTH_SHORT).show();
                        items.setAdapter(new imageadapter(getApplicationContext(),dataitem));
                        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(getApplicationContext(),dataitem.get(position),Toast.LENGTH_SHORT).show();
                                itemclass itemclass = dataitem.get(position);
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
