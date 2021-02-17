package com.example.llesson1.pillReminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmReminderDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pillreminder.db";
    private static final int DATABASE_VERSION = 1;
    public  AlarmReminderDBHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void  onCreate(SQLiteDatabase sqLiteDatabase) {
        //create string that contains the SQL statement to create the reminder table
        String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE" + AlarmReminderContract.AlarmReminderEntry.TABLE_NAME +"("
        + AlarmReminderContract.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + AlarmReminderContract.AlarmReminderEntry.KEY_PILL_NAME + "TEXT NOT NULL"
        + AlarmReminderContract.AlarmReminderEntry.KEY_HOUR +"INTEGER"
        +AlarmReminderContract.AlarmReminderEntry.KEY_MINUTE +"INTEGER"
        +AlarmReminderContract.AlarmReminderEntry.KEY_DAY_WEEK + "INTEGER"
        +AlarmReminderContract.AlarmReminderEntry.KEY_DOSE_QUANTITY +"TEXT"
        +AlarmReminderContract.AlarmReminderEntry.KEY_DOSE_UNITS +"TEXT" +");";

        //Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
