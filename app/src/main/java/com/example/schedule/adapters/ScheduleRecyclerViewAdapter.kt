package com.example.schedule.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.Util.LessonItemUtils
import com.example.schedule.Util.LessonStatus
import com.example.schedule.Util.TimeUtil
import com.example.schedule.ui.schedule.*
import com.example.schedule.ui.schedule.editDialog.IFragmentMovement
import com.example.schedule.ui.schedule.editDialog.ScheduleEditFragment
import java.util.*

class ScheduleRecyclerViewAdapter internal constructor(private val data: ArrayList<RecyclerViewItem>,
                                                       private val move: AdapterMovement)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ScheduleRecyclerView, ScheduleEditFragment.OnScheduleChangedListener {

    private val presenter = ScheduleFragmentPresenter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val li = LayoutInflater.from(parent.context)
        return if (viewType == LESSON) {
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
            val fr = ScheduleEditFragment.newInstance(pos, data[pos] as LessonItem)
            move.moveFromAdapter(fr,IFragmentMovement.EDIT_INTENTION)
        }
    }

    override fun onScheduleChanged(pos: Int, intention: String, model: LessonItem?) {
        if (pos != RecyclerView.NO_POSITION) {
            when (intention) {
                IFragmentMovement.EDIT_INTENTION -> {
                    presenter.changeLesson(data, pos, model!!)
                }
                IFragmentMovement.REMOVE_INTENTION -> {
                    presenter.removeItem(pos, data)
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
            lesson as HeaderItem
            bindHeader(holder, lesson, context.resources)
        } else {
            lesson as LessonItem
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
            holder.auditoryWithStyleOfSubject.text = lesson.auditoryWithTypeOfSubject

            holder.time.text = lesson.formattedTime
            holder.secondDivider.visibility = View.VISIBLE
            holder.firstDivider.visibility = View.VISIBLE
            holder.fullSideDivider.visibility = View.GONE
            (holder.statusCircle.parent as View).visibility = View.VISIBLE

            val currentLessonStatus = LessonStatus.getInstance(lesson)
            val currentLessonNum = lesson.counter + 1

            var isCurrentItemUnderHeader = false
            // We choose the first lesson of a week
            if (data[position - 1].isHeader) {
                holder.firstDivider.visibility = View.INVISIBLE

                var counter = currentLessonNum
                val isSecondDivGreen = currentLessonStatus.isOver().also { if (it) counter = 0 }

                //First divider color doesn't matter
                setColor(holder, false, isSecondDivGreen, counter, context)
                isCurrentItemUnderHeader = true
            }
            //If the lesson is the last in a day of week
            if (!LessonItemUtils.isNextLessonExist(data, position)) {
                holder.secondDivider.visibility = View.INVISIBLE
                val isFirstDividerGreen: Boolean
                var counter = currentLessonNum

                val prevLessonStatus = LessonItemUtils.getPrevLesson(data, position)?.status()
                //It means that lesson is the only one in the day of week
                if (prevLessonStatus == null) {
                    if (currentLessonStatus.isOver()) counter = 0
                    isFirstDividerGreen = false //stub

                } else isFirstDividerGreen = when {
                    currentLessonStatus.willStart() -> prevLessonStatus.isOver()
                    currentLessonStatus.inProgress() -> true
                    else -> {
                        counter = 0; true
                    } //It means inOver() = true

                }
                //Second divider color doesn't matter
                setColor(holder, isFirstDividerGreen, false, counter, context)

                //We cannot do the code below if the current element is the last or the first in the day
            } else if (!isCurrentItemUnderHeader) {
                if ((data[position] == data[position - 1]) && (data[position] == data[position + 1])) {

                    setOptionalLessonHolder(holder)

                    val isFullSideDividerGreen = !currentLessonStatus.willStart()
                    holder.fullSideDivider.setBackgroundColor(
                            if (isFullSideDividerGreen) getGreen(context) else getBlue(context))

                } else {

                    var counter = currentLessonNum
                    val isFirstDividerGreen: Boolean
                    val isSecondDividerGreen: Boolean
                    val prevLessonStatus = LessonItemUtils.getPrevLesson(data, position)!!.status()

                    when {
                        currentLessonStatus.willStart() -> {
                            isFirstDividerGreen = prevLessonStatus.isOver()
                            isSecondDividerGreen = false
                        }
                        currentLessonStatus.inProgress() -> {
                            isFirstDividerGreen = true
                            isSecondDividerGreen = false
                        }
                        //isOver() = true
                        else -> {
                            isFirstDividerGreen = true
                            isSecondDividerGreen = true
                            counter = 0
                        }

                    }
                    setColor(holder, isFirstDividerGreen, isSecondDividerGreen, counter, context)

                }
            }
        }
    }


    private fun setOptionalLessonHolder(holder: ScheduleViewHolder) {
        (holder.statusCircle.parent as View).visibility = View.GONE
        holder.firstDivider.visibility = View.GONE
        holder.secondDivider.visibility = View.GONE
        holder.fullSideDivider.visibility = View.VISIBLE
    }


    private fun bindHeader(holder: RecyclerView.ViewHolder,
                           lesson: HeaderItem,
                           resources: Resources) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_WEEK] = TimeUtil.EUDayOfWeekToUS(lesson.dayOfWeek)
        var dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val date = calendar[Calendar.DATE]
        dayOfWeek = if (calendar[Calendar.DAY_OF_WEEK] == day) {
            resources.getString(R.string.today_ru)
        } else dayOfWeek!!.capitalize()
        (holder as ScheduleHeaderViewHolder).dayOfWeek.text = "$dayOfWeek, $date $month"
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isHeader) HEADER else LESSON
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //The default values means that field doesn't matter
    private fun setColor(holder: ScheduleViewHolder,
                         isFirstDividerGreen: Boolean,
                         isSecondDividerGreen: Boolean,
                         num: Int, context: Context) {


        if (num == 0) holder.statusCircle.setBackgroundColor(getGreen(context))
        val DRAWABLES_RES = intArrayOf(R.drawable.check_ic,
                R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three,
                R.drawable.ic_four, R.drawable.ic_five, R.drawable.ic_six,
                R.drawable.ic_seven, R.drawable.ic_eight, R.drawable.ic_nine)
        holder.statusCircle.setImageResource(DRAWABLES_RES[num])
        holder.firstDivider.setBackgroundColor(if (isFirstDividerGreen) getGreen(context) else getBlue(context))
        holder.secondDivider.setBackgroundColor(if (isSecondDividerGreen) getGreen(context) else getBlue(context))

    }

    interface AdapterMovement{
        fun moveFromAdapter(to: Fragment, intention: String)
    }

    companion object {
        private fun getGreen(context: Context) = ContextCompat.getColor(context, R.color.green)
        private fun getBlue(context: Context) = ContextCompat.getColor(context, R.color.blue)

        private const val LESSON = 1
        private const val HEADER = 2
        private val day = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
    }

}
