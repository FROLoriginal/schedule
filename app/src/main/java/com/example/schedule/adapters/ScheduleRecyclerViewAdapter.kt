package com.example.schedule.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.Util.Time
import com.example.schedule.Util.Time.Companion.EUDayOfWeekToUS
import com.example.schedule.Util.Time.Companion.lessonStatus
import com.example.schedule.ui.schedule.*
import com.example.schedule.viewModel.SimpleScheduleModel
import com.example.schedule.viewModel.SimpleScheduleModel.Companion.getNextLesson
import java.util.*

class ScheduleRecyclerViewAdapter internal constructor(private val data: MutableList<SimpleScheduleModel>,
                                                       private val move: IFragmentMovement)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ScheduleRecyclerView, OnScheduleChangedListener {

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
            val fr = ScheduleEditFragment(this, data[pos], pos)
            move.onMove(fr, IFragmentMovement.EDIT_INTENTION)
        }
    }

    override fun onScheduleIsChanged(pos: Int, intention: String, lesson: SimpleScheduleModel?) {
        if (pos != RecyclerView.NO_POSITION) {
            when (intention) {
                IFragmentMovement.EDIT_INTENTION -> {
                    presenter.changeLesson(data, pos, lesson!!)
                }
                IFragmentMovement.REMOVE_INTENTION -> {
                    presenter.removeLesson(pos, data)
                }
            }
        }
    }

    override fun onItemAdded() {
        //This fun is already implemented in ScheduleFragment
    }

    override fun onItemRemoved(pos: Int) {
        notifyDataSetChanged()
    }

    override fun onItemChanged() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val lesson = data[position]
        val context = holder.itemView.context
        if (holder.itemViewType == HEADER) {
            bindHeader(holder, lesson, context.resources)
        } else {
            holder as ScheduleViewHolder

            if (lesson.teacher.isNullOrEmpty()) {
                holder.teacherIc.visibility = View.INVISIBLE
                holder.teacher.visibility = View.INVISIBLE
            } else {
                holder.teacherIc.visibility = View.VISIBLE
                holder.teacher.visibility = View.VISIBLE
                holder.teacher.text = lesson.teacher
            }
            holder.subject.text = lesson.subject
            holder.auditoryWithStyleOfSubject.visibility = View.VISIBLE
            holder.auditoryWithStyleOfSubject.text = lesson.auditoryWithStyleOfSubject

            holder.time.text = lesson.formattedTime
            holder.secondDivider.visibility = View.VISIBLE
            holder.firstDivider.visibility = View.VISIBLE
            holder.fullSideDivider.visibility = View.GONE
            (holder.statusCircle.parent as View).visibility = View.VISIBLE

            if (data[position - 1].isHeader) holder.firstDivider.visibility = View.INVISIBLE
            if (position + 1 < itemCount) {
                if (data[position + 1].isHeader) holder.secondDivider.visibility = View.INVISIBLE
            } else holder.secondDivider.visibility = View.INVISIBLE

            val nextLesStat: Int =
                    if (SimpleScheduleModel.isNextLessonExists(data, position)) {
                        val nextLes = getNextLesson(data, position)
                        lessonStatus(
                                Time(nextLes.from),
                                Time(nextLes.to),
                                nextLes.dayOfWeek)
                    } else Time.LESSON_IS_NOT_EXISTS

            val curLesStat = lessonStatus(
                    Time(lesson.from),
                    Time(lesson.to),
                    lesson.dayOfWeek)
            val currentLesson = lesson.counter + 1

            if (isOptLesHeader(position) || !lesson.isOptionally() || position == itemCount - 1) {
                val colorFirstOpt: Int
                //Сверху и снизу идентификаторы синие. Урок не начат
                if (curLesStat == Time.LESSON_WILL_START) {
                    colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    setColor(holder, colorFirstOpt, colorFirstOpt, colorFirstOpt, currentLesson)
                    //Сверху и снизу идентификаторы зеленые. Урок закончен
                } else if (curLesStat == Time.LESSON_IS_OVER &&
                        nextLesStat != Time.LESSON_WILL_START) {
                    colorFirstOpt = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    setColor(holder, colorFirstOpt, colorFirstOpt, colorFirstOpt, 0)
                    //Сверху зеленое, урон не начат, снизу синее
                } else if (curLesStat == Time.LESSON_IS_NOT_OVER) {
                    val colorRes1 = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    setColor(holder, colorRes1, colorFirstOpt, colorFirstOpt, currentLesson)
                } else {
                    //Сверху зеленое, урок закончен, снизу синее
                    val colorRes1 = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    setColor(holder, colorRes1, colorFirstOpt, colorRes1, 0)
                }
                if (nextLesStat == Time.LESSON_IS_NOT_EXISTS) {
                    holder.secondDivider.visibility = View.INVISIBLE
                }
            } else {
                (holder.statusCircle.parent as View).visibility = View.GONE
                holder.firstDivider.visibility = View.GONE
                holder.secondDivider.visibility = View.GONE
                holder.fullSideDivider.visibility = View.VISIBLE
                if (nextLesStat == Time.LESSON_IS_OVER || nextLesStat == Time.LESSON_IS_NOT_OVER) {
                    val colorRes1 = ContextCompat.getColor(context, R.color.end_of_lesson_timeline)
                    holder.fullSideDivider.setBackgroundColor(colorRes1)
                } else {
                    val colorFirstOpt = ContextCompat.getColor(context, R.color.lesson_is_not_started)
                    holder.fullSideDivider.setBackgroundColor(colorFirstOpt)
                }
            }
        }
    }

    private fun isOptLesHeader(position: Int) = if (position > 0) {
        data[position - 1] != data[position]
    } else false


    private fun bindHeader(holder: RecyclerView.ViewHolder,
                           lesson: SimpleScheduleModel,
                           resources: Resources) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_WEEK] = EUDayOfWeekToUS(lesson.dayOfWeek)
        var dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val date = calendar[Calendar.DATE]
        dayOfWeek = if (calendar[Calendar.DAY_OF_WEEK] == day) {
            resources.getString(R.string.today_ru)
        } else dayOfWeek!!.capitalize(Locale.getDefault())
        (holder as ScheduleHeaderViewHolder).dayOfWeek.text = "$dayOfWeek, $date $month"
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isHeader) HEADER else INFORMATION
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun setColor(holder: ScheduleViewHolder, firstDividerRes: Int, secondDividerRes: Int, circleRes: Int, num: Int) {
        if (num == 0) holder.statusCircle.setBackgroundColor(circleRes)
        val DRAWABLES_RES = intArrayOf(R.drawable.check_ic,
                R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three,
                R.drawable.ic_four, R.drawable.ic_five, R.drawable.ic_six,
                R.drawable.ic_seven, R.drawable.ic_eight, R.drawable.ic_nine)
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
