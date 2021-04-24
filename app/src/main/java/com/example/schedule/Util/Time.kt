package com.example.schedule.Util

data class Time(val totalMin: Int) {

    data class LessonTiming(val from: Time, val to: Time)

    init {
        if (totalMin !in 0..1440) throw IllegalArgumentException("Min $totalMin must be in 0..1440")
    }

    val hour = totalMin / 60
    val minute = totalMin % 60

}
