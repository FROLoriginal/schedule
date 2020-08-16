package com.example.schedule.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class SQLScheduleReader(context: Context,
                        tableName: String,
                        val version: Int)
    : SQLManager(context,
        tableName,
        null,
        version) {


    private var sqLiteDatabase: SQLiteDatabase = readableDatabase


    fun getScheduleByDay(columns: Array<String>, dayOfWeek: Int, weekStatus: Int): Cursor {

        val selection = DAY_OF_WEEK + " = ? AND (" +
                BOTH_NUMERATOR_DIVIDER + " = ? OR " +
                BOTH_NUMERATOR_DIVIDER + " = ? )"

        val selectionArgs: Array<String> = arrayOf(
                dayOfWeek.toString(),
                BOTH.toString(),
                weekStatus.toString()
        )

        return sqLiteDatabase.query(databaseName, columns,
                selection, selectionArgs,
                null, null, null);
    }

}