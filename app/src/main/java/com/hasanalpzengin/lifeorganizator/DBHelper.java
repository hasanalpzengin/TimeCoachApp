package com.hasanalpzengin.lifeorganizator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hasal on 28-Jan-18.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lifeOrganizator";
    private static final String GENERATED_ACTIVITY_TABLE = "activities";
    private static final String[] COLUMNS = {"id", "activity_name", "start_clock", "image_id"};

    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create generated_activity_table
        String sql = "CREATE TABLE " + GENERATED_ACTIVITY_TABLE + " (id INTEGER PRIMARY KEY, activity_name TEXT, start_clock TEXT, image_id INTEGER)";
        Log.d("SQL: ", sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GENERATED_ACTIVITY_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void setGeneratedActivityTable(ArrayList<GeneratedActivity> datas){
        SQLiteDatabase database = getWritableDatabase();
        //truncate table
        database.execSQL("DELETE FROM "+GENERATED_ACTIVITY_TABLE);
        database.execSQL("VACUUM");

        for (GeneratedActivity data : datas){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMNS[0], data.getId());
            contentValues.put(COLUMNS[1], data.getActivityName());
            contentValues.put(COLUMNS[2], data.getStart_clock());
            contentValues.put(COLUMNS[3], data.getImageID());

            database.insert(GENERATED_ACTIVITY_TABLE, null, contentValues);
        }
        database.close();
    }

    public ArrayList<GeneratedActivity> getSchedule(){
        ArrayList<GeneratedActivity> generatedActivities = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        //getSchedule Query
        Cursor cursor = database.query(GENERATED_ACTIVITY_TABLE, COLUMNS,null,null,null ,null, "start_clock ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            generatedActivities.add(new GeneratedActivity(cursor.getInt(cursor.getColumnIndex(COLUMNS[0])), cursor.getString(cursor.getColumnIndex(COLUMNS[1])),
                    cursor.getString(cursor.getColumnIndex(COLUMNS[2])), cursor.getInt(cursor.getColumnIndex(COLUMNS[3]))));
            cursor.moveToNext();
        }

        database.close();

        return generatedActivities;
    }

    public void deleteActivity(int id){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(GENERATED_ACTIVITY_TABLE, COLUMNS[0]+"=?",new String[]{String.valueOf(id)});
        database.close();
    }

    public void resetSchedule(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(GENERATED_ACTIVITY_TABLE,null, null);
    }

    public void updateStartTime(int id, String time){
        //call writable db
        SQLiteDatabase database = getWritableDatabase();
        //init values
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMNS[2],time);
        database.update(GENERATED_ACTIVITY_TABLE, contentValues, COLUMNS[0]+"=?", new String[]{String.valueOf(id)});
        database.close();
    }
}

