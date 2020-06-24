package com.example.schedule.ui.schedule;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.ScheduleConstants;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        if (viewType == TYPE_ONE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

            return new ScheduleViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_header_card_view, parent, false);

            return new ScheduleHeaderViewHolder(v);
        }
    }

    private static final int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SimplifiedScheduleModel lesson = data.get(position);
        Resources resources = holder.itemView.getResources();

        if (holder.getItemViewType() == TYPE_TWO) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.DAY_OF_WEEK, Utils.Time.convertDayOfWeekToUS(lesson.getDayOfWeek()));
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            int date = calendar.get(Calendar.DATE);
            String displayedDate;

            if (calendar.get(Calendar.DAY_OF_WEEK) == day) {
                dayOfWeek = resources.getString(R.string.today_ru);
            } else dayOfWeek = Utils.toUpperCaseFirstLetter(dayOfWeek);

            displayedDate = dayOfWeek + ", " + date + " " + month;
            ((ScheduleHeaderViewHolder) holder).dayOfWeek.setText(displayedDate);

        } else {
            ScheduleViewHolder casted = (ScheduleViewHolder) holder;
            if (!ScheduleConstants.Type.ACTIVITY.equals(lesson.getTypeOfSubject())) {
                casted.teacherIc.setVisibility(View.VISIBLE);
                casted.teacher.setVisibility(View.VISIBLE);
                casted.auditoryWithStyleOfSubject.setVisibility(View.VISIBLE);
                casted.teacher.setText(lesson.getTeacher());
                casted.subject.setText(Utils.deleteTypeOfSubjectPart(lesson.getSubject()));
                casted.auditoryWithStyleOfSubject.setText(lesson.getAuditoryWithStyleOfSubject());
            } else{
                casted.teacherIc.setVisibility(View.INVISIBLE);
                casted.teacher.setVisibility(View.INVISIBLE);
                casted.auditoryWithStyleOfSubject.setVisibility(View.INVISIBLE);
                casted.subject.setText("Обед");
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

            int bol = Utils.Time.isCurrentTimeBetween(lesson.getFrom(), lesson.getTo(),
                    lesson.getDayOfWeek());

            int currentLesson = lesson.getCounter() + 1;
            if (bol == Utils.Time.CURRENT_TIME_LESS) {
                int colorRes = resources.getColor(R.color.lesson_is_not_started);

                setColor(casted, colorRes, colorRes, colorRes, currentLesson);
            } else if (bol == Utils.Time.CURRENT_TIME_BETWEEN) {
                int colorRes1 = resources.getColor(R.color.end_of_lesson_timeline);
                int colorRes2 = resources.getColor(R.color.lesson_is_not_started);
                setColor(casted, colorRes1, colorRes2, colorRes2, currentLesson);

            } else if (bol == Utils.Time.CURRENT_TIME_MORE) {
                int colorRes = resources.getColor(R.color.end_of_lesson_timeline);
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
