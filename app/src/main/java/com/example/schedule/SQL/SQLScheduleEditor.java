package com.example.schedule.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.POJO.JsonResponse;
import com.example.schedule.POJO.Lesson;
import com.example.schedule.POJO.Object_;
import com.example.schedule.POJO.Schedule;
import com.example.schedule.POJO.Subobject;
import com.example.schedule.ScheduleConstants;
import com.example.schedule.Utils;

import java.util.ArrayList;
import java.util.List;

public class SQLScheduleEditor extends SQLManager {


    private SQLiteDatabase sqLiteDatabase;

    public SQLScheduleEditor(Context context, String tableName, int version) {
        super(context, tableName, null, version);
        sqLiteDatabase = getWritableDatabase();
    }

    public void fillDataBase(JsonResponse jr) {

        List<ContentValues> cvList = new ArrayList<>();

        int dayOfWeekSize = jr.getSchedule().size();

        for (int dayOfWeek = 0; dayOfWeek < dayOfWeekSize; dayOfWeek++) {
            ContentValues cv = new ContentValues();
            Schedule schedule = jr.getSchedule().get(dayOfWeek);
            cv.put(SQLManager.DAY_OF_WEEK, dayOfWeek + 1);

            int lessonSize = schedule.getLessons().size();

            for (int lesson = 0; lesson < lessonSize; lesson++) {
                Lesson les = schedule.getLessons().get(lesson);
                int objectSize = les.getObject().size();
                cv.put(SQLManager.FROM, les.getFrom());
                cv.put(SQLManager.TO, les.getTo());

                for (int object = 0; object < objectSize; object++) {
                    Object_ object_ = les.getObject().get(object);
                    String type = les.getType();
                    String subtype = object_.getSubtype();
                    cv.put(SQLManager.TYPE_OF_SUBJECT, type);
                    if (object_.getSubobject() != null) {
                        int subObjects = object_.getSubobject().size();

                        for (int subObject = 0; subObject < subObjects; subObject++) {
                            Subobject so = object_.getSubobject().get(subObject);
                            if (les.getType().equals(ScheduleConstants.Type.CHANGING)) {

                                cv.put(SQLManager.BOTH_NUMERATOR_DIVIDER, object == 0 ?
                                        SQLManager.NUMERATOR : SQLManager.DIVIDER);

                            } else cv.put(SQLManager.BOTH_NUMERATOR_DIVIDER, SQLManager.BOTH);
                            String[] arr = so.getSubject().split("\\.");

                            if (arr.length == 2) {
                                cv.put(SQLManager.STYLE_OF_SUBJECT, arr[0]);
                            } else cv.put(SQLManager.STYLE_OF_SUBJECT, "null");
                            cv.put(SQLManager.OPTIONALLY, subtype.equals(ScheduleConstants.Subtype.OPTIONALLY) ? 1 : 0);
                            cv.put(SQLManager.SUBJECT, Utils.deleteTypeOfSubjectPart(so.getSubject()));
                            cv.put(SQLManager.AUDITORY, so.getAuditory());
                            cv.put(SQLManager.TEACHER, so.getTeacher());
                            cvList.add(new ContentValues(cv));
                        }
                    } else {
                        if (ScheduleConstants.Type.ACTIVITY.equals(type)) {
                            cv.put(SQLManager.OPTIONALLY, 0);//todo
                            //I don't know why subtype is name of activity subject...
                            cv.put(SQLManager.SUBJECT, subtype);
                            cv.put(SQLManager.AUDITORY, ScheduleConstants.UNKNOWN_OBJECT);
                            cv.put(SQLManager.TEACHER, ScheduleConstants.UNKNOWN_OBJECT);
                        } else cv.put(SQLManager.SUBJECT, ScheduleConstants.UNKNOWN_OBJECT);
                        cvList.add(new ContentValues(cv));
                    }
                }
            }
        }
        for (ContentValues cv : cvList) insert(cv);
    }

    public void insert(ContentValues cv) {
        sqLiteDatabase.insert(getDatabaseName(), null, cv);
    }

    public void edit(ContentValues cv, int id) {
        sqLiteDatabase.update(
                getDatabaseName(),
                cv,
                SQLManager.ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void remove(int id) {
        sqLiteDatabase.delete(
                getDatabaseName(),
                SQLManager.ID + " = ?",
                new String[]{String.valueOf(id)});

    }


}
