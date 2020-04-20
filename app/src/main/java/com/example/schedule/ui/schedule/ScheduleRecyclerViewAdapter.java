package com.example.schedule.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.Utils;

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
        TextView auditoryWithStyleOfSubject;
        View firstDivider, secondDivider;
        ImageView statusCircle;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher = itemView.findViewById(R.id.container_name);
            subject = itemView.findViewById(R.id.container_subject);
            time = itemView.findViewById(R.id.container_clock_from_to);
            auditoryWithStyleOfSubject = itemView.findViewById(R.id.container_auditory_with_style_of_subject);
            firstDivider = itemView.findViewById(R.id.firstTimelineDivider);
            secondDivider = itemView.findViewById(R.id.secondTimelineDivider);
            statusCircle = itemView.findViewById(R.id.lessonStatusTimelineCircle);
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

        SimplifiedScheduleModel lesson = data.get(position);
        if (holder.getItemViewType() == TYPE_TWO) {
            Calendar calendar = Calendar.getInstance();
            //First day of week is sunday => sunday + 2 = monday
            calendar.set(Calendar.DAY_OF_WEEK, lesson.getDayOfWeek() + 2);
            ((ScheduleHeaderViewHolder) holder).dayOfWeek.setText(
                    calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));

        } else {
            ScheduleViewHolder casted = (ScheduleViewHolder) holder;
            casted.teacher.setText(lesson.getTeacher());
            casted.subject.setText(Utils.deleteTypeOfSubjectPart(lesson.getSubject()));
            casted.time.setText(lesson.getFormattedTime());
            casted.auditoryWithStyleOfSubject.setText(lesson.getAuditoryWithStyleOfSubject());
            if (position + 1 < getItemCount()) {
                casted.secondDivider.setVisibility(View.VISIBLE);
                casted.firstDivider.setVisibility(View.VISIBLE);
                if (data.get(position + 1).isHeader())
                    casted.secondDivider.setVisibility(View.INVISIBLE);
                if (data.get(position - 1).isHeader())
                    casted.firstDivider.setVisibility(View.INVISIBLE);
                //todo day of month
                Calendar calendar = Calendar.getInstance();
                int bol = Utils.Time.isCurrentTimeBetween(lesson.getFrom(), lesson.getTo(),lesson.getDayOfWeek(), calendar);

                if (bol == Utils.Time.CURRENT_TIME_LESS) {
                    int colorRes = casted.statusCircle.getResources().getColor(R.color.lesson_is_not_started);

                    setColor(casted, colorRes, colorRes, colorRes);
                } else if (bol == Utils.Time.CURRENT_TIME_BETWEEN) {
                    int colorRes1 = casted.statusCircle.getResources().getColor(R.color.end_of_lesson_timeline);
                    int colorRes2 = casted.statusCircle.getResources().getColor(R.color.lesson_is_not_started);
                    setColor(casted, colorRes1, colorRes2, colorRes2);

                } else if (bol == Utils.Time.CURRENT_TIME_MORE) {
                    int colorRes = casted.statusCircle.getResources().getColor(R.color.end_of_lesson_timeline);
                    setColor(casted, colorRes, colorRes, colorRes);
                }
            } else casted.secondDivider.setVisibility(View.INVISIBLE);
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

    private void setColor(ScheduleViewHolder holder, int firstDividerRes, int secondDividerRes, int circleRes) {

        holder.firstDivider.setBackgroundColor(firstDividerRes);
        holder.secondDivider.setBackgroundColor(secondDividerRes);
        holder.statusCircle.setBackgroundColor(circleRes);
    }


}
