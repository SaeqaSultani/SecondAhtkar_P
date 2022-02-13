package com.example.women.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.women.R;
import com.example.women.adaptrs.ViewPagerAdapter;
import com.example.women.fragments.FragmentHoard;
import com.example.women.fragments.FragmentPlaces;
import com.example.women.util.MySharedPreferences;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Locale;


public class MainActivity extends AppCompatActivity{

    SmartTabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    Toast toast;
    long aLong;
    private MySharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();

        //change the language of App white isRtl method
        String languageToLoad = "fa";
        Locale locale = new Locale(languageToLoad);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //SharePreferences
         preferences = MySharedPreferences.getInstance(this);

    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout =  findViewById(R.id.viewpagertab);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentHoard(),"احتکارات");
        adapter.addFragment(new FragmentPlaces(),"مکان های ممنوعه");

        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {

        if (aLong + 2000 > System.currentTimeMillis()){
            toast.cancel();
            super.onBackPressed();
            return;
        }else {

            toast = Toast.makeText(this, R.string.exist, Toast.LENGTH_SHORT);
            toast.show();
        }
        aLong = System.currentTimeMillis();
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        Intent intent;
        switch (id) {

            case R.id.action_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.action_logout:

                logoutDialog();
               break;
        }
        return super.onOptionsItemSelected(item);
    }

    //logout method
    private void logoutDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout);
        builder.setMessage(R.string.logout_text);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this,LoginPageActivity.class);
                preferences.setSignInStatus(false);
                preferences.clearAll();
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
