package com.hasanalpzengin.lifeorganizator;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ConfigurationActivity extends AppCompatActivity {

    //variables
    static ViewPager viewPager;
    public static int currentPage = 0;
    public static final int FIRST_PAGE = 0;
    ConfigurationAdapter configurationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        //init sleep page
        ConfigureSleep configureSleep = new ConfigureSleep();
        ConfigureActivites configureActivites = new ConfigureActivites();
        ConfigureWork configureWork = new ConfigureWork();
        //setup viewpager
        viewPager = findViewById(R.id.viewPager);
        //set adapter
        configurationAdapter = new ConfigurationAdapter(getSupportFragmentManager());
        configurationAdapter.addFragment(configureSleep);
        configurationAdapter.addFragment(configureWork);
        configurationAdapter.addFragment(configureActivites);
        viewPager.setAdapter(configurationAdapter);
        viewPager.setCurrentItem(currentPage);
    }

    public static void nextPage(){
        if (currentPage < viewPager.getAdapter().getCount()){
            viewPager.setCurrentItem(currentPage+1);
            currentPage++;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DBHelper dbHelper = new DBHelper(getApplicationContext(),1);
        if (!dbHelper.getSchedule().isEmpty()){
            Intent result = new Intent(getApplicationContext(),ResultActivity.class);
            startActivity(result);
        }
    }

    public static void previousPage(){
        if (currentPage > FIRST_PAGE){
            viewPager.setCurrentItem(currentPage-1);
        }
    }
}
