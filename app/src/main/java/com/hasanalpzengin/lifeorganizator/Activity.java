package com.hasanalpzengin.lifeorganizator;

/**
 * Created by hasalp on 06.02.2018.
 */

public class Activity {
    private String activityName;
    private int imageID;
    private int priotri = 2;

    public Activity(String activityName, int imageID) {
        this.activityName = activityName;
        this.imageID = imageID;
    }

    public Activity(String activityName) {
        this.activityName = activityName;
        this.imageID = getImage(-1);
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getPriotri() {
        return priotri;
    }

    public void setPriotri(int priotri) {
        this.priotri = priotri;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public static int getImage(int activityID){
        switch (activityID){
            case 0: return R.mipmap.home;
            case 1: return R.mipmap.clean;
            case 2: return R.mipmap.tv;
            case 3: return R.mipmap.cook;
            case 4: return R.mipmap.washdishes;
            case 5: return R.mipmap.eat;
            case 6: return R.mipmap.sleep;
            case 7: return R.mipmap.read;
            case 8: return R.mipmap.shower;
            case 9: return R.mipmap.brush;
            case 10: return R.mipmap.diy;
            case 11: return R.mipmap.family;
            case 12: return R.mipmap.bath;
            case 14: return R.mipmap.iron;
            case 15: return R.mipmap.flower;
            case 16: return R.mipmap.laundry;
            case 18: return R.mipmap.music;
            case 19: return R.mipmap.game;
            case 20: return R.mipmap.news;
            case 21: return R.mipmap.bike;
            case 22: return R.mipmap.boat;
            case 23: return R.mipmap.bus;
            case 24: return R.mipmap.phone;
            case 25: return R.mipmap.car;
            case 26: return R.mipmap.computer;
            case 27: return R.mipmap.dance;
            case 28: return R.mipmap.date;
            case 29: return R.mipmap.doctor;
            case 30: return R.mipmap.draw;
            case 31: return R.mipmap.gym;
            case 32: return R.mipmap.language;
            case 33: return R.mipmap.lesson;
            case 34: return R.mipmap.meditation;
            case 35: return R.mipmap.meeting;
            case 36: return R.mipmap.movie;
            case 37: return R.mipmap.paint;
            case 38: return R.mipmap.party;
            case 39: return R.mipmap.pet;
            case 40: return R.mipmap.phone;
            case 41: return R.mipmap.photography;
            case 42: return R.mipmap.programming;
            case 43: return R.mipmap.relax;
            case 44: return R.mipmap.religion;
            case 45: return R.mipmap.run;
            case 46: return R.mipmap.shopping;
            case 47: return R.mipmap.sing;
            case 48: return R.mipmap.ski;
            case 49: return R.mipmap.social;
            case 50: return R.mipmap.spa;
            case 51: return R.mipmap.sport;
            case 52: return R.mipmap.study;
            case 53: return R.mipmap.subway;
            case 54: return R.mipmap.swim;
            case 55: return R.mipmap.theater;
            case 56: return R.mipmap.tourism;
            case 57: return R.mipmap.train;
            case 58: return R.mipmap.vehicle;
            case 59: return R.mipmap.wait;
            case 60: return R.mipmap.walk;
            case 61: return R.mipmap.write;
            default: return R.mipmap.no_image;
        }
    }
}
