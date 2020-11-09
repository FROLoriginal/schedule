package com.example.schedule.ui.schedule

import com.example.schedule.viewModel.SimpleScheduleModel

interface OnScheduleChangedListener {

    fun onScheduleIsChanged(pos : Int,
                            intention : String,
                            lesson: SimpleScheduleModel? = null)
}
