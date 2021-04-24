package com.example.schedule.Util

import com.example.schedule.ui.schedule.LessonItem
import com.example.schedule.ui.schedule.RecyclerViewItem
import java.util.*

object LessonItemUtils {

    fun getPrevLesson(data: List<RecyclerViewItem>, position: Int): LessonItem? {

        if (position >= data.size)
            throw IndexOutOfBoundsException("Data size ${data.size} must be more then $position position")
        if (position > -1) {
            if (data[position].isHeader) throw IllegalArgumentException("Current item is Header. You can use this fun only for ${::LessonItem.name}")
            for (i in position downTo  1) {
                when {
                    data[i - 1].isHeader -> return null
                    data[position] != data[i - 1] -> return data[i - 1] as LessonItem
                }
            }
        }
        throw IllegalArgumentException("Position $position must be positive value")
    }

    fun isNextLessonExist(data: List<RecyclerViewItem>, position: Int): Boolean {
        if (position >= data.size)
            throw IndexOutOfBoundsException("Data size ${data.size} must be more then $position position")
        if (position > -1) {
            if (data[position].isHeader) throw IllegalArgumentException("Current item is Header. You can use this fun only for ${::LessonItem.name}")
            for (i in position until data.size) {
                when {
                    i == data.lastIndex -> return false
                    data[i + 1].isHeader -> return false
                    data[position] != data[i + 1] -> return true
                }
            }
        }
        throw IllegalArgumentException("Position $position must be positive value")
    }
}
