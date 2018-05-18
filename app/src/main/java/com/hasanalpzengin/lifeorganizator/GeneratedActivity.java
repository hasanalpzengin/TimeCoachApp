package com.hasanalpzengin.lifeorganizator;

/**
 * Created by hasalp on 24.02.2018.
 */

public class GeneratedActivity {
    private int id, imageID;
    private String activityName, start_clock;

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public GeneratedActivity(int id, String activityName, String start_clock, int imageID) {
        this.id = id;
        this.activityName = activityName;
        this.start_clock = start_clock;
        this.imageID = imageID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getStart_clock() {
        return start_clock;
    }

    public void setStart_clock(String start_clock) {
        this.start_clock = start_clock;
    }
}
