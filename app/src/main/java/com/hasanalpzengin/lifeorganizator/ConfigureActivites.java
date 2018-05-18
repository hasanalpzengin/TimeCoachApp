package com.hasanalpzengin.lifeorganizator;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ConfigureActivites extends Fragment {
    RecyclerView recyclerView;
    ActivityRecyclerAdapter recyclerAdapter;
    LinearLayoutManager layoutManager;
    Button nextButton;
    FloatingActionButton addActivity;
    SharedPreferences sharedPreferences;
    ArrayList<Activity> selectedActivities;
    private static final int REQUEST_CODE = 3838;

    public ConfigureActivites() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configure_activites, container, false);

        //init variables
        selectedActivities = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        addActivity = view.findViewById(R.id.addActivity);
        nextButton = view.findViewById(R.id.nextButton);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        listenerInit();

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new ActivityRecyclerAdapter(getContext(),selectedActivities);
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    private void listenerInit(){
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(),AddActivity.class), REQUEST_CODE);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Worked","ClickListener");
                String[] sleep = new String[2];
                String[] work = new String[2];
                ActivityScheduler activityScheduler = new ActivityScheduler();
                //set sleep times
                sleep[1] =  sharedPreferences.getString("bedtime","23:00");
                sleep[0] =  sharedPreferences.getString("wakeup","7:00");
                //set work times
                work[0] =  sharedPreferences.getString("starttime","0:00");
                work[1] =  sharedPreferences.getString("endtime","0:00");

                Log.d("Worked", sleep[0]+":"+sleep[1]+" W "+work[0]+":"+work[1]);

                ArrayList<GeneratedActivity> scheduledActivities = activityScheduler.scheduleActivities(selectedActivities, sleep, work);

                //call db to register
                DBHelper dbHelper = new DBHelper(getContext(),1);
                dbHelper.setGeneratedActivityTable(scheduledActivities);

                Intent intent = new Intent(getContext(), ActivityGeneratedSchedule.class);
                intent.putExtra("sleepTimes",sleep);
                intent.putExtra("workTimes", work);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE){
            String clicked = data.getStringExtra("clicked");
            int clickedimage = data.getIntExtra("clickedimage",-1);
            selectedActivities.add(new Activity(clicked,clickedimage));
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    class ActivityRecyclerAdapter extends RecyclerView.Adapter<ActivityRecyclerAdapter.ActivityViewHolder>{

        ArrayList<Activity> activities = new ArrayList<>();
        private Context context;
        ArrayAdapter<CharSequence> priorityAdapter;


        public ActivityRecyclerAdapter(Context context, ArrayList<Activity> activities) {
            this.context = context;
            this.activities = activities;
            priorityAdapter = ArrayAdapter.createFromResource(getContext(),R.array.priorities,android.R.layout.simple_spinner_item);
        }

        @Override
        public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.activity_withpriority_card, null);
            return new ActivityViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityViewHolder holder, int position) {
            Activity activity = activities.get(position);

            holder.activityName.setText(activity.getActivityName());
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
            TextView activityName;
            Spinner prioritySpinner;
            int position;

            public ActivityViewHolder(View itemView) {
                super(itemView);
                //init
                activityImage = itemView.findViewById(R.id.activityImage);
                activityName = itemView.findViewById(R.id.timeText);
                prioritySpinner = itemView.findViewById(R.id.spinner);
                //set adapter
                priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                prioritySpinner.setAdapter(priorityAdapter);
                prioritySpinner.setSelection(2);

                prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        selectedActivities.get(position).setPriotri(pos+1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                TextView tv = view.findViewById(R.id.timeText);

                for (Activity activity: activities){
                    if (activity.getActivityName() == tv.getText().toString()){
                        activities.remove(activity);
                        break;
                    }
                }
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }
}