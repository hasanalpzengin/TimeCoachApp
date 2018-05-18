package com.hasanalpzengin.lifeorganizator;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<GeneratedActivity> generatedActivities;
    LinearLayoutManager layoutManager;

    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DBHelper dbHelper = new DBHelper(getContext(), 1);
        generatedActivities = dbHelper.getSchedule();

        ActivityRecyclerAdapter recyclerAdapter = new ActivityRecyclerAdapter(getContext(), generatedActivities);

        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    class ActivityRecyclerAdapter extends RecyclerView.Adapter<ActivityRecyclerAdapter.ActivityViewHolder>{

        ArrayList<GeneratedActivity> activities;
        private Context context;


        public ActivityRecyclerAdapter(Context context, ArrayList<GeneratedActivity> activities) {
            this.context = context;
            this.activities = activities;
        }

        @Override
        public ActivityRecyclerAdapter.ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.finished_activity_card, parent, false);
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


        class ActivityViewHolder extends RecyclerView.ViewHolder{
            //view elements from activities_card layout
            ImageView activityImage;
            TextView activityName, timeText;
            int position;

            public ActivityViewHolder(View itemView) {
                super(itemView);
                //init
                activityImage = itemView.findViewById(R.id.activityImage);
                activityName = itemView.findViewById(R.id.activityText);
                timeText = itemView.findViewById(R.id.timeText);

            }
        }
    }

}
