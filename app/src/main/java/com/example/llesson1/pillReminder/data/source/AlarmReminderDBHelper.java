package com.example.llesson1.pillReminder.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.llesson1.pillReminder.data.History;
import com.example.llesson1.pillReminder.data.MedicineAlarm;
import com.example.llesson1.pillReminder.data.Pills;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlarmReminderDBHelper extends SQLiteOpenHelper {
    //db name
    private static final String DATABASE_NAME = "pillreminder.db";
    // db version
    private static final int DATABASE_VERSION = 1;
    //table name
    private static final String PILL_TABLE = "pills";
    private static final String ALARM_TABLE = "alarms";
    private static final String PILL_ALARM_LINKS = "pill_alarm";
    private static final String HISTORIES_TABLE = "histories";
    //coomon colum name and loc
    public static final String KEY_ROWID = "id";
    //pill table col, used by history table
    private static final String KEY_PILLNAME = "pillName";
    //alarm table column, hour and  minute used by history table
    private static final String KEY_INTENT = "intent";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_DAY_WEEK = "day_of_week";
    private static final String KEY_ALARMS_PILL_NAME = "pillName";
    private static final String KEY_DOSE_QUANTITY = "dose_quantity";
    private static final String KEY_DOSE_UNITS = "dose_units";
    private static final String KEY_ALARM_ID = "alarm_id";

    //pill-alarm link table columns
    private static final String KEY_PILLTABLE_ID = "pill_id";
    private static final String KEY_ALARMTABLE_ID = "alarm_id";

    //history table columns, some used above
    private static final String KEY_DATE_STRING = "date";
    private static final String KEY_ACTION = "action";

    //pill table : create statement
    private static final String CREATE_PILL_TABLE =
            "create table " + PILL_TABLE + "("
                    + KEY_ROWID + " integer primary key not null,"
                    + KEY_PILLNAME + " text not null" + ")";

    //alarm table: create statement
    private static final String CREATE_ALARM_TABLE =
            "create table " + ALARM_TABLE + "("
                    + KEY_ROWID + " integer primary key,"
                    + KEY_ALARM_ID + " integer,"
                    + KEY_HOUR + " integer,"
                    + KEY_MINUTE + " integer,"
                    + KEY_ALARMS_PILL_NAME + " text not null,"
                    + KEY_DATE_STRING + " text,"
                    + KEY_DOSE_QUANTITY + " text,"
                    + KEY_DOSE_UNITS + " text,"
                    + KEY_DAY_WEEK + " integer" + ")";
    // pill-alarm link table: create statement
    private static final String CREATE_PILL_ALARM_LINKS_TABLE =
            "create table " + PILL_ALARM_LINKS + "("
                    + KEY_ROWID + " integer primary key not null,"
                    + KEY_PILLTABLE_ID + " integer not null,"
                    + KEY_ALARMTABLE_ID + " integer not null" + ")";

    //histories table:create statemtn
    private static final String CREATE_HISTORIES_TABLE =
            String.format("CREATE TABLE %s(%s integer primary key, %s text not null, %s text, %s text, %s text, %s integer, %s integer, %s integer , %s integer)", HISTORIES_TABLE, KEY_ROWID, KEY_PILLNAME, KEY_DOSE_QUANTITY, KEY_DOSE_UNITS, KEY_DATE_STRING, KEY_HOUR, KEY_ACTION, KEY_MINUTE, KEY_ALARM_ID);

    //constructor
    public AlarmReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    // Creating tables
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PILL_TABLE);
        db.execSQL(CREATE_ALARM_TABLE);
        db.execSQL(CREATE_PILL_ALARM_LINKS_TABLE);
        db.execSQL(CREATE_HISTORIES_TABLE);
    }

    @Override
    // TODO: change this so that updating doesn't delete old data
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PILL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PILL_ALARM_LINKS);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORIES_TABLE);
        onCreate(db);
    }

    //create methods
    public long createPill(Pills pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILLNAME, pill.getPillName());
        return db.insert(PILL_TABLE, null, values);
    }
    // takes in a model alarm object and insert a row into db
    //for each day of the week the alarm is meant to go off

    public long[] createAlarm(MedicineAlarm alarm, long pill_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long[] alarm_ids = new long[7];

        // Create a separate row in the table for every day of the week for this alarm
        int arrayPos = 0;
        for (boolean day : alarm.getDayOfWeek()) {
            if (day) {
                ContentValues values = new ContentValues();
                values.put(KEY_HOUR, alarm.getHour());
                values.put(KEY_MINUTE, alarm.getMinute());
                values.put(KEY_DAY_WEEK, arrayPos + 1);
                values.put(KEY_ALARMS_PILL_NAME, alarm.getPillName());
                values.put(KEY_DOSE_QUANTITY, alarm.getDoseQuantity());
                values.put(KEY_DOSE_UNITS, alarm.getDoseUnit());
                values.put(KEY_DATE_STRING, alarm.getDateString());
                values.put(KEY_ALARM_ID, alarm.getAlarmId());

                // Insert row
                long alarm_id = db.insert(ALARM_TABLE, null, values);
                alarm_ids[arrayPos] = alarm_id;

                // Link alarm to a pill
                createPillAlarmLink(pill_id, alarm_id);
            }
            arrayPos++;
        }
        return alarm_ids;
    }
    //private funct that inserta row into a table that links pikll and alarm
    private long createPillAlarmLink(long pill_id, long alarm_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PILLTABLE_ID, pill_id);
        values.put(KEY_ALARMTABLE_ID, alarm_id);
        return db.insert(PILL_ALARM_LINKS, null, values);
    }
