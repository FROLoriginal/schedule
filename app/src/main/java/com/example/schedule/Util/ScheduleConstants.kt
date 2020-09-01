package com.example.schedule.Util

object ScheduleConstants {
    const val TEACHER = "teacher"
    const val AUDITORY = "class"
    const val SUBJECT = "subject"
    const val LABEL = "label"
    const val UNKNOWN_OBJECT = "неизвестно"

    object Type {
        const val DEFAULT = "default"
        const val ACTIVITY = "activity"
        const val CHANGING = "changing"
    }

    object Subtype {
        const val PASS = "pass"
        const val REQUIRED = "required"
        const val OPTIONALLY = "optionally"
    }
}
