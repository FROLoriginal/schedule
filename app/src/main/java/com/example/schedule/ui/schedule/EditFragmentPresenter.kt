package com.example.schedule.ui.schedule

import android.content.ContentValues
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import com.example.schedule.viewModel.SimpleScheduleModel

class EditFragmentPresenter(private val efv: EditFragmentView) {

    fun applyChanges(lesson: SimpleScheduleModel, editor: SQLScheduleEditor) {

        val from = lesson.from
        val to = lesson.to
        val subject = lesson.subject
        val dayOfWeek = lesson.dayOfWeek
        val id = lesson.id

        if (from!!.isEmpty() || to!!.isEmpty()
                || subject!!.isEmpty() || dayOfWeek !in 1..6) efv.onFieldIsNull()
        else {
            val cv: ContentValues = ContentValues().apply {
                put(SQLManager.FROM, from)
                put(SQLManager.TO, to)
                put(SQLManager.SUBJECT, subject)
                put(SQLManager.DAY_OF_WEEK, dayOfWeek)

                put(SQLManager.TEACHER, if (lesson.teacher!!.isEmpty()) "null" else lesson.teacher)
                put(SQLManager.AUDITORY, if (lesson.auditory!!.isEmpty()) "null" else lesson.auditory)
                put(SQLManager.STYLE_OF_SUBJECT, if (lesson.styleOfSubject.isEmpty()) "null" else lesson.styleOfSubject)

            }
            editor.edit(cv, id)

        }

    }


}
