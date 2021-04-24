package com.example.schedule.SQL

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

open class SQLManager(private val context: Context,
                      private val name: String,
                      factory: CursorFactory?,
                      version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (" +
                ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                DAY_OF_WEEK + " INTEGER NOT NULL," +
                SUBJECT + " TEXT," +
                FROM + " INTEGER NOT NULL," +
                TO + " INTEGER NOT NULL," +
                AUDITORY + " TEXT," +
                TEACHER + " TEXT," +
                TYPE_OF_SUBJECT + " TEXT" +
                ")")
        context.getSharedPreferences(SHARED_PREF_DB_TABLE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(SHARED_PREF_TABLE_NAME_KEY, name)
                .apply()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        const val ID = "_id"

        //Day of week start from one
        const val DAY_OF_WEEK = "day_of_week"
        const val SUBJECT = "subject"
        const val FROM = "from_"
        const val TO = "to_"
        const val AUDITORY = "auditory"
        const val TEACHER = "teacher"
        const val TYPE_OF_SUBJECT = "STYLE_OF_SUBJECT"

        const val SHARED_PREF_DB_TABLE_NAME = "tables"
        const val SHARED_PREF_TABLE_NAME_KEY = "table"

        //For example, lecture, seminar and others
        const val VERSION = 1
    }

}
