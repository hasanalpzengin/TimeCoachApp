package com.hasanalpzengin.lifeorganizator;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private View headerView;
    private  ImageView userImage;
    private TextView userName;
    private ArrayList<Fragment> fragments;
    private DialogInterface.OnClickListener dialogClickListener;
    private ArrayList<GeneratedActivity> activities;
    private AlertDialog alertDialog;
    private Intent sharingIntent;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dbHelper = new DBHelper(ResultActivity.this,1);

        //init fragments
        fragments = new ArrayList<>();
        fragments.add(new ContentFragment());
        fragments.add(new EditActivityFragment());
        fragments.add(new LanguageFragment());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragments.get(0));
        fragmentTransaction.commit();
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        userName = headerView.findViewById(R.id.userName);
        userImage = headerView.findViewById(R.id.userImage);

        Drawable defaultImage = getResources().getDrawable(R.mipmap.ic_launcher_round);

        activities = dbHelper.getSchedule();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth != null) {
            if (firebaseAuth.getUid()!=null) {
                user = firebaseAuth.getCurrentUser();
                Picasso.with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.circle)
                        .resize(defaultImage.getMinimumWidth(), defaultImage.getMinimumHeight())
                        .centerCrop()
                        .into(userImage);
                userName.setText(user.getDisplayName());
            }else{
                userName.setText("Unregistered");
            }

        }
        //init sharing intent
        sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.shareText));
        sb.append("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareApp));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

        startAlert();

    }

    public void startAlert() {
        boolean notification = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("notification",true);
        if (notification) {
            GeneratedActivity activity = getNextActivity(activities);
            if (activity != null) {
//                Intent intent = new Intent(this, ReminderService.class);
//                intent.putExtra("activityName", activity.getActivityName());
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
//                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

                //set alarm
                do{
                    Intent backgroundService = new Intent(ResultActivity.this, BackgroundService.class);
                    Log.d("activityName",activity.getActivityName());
                    backgroundService.putExtra("activityName", activity.getActivityName());
                    backgroundService.putExtra("activityStartTime", activityStartTimeinMilis(activity.getStart_clock()));
                    startService(backgroundService);
                    activity = getNextActivity(activity,activities);
                }while(activity != null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean notificaton = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("notification",true);
        getMenuInflater().inflate(R.menu.result, menu);
        menu.getItem(0).setChecked(notificaton);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.notifications:{
                SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                if (item.isChecked()){
                    item.setChecked(false);
                    sharedPreferences.putBoolean("notification", false);
                }else{
                    item.setChecked(true);
                    sharedPreferences.putBoolean("notification", true);
                    startAlert();
                }
                sharedPreferences.apply();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initDialog(){
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dbHelper.resetSchedule();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        alertDialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResultActivity.this);
        alertDialogBuilder.setMessage(getString(R.string.areYouSure))
                .setPositiveButton(getString(R.string.yesAnswer),dialogClickListener)
                .setNegativeButton(getString(R.string.noAnswer),dialogClickListener);

        alertDialog = alertDialogBuilder.create();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_language:{
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragments.get(2));
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_editActivity:{
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragments.get(1));
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_share:{
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareApp)));
                break;
            }
            case R.id.nav_aboutApp:{
                Intent aboutPage = new Intent(getApplicationContext(), AboutApp.class);
                startActivity(aboutPage);
                break;
            }
            case R.id.nav_logout:{
                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(), getString(R.string.logoutSuccess), Toast.LENGTH_LONG);
                Intent firstPage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(firstPage);
                break;
            }
            case R.id.nav_list:{
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragments.get(0));
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_resetActivities:{
                initDialog();
                alertDialog.show();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public GeneratedActivity getNextActivity(ArrayList<GeneratedActivity> activities) {
        //get current time
        Calendar currentTime = Calendar.getInstance();
        long timeInMillis = activityStartTimeinMilis(currentTime.get(Calendar.HOUR_OF_DAY)+":"+currentTime.get(Calendar.MINUTE));
        for (int i = activities.size()-1; i>=0; i--){
            GeneratedActivity activity = activities.get(i);
            String clock = activity.getStart_clock();
            Log.d("ActivityTime", String.valueOf(activityStartTimeinMilis(clock)));
            Log.d("CurrentTime", String.valueOf(timeInMillis));
            if(activityStartTimeinMilis(clock)<timeInMillis) {
                return activity;
            }
        }
        return null;
    }

    public GeneratedActivity getNextActivity(GeneratedActivity referenceActivity, ArrayList<GeneratedActivity> activities) {
        for (int i = activities.size()-1; i>=0; i--){
            GeneratedActivity activity = activities.get(i);
            String clock = activity.getStart_clock();
            if(activityStartTimeinMilis(clock)<activityStartTimeinMilis(referenceActivity.getStart_clock()) && !referenceActivity.getStart_clock().contentEquals(activity.getStart_clock())) {
                return activity;
            }
        }
        return null;
    }

    public long activityStartTimeinMilis(String startTime){
        long milis = 0;
        String[] hourMinute = startTime.split(":");
        //hour to milis
        milis += Integer.parseInt(hourMinute[0])*60*60*1000;
        milis += Integer.parseInt(hourMinute[1])*60*1000;
        return milis;
    }

}