//use history model obj to store histories in the db
public void createHistory(History history) {
    SQLiteDatabase db = getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_PILLNAME, history.getPillName());
    values.put(KEY_DATE_STRING, history.getDateString());
    values.put(KEY_HOUR, history.getHourTaken());
    values.put(KEY_MINUTE, history.getMinuteTaken());
    values.put(KEY_DOSE_QUANTITY, history.getDoseQuantity());
    values.put(KEY_DOSE_UNITS, history.getDoseUnit());
    values.put(KEY_ACTION, history.getAction());
    values.put(KEY_ALARM_ID, history.getAlarmId());

    // Insert row
    db.insert(HISTORIES_TABLE, null, values);
}


//get methhods
    //allow pillBox to retrieve a row fr pill table in db
public Pills getPillByName(String pillName) {
    SQLiteDatabase db = this.getReadableDatabase();

    String dbPill = "select * from "
            + PILL_TABLE + " where "
            + KEY_PILLNAME + " = "
            + "'" + pillName + "'";

    Cursor c = db.rawQuery(dbPill, null);

    Pills pill = new Pills();

    if (c.moveToFirst() && c.getCount() >= 1) {
        pill.setPillName(c.getString(c.getColumnIndex(KEY_PILLNAME)));
        pill.setPillId(c.getLong(c.getColumnIndex(KEY_ROWID)));
        c.close();
    }
    return pill;
}
//retrieve all pill row fr db
public List<Pills> getAllPills() {
    List<Pills> pills = new ArrayList<>();
    String dbPills = "SELECT * FROM " + PILL_TABLE;

    SQLiteDatabase db = getReadableDatabase();
    Cursor c = db.rawQuery(dbPills, null);

    // Loops through all rows, adds to list
    if (c.moveToFirst()) {
        do {
            Pills p = new Pills();
            p.setPillName(c.getString(c.getColumnIndex(KEY_PILLNAME)));
            p.setPillId(c.getLong(c.getColumnIndex(KEY_ROWID)));

            pills.add(p);
        } while (c.moveToNext());
    }
    c.close();
    return pills;
}

