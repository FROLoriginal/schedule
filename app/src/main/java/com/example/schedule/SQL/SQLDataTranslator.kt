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
                    SQLManager.PREFIX_OF_SUBJECT // index 7
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
            //see indices higher
            return SimpleScheduleModel().apply {
                from = c.getInt(3)
                to = c.getInt(4)
                auditory = c.getString(5)
                subject = c.getString(1)
                teacher = c.getString(2)
                setOptionally(c.getInt(6) == 1)
                prefixOfSubject = c.getString(7)
                id = c.getInt(0)
                this.dayOfWeek = dayOfWeek
            }
        }
    }
}
