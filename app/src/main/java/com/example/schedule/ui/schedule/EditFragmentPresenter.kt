package com.example.schedule.ui.schedule

import android.content.ContentValues
import com.example.schedule.SQL.SQLDataTranslator
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import com.example.schedule.SQL.SQLScheduleReader
import com.example.schedule.viewModel.SimpleScheduleModel

class EditFragmentPresenter(private val efv: EditFragmentView) {

    /**
     * @return {@code true} if the value changes was applied, {@code false} otherwise
     */
    fun applyChanges(lesson: SimpleScheduleModel, editor: SQLScheduleEditor): Boolean {

        val from = lesson.from
        val to = lesson.to
        val subject = lesson.subject
        val dayOfWeek = lesson.dayOfWeek
        val id = lesson.id

        if (from < 0 || to < 0 || subject!!.isEmpty() || dayOfWeek !in 1..7) {
            efv.onFieldIsNull()
            return false
        } else {
            val cv: ContentValues = ContentValues().apply {

                put(SQLManager.FROM, from)
                put(SQLManager.TO, to)
                put(SQLManager.SUBJECT, subject)
                put(SQLManager.DAY_OF_WEEK, dayOfWeek)
                put(SQLManager.TEACHER, lesson.teacher)
                put(SQLManager.AUDITORY, lesson.auditory)
                put(SQLManager.STYLE_OF_SUBJECT, lesson.styleOfSubject)

            }
            if (id != 0) editor.edit(cv, id)
            else editor.insert(cv)
            editor.close()

            return true
        }

    }


}
