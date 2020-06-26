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

    private static final int INFORMATION = 1;
    private static final int HEADER = 2;

    private List<SimpleScheduleModel> data;

    ScheduleRecyclerViewAdapter(List<SimpleScheduleModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(parent.getContext());
        if (viewType == INFORMATION) {
            return new ScheduleViewHolder(
                    li.inflate(R.layout.card_view, parent, false));
        } else {
            return new ScheduleHeaderViewHolder(
                    li.inflate(R.layout.schedule_header_card_view, parent, false));
        }
    }

    private static final int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SimpleScheduleModel lesson = data.get(position);
        Resources resources = holder.itemView.getResources();

        if (holder.getItemViewType() == HEADER) {
            bindHeader(holder, lesson, resources);

        } else {
            ScheduleViewHolder casted = (ScheduleViewHolder) holder;
            if (!ScheduleConstants.Type.ACTIVITY.equals(lesson.getTypeOfSubject())) {
                casted.teacherIc.setVisibility(View.VISIBLE);
                casted.teacher.setVisibility(View.VISIBLE);
                casted.auditoryWithStyleOfSubject.setVisibility(View.VISIBLE);
                casted.teacher.setText(lesson.getTeacher());
                casted.subject.setText(Utils.deleteTypeOfSubjectPart(lesson.getSubject()));
                casted.auditoryWithStyleOfSubject.setText(lesson.getAuditoryWithStyleOfSubject());
            } else bindDinnerEl(casted);

            casted.time.setText(lesson.getFormattedTime());
            casted.secondDivider.setVisibility(View.VISIBLE);
            casted.firstDivider.setVisibility(View.VISIBLE);
            casted.fullSideDivider.setVisibility(View.GONE);
            ((View) casted.statusCircle.getParent()).setVisibility(View.VISIBLE);

            if (position + 1 < getItemCount()) {
                if (data.get(position + 1).isHeader())
                    casted.secondDivider.setVisibility(View.INVISIBLE);
                if (data.get(position - 1).isHeader())
                    casted.firstDivider.setVisibility(View.INVISIBLE);
            } else casted.secondDivider.setVisibility(View.INVISIBLE);

            SimpleScheduleModel nextLes = getNextLesson(lesson, position);

            int nextLesStat = Utils.Time.lessonStatus(
                    nextLes.getFrom(),
                    nextLes.getTo(),
                    nextLes.getDayOfWeek());
            int curLesStat = Utils.Time.lessonStatus(
                    lesson.getFrom(),
                    lesson.getTo(),
                    lesson.getDayOfWeek());

            int currentLesson = lesson.getCounter() + 1;

            if (isOptLesHeader(position) || !lesson.isOptionally()) {
                int colorFirstOpt;
                //Сверху и снизу идентификаторы синие. Урок не начат
                if (curLesStat == Utils.Time.LESSON_WILL_START) {
                    colorFirstOpt = resources.getColor(R.color.lesson_is_not_started);
                    setColor(casted, colorFirstOpt, colorFirstOpt, colorFirstOpt, currentLesson);
                    //Сверху и снизу идентификаторы зеленые. Урок закончен
                } else if (curLesStat == Utils.Time.LESSON_IS_OVER &&
                        (nextLesStat != Utils.Time.LESSON_WILL_START)) {
                    colorFirstOpt = resources.getColor(R.color.end_of_lesson_timeline);
                    setColor(casted, colorFirstOpt, colorFirstOpt, colorFirstOpt, 0);
                    //Сверху зеленое, урон не начат, снизу синее
                } else if (curLesStat == Utils.Time.LESSON_IS_NOT_OVER) {
                    int colorRes1 = resources.getColor(R.color.end_of_lesson_timeline);
                    colorFirstOpt = resources.getColor(R.color.lesson_is_not_started);
                    setColor(casted, colorRes1, colorFirstOpt, colorFirstOpt, currentLesson);
                } else {
                    //Сверху зеленое, урок закончен, снизу синее
                    int colorRes1 = resources.getColor(R.color.end_of_lesson_timeline);
                    colorFirstOpt = resources.getColor(R.color.lesson_is_not_started);
                    setColor(casted, colorRes1, colorFirstOpt, colorRes1, 0);
                }
            } else {

                ((View) casted.statusCircle.getParent()).setVisibility(View.GONE);
                casted.firstDivider.setVisibility(View.GONE);
                casted.secondDivider.setVisibility(View.GONE);
                casted.fullSideDivider.setVisibility(View.VISIBLE);

                if (nextLesStat == Utils.Time.LESSON_IS_OVER || nextLesStat == Utils.Time.LESSON_IS_NOT_OVER) {
                    int colorRes1 = resources.getColor(R.color.end_of_lesson_timeline);
                    casted.fullSideDivider.setBackgroundColor(colorRes1);
                } else {
                    int colorFirstOpt = resources.getColor(R.color.lesson_is_not_started);
                    casted.fullSideDivider.setBackgroundColor(colorFirstOpt);
                }
            }
        }
    }

    private SimpleScheduleModel getNextLesson(SimpleScheduleModel currentLesson, int position) {
        SimpleScheduleModel nextLes;
        int i = 0;
        do {
            ++i;
            nextLes = position + i < data.size()
                    ? data.get(position + i) :
                    new SimpleScheduleModel();

        } while (SimpleScheduleModel.equals(currentLesson, nextLes));
        return nextLes;
    }

    private boolean isOptLesHeader(int position) {
        if (position > 0) {
            return !SimpleScheduleModel.equals(data.get(position - 1), data.get(position));
        }
        return false;
    }


    private void bindHeader(RecyclerView.ViewHolder holder,
                            SimpleScheduleModel lesson, Resources resources) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Utils.Time.convertEUDayOfWeekToUS(lesson.getDayOfWeek()));
        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int date = calendar.get(Calendar.DATE);
        String displayedDate;

        if (calendar.get(Calendar.DAY_OF_WEEK) == day) {
            dayOfWeek = resources.getString(R.string.today_ru);
        } else dayOfWeek = Utils.toUpperCaseFirstLetter(dayOfWeek);

        displayedDate = dayOfWeek + ", " + date + " " + month;
        ((ScheduleHeaderViewHolder) holder).dayOfWeek.setText(displayedDate);

    }

    private void bindDinnerEl(ScheduleViewHolder casted) {
        casted.teacherIc.setVisibility(View.INVISIBLE);
        casted.teacher.setVisibility(View.INVISIBLE);
        casted.auditoryWithStyleOfSubject.setVisibility(View.INVISIBLE);
        casted.subject.setText("Обед");
    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position).isHeader() ? HEADER : INFORMATION;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void setColor(ScheduleViewHolder holder, int firstDividerRes, int secondDividerRes, int circleRes, int num) {

        final int[] DRAWABLES_RES = new int[]{R.drawable.check_ic,
                R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three,
                R.drawable.ic_four, R.drawable.ic_five, R.drawable.ic_six,
                R.drawable.ic_seven, R.drawable.ic_eight, R.drawable.ic_nine};

        if (num == 0) holder.statusCircle.setBackgroundColor(circleRes);
        holder.statusCircle.setImageResource(DRAWABLES_RES[num]);
        holder.firstDivider.setBackgroundColor(firstDividerRes);
        holder.secondDivider.setBackgroundColor(secondDividerRes);
    }

}
