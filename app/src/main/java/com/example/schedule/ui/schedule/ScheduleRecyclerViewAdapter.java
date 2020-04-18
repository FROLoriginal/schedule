package com.example.schedule.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedule.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

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

    class ScheduleHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeek;

        ScheduleHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeek = itemView.findViewById(R.id.day_of_week_header);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ONE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

            return new ScheduleViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_header_card_view, parent, false);

            return new ScheduleHeaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == TYPE_TWO) {
            Calendar calendar = Calendar.getInstance();
            //First day of week is sunday => sunday + 3 = monday
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, data.get(position).getDayOfWeek() + 3);
            ((ScheduleHeaderViewHolder) holder).dayOfWeek.setText(
                    calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));

        } else {
            SimplifiedScheduleModel model = data.get(position);
            if (model.getAuditory().equals("null")) model.setAuditory("неизвестно");
            if (model.getTeacher().equals("null")) model.setTeacher("неизвестно");

            ((ScheduleViewHolder) holder).teacher.setText(model.getTeacher());
            ((ScheduleViewHolder) holder).subject.setText(model.getSubject());
            ((ScheduleViewHolder) holder).time.setText(model.getFormattedTime());
            ((ScheduleViewHolder) holder).auditory.setText(model.getFormattedAuditory());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isHeader() ? TYPE_TWO : TYPE_ONE;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
