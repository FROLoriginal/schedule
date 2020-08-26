package com.example.schedule.ui.schedule

import com.example.schedule.viewModel.SimpleScheduleModel

interface OnScheduleChangedListener {

    fun onScheduleIsChanged(lesson: SimpleScheduleModel,
                            pos : Int,
                            intention : String)
}
