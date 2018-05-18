package com.hasanalpzengin.lifeorganizator;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditActivityDialog extends Fragment {

    private ImageView imageView;
    private TextView titleView;
    private TextInputLayout textInputLayout;
    private TextInputEditText inputEditText;
    private Button applyButton, deleteButton;
    private View view;
    private DBHelper dbHelper;

    private int id, iconid;
    private String name, time;

    public EditActivityDialog() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_activity_dialog, container, false);
        init();
        return view;
    }

    private void init() {
        id = getArguments().getInt("id");
        iconid = getArguments().getInt("iconid");
        name = getArguments().getString("name");
        time = getArguments().getString("time");


        applyButton = view.findViewById(R.id.applyButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        imageView = view.findViewById(R.id.IconImage);
        titleView = view.findViewById(R.id.titleText);
        textInputLayout = view.findViewById(R.id.startTimeLayout);
        inputEditText = (TextInputEditText) textInputLayout.getEditText();
        dbHelper = new DBHelper(getContext(),1);

        inputEditText.setText(time);
        titleView.setText(name);
        imageView.setImageResource(iconid);

        inputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimer();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateActivity();
            }
        });
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActivity();
            }
        });
    }

    private void deleteActivity() {
        dbHelper.deleteActivity(id);
        Toast.makeText(getContext(), getString(R.string.deleteSuccess),Toast.LENGTH_SHORT).show();
        EditActivityFragment editFragment = new EditActivityFragment();
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, editFragment).commit();
    }

    private void updateActivity() {
        dbHelper.updateStartTime(id, inputEditText.getText().toString());
        Toast.makeText(getContext(), getString(R.string.updateSuccess),Toast.LENGTH_SHORT).show();
        EditActivityFragment editFragment = new EditActivityFragment();
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, editFragment).commit();
    }

    public void showTimer(){
        int selectedHour = Integer.parseInt(inputEditText.getText().toString().split(":")[0]);
        int selectedMinute = Integer.parseInt(inputEditText.getText().toString().split(":")[1]);

        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
                inputEditText.setText(time);
            }
        }, selectedHour, selectedMinute, true).show();
    }
}
