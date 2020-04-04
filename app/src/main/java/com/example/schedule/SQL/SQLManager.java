package com.example.schedule.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLManager extends SQLiteOpenHelper {

    private String name;

    public static final String ID = "_id";
    public static final String DAY_OF_WEEK = "day_of_week";
    public static final String COUNTER = "counter";
    public static final String SUBJECT = "subject";
    public static final String FROM = "from_";
    public static final String TO = "to_";
    public static final String AUDITORY = "auditory";
    public static final String TEACHER = "teacher";

    public SQLManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.name = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (" +
                ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                DAY_OF_WEEK + " INTEGER NOT NULL," +
                COUNTER + " INTEGER NOT NULL," +
                SUBJECT + " TEXT," +
                FROM + " TEXT NOT NULL," +
                TO + " TEXT NOT NULL," +
                AUDITORY + " TEXT," +
                TEACHER + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
