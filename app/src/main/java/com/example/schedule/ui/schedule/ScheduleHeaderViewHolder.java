package com.example.schedule.ui.schedule;

import android.view.View;
import android.widget.TextView;

import com.example.schedule.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ScheduleHeaderViewHolder extends RecyclerView.ViewHolder {
    TextView dayOfWeek;

    ScheduleHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        dayOfWeek = itemView.findViewById(R.id.day_of_week_header);
    }
}
