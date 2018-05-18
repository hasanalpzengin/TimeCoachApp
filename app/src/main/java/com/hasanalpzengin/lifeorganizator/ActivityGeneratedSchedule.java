package com.hasanalpzengin.lifeorganizator;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityGeneratedSchedule extends AppCompatActivity {

    private String[] sleepTime;
    private String[] workTime;

    ActivityRecyclerAdapter recyclerAdapter;
    LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    ArrayList<GeneratedActivity> generatedActivities;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private View changeActivityDialog;
    //dialog's items
    ImageView d_imageView;
    TextView d_textView;
    Button d_changeButton, endButton;
    TextInputEditText d_textInputEditText;
    private int clickedActivityIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sleepTime = getIntent().getStringArrayExtra("sleepTimes");
        workTime = getIntent().getStringArrayExtra("workTimes");
        setContentView(R.layout.activity_generated_schedule);

        init();
    }

    private void init(){
        //init recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        endButton = findViewById(R.id.endButton);

        DBHelper dbHelper = new DBHelper(getApplicationContext(),1);
        generatedActivities = dbHelper.getSchedule();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new ActivityRecyclerAdapter(getApplicationContext(),generatedActivities);
        recyclerView.setAdapter(recyclerAdapter);

        alertDialogBuilder = new AlertDialog.Builder(ActivityGeneratedSchedule.this);
        changeActivityDialog = getLayoutInflater().inflate(R.layout.dialog_change_activity, null);
        //init dialog items
        d_imageView = changeActivityDialog.findViewById(R.id.IconImage);
        d_textView = changeActivityDialog.findViewById(R.id.titleText);
        d_textInputEditText = changeActivityDialog.findViewById(R.id.startTimeEditText);
        d_changeButton = changeActivityDialog.findViewById(R.id.acceptButton);

        d_changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
            }
        });

        d_textInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimer();
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
            }
        });
    }
    //function works after changeButton Clicked
    private void changeActivity(){
        if (checkTimes()){
            String text = d_textInputEditText.getText().toString();
            //init dbHelper to update StartTime
            DBHelper dbHelper = new DBHelper(getApplicationContext(), 1);
            dbHelper.updateStartTime(generatedActivities.get(clickedActivityIndex).getId(), text);
            generatedActivities.get(clickedActivityIndex).setStart_clock(text);
            recyclerAdapter.notifyDataSetChanged();
            //close dialog
            alertDialog.dismiss();
            Snackbar.make(recyclerView, "Activity Edited", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean checkTimes(){
        String startString = d_textInputEditText.getText().toString();
        Log.d("startString",startString);
        if (startString.charAt(2) != ':' || startString.matches("^[A-Za-z]") || startString.length()>5) {
            if (d_textInputEditText != null) {
                d_textInputEditText.setError("Invalid Time Format");
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Time Format", Toast.LENGTH_LONG);
            }
            return false;
        }
        return true;
    }

    public void showTimer(){
        new TimePickerDialog(ActivityGeneratedSchedule.this, new TimePickerDialog.OnTimeSetListener() {
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
                d_textInputEditText.setText(time);
            }
        }, 9, 00, true).show();
    }

    class ActivityRecyclerAdapter extends RecyclerView.Adapter<ActivityRecyclerAdapter.ActivityViewHolder>{

        ArrayList<GeneratedActivity> activities = new ArrayList<>();
        private Context context;


        public ActivityRecyclerAdapter(Context context, ArrayList<GeneratedActivity> activities) {
            this.context = context;
            this.activities = activities;
        }

        @Override
        public ActivityRecyclerAdapter.ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.time_card, parent, false);
            return new ActivityRecyclerAdapter.ActivityViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityRecyclerAdapter.ActivityViewHolder holder, int position) {
            GeneratedActivity activity = activities.get(position);

            holder.activityName.setText(activity.getActivityName());
            holder.timeText.setText(activity.getStart_clock());
            holder.activityImage.setImageResource(activity.getImageID());
            holder.position = position;
        }

        @Override
        public int getItemCount() {
            return activities.size();
        }


        class ActivityViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
            //view elements from activities_card layout
            ImageView activityImage;
            TextView activityName, timeText;
            Button editButton;
            int position;

            public ActivityViewHolder(View itemView) {
                super(itemView);
                //init
                activityImage = itemView.findViewById(R.id.activityImage);
                activityName = itemView.findViewById(R.id.titleText);
                timeText = itemView.findViewById(R.id.timeText);
                editButton = itemView.findViewById(R.id.editButton);

                editButton.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                GeneratedActivity clickedActivity = activities.get(position);
                d_imageView.setImageResource(clickedActivity.getImageID());
                d_textView.setText(clickedActivity.getActivityName());
                d_textInputEditText.setText(clickedActivity.getStart_clock());
                clickedActivityIndex = position;
                //show dialog
                alertDialogBuilder.setView(changeActivityDialog);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
}
