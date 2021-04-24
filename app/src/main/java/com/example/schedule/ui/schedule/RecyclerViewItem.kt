package com.example.schedule.ui.schedule

abstract class RecyclerViewItem {
    abstract var isHeader: Boolean
    //Day of week starts at 1
    abstract var dayOfWeek: Int
}
