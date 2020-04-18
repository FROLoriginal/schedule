package com.example.schedule.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedule.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ScheduleViewHolder> {

    private List<SimplifiedScheduleModel> data;

    ScheduleRecyclerViewAdapter(List<SimplifiedScheduleModel> data) {
        this.data = data;
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView teacher;
        TextView subject;
        TextView time;
        TextView auditory;
        View itemView;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher = itemView.findViewById(R.id.container_name);
            subject = itemView.findViewById(R.id.container_subject);
            time = itemView.findViewById(R.id.container_clock_from_to);
            auditory = itemView.findViewById(R.id.container_auditory);
            this.itemView = itemView;
        }
    }

    @NonNull
    @Override
    public ScheduleRecyclerViewAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        return new ScheduleRecyclerViewAdapter.ScheduleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleRecyclerViewAdapter.ScheduleViewHolder holder, int position) {

        SimplifiedScheduleModel model = data.get(position);
        holder.teacher.setText(model.getTeacher());
        holder.subject.setText(model.getSubject());
        holder.time.setText(model.getFrom() + " - " + model.getTo());
        holder.auditory.setText("Аудитория: " + model.getAuditory());

        }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
