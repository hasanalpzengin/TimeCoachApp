package com.hasanalpzengin.lifeorganizator;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ActivityAddRecyclerAdapter recyclerAdapter;
    LinearLayoutManager layoutManager;
    Button addButton;
    TextInputEditText TIeditText;
    TextInputLayout TIlayout;
    private ArrayList<Activity> activities;
    private static final int REQUEST_CODE = 3838;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //init views
        recyclerView = findViewById(R.id.activityList);
        addButton = findViewById(R.id.addButton);
        TIeditText = findViewById(R.id.activityEditText);
        TIlayout = findViewById(R.id.activityInputLayout);
        //init activity ArrayList
        activities = new ArrayList<>();
        initActivities();
        //init recycler view
        //set layoutmanager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //set adapter
        recyclerAdapter = new ActivityAddRecyclerAdapter(getApplicationContext(),activities);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("clicked",TIeditText.getText().toString());
                intent.putExtra("clickedimage",Activity.getImage(-1));
                setResult(REQUEST_CODE, intent);
                finish();
            }
        });

    }

    private void initActivities() {
        String[] activityList = getResources().getStringArray(R.array.activities);
        for (int i = 0; i<activityList.length; i++) {
            String activityName = activityList[i];
            activities.add(new Activity(activityName, Activity.getImage(i)));
        }
    }

    //adapter
    class ActivityAddRecyclerAdapter extends RecyclerView.Adapter<ActivityAddRecyclerAdapter.ProductViewHolder>{

        ArrayList<Activity> activities = new ArrayList<>();
        private Context context;


        public ActivityAddRecyclerAdapter(Context context, ArrayList<Activity> activities) {
            this.context = context;
            this.activities = activities;
        }

        @Override
        public ActivityAddRecyclerAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.activities_card, null);
            return new ActivityAddRecyclerAdapter.ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            Activity activity = activities.get(position);

            holder.activityName.setText(activity.getActivityName());
            holder.activityImage.setImageResource(activity.getImageID());
            holder.position = position;
        }

        @Override
        public int getItemCount() {
            return activities.size();
        }


        class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            //view elements from activities_card layout
            ImageView activityImage;
            TextView activityName;
            int position;

            public ProductViewHolder(View itemView) {
                super(itemView);
                //init
                activityImage = itemView.findViewById(R.id.activityImage);
                activityName = itemView.findViewById(R.id.timeText);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("clicked",activities.get(position).getActivityName());
                intent.putExtra("clickedimage",activities.get(position).getImageID());
                setResult(REQUEST_CODE, intent);
                finish();
            }
        }
    }
}
