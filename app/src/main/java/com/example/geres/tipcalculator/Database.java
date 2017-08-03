package com.example.geres.tipcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by geres on 8/2/2017.
 */

public class Database extends SQLiteOpenHelper {


    //Database variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tipCalculator.db";
    public static final String TABLE_NAME= "tips";
    public static final String COLUMN_ID = "_id";
    public static final int COLUMN_BILL_DATE = 0;
    public static final float COLUMN_BILL_AMOUNT = 50.10f;
    public static final float COLUMN_TIP_PERCENT= .15f;

    //SQLite  variables
    private SQLiteDatabase databaseHandler;

    //Constructor
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creates table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BILL_DATE +" INTEGER NOT NULL, "
                + COLUMN_BILL_AMOUNT + " REAL NOT NULL"
                + COLUMN_TIP_PERCENT + " REAL NOT NULL"+");";
        sqLiteDatabase.execSQL(query);
    }

    //updates the existing table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        //deletes the table first if it exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // creates the same table with the new properties
        onCreate(sqLiteDatabase);
    }
    public Database open() throws SQLException {
        databaseHandler = getWritableDatabase(); // gets the reference to the database
        return this;
    }

    // Adds new row to the table
    public ArrayList<Tip> getTips(){

        Tip tip = new Tip();

        // Content value is built into android that allows you to add several values in one statement
        ContentValues values = new ContentValues();
        values.put(tip.getBillAmountFormatted(), COLUMN_BILL_AMOUNT);
        values.put(tip.getDateStringFormatted(), COLUMN_BILL_DATE);
        values.put(tip.getTipPercentFormatted(), COLUMN_TIP_PERCENT);

        // Call the open method to get reference to the database
        open();
        databaseHandler.insert(TABLE_NAME, null, values);

        //
        String[] allColumns = new String[] {
                tip.getBillAmountFormatted(), tip.getDateStringFormatted(), tip.getTipPercentFormatted()};
        ArrayList<Tip> tipArrayList = new ArrayList<Tip>();
        Cursor cursor = databaseHandler.query(TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(cursor.moveToNext())
        {
            tipArrayList.add(tip);
        }

        // Once you're done with database close it to give memory back (prevent memory leak)
        close();

        return tipArrayList;
    }

    // Deletes the database
    public void deleteTable() {

        open();

        databaseHandler.execSQL("DELETE FROM " + TABLE_NAME + ";");
    }
}