//allow retieve all alarms linked to a pill
public List<MedicineAlarm> getAllAlarmsByPill(String pillName) throws URISyntaxException {
    List<MedicineAlarm> alarmsByPill = new ArrayList<>();

    // HINT: When reading string: '.' are not periods ex) pill.rowIdNumber
    String selectQuery = "SELECT * FROM " +
            ALARM_TABLE + " alarm, " +
            PILL_TABLE + " pill, " +
            PILL_ALARM_LINKS + " pillAlarm WHERE " +
            "pill." + KEY_PILLNAME + " = '" + pillName + "'" +
            " AND pill." + KEY_ROWID + " = " +
            "pillAlarm." + KEY_PILLTABLE_ID +
            " AND alarm." + KEY_ROWID + " = " +
            "pillAlarm." + KEY_ALARMTABLE_ID;

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor c = db.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
        do {
            MedicineAlarm al = new MedicineAlarm();
            al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
            al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
            al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
            al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_PILL_NAME)));
            al.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
            al.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
            al.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
            al.setAlarmId(c.getInt(c.getColumnIndex(KEY_ALARM_ID)));

            alarmsByPill.add(al);
        } while (c.moveToNext());
    }

    c.close();


    return combineAlarms(alarmsByPill);
}

    public List<MedicineAlarm> getAllAlarms(String pillName) throws URISyntaxException {
        List<MedicineAlarm> alarmsByPill = new ArrayList<>();

        // HINT: When reading string: '.' are not periods ex) pill.rowIdNumber
        String selectQuery = "SELECT * FROM " +
                ALARM_TABLE + " alarm, " +
                PILL_TABLE + " pill, " +
                PILL_ALARM_LINKS + " pillAlarm WHERE " +
                "pill." + KEY_PILLNAME + " = '" + pillName + "'" +
                " AND pill." + KEY_ROWID + " = " +
                "pillAlarm." + KEY_PILLTABLE_ID +
                " AND alarm." + KEY_ROWID + " = " +
                "pillAlarm." + KEY_ALARMTABLE_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                MedicineAlarm al = new MedicineAlarm();
                al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
                al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
                al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_PILL_NAME)));
                al.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
                al.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
                al.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
                al.setAlarmId(c.getInt(c.getColumnIndex(KEY_ALARM_ID)));

                alarmsByPill.add(al);
            } while (c.moveToNext());
        }

        c.close();


        return alarmsByPill;
    }

// return all individual alarm that occur on a certain day of the week
    //alarm returned do not know of their counterparts that occur on diff days
public List<MedicineAlarm> getAlarmsByDay(int day) {
    List<MedicineAlarm> daysAlarms = new ArrayList<>();

    String selectQuery = "SELECT * FROM " +
            ALARM_TABLE + " alarm WHERE " +
            "alarm." + KEY_DAY_WEEK +
            " = '" + day + "'";

    SQLiteDatabase db = getReadableDatabase();
    Cursor c = db.rawQuery(selectQuery, null);

    if (c.moveToFirst()) {
        do {
            MedicineAlarm al = new MedicineAlarm();
            al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
            al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
            al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
            al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_PILL_NAME)));
            al.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
            al.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
            al.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
            al.setAlarmId(c.getInt(c.getColumnIndex(KEY_ALARM_ID)));
            daysAlarms.add(al);
        } while (c.moveToNext());
    }
    c.close();

    return daysAlarms;
}

    public MedicineAlarm getAlarmById(long alarm_id) throws URISyntaxException {

        String dbAlarm = "SELECT * FROM " +
                ALARM_TABLE + " WHERE " +
                KEY_ROWID + " = " + alarm_id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        MedicineAlarm al = new MedicineAlarm();
        al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
        al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
        al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
        al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_PILL_NAME)));
        al.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
        al.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
        al.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
        al.setAlarmId(c.getInt(c.getColumnIndex(KEY_ALARM_ID)));
        c.close();

        return al;
    }
