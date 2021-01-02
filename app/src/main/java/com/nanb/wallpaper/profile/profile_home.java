package com.nanb.wallpaper.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nanb.wallpaper.R;
import com.nanb.wallpaper.fonts.fonts_home;
import com.nanb.wallpaper.home;
import com.nanb.wallpaper.ringtone.ringtone_Home;

public class profile_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bacgroundnav);
        //bottomNavigationView.setSelectedItemId(R.id.profile);
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
                   /** case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile_home.class));
                        overridePendingTransition(0,0);
                        return true;
                    **/
                }
                return false;
            }
        });
    }
}
