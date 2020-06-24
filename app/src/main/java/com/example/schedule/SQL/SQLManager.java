package com.example.schedule.SQL;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLManager extends SQLiteOpenHelper {

    private String name;
    private Context context;

    public static final String ID = "_id";
    public static final String DAY_OF_WEEK = "day_of_week";
    public static final String COUNTER = "counter";
    public static final String SUBJECT = "subject";
    public static final String FROM = "from_";
    public static final String TO = "to_";
    public static final String AUDITORY = "auditory";
    public static final String TEACHER = "teacher";
    public static final String TYPE_OF_SUBJECT = "type_of_subject";
    public static final String BOTH_NUMERATOR_DIVIDER = "both_numerator_divider";
    public static final String SHARED_PREFERENCES_TABLES = "tables";
    public static final String SHARED_PREFERENCES_TABLE = "table";

    public static final int NUMERATOR = 1;
    public static final int DIVIDER = 2;
    public static final int BOTH = 0;

    public SQLManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.name = name;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if (name != null) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (" +
                    ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    DAY_OF_WEEK + " INTEGER NOT NULL," +
                    COUNTER + " INTEGER NOT NULL," +
                    SUBJECT + " TEXT," +
                    FROM + " TEXT NOT NULL," +
                    TO + " TEXT NOT NULL," +
                    AUDITORY + " TEXT," +
                    TEACHER + " TEXT," +
                    TYPE_OF_SUBJECT + " TEXT," +
                    BOTH_NUMERATOR_DIVIDER + " INTEGER NOT NULL" +
                     ")");
            SharedPreferences sh = context.getSharedPreferences(SHARED_PREFERENCES_TABLES,Context.MODE_PRIVATE);
            sh.edit().putString(SHARED_PREFERENCES_TABLE,name).apply();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
