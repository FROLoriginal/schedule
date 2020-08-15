package com.example.schedule.ui.schedule

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R

internal class ScheduleHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var dayOfWeek: TextView = itemView.findViewById(R.id.day_of_week_header)

}
