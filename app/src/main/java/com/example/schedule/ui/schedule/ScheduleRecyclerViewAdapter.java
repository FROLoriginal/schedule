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
        ImageView showOptionallySubjects;
        ImageView teacherIc, clockIc;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher = itemView.findViewById(R.id.container_name);
            subject = itemView.findViewById(R.id.container_subject);
            time = itemView.findViewById(R.id.container_clock_from_to);
            auditoryWithStyleOfSubject = itemView.findViewById(R.id.container_auditory_with_style_of_subject);
            firstDivider = itemView.findViewById(R.id.firstTimelineDivider);
            secondDivider = itemView.findViewById(R.id.secondTimelineDivider);
            statusCircle = itemView.findViewById(R.id.lessonStatusTimelineCircle);
            showOptionallySubjects = itemView.findViewById(R.id.show_optionally_subjects);
            clockIc = itemView.findViewById(R.id.container_clock_ic);
            teacherIc = itemView.findViewById(R.id.container_teacher_ic);

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

            calendar.set(Calendar.DAY_OF_WEEK, Utils.Time.convertDayOfWeekToUS(lesson.getDayOfWeek()));
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            ((ScheduleHeaderViewHolder) holder).dayOfWeek.setText(Utils.toUpperCaseFirstLetter(dayOfWeek));

        } else {
            ScheduleViewHolder casted = (ScheduleViewHolder) holder;
            if (lesson.getIfOptionally() == null) {
                casted.teacherIc.setVisibility(View.VISIBLE);
                casted.teacher.setVisibility(View.VISIBLE);
                casted.auditoryWithStyleOfSubject.setVisibility(View.VISIBLE);
                casted.teacher.setText(lesson.getTeacher());
                casted.subject.setText(Utils.deleteTypeOfSubjectPart(lesson.getSubject()));
                casted.auditoryWithStyleOfSubject.setText(lesson.getAuditoryWithStyleOfSubject());
            }else {
                casted.showOptionallySubjects.setVisibility(View.VISIBLE);
                casted.teacherIc.setVisibility(View.INVISIBLE);
                casted.teacher.setVisibility(View.INVISIBLE);
                casted.subject.setText("Предмет по выбору");
                casted.auditoryWithStyleOfSubject.setVisibility(View.INVISIBLE);

            }
            casted.time.setText(lesson.getFormattedTime());
            casted.secondDivider.setVisibility(View.VISIBLE);
            casted.firstDivider.setVisibility(View.VISIBLE);

            if (position + 1 < getItemCount()) {
                if (data.get(position + 1).isHeader())
                    casted.secondDivider.setVisibility(View.INVISIBLE);
                if (data.get(position - 1).isHeader())
                    casted.firstDivider.setVisibility(View.INVISIBLE);
            } else casted.secondDivider.setVisibility(View.INVISIBLE);

            Calendar calendar = Calendar.getInstance();

            int bol = Utils.Time.isCurrentTimeBetween(lesson.getFrom(), lesson.getTo(), lesson.getDayOfWeek(), calendar);

            int currentLesson = lesson.getCounter() + 1;
            if (bol == Utils.Time.CURRENT_TIME_LESS) {
                int colorRes = casted.statusCircle.getResources().getColor(R.color.lesson_is_not_started);

                setColor(casted, colorRes, colorRes, colorRes, currentLesson);
            } else if (bol == Utils.Time.CURRENT_TIME_BETWEEN) {
                int colorRes1 = casted.statusCircle.getResources().getColor(R.color.end_of_lesson_timeline);
                int colorRes2 = casted.statusCircle.getResources().getColor(R.color.lesson_is_not_started);
                setColor(casted, colorRes1, colorRes2, colorRes2, currentLesson);

            } else if (bol == Utils.Time.CURRENT_TIME_MORE) {
                int colorRes = casted.statusCircle.getResources().getColor(R.color.end_of_lesson_timeline);
                setColor(casted, colorRes, colorRes, colorRes, 0);
            }
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

    private static final int[] DRAWABLES_RES = new int[]{R.drawable.check_ic,
            R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three,
            R.drawable.ic_four, R.drawable.ic_five, R.drawable.ic_six,
            R.drawable.ic_seven, R.drawable.ic_eight, R.drawable.ic_nine};

    private void setColor(ScheduleViewHolder holder, int firstDividerRes, int secondDividerRes, int circleRes, int num) {
        if (num == 0) holder.statusCircle.setBackgroundColor(circleRes);
        holder.statusCircle.setImageResource(DRAWABLES_RES[num]);
        holder.firstDivider.setBackgroundColor(firstDividerRes);
        holder.secondDivider.setBackgroundColor(secondDividerRes);
    }


}
