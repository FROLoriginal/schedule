package com.example.schedule

import java.util.*
import kotlin.jvm.Throws

object Utils {
    const val OTHER = 0
    const val SEMINAR = 1
    const val LABORATORY_WORK = 2
    const val LECTURE = 3
    private const val sem = "Пр"
    private const val lab = "Лаб"
    private const val lect = "Лекц"

    @JvmStatic
    fun deleteTypeOfSubjectPart(subjectName: String): String {
        val s = subjectName.split(".")
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


    class Time(private val from : String? , private val to : String?) {

        companion object {
            fun lessonStatus(t : Time, dayOfWeek: Int): Int {
                return if (t.from != null && t.to != null) {
                    val firstHalfFrom = t.from.split(":")[0].toInt()
                    val secondHalfFrom = t.from.split(":")[1].toInt()
                    val firstHalfTo = t.to.split(":")[0].toInt()
                    val secondHalfTo = t.to.split(":")[1].toInt()
                    val firstTime = Calendar.getInstance()
                    val secondTime = Calendar.getInstance()
                    firstTime[Calendar.DAY_OF_WEEK] = EUDayOfWeekToUS(dayOfWeek + 1) - 1
                    firstTime[Calendar.HOUR_OF_DAY] = firstHalfFrom
                    firstTime[Calendar.MINUTE] = secondHalfFrom
                    secondTime[Calendar.DAY_OF_WEEK] = EUDayOfWeekToUS(dayOfWeek + 1) - 1
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

            fun USDayOfWeekToEU(day: Int): Int {
                return if (day == 1) 7 else day - 1
            }

            fun EUDayOfWeekToUS(day: Int): Int {
                return if (day == 7) 1 else day + 1
            }
            fun strDayOfWeekToEUNum(dayOfWeek : String) : Int {

               return when(dayOfWeek){
                    "Понедельник" -> 1
                    "Вторник" -> 2
                    "Среда" -> 3
                    "Четверг" -> 4
                    "Пятница" -> 5
                    "Суббота" -> 6
                    "Воскресенье" -> 7
                    else -> -1

                }
            }

            /*
            Timeline looks like

            (12:00)x1 - - - - - - - - (13:20)x2
                            (12:30)y1 - - - - - - - - - (14:00)y2
             */

            fun isTimeIntersect(t1 : Time, t2 : Time): Boolean {
                val t1_from_ = stringArrToInt(t1.from!!.split(":").toTypedArray())
                val t1_to_ = stringArrToInt(t1.to!!.split(":").toTypedArray())
                val t2_from_ = stringArrToInt(t2.from!!.split(":").toTypedArray())
                val t2_to_ = stringArrToInt(t2.to!!.split(":").toTypedArray())
                val x1 = t1_from_[0] * 60 + t1_from_[1]
                val x2 = t1_to_[0] * 60 + t1_to_[1]
                val y1 = t2_from_[0] * 60 + t2_from_[1]
                val y2 = t2_to_[0] * 60 + t2_to_[1]
                return y1 in x1..x2 || y2 in x1..x2 || x1 in y1..y2 || x2 in y1..y2
            }


            fun timeToMinutes(time: String): Int {
                val arr = time.split(":")
                return arr[0].toInt() * 60 + arr[1].toInt()
            }

            private fun stringArrToInt(arr: Array<String>): List<Int> = arr.map { p -> p.toInt() }

            const val LESSON_IS_NOT_OVER = 1
            const val LESSON_WILL_START = 2
            const val LESSON_IS_OVER = 0
            const val LESSON_IS_NOT_EXISTS = -1
        }
    }
}
