package com.nanb.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ViewPager slideviewpager;
    private LinearLayout dotlayout;
    private sliderAdapter sliderAdapter;
    private TextView[] dots;
    private Button start;
    private FirebaseAuth mAuth;
    static final int REQUEST_CODE = 786;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slideviewpager = findViewById(R.id.sliderviewpager);
        dotlayout = findViewById(R.id.dotlayout);
        start = findViewById(R.id.start);
        mAuth = FirebaseAuth.getInstance();

        sliderAdapter = new sliderAdapter(this);
        slideviewpager.setAdapter(sliderAdapter);

        adddotsidenticater(0);
        slideviewpager.addOnPageChangeListener(viewlistner);

        request_permission();
        checkappfolderexitornot();
    }

    private void checkappfolderexitornot() {
        File file = new File(Environment.getExternalStorageDirectory()+"/Wallpaper");
        if(!file.exists()){
            file.mkdir();
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
           // checkCurrentuseravailableornot();
        }else{
            //Intent intent = new Intent(getApplicationContext(),Login.class);
            //startActivity(intent);
            //checkCurrentuseravailableornot();
        }
    }

    private void checkCurrentuseravailableornot() {
        if(mAuth.getCurrentUser() == null){
           //send user to login
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }else{
           //send user to home
            if(mAuth.getCurrentUser().isEmailVerified()){
                sendusertohome();
            }else{
                //Toast.makeText(getApplicationContext(),"send user to login to verify email",Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
            }
        }
    }

    private void sendusertohome() {
        Intent intent = new Intent(getApplicationContext(),home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void request_permission() {
        //check whether thge permissions are been granted or not
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_NETWORK_STATE)+
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SET_WALLPAPER)+
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED){
            //if permission is not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.SET_WALLPAPER)||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_SETTINGS)||

                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_NETWORK_STATE)){

                //create alertdialog tpo ask permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Grant Permissions");
                builder.setMessage("Camera, Storage and Internet Permission are required to Access feature of this App.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //allow permission
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.WRITE_SETTINGS,
                                Manifest.permission.SET_WALLPAPER,
                        },REQUEST_CODE);
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else{
                // allow permission
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_SETTINGS,
                        Manifest.permission.SET_WALLPAPER,
                },REQUEST_CODE);
            }
        }else{
            Log.d("Permission","Permission granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if((grantResults.length>0) && grantResults[0]+grantResults[1]+grantResults[2]+grantResults[3]+grantResults[4]+grantResults[5] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission Granted....",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Permission Denied....",Toast.LENGTH_SHORT).show();
            }
        }
    }


//add dots to the slider
    private void adddotsidenticater(int postition){
        dots = new TextView[3];
        dotlayout.removeAllViews();
        for(int i = 0;i< dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparentcolor));
            dotlayout.addView(dots[i]);
        }

        if(dots.length >0){
            dots[postition].setTextColor(getResources().getColor(R.color.white));
        }

    }
    ViewPager.OnPageChangeListener viewlistner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            adddotsidenticater(position);
            if(position == 2){
               start.setVisibility(View.VISIBLE);
            }else{
                start.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
