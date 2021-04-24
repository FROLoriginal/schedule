package com.example.schedule.ui.schedule.editDialog

import android.content.ContentValues
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleEditor
import com.example.schedule.ui.schedule.LessonItem

class EditFragmentPresenter(private val efv: EditFragmentView,
                            private val editor: SQLScheduleEditor) {

    /**
     * @return {@code true} if the value changes was applied, {@code false} otherwise
     */
    fun applyChanges(lesson: LessonItem): Boolean {

        val from = lesson.from
        val to = lesson.to
        val subject = lesson.subject
        val dayOfWeek = lesson.dayOfWeek
        val id = lesson.id

        if (from !in 0..1440 || to !in 0..1440 || subject!!.isEmpty() || dayOfWeek !in 1..7) {
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
                put(SQLManager.TYPE_OF_SUBJECT, lesson.typeOfSubject)

            }
            editor.use {
                if (id != 0) it.edit(cv, id)
                else it.insert(cv)
            }
            return true
        }

    }

    fun removeLesson(id: Int) {
        editor.use { it.remove(id) }
    }


}
