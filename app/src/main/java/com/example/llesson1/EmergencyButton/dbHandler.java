package com.example.llesson1.EmergencyButton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHandler extends SQLiteOpenHelper {
    public static final  String DATABASE_NAME = "contactlist.db";
    public static final String TABLE_NAME = "contactlist_data";
    public static final String COLOUMN1 = "ID";
    public static final String COLUMN2 = "ITEM1";

    //create the table
    public dbHandler (Context context)  {super(context, DATABASE_NAME,null,1);}
    @Override
    public void onCreate(SQLiteDatabase db){

        String createTable = "CREATE TABLE " + TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "ITEM1 TEXT)";

        db.execSQL( createTable );

        }

        // upgrade table
     @Override
     public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        String a ="DROP TABLE IF EXISTS " +TABLE_NAME;
        db.execSQL(a);
        onCreate(db);

        }
        //add data
        public boolean addData (String item1)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN2,item1);
            //insert values to the database
            long result = db.insert(TABLE_NAME,null,contentValues);

            if (result== -1) {
                return false;
            } else{
                return true;
            }
        }
        // getting the db content
        public Cursor getListContent(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT* FROM "+ TABLE_NAME,null);
        return data;
        }
}

