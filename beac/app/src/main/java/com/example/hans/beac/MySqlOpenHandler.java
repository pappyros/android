package com.example.hans.beac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hans on 2017-02-23.
 */
public class MySqlOpenHandler extends SQLiteOpenHelper{


    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 1;

    public MySqlOpenHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_create_data = "create table data ("
                + "pos varchar(30), "
                + "rssi int(4), "
                + "mac_a varchar(30) not null"
                + ");";
        sqLiteDatabase.execSQL(sql_create_data);

        String sql_create_avg = "create table avg_tb ("
                + "pos varchar(30), "
                + "mac_a varchar(30) not null,"
                + "rssi int(4)"
                + ");";
        sqLiteDatabase.execSQL(sql_create_avg);

//        String sql_here = "create table here ("
//                + "rssi int(4), "
//                + "mac_a varchar(30) not null"
//                + ");";
//        sqLiteDatabase.execSQL(sql_here);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists data;");
        onCreate(sqLiteDatabase);
    }
}