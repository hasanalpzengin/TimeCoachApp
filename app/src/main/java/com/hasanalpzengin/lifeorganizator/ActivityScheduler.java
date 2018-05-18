package com.hasanalpzengin.lifeorganizator;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by hasalp on 01.02.2018.
 */

public class ActivityScheduler {

    Random rand;

    public ActivityScheduler() {
        this.rand = new Random();
    }

    public ArrayList<GeneratedActivity> scheduleActivities(ArrayList<Activity> activities, String[] sleepTime, String[] workTime){
        Log.d("is getTimeAmount works:", getTimeAmount(sleepTime[0], sleepTime[1])+"");
        //calculate how many minute will be used for each priority}
        int eachPriority = (getTimeAmount(sleepTime[0], sleepTime[1]) - getTimeAmount(workTime[0], workTime[1])) / getPriorityCount(activities);
        int[] arrangedTimes = new int[activities.size()];
        ArrayList<GeneratedActivity> preparedActivities = new ArrayList<>();
        //start time
        int currentHour = Integer.parseInt(sleepTime[0].split(":")[0]);
        int currentMinute = Integer.parseInt(sleepTime[0].split(":")[1]);
        //parse worktime
        int[] startTime = {Integer.parseInt(workTime[0].split(":")[0]),Integer.parseInt(workTime[0].split(":")[1])};
        int[] endTime = {Integer.parseInt(workTime[1].split(":")[0]),Integer.parseInt(workTime[1].split(":")[1])};

        Log.d("Each Priority", String.valueOf(eachPriority));


        int i=0;
        while(activities.size()>0){
            int randIndex = rand.nextInt(activities.size());
            Activity randActivity = activities.get(randIndex);
            //if currentHour with new activity entered between work time
            if (currentHour+(randActivity.getPriotri()*eachPriority/60) > startTime[0] && currentHour < endTime[0]){
                //set currentTime to end of work time.
                currentHour = endTime[0];
                currentMinute = endTime[1];
            }

            //calculate time area of selected item between sleeptime and wakeuptime
            arrangedTimes[i] = randActivity.getPriotri()*eachPriority;
            //convert minute time to hourminute time;
            int hour = Math.abs(arrangedTimes[i]/60);
            int minute = arrangedTimes[i]%60;

            preparedActivities.add(new GeneratedActivity(i, randActivity.getActivityName(), String.format("%02d:%02d", currentHour, currentMinute), randActivity.getImageID()));

            currentMinute += minute;
            if (currentMinute>60){
                currentMinute %= 60;
                currentHour++;
            }
            currentHour += hour;

            i++;

            activities.remove(randIndex);
        }

        return preparedActivities;

    }

    private int getPriorityCount(ArrayList<Activity> activities){
        int sumPriority = 0;
        for (int i=0; i<activities.size(); i++){
            Activity activity = activities.get(i);
            sumPriority += activity.getPriotri();
        }
        return sumPriority;
    }

    private int getTimeAmount(String firstTime, String secondTime){
        int[] second_time, first_time;
        int minute,hour;

        second_time = new int[2];
        first_time = new int[2];
        //parse string times to int array
        for (int i=0; i<2; i++){
            //parsing 22:00 -> 22,00
            second_time[i] = Integer.parseInt((secondTime.split(":"))[i]);
            first_time[i] = Integer.parseInt((firstTime.split(":"))[i]);
        }
        //if minute of time isn't enough to minus operator barrow one hour from sleep_time...
        //simple math of time
        if (second_time[1]-first_time[1]<0){
            second_time[0]--;
            second_time[1] += 60;
        }
        minute = second_time[1]-first_time[1];
        //calculate hour
        hour = Math.abs(second_time[0]-first_time[0]);
        //convert hour to minute and sum with old minute
        minute += hour*60;

        return minute;
    }
}
