
package com.example.llesson1.EmergencyButton;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.llesson1.R;

import java.util.ArrayList;

public class Register extends AppCompatActivity {
     Button addButton,deleteButton, viewButton;
     EditText phoneNumber;
     ListView listView;
     SQLiteOpenHelper sqLiteOpenHelper;
     SQLiteDatabase sqLiteDatabase;
dbHandler myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phoneNumber = findViewById(R.id.phone);
        addButton = findViewById(R.id.addPN);
        deleteButton = findViewById(R.id.deletePN);
        viewButton = findViewById(R.id.viewPN);

        myDB= new dbHandler(this);

        //add button to add phone numeber data to the db
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addString = phoneNumber.getText().toString();
                //call add data
                addData (addString);
                Toast.makeText(Register.this, "Data Added", Toast.LENGTH_SHORT).show();
                phoneNumber.setText("");
            }
        });
        //when delete button is clicked, remove data fr db
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase = myDB.getWritableDatabase();
                String deleteString = phoneNumber.getText().toString();
                //call the delete data funct
                DeleteData(deleteString);
                Toast.makeText(Register.this, "DATA DELETED", Toast.LENGTH_SHORT).show();
                phoneNumber.setText("");
            }
        });
        // load data fr db to be seen by user
        viewButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call load data
                loadData();
            }
        }));
    }

    //get the data fr db
    private void loadData() {
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContent();
        //if no data in db
        if (data.getCount()==0){
            Toast.makeText(Register.this, "There is no data", Toast.LENGTH_SHORT).show();
        }//if data there is data in the db
         else{
            while (data.moveToNext()) {
                theList.add((data.getString(1)));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }

    }


    //delet data funct
    private boolean DeleteData(String deleteString) {
        return sqLiteDatabase.delete(dbHandler.TABLE_NAME,dbHandler.COLUMN2 + "=?", new String[]{deleteString})>0;
    }

    private void addData(String newEntry) {
        boolean insertData = myDB.addData(newEntry);

        //correct data input
        if(insertData==true) {
            Toast.makeText(Register.this, "Data Added", Toast.LENGTH_SHORT).show();
        } //wrong data input
        else {
            Toast.makeText(Register.this, "Unsuccesful input", Toast.LENGTH_SHORT).show();
        }

    }
}