package com.example.schedule.ui.schedule

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R

internal open class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    val  teacher: TextView = itemView.findViewById(R.id.container_name)
    @JvmField
    val  subject: TextView = itemView.findViewById(R.id.container_subject)
    @JvmField
    val time: TextView = itemView.findViewById(R.id.container_clock_from_to)
    @JvmField
    val auditoryWithStyleOfSubject: TextView = itemView.findViewById(R.id.container_auditory_with_style_of_subject)
    @JvmField
    val firstDivider: View = itemView.findViewById(R.id.firstTimelineDivider)
    @JvmField
    val secondDivider: View = itemView.findViewById(R.id.secondTimelineDivider)
    @JvmField
    val fullSideDivider: View = itemView.findViewById(R.id.full_side_divider)
    @JvmField
    val statusCircle: ImageView = itemView.findViewById(R.id.lessonStatusTimelineCircle)
    @JvmField
    val teacherIc: ImageView = itemView.findViewById(R.id.container_teacher_ic)

}
