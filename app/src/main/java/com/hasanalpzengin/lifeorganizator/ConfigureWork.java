package com.hasanalpzengin.lifeorganizator;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ConfigureWork extends Fragment {
    TextInputEditText startTime, endTime;
    Button nextButton;
    Calendar calendar;
    View thisFragment;
    SharedPreferences preferences;

    static final int DEFAULT_START_HOUR = 9;
    static final int DEFAULT_END_HOUR = 17;

    public ConfigureWork() {
        calendar = Calendar.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisFragment = inflater.inflate(R.layout.fragment_configure_work, container, false);
        nextButton = thisFragment.findViewById(R.id.nextButton);
        startTime = thisFragment.findViewById(R.id.startTime);
        endTime = thisFragment.findViewById(R.id.endTime);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initListeners();

        return thisFragment;
    }

    public void initListeners(){
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimer(false);
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
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
                    editor.putString("starttime",startTime.getText().toString());
                    editor.putString("endtime",endTime.getText().toString());
                    editor.commit();

                    ConfigurationActivity.nextPage();
                }
            }
        });

    }

    private boolean checkTimes(){
        String startString = startTime.getText().toString();
        String endString = endTime.getText().toString();
        Log.d("startString",startString);
        Log.d("endString",endString);

        if (startString.length()>0 && endString.length()>0 && startString.length()<=5 && endString.length()<=5) {
            if (startString.charAt(2) != ':' || endString.matches("^[A-Za-z]")) {
                if (startTime != null) {
                    startTime.setError("Invalid Time Format");
                } else {
                    Toast.makeText(thisFragment.getContext(), "Invalid Time Format", Toast.LENGTH_LONG);
                }
                return false;
            }
            if (endString.charAt(2) != ':' || endString.matches("^[A-Za-z]")) {
                if (endTime != null) {
                    endTime.setError("Invalid Time Format");
                } else {
                    Toast.makeText(thisFragment.getContext(), "Invalid Time Format", Toast.LENGTH_LONG);
                }
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    public void showTimer(boolean isStart){
        if (isStart){
            new TimePickerDialog(thisFragment.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    int startHour, startMinute;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        startHour = timePicker.getHour();
                        startMinute = timePicker.getMinute();
                    } else {
                        startHour = hour;
                        startMinute = minute;
                    }
                    String time = String.format("%02d:%02d", startHour, startMinute);
                    startTime.setText(time);
                }
            }, DEFAULT_START_HOUR, 00, true).show();
        }else{
            new TimePickerDialog(thisFragment.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    int endHour, endMinute;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        endHour = timePicker.getHour();
                        endMinute = timePicker.getMinute();
                    }else{
                        endHour = hour;
                        endMinute = minute;
                    }
                    String time = String.format("%02d:%02d", endHour, endMinute);
                    endTime.setText(time);
                }
            }, DEFAULT_END_HOUR, 00, true).show();
        }
    }
}
