package com.example.schedule.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLScheduleReader extends SQLManager {

    private SQLiteDatabase sqLiteDatabase;

    public SQLScheduleReader(Context context, String tableName, int version) {
        super(context, tableName, null, version);
        sqLiteDatabase = getReadableDatabase();
    }

    public Cursor getScheduleByDay(String[] columns, int dayOfWeek, int weekStatus) {

        String selection = SQLManager.DAY_OF_WEEK + " = ? AND (" +
                SQLManager.BOTH_NUMERATOR_DIVIDER + " = ? OR " +
                SQLManager.BOTH_NUMERATOR_DIVIDER + " = ? )";

        String[] selectionArgs = new String[]{
                String.valueOf(dayOfWeek),
                String.valueOf(SQLManager.BOTH),
                String.valueOf(weekStatus)};

        return sqLiteDatabase.query(getDatabaseName(), columns,
                selection, selectionArgs,
                null, null, null);
    }

}
