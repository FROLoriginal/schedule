package com.example.schedule.ui.schedule

interface ScheduleRecyclerView {

    fun onItemRemoved(pos : Int)
    fun onItemAdded()
    fun onItemChanged(pos : Int)
}
