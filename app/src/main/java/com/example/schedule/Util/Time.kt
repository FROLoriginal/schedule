package com.example.schedule.Util

import java.util.*

class Time(val totalMin: Int) {

    class Lesson(val from: Time, val to: Time)

    init {
        if (totalMin !in 0..1440) throw IllegalArgumentException("Min $totalMin must be in 0..1440")
    }

    val hour = totalMin / 60
    val minutes = totalMin % 60

    companion object {
        fun lessonStatus(from: Time, to: Time, dayOfWeek: Int): Int {
            if (dayOfWeek !in 1..7) return LESSON_IS_NOT_EXISTS
            return run {
                val firstTime = Calendar.getInstance()
                val secondTime = Calendar.getInstance()
                firstTime[Calendar.DAY_OF_WEEK] = EUDayOfWeekToUS(dayOfWeek + 1) - 1
                firstTime[Calendar.HOUR_OF_DAY] = from.hour
                firstTime[Calendar.MINUTE] = from.minutes
                secondTime[Calendar.DAY_OF_WEEK] = EUDayOfWeekToUS(dayOfWeek + 1) - 1
                secondTime[Calendar.HOUR_OF_DAY] = to.hour
                secondTime[Calendar.MINUTE] = to.minutes
                val current = Calendar.getInstance().timeInMillis
                val second = secondTime.timeInMillis
                if (current >= firstTime.timeInMillis && current <= second) {
                    LESSON_IS_NOT_OVER
                } else if (current > second) {
                    LESSON_IS_OVER
                } else LESSON_WILL_START
            }
        }

        fun USDayOfWeekToEU(day: Int): Int {
            if (day !in 1..7) throw IllegalArgumentException("Day $day must be in 1..7")
            return if (day == 1) 7 else day - 1
        }

        fun EUDayOfWeekToUS(day: Int): Int {
            if (day !in 1..7) throw IllegalArgumentException("Day $day must be in 1..7")
            return if (day == 7) 1 else day + 1
        }

        fun minutesToDisplayedTime(min: Int): String {
            //24 * 60 = 1440
            if (min !in 0..1440) throw NumberFormatException("Minutes $min must be in 0..1440")
            val time = Time(min)
            return String.format("%02d:%02d", time.hour, time.minutes)
        }

        fun displayedTimeToTime(time: String): Time {

            if (isDisplayedTimeCorrect(time))
                throw IllegalArgumentException("Time $time must meet time pattern like tt:mm")
            else {
                val timeArr = time.split(':').map { it.toInt() }
                return Time(timeArr[0]*60 + timeArr[1])
            }

        }

        private fun isDisplayedTimeCorrect(time: String): Boolean {
            val pattern = ':'
            val timeArr = time.split(pattern).map { it.toInt() }
            return if (time.length !in 4..5) false
            else if (time[2] != pattern || time[1] != pattern) false
            else if (time.length > 1) false
            else if (timeArr[0] !in 0..24 && timeArr[1] !in 0..60)
                false
            else return true
        }

        fun strDayOfWeekToEUNum(dayOfWeek: String): Int {

            return when (dayOfWeek) {
                "Понедельник" -> 1
                "Вторник" -> 2
                "Среда" -> 3
                "Четверг" -> 4
                "Пятница" -> 5
                "Суббота" -> 6
                "Воскресенье" -> 7
                else -> throw IllegalArgumentException("DayOFWeek $dayOfWeek must be in 1..7")

            }
        }

        /*
        Timeline looks like
        (12:00)x1 - - - - -  - (13:20)x2
                    (12:30)y1 - - - - - - (14:00)y2
         */
        fun isTimeIntersect(l1: Lesson, l2: Lesson): Boolean {
            val x1 = l1.from.totalMin
            val x2 = l1.to.totalMin
            val y1 = l2.from.totalMin
            val y2 = l2.to.totalMin
            return y1 in x1..x2 || y2 in x1..x2 || x1 in y1..y2 || x2 in y1..y2
        }

        const val LESSON_IS_NOT_OVER = 1
        const val LESSON_WILL_START = 2
        const val LESSON_IS_OVER = 0
        const val LESSON_IS_NOT_EXISTS = -1
    }
}
