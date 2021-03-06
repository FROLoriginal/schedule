package com.example.schedule.SQL

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.schedule.POJO.JsonResponse
import com.example.schedule.Util.Time
import com.example.schedule.Util.TimeUtil
import java.util.*

class SQLScheduleEditor(context: Context,
                        tableName: String,
                        version: Int)
    : SQLManager(context, tableName,
        null, version) {

    private var sqLiteDatabase: SQLiteDatabase = writableDatabase

    fun fillDataBase(jr: JsonResponse) {

        val cvList = ArrayList<ContentValues>()
        val dayOfWeekSize = jr.schedule!!.size

        for (dayOfWeek in 0 until dayOfWeekSize) {
            val cv = ContentValues()
            val schedule = jr.schedule!![dayOfWeek]
            cv.put(DAY_OF_WEEK, dayOfWeek + 1)
            val lessonSize = schedule.lessons.size

            for (lesson in 0 until lessonSize) {
                val les = schedule.lessons[lesson]
                val objectSize = les.`object`.size
                cv.put(FROM, TimeUtil.displayedTimeToTime(les.from).totalMin)
                cv.put(TO, TimeUtil.displayedTimeToTime(les.to).totalMin)

                for (`object` in 0 until objectSize) {
                    val object_ = les.`object`[`object`]!!
                    val subtype = object_.subtype

                    if (object_.subobject != null) {

                        val subObjects = object_.subobject!!.size
                        for (subObject in 0 until subObjects) {
                            val so = object_.subobject!![subObject]

                            val arr = so.subject.split('.')
                            if (arr.size == 2) {
                                cv.put(TYPE_OF_SUBJECT, arr[0])
                            } else cv.put(TYPE_OF_SUBJECT, "")

                            cv.put(SUBJECT, so.subject)
                            cv.put(AUDITORY, so.auditory)
                            cv.put(TEACHER, so.teacher)
                            cvList.add(ContentValues(cv))
                        }
                    } else {

                        //I don't know why subtype is name of activity subject...
                        cv.put(SUBJECT, subtype)
                        cv.put(AUDITORY, "")
                        cv.put(TEACHER, "")
                        cv.put(TYPE_OF_SUBJECT, "")

                        cvList.add(ContentValues(cv))
                    }
                }
            }
        }
        cvList.forEach { insert(it) }
    }

    fun insert(cv: ContentValues) {
        sqLiteDatabase.insert(databaseName, null, cv)
    }

    fun edit(cv: ContentValues, id: Int) {
        sqLiteDatabase.update(
                databaseName,
                cv,
                "$ID = ?",
                arrayOf(id.toString()))
    }

    fun remove(id: Int) {
        sqLiteDatabase.delete(
                databaseName,
                "$ID = ?",
                arrayOf(id.toString()))
    }
    companion object {
        //One of subtype's fields
       private const val OPTIONALLY = "optionally"
    }
}
