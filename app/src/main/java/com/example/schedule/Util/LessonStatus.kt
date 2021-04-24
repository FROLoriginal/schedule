package com.example.schedule.Util

import com.example.schedule.ui.schedule.LessonItem
import java.util.*

class LessonStatus private constructor(private val status: Int) {

    fun inProgress() = status == LESSON_IN_PROGRESS
    fun willStart() = status == LESSON_WILL_START
    fun isOver() = status == LESSON_IS_OVER
    fun isExist() = status == LESSON_IS_NOT_EXIST


    companion object {
        fun getInstance(lessonItem: LessonItem?): LessonStatus {
            if (lessonItem == null) return LessonStatus(LESSON_IS_NOT_EXIST)

            val dayOfWeek = lessonItem.dayOfWeek
            val from = Time(lessonItem.from)
            val to = Time(lessonItem.to)

            if (dayOfWeek !in 1..7) throw IllegalArgumentException("Day of week $dayOfWeek must be in 1..7")
            return run {
                val firstTime = Calendar.getInstance()
                val secondTime = Calendar.getInstance()
                firstTime[Calendar.DAY_OF_WEEK] = TimeUtil.EUDayOfWeekToUS(dayOfWeek + 1) - 1
                firstTime[Calendar.HOUR_OF_DAY] = from.hour
                firstTime[Calendar.MINUTE] = from.minute
                secondTime[Calendar.DAY_OF_WEEK] = TimeUtil.EUDayOfWeekToUS(dayOfWeek + 1) - 1
                secondTime[Calendar.HOUR_OF_DAY] = to.hour
                secondTime[Calendar.MINUTE] = to.minute
                val currentTime = Calendar.getInstance().timeInMillis
                val start = firstTime.timeInMillis
                val end = secondTime.timeInMillis
                when {
                    currentTime in start..end -> LessonStatus(LESSON_IN_PROGRESS)
                    currentTime > end -> LessonStatus(LESSON_IS_OVER)
                    else -> LessonStatus(LESSON_WILL_START)
                }
            }
        }

        private const val LESSON_IN_PROGRESS = 1
        private const val LESSON_WILL_START = 2
        private const val LESSON_IS_OVER = 0
        private const val LESSON_IS_NOT_EXIST = -1
    }

}
