package com.example.schedule

import java.util.*

object Utils {
    const val OTHER = 0
    const val SEMINAR = 1
    const val LABORATORY_WORK = 2
    const val LECTURE = 3
    private const val sem = "Пр"
    private const val lab = "Лаб"
    private const val lect = "Лекц"

    @JvmStatic
    fun typeOfSubject(subjectName: String): Int {
        val sem = "Пр"
        val lab = "Лаб"
        val lect = "Лекц"
        val s = subjectName.split("\\.")
        return if (s.isNotEmpty()) {
            when (s[0]) {
                sem -> SEMINAR
                lab -> LABORATORY_WORK
                lect -> LECTURE
                else -> OTHER
            }
        } else OTHER
    }

    @JvmStatic
    fun deleteTypeOfSubjectPart(subjectName: String): String {
        val s = subjectName.split("\\.")
        return if (s.isNotEmpty()) {
            when (s[0]) {
                sem, lab, lect -> s[1].trim()
                else -> subjectName
            }
        } else subjectName
    }

    @JvmStatic
    @Throws(NullPointerException::class)
    fun toUpperCaseFirstLetter(input: String): String = input[0].toUpperCase() + input.substring(1)


    object Time {
        const val LESSON_IS_NOT_EXISTS = -1
        const val LESSON_IS_OVER = 0
        const val LESSON_IS_NOT_OVER = 1
        const val LESSON_WILL_START = 2

        @JvmStatic
        fun lessonStatus(from: String?, to: String?, dayOfWeek: Int): Int {
            return if (from != null && to != null) {
                val firstHalfFrom = Integer.valueOf(from.split(":").toTypedArray()[0])
                val secondHalfFrom = Integer.valueOf(from.split(":").toTypedArray()[1])
                val firstHalfTo = Integer.valueOf(to.split(":").toTypedArray()[0])
                val secondHalfTo = Integer.valueOf(to.split(":").toTypedArray()[1])
                val firstTime = Calendar.getInstance()
                val secondTime = Calendar.getInstance()
                firstTime[Calendar.DAY_OF_WEEK] = convertEUDayOfWeekToUS(dayOfWeek + 1) - 1
                firstTime[Calendar.HOUR_OF_DAY] = firstHalfFrom
                firstTime[Calendar.MINUTE] = secondHalfFrom
                secondTime[Calendar.DAY_OF_WEEK] = convertEUDayOfWeekToUS(dayOfWeek + 1) - 1
                secondTime[Calendar.HOUR_OF_DAY] = firstHalfTo
                secondTime[Calendar.MINUTE] = secondHalfTo
                val current = Calendar.getInstance().timeInMillis
                val second = secondTime.timeInMillis
                if (current >= firstTime.timeInMillis && current <= second) {
                    LESSON_IS_NOT_OVER
                } else if (current > second) {
                    LESSON_IS_OVER
                } else LESSON_WILL_START
            }else LESSON_IS_NOT_EXISTS
        }

        @JvmStatic
        fun convertUSDayOfWeekToEU(day: Int): Int {
            return if (day == 1) 7 else day - 1
        }

        @JvmStatic
        fun convertEUDayOfWeekToUS(day: Int): Int {
            return if (day == 7) 1 else day + 1
        }

        /*
        Timeline looks like

        (12:00)x1 - - - - - - - - (13:20)x2
                        (12:30)y1 - - - - - - - - - (14:00)y2
         */
        @JvmStatic
        fun isTimeIntersect(t1_from: String, t1_to: String,
                            t2_from: String, t2_to: String): Boolean {
            val t1_from_ = stringArrToInt(t1_from.split(":").toTypedArray())
            val t1_to_ = stringArrToInt(t1_to.split(":").toTypedArray())
            val t2_from_ = stringArrToInt(t2_from.split(":").toTypedArray())
            val t2_to_ = stringArrToInt(t2_to.split(":").toTypedArray())
            val x1 = t1_from_[0] * 60 + t1_from_[1]
            val x2 = t1_to_[0] * 60 + t1_to_[1]
            val y1 = t2_from_[0] * 60 + t2_from_[1]
            val y2 = t2_to_[0] * 60 + t2_to_[1]
            return x1 < y1 && y1 < x2 || x1 < y2 && y2 < x2 || y1 < x1 && x1 < y2 || y1 < x2 && x2 < y2
        }

        @JvmStatic
        fun timeToMinutes(time: String): Int {
            val arr = time.split(":")
            return arr[0].toInt() * 60 + arr[1].toInt()
        }

        private fun stringArrToInt(arr: Array<String>): List<Int> = arr.map { p -> p.toInt() }
    }
}
