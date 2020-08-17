package com.example.schedule.SQL

import android.database.Cursor
import com.example.schedule.ui.schedule.SimpleScheduleModel

class SQLDataTranslator {

    companion object {
        internal fun getListSimpleScheduleModel(reader: SQLScheduleReader):
                MutableList<SimpleScheduleModel> {

            val columns = arrayOf(
                    SQLManager.ID,  //index 0
                    SQLManager.SUBJECT,  //index 1
                    SQLManager.TEACHER,  //index 2
                    SQLManager.FROM,  //index 3
                    SQLManager.TO,  //index 4
                    SQLManager.AUDITORY,  //index 5
                    SQLManager.COUNTER,  //index 6
                    SQLManager.TYPE_OF_SUBJECT,  // index 7
                    SQLManager.OPTIONALLY // index 8
            )
            val week: MutableList<SimpleScheduleModel> = ArrayList()
            //TODO
            val weekStatus = SQLManager.NUMERATOR
            val isNumerator = weekStatus == SQLManager.NUMERATOR

            for (dayOfWeek in 1..6) {
                val c: Cursor = reader.getScheduleByDay(columns, dayOfWeek, weekStatus)
                val s = SimpleScheduleModel()
                s.dayOfWeek = dayOfWeek
                s.counter = -1
                s.isHeader = true
                SimpleScheduleModel.isNumerator = isNumerator
                week.add(s)
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
            model.from = c.getString(3)
            model.to = c.getString(4)
            model.auditory = c.getString(5)
            model.subject = c.getString(1)
            model.teacher = c.getString(2)
            model.counter = c.getInt(6)
            model.typeOfSubject = c.getString(7)
            model.dayOfWeek = dayOfWeek
            model.setOptionally(c.getInt(8))
            model.id = c.getInt(0)
            return SimpleScheduleModel(model)

        }
    }
}
