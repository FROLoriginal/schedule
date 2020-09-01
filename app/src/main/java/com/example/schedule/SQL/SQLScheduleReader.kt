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

        val selectionArgs: Array<String> = arrayOf(dayOfWeek.toString())

        return sqLiteDatabase.query(databaseName, columns,
                selection, selectionArgs,
                null, null, null)
    }

    fun getAllIdsNotes(): Cursor {

        return sqLiteDatabase.query(databaseName, arrayOf(ID), null,
                null, null, null, ID)

    }

    fun isIdExists(id: Int): Cursor {
        return sqLiteDatabase.rawQuery(
                "SELECT EXISTS(SELECT $ID FROM $databaseName WHERE id = $id)",
                null)
    }


}