//private helper funct thta combines rows in db back into a full model alarm w a dayOfWeek array
private List<MedicineAlarm> combineAlarms(List<MedicineAlarm> dbAlarms) throws URISyntaxException {
    List<String> timesOfDay = new ArrayList<>();
    List<MedicineAlarm> combinedAlarms = new ArrayList<>();

    for (MedicineAlarm al : dbAlarms) {
        if (timesOfDay.contains(al.getStringTime())) {
            // Add this db row to alarm object
            for (MedicineAlarm ala : combinedAlarms) {
                if (ala.getStringTime().equals(al.getStringTime())) {
                    int day = getDayOfWeek(al.getId());
                    boolean[] days = ala.getDayOfWeek();
                    days[day - 1] = true;
                    ala.setDayOfWeek(days);
                    ala.addId(al.getId());
                }
            }
        } else {
            // Create new Alarm object with day of week array */
            MedicineAlarm newAlarm = new MedicineAlarm();
            boolean[] days = new boolean[7];

            newAlarm.setPillName(al.getPillName());
            newAlarm.setMinute(al.getMinute());
            newAlarm.setHour(al.getHour());
            newAlarm.addId(al.getId());
            newAlarm.setDateString(al.getDateString());
            newAlarm.setAlarmId(al.getAlarmId());
            int day = getDayOfWeek(al.getId());
            days[day - 1] = true;
            newAlarm.setDayOfWeek(days);

            timesOfDay.add(al.getStringTime());
            combinedAlarms.add(newAlarm);
        }
    }

    Collections.sort(combinedAlarms);
    return combinedAlarms;
}
// get a single pillapp.model-alarm
    //used as a helper function

    public int getDayOfWeek(long alarm_id) throws URISyntaxException {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbAlarm = "SELECT * FROM " +
                ALARM_TABLE + " WHERE " +
                KEY_ROWID + " = " + alarm_id;

        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        int dayOfWeek = c.getInt(c.getColumnIndex(KEY_DAY_WEEK));
        c.close();

        return dayOfWeek;
    }
//allow  pillBox to retrieve fr hist table

    public List<History> getHistory() {
        List<History> allHistory = new ArrayList<>();
        String dbHist = "SELECT * FROM " + HISTORIES_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbHist, null);

        if (c.moveToFirst()) {
            do {
                History h = new History();
                h.setPillName(c.getString(c.getColumnIndex(KEY_PILLNAME)));
                h.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
                h.setHourTaken(c.getInt(c.getColumnIndex(KEY_HOUR)));
                h.setMinuteTaken(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                h.setDoseQuantity(c.getString(c.getColumnIndex(KEY_DOSE_QUANTITY)));
                h.setDoseUnit(c.getString(c.getColumnIndex(KEY_DOSE_UNITS)));
                h.setAction(c.getInt(c.getColumnIndex(KEY_ACTION)));
                h.setAlarmId(c.getInt(c.getColumnIndex(KEY_ALARM_ID)));

                allHistory.add(h);
            } while (c.moveToNext());
        }
        c.close();
        return allHistory;
    }

    //delete methods
    private void deletePillAlarmLinks(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PILL_ALARM_LINKS, KEY_ALARMTABLE_ID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deleteAlarm(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // First delete any link in PillAlarmLink Table
        deletePillAlarmLinks(alarmId);

        /* Then delete alarm */
        db.delete(ALARM_TABLE, KEY_ROWID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deletePill(String pillName) throws URISyntaxException {
        SQLiteDatabase db = this.getWritableDatabase();
        List<MedicineAlarm> pillsAlarms;

        // First get all Alarms and delete them and their Pill-links */
        pillsAlarms = getAllAlarmsByPill(pillName);
        for (MedicineAlarm alarm : pillsAlarms) {
            long id = alarm.getId();
            deleteAlarm(id);
        }

        // Then delete Pill
        db.delete(PILL_TABLE, KEY_PILLNAME
                + " = ?", new String[]{pillName});
    }
}

    /*public  AlarmReminderDBHelper(Context context){
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

}*/
