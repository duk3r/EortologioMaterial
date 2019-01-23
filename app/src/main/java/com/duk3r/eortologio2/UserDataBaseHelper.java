package com.duk3r.eortologio2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDataBaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "userdb";
    private static final int DATABASE_VERSION = 2;
    public static final String customdb_table = "user_table";

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("Gandevs","user db opened");
    }


    public UserDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + customdb_table
                + " (name TEXT , day INTEGER,"
                + " month INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
