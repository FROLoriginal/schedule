package com.example.schedule.ui.schedule;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schedule.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ScheduleViewHolder extends RecyclerView.ViewHolder {
    TextView teacher;
    TextView subject;
    TextView time;
    TextView auditoryWithStyleOfSubject;
    View firstDivider, secondDivider;
    ImageView statusCircle;
    ImageView teacherIc;

        ScheduleViewHolder(@NonNull View itemView) {
        super(itemView);
        teacher = itemView.findViewById(R.id.container_name);
        subject = itemView.findViewById(R.id.container_subject);
        time = itemView.findViewById(R.id.container_clock_from_to);
        auditoryWithStyleOfSubject = itemView.findViewById(R.id.container_auditory_with_style_of_subject);
        firstDivider = itemView.findViewById(R.id.firstTimelineDivider);
        secondDivider = itemView.findViewById(R.id.secondTimelineDivider);
        statusCircle = itemView.findViewById(R.id.lessonStatusTimelineCircle);
        teacherIc = itemView.findViewById(R.id.container_teacher_ic);

    }
}
