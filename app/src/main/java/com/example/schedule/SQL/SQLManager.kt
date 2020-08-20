package com.example.schedule.SQL

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

open class SQLManager(private val context: Context?,
                      private val name: String?,
                      factory: CursorFactory?,
                      version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        name?.let {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (" +
                    ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    DAY_OF_WEEK + " INTEGER NOT NULL," +
                    SUBJECT + " TEXT," +
                    FROM + " TEXT NOT NULL," +
                    TO + " TEXT NOT NULL," +
                    AUDITORY + " TEXT," +
                    TEACHER + " TEXT," +
                    TYPE_OF_SUBJECT + " TEXT," +
                    OPTIONALLY + " INTEGER," +
                    BOTH_NUMERATOR_DIVIDER + " INTEGER NOT NULL" +
                    ")")
            val sh : SharedPreferences
                    = context!!.getSharedPreferences(SHARED_PREFERENCES_TABLES, Context.MODE_PRIVATE)
            sh.edit().putString(SHARED_PREFERENCES_TABLE, name).apply()
        }
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
        const val TYPE_OF_SUBJECT = "type_of_subject"
        const val BOTH_NUMERATOR_DIVIDER = "both_numerator_divider"
        const val OPTIONALLY = "optionally"
        const val SHARED_PREFERENCES_TABLES = "tables"
        const val SHARED_PREFERENCES_TABLE = "table"
        const val VERSION = 1
        const val NUMERATOR = 1
        const val DIVIDER = 2
        const val BOTH = 0
    }

}
