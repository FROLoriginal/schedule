package com.example.schedule.ui.schedule

interface OnScheduleChangedListener {

    fun onScheduleIsChanged(pos : Int,
                            intention : String,
                            lesson: LessonItem? = null)
}
