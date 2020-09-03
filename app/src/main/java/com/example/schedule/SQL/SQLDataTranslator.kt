package com.example.schedule.SQL

import android.database.Cursor
import com.example.schedule.viewModel.SimpleScheduleModel

class SQLDataTranslator {

    companion object {
        internal fun getRawListSimpleScheduleModel(reader: SQLScheduleReader):
                MutableList<SimpleScheduleModel> {

            val columns = arrayOf(
                    SQLManager.ID,  //index 0
                    SQLManager.SUBJECT,  //index 1
                    SQLManager.TEACHER,  //index 2
                    SQLManager.FROM,  //index 3
                    SQLManager.TO,  //index 4
                    SQLManager.AUDITORY,  //index 5
                    SQLManager.OPTIONALLY, // index 6
                    SQLManager.STYLE_OF_SUBJECT // index 7
            )
            val week: MutableList<SimpleScheduleModel> = ArrayList()

            for (dayOfWeek in 1..7) {
                val c: Cursor = reader.getScheduleByDay(columns, dayOfWeek)
                while (c.moveToNext()) {
                    week.add(getSimpleScheduleModel(c, dayOfWeek))
                }
                c.close()
            }
            reader.close()
            return week
        }

        private fun getSimpleScheduleModel(c: Cursor, dayOfWeek: Int): SimpleScheduleModel {

            val model = SimpleScheduleModel()
            model.from = c.getInt(3)
            model.to = c.getInt(4)
            model.auditory = c.getString(5)
            model.subject = c.getString(1)
            model.teacher = c.getString(2)
            model.dayOfWeek = dayOfWeek
            model.setOptionally(c.getInt(6))
            model.styleOfSubject = c.getString(7)
            model.id = c.getInt(0)
            return SimpleScheduleModel(model)

        }

        fun getFreeId(reader: SQLScheduleReader): Int {

            val cursor: Cursor = reader.getAllIdsNotes()
            cursor.moveToLast()
            val freeId = cursor.getInt(0)
            cursor.close()
            return freeId + 1

        }

        fun isIdExists(id: Int, reader: SQLScheduleReader): Boolean {

            val cursor: Cursor = reader.isIdExists(id)
            while (cursor.moveToNext())
                if (cursor.getInt(0) == 1)
                    return true
            cursor.close()
            return false
        }


    }
}
