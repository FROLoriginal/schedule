package com.example.schedule.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class SQLScheduleReader(context: Context,
                        tableName: String,
                        val version: Int)
    : SQLManager(context, tableName,
        null, version) {

    private var sqLiteDatabase: SQLiteDatabase = readableDatabase

    fun getScheduleByDay(columns: Array<String>, dayOfWeek: Int): Cursor {
        if (dayOfWeek !in 1..7) throw IllegalArgumentException("DayOfWeek $dayOfWeek must be in 1..7")

        val selection = "$DAY_OF_WEEK = ? "

        val selectionArgs = arrayOf(dayOfWeek.toString())

        return sqLiteDatabase.query(databaseName, columns,
                selection, selectionArgs,
                null, null, null)
    }
}
