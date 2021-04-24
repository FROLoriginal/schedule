package com.example.schedule.SQL

import android.database.Cursor
import com.example.schedule.ui.schedule.LessonItem

class SQLDataTranslator {

    companion object {
        internal fun getRawListLessonModel(reader: SQLScheduleReader):
                ArrayList<LessonItem> {

            val columns = arrayOf(
                    SQLManager.ID,  //index 0
                    SQLManager.SUBJECT,  //index 1
                    SQLManager.TEACHER,  //index 2
                    SQLManager.FROM,  //index 3
                    SQLManager.TO,  //index 4
                    SQLManager.AUDITORY,  //index 5
                    SQLManager.TYPE_OF_SUBJECT // index 6
            )
            val week = ArrayList<LessonItem>()

            for (dayOfWeek in 1..7) {
                reader.getScheduleByDay(columns, dayOfWeek).use {
                    while (it.moveToNext()) {
                        week.add(getLessonItem(it, dayOfWeek))
                    }
                }
            }
            reader.close()
            return week
        }

        private fun getLessonItem(c: Cursor, dayOfWeek: Int): LessonItem {
            //see indices higher
            val from = c.getInt(3)
            val to = c.getInt(4)
            val auditory = c.getString(5)
            val subject = c.getString(1)
            val teacher = c.getString(2)
            val typeOfSubject = c.getString(6)
            val id = c.getInt(0)

            return LessonItem(dayOfWeek, from, to, teacher, auditory, subject, typeOfSubject, -1, id)
        }
    }
}
