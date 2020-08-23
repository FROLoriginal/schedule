package com.example.schedule.ui.schedule

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.ScheduleConstants
import com.example.schedule.Utils
import com.example.schedule.Utils.Time.Companion.convertEUDayOfWeekToUS
import com.example.schedule.Utils.Time.Companion.lessonStatus
import com.example.schedule.Utils.deleteTypeOfSubjectPart
import com.example.schedule.Utils.toUpperCaseFirstLetter
import com.example.schedule.ui.schedule.SimpleScheduleModel.Companion.equals
import com.example.schedule.ui.schedule.SimpleScheduleModel.Companion.getNextLesson
import java.util.*

class ScheduleRecyclerViewAdapter internal constructor(private val data: MutableList<SimpleScheduleModel>,
                                                       private val fragment: Fragment)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ScheduleRecyclerView {

    private val presenter = ScheduleFragmentPresenter(this)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val li = LayoutInflater.from(parent.context)
        return if (viewType == INFORMATION) {
            val view = li.inflate(R.layout.card_view, parent, false)
            val scheduleViewHolder = object : ScheduleViewHolder(view) {}
            view.setOnClickListener(getOnClickListenerForItems(scheduleViewHolder))
            scheduleViewHolder
        } else {
            ScheduleHeaderViewHolder(
                    li.inflate(R.layout.schedule_header_card_view, parent, false))
        }
    }

    private fun getOnClickListenerForItems(scheduleViewHolder: ScheduleViewHolder): View.OnClickListener? {
        return View.OnClickListener {
            val pos = scheduleViewHolder.adapterPosition
            val fr = ScheduleEditFragment(getOnClickListenerForChangingLesson(pos), data[pos])

            fragment.parentFragmentManager
                    .beginTransaction()
                    .hide(fragment)
                    .addToBackStack("editFragment")
                    .add(fragment.id, fr)
                    .show(fr)
                    .commit()
        }
    }

    private fun getOnClickListenerForChangingLesson(pos: Int): OnClickListener {
        return object : OnClickListener {
            override fun onClickToChangeLesson(lesson: SimpleScheduleModel) {
                if (pos != RecyclerView.NO_POSITION)
                    presenter.changeLesson(data, pos, lesson)
            }
        }
    }

    override fun onItemAdded() {
        notifyDataSetChanged()
    }

    override fun onItemRemoved(pos: Int) {
        notifyItemRemoved(pos)
    }

    override fun onItemChanged() {
        notifyDataSetChanged()
    }

    interface OnClickListener {

        fun onClickToChangeLesson(lesson: SimpleScheduleModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val lesson = data[position]
        val context = holder.itemView.context
        if (holder.itemViewType == HEADER) {
            bindHeader(holder, lesson, context.resources)
        } else {
            val casted = holder as ScheduleViewHolder

            if (ScheduleConstants.Type.ACTIVITY != lesson.typeOfSubject) {
                casted.teacherIc.visibility = View.VISIBLE
                casted.teacher.visibility = View.VISIBLE
                casted.auditoryWithStyleOfSubject.visibility = View.VISIBLE
                casted.teacher.text = lesson.teacher
                casted.subject.text = lesson.subject

                casted.auditoryWithStyleOfSubject.text =
                        if (lesson.styleOfSubject.isEmpty()) lesson.auditory
                        else "${lesson.styleOfSubject}, ${lesson.auditory}"

            } else bindDinnerEl(casted)

            casted.time.text = lesson.formattedTime
            casted.secondDivider.visibility = View.VISIBLE
            casted.firstDivider.visibility = View.VISIBLE
            casted.fullSideDivider.visibility = View.GONE
            (casted.statusCircle.parent as View).visibility = View.VISIBLE
            if (position + 1 < itemCount) {
                if (data[position + 1].isHeader) casted.secondDivider.visibility = View.INVISIBLE
                if (data[position - 1].isHeader) casted.firstDivider.visibility = View.INVISIBLE
            } else casted.secondDivider.visibility = View.INVISIBLE

            val nextLes = getNextLesson(data, position)
            val nextLesStat = lessonStatus(
                    Utils.Time(nextLes.from,
                            nextLes.to),
                    nextLes.dayOfWeek)

            val curLesStat = lessonStatus(
                    Utils.Time(lesson.from,
                            lesson.to),
                    lesson.dayOfWeek)
            val currentLesson = lesson.counter + 1

            if (isOptLesHeader(position) || !lesson.isOptionally()) {
                val colorFirstOpt: Int
                //Сверху и снизу идентификаторы синие. Урок не начат
                if (curLesStat == Utils.Time.LESSON_WILL_START) {
                    colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    setColor(casted, colorFirstOpt, colorFirstOpt, colorFirstOpt, currentLesson)
                    //Сверху и снизу идентификаторы зеленые. Урок закончен
                } else if (curLesStat == Utils.Time.LESSON_IS_OVER &&
                        nextLesStat != Utils.Time.LESSON_WILL_START) {
                    colorFirstOpt = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    setColor(casted, colorFirstOpt, colorFirstOpt, colorFirstOpt, 0)
                    //Сверху зеленое, урон не начат, снизу синее
                } else if (curLesStat == Utils.Time.LESSON_IS_NOT_OVER) {
                    val colorRes1 = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    setColor(casted, colorRes1, colorFirstOpt, colorFirstOpt, currentLesson)
                } else {
                    //Сверху зеленое, урок закончен, снизу синее
                    val colorRes1 = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    setColor(casted, colorRes1, colorFirstOpt, colorRes1, 0)
                }
            } else {
                (casted.statusCircle.parent as View).visibility = View.GONE
                casted.firstDivider.visibility = View.GONE
                casted.secondDivider.visibility = View.GONE
                casted.fullSideDivider.visibility = View.VISIBLE
                if (nextLesStat == Utils.Time.LESSON_IS_OVER || nextLesStat == Utils.Time.LESSON_IS_NOT_OVER) {
                    val colorRes1 = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    casted.fullSideDivider.setBackgroundColor(colorRes1)
                } else {
                    val colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    casted.fullSideDivider.setBackgroundColor(colorFirstOpt)
                }
            }
        }
    }

    private fun isOptLesHeader(position: Int): Boolean {
        return if (position > 0) {
            !equals(data[position - 1], data[position])
        } else false
    }

    private fun bindHeader(holder: RecyclerView.ViewHolder,
                           lesson: SimpleScheduleModel, resources: Resources) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_WEEK] = convertEUDayOfWeekToUS(lesson.dayOfWeek)
        var dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val date = calendar[Calendar.DATE]
        val displayedDate: String
        dayOfWeek = if (calendar[Calendar.DAY_OF_WEEK] == day) {
            resources.getString(R.string.today_ru)
        } else toUpperCaseFirstLetter(dayOfWeek)
        displayedDate = "$dayOfWeek, $date $month"
        (holder as ScheduleHeaderViewHolder).dayOfWeek.text = displayedDate
    }

    private fun bindDinnerEl(casted: ScheduleViewHolder) {
        casted.teacherIc.visibility = View.INVISIBLE
        casted.teacher.visibility = View.INVISIBLE
        casted.auditoryWithStyleOfSubject.visibility = View.INVISIBLE
        casted.subject.text = "Обед"
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isHeader) HEADER else INFORMATION
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun setColor(holder: ScheduleViewHolder, firstDividerRes: Int, secondDividerRes: Int, circleRes: Int, num: Int) {
        val DRAWABLES_RES = intArrayOf(R.drawable.check_ic,
                R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three,
                R.drawable.ic_four, R.drawable.ic_five, R.drawable.ic_six,
                R.drawable.ic_seven, R.drawable.ic_eight, R.drawable.ic_nine)
        if (num == 0) holder.statusCircle.setBackgroundColor(circleRes)
        holder.statusCircle.setImageResource(DRAWABLES_RES[num])
        holder.firstDivider.setBackgroundColor(firstDividerRes)
        holder.secondDivider.setBackgroundColor(secondDividerRes)
    }

    companion object {
        private const val INFORMATION = 1
        private const val HEADER = 2
        private val day = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
    }

}
