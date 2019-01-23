package com.duk3r.eortologio2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BdayDataBaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "bdaydb";
    private static final int DATABASE_VERSION =29;
    public static final String customdb_table = "bday_table";


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("duk3r","BDAY db opened");
    }

    public BdayDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + customdb_table
                + " (name TEXT , day INTEGER,"
                + " month INTEGER, id INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
