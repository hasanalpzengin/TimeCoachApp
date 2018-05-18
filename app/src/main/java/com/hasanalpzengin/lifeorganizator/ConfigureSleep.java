package com.hasanalpzengin.lifeorganizator;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigureSleep extends Fragment {

    EditText bedtime, wakeupTime;
    TextInputEditText bedtimeNew, wakeupTimeNew;
    Button nextButton;
    Calendar calendar;
    View thisFragment;
    SharedPreferences preferences;

    static final int DEFAULT_BEDTIME_HOUR = 23;
    static final int DEFAULT_WAKEUP_HOUR = 7;

    public ConfigureSleep() {
        calendar = Calendar.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisFragment = inflater.inflate(R.layout.fragment_configure_sleep, container, false);
        nextButton = thisFragment.findViewById(R.id.nextButton);
        bedtime = thisFragment.findViewById(R.id.bedTime);
        wakeupTime = thisFragment.findViewById(R.id.wakeupTime);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            bedtimeNew = thisFragment.findViewById(R.id.bedTime);
            wakeupTimeNew = thisFragment.findViewById(R.id.wakeupTime);
        }

        initListeners();

        return thisFragment;
    }

    public void initListeners(){
        bedtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimer(false);
            }
        });

        wakeupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimer(true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkTimes()){
                    SharedPreferences.Editor editor = preferences.edit();
                    //set sharedpreferences and commit
                    editor.putString("bedtime",bedtime.getText().toString());
                    editor.putString("wakeup",wakeupTime.getText().toString());
                    editor.commit();

                    ConfigurationActivity.nextPage();
                }
            }
        });

    }

    private boolean checkTimes(){
        String wakeupString = wakeupTime.getText().toString();
        String bedtimeString = bedtime.getText().toString();
        Log.d("wakeupString",wakeupString);
        Log.d("bedtimeString",bedtimeString);

        if (wakeupString.length()>0 && bedtimeString.length()>0 && wakeupString.length()<=5 && bedtimeString.length()<=5) {
            if (wakeupString.charAt(2) != ':' || wakeupString.matches("^[A-Za-z]")) {
                if (wakeupTimeNew != null) {
                    wakeupTimeNew.setError("Invalid Time Format");
                } else {
                    Toast.makeText(thisFragment.getContext(), "Invalid Time Format", Toast.LENGTH_LONG);
                }
                return false;
            }
            if (bedtimeString.charAt(2) != ':' || bedtimeString.matches("^[A-Za-z]")) {
                if (bedtimeNew != null) {
                    bedtimeNew.setError("Invalid Time Format");
                } else {
                    Toast.makeText(thisFragment.getContext(), "Invalid Time Format", Toast.LENGTH_LONG);
                }
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public void showTimer(boolean isWakeup){
        if (isWakeup){
            new TimePickerDialog(thisFragment.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    int wakeupHour, wakeupMinute;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        wakeupHour = timePicker.getHour();
                        wakeupMinute = timePicker.getMinute();
                    } else {
                        wakeupHour = hour;
                        wakeupMinute = minute;
                    }
                    String time = String.format("%02d:%02d", wakeupHour, wakeupMinute);
                    wakeupTime.setText(time);
                }
            }, DEFAULT_WAKEUP_HOUR, 00, true).show();
        }else{
            new TimePickerDialog(thisFragment.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    int bedtimeHour, bedtimeMinute;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        bedtimeHour = timePicker.getHour();
                        bedtimeMinute = timePicker.getMinute();
                    }else{
                        bedtimeHour = hour;
                        bedtimeMinute = minute;
                    }
                    String time = String.format("%02d:%02d", bedtimeHour, bedtimeMinute);
                    bedtime.setText(time);
                }
            }, DEFAULT_BEDTIME_HOUR, 00, true).show();
        }
    }

}
