package com.example.schedule.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

public class SQLManager {

    private SQLiteDatabase db;
    private String name;

    private static final String ID = "_id";
    private static final String DAY_OF_WEEK = "day_of_week";
    private static final String COUNTER = "counter";
    private static final String SUBJECT = "subject";
    private static final String FROM = "from_";
    private static final String TO = "to_";
    private static final String AUDITORY = "auditory";
    private static final String TEACHER = "teacher";

    public SQLManager(String name, Context baseContext) {

        db = baseContext.openOrCreateDatabase(name + ".db", MODE_PRIVATE, null);
        this.name = name;
    }

    public void createTable() {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (" +
                ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                DAY_OF_WEEK + " INTEGER NOT NULL," +
                COUNTER + " INTEGER NOT NULL," +
                SUBJECT + " TEXT NOT NULL," +
                FROM + " TEXT NOT NULL," +
                TO + " TEXT NOT NULL," +
                AUDITORY + " TEXT," +
                TEACHER + " TEXT" +
                ")");
    }

   public void insert(int dayOfWeek, int counter, String subject, String from, String to, String auditory, String teacher) {

        db.execSQL("INSERT INTO " + name +
                " (" + DAY_OF_WEEK + ","
                + COUNTER + ","
                + SUBJECT + ","
                + FROM + ","
                + TO + ","
                + AUDITORY + ","
                + TEACHER + ") VALUES (" + dayOfWeek + ","
                + counter + ","
                + subject + ","
                + from + ","
                + to + ","
                + auditory + ","
                + teacher + ")");


    }


}
