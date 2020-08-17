package com.example.schedule.ui.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.SQL.SQLDataTranslator
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleReader
import com.example.schedule.Utils.Time.convertEUDayOfWeekToUS
import com.example.schedule.Utils.Time.convertUSDayOfWeekToEU
import com.example.schedule.Utils.Time.isTimeIntersect
import com.example.schedule.Utils.Time.timeToMinutes
import com.example.schedule.Utils.toUpperCaseFirstLetter
import com.example.schedule.ui.MainActivity
import com.example.schedule.ui.schedule.ScheduleHeaderItemDecorator.StickyHeaderInterface
import com.example.schedule.ui.schedule.SimpleScheduleModel.Companion.getNextLesson
import java.util.*

class ScheduleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val pref = requireActivity().getSharedPreferences(SQLManager.SHARED_PREFERENCES_TABLES, Context.MODE_PRIVATE)
        val table = pref.getString(SQLManager.SHARED_PREFERENCES_TABLE, "")
        val data: MutableList<SimpleScheduleModel> = SQLDataTranslator.getListSimpleScheduleModel(
                SQLScheduleReader(context, table!!, SQLManager.VERSION)
        )

        //data.removeAt(1)
        // data.removeAt(1)

        val lesson = SimpleScheduleModel()
        lesson.apply {
            from = "9:00"
            to = "10:20"
            teacher = "Frol V."
            auditory = "5A"
            typeOfSubject = "lecture"
            subject = "Subject name"
            styleOfSubject = "activity"
            dayOfWeek = 1
            isHeader = false
            id = 15 //todo
            setOptionally(1)

        }
        //addLessonToList(data, lesson)

        data.sortWith(compareBy({ it.dayOfWeek }, {
            when {
                it.from == null -> " "
                it.from!!.length == 4 -> "0" + it.from
                else -> it.from
            }
        }, {
            when {
                it.to == null -> " "
                it.to!!.length == 4 -> "0" + it.to
                else -> it.to
            }
        }))

        recyclerView.addItemDecoration(getDecorator(recyclerView, data))
        val adapter = ScheduleRecyclerViewAdapter(data, this)
        recyclerView.adapter = adapter
        val dayOfWeek = convertUSDayOfWeekToEU(Calendar.getInstance()[Calendar.DAY_OF_WEEK]) - 1

        recyclerView.scrollToPosition(getActualPosition(data, dayOfWeek))

        (requireActivity() as MainActivity?)!!.supportActionBar!!.hide()
        return root
    }
/*
    /*TODO Обработать случаи когда:
        Добавление урока в день, когда других уроков нет (добавление header`а)
        Добавление урока, когда он является последним
     */

    private fun addLessonToList(list: MutableList<SimpleScheduleModel>, lesson: SimpleScheduleModel) {

        if (!lesson.isHeader) {
            for (i in list.indices) {
                val lesOfList = list[i]
                if (lesOfList.dayOfWeek == lesson.dayOfWeek && !lesOfList.isHeader) {
                    val lessonTo = lesson.to
                    val nextLes: SimpleScheduleModel = getNextLesson(list, i)
                    if (!nextLes.isHeader) {
                        if (isTimeIntersect(lesOfList.from!!, lesOfList.to!!,
                                        lesson.from!!, lessonTo!!)) {
                            lesson.counter = lesOfList.counter
                            list.add(i + 1, lesson)
                            break
                        } else if (timeToMinutes(lessonTo) <
                                timeToMinutes(nextLes.from!!)) {
                            lesson.counter = nextLes.counter - 2
                            list.add(i, lesson) //todo
                            incrementCounters(i, list)
                            break
                        }
                    } else {
                        //todo
                        lesson.counter = list[i - 1].counter + 1
                        list.add(i + 1, lesson)
                        break
                    }
                }
            }
        }
    }
*/
    private fun fixCounters(list: MutableList<SimpleScheduleModel>) {

        var counter = 0
        for (i in 0 until list.size) {
            val model: SimpleScheduleModel = list[i]
            if (model.isHeader) counter = 0
            else {
                val nextModel = list[i + 1]
                if (!nextModel.isHeader) {
                    if (isTimeIntersect(model.from!!, model.to!!,
                                    nextModel.from!!, nextModel.to!!)) {
                        val max = model.counter.coerceAtLeast(nextModel.counter)
                        model.counter = max
                        nextModel.counter = max
                    }
                }
            }
        }

    }

    private fun incrementCounters(rangeFrom: Int,
                                  list: MutableList<SimpleScheduleModel>) {

        for (i in rangeFrom until list.size)
            if (!list[i].isHeader) {
                list[i].counter++
            } else break
    }

    private fun getActualPosition(data: List<SimpleScheduleModel>,
                                  dayOfWeek: Int): Int {
        var pos = 0
        val dataDayOfWeek = data[data.size - 1].dayOfWeek - 1
        if (dayOfWeek == dataDayOfWeek) pos = data.size - 1
        else if (dayOfWeek < dataDayOfWeek) {
            while (data[pos].dayOfWeek != dayOfWeek + 1) {
                pos++
            }
        }
        return pos
    }

    private fun getDecorator(recyclerView: RecyclerView,
                             data: List<SimpleScheduleModel>): ScheduleHeaderItemDecorator {
        return ScheduleHeaderItemDecorator(recyclerView, object : StickyHeaderInterface {
            override fun getHeaderPositionForItem(itemPosition: Int): Int {
                var itemPosition = itemPosition
                val headerPosition: Int
                while (itemPosition >= 0) {
                    if (isHeader(itemPosition)) {
                        headerPosition = itemPosition
                        return headerPosition
                    }
                    itemPosition--
                }
                return 0
            }

            override fun getHeaderLayout(headerPosition: Int): Int {
                return R.layout.schedule_header_card_view
            }

            override fun bindHeaderData(header: View?, headerPosition: Int) {
                val calendar = Calendar.getInstance()
                calendar[Calendar.DAY_OF_WEEK] = convertEUDayOfWeekToUS(data[headerPosition].dayOfWeek)
                var dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                val date = calendar[Calendar.DATE]
                val displayedDate: String
                dayOfWeek = if (Calendar.getInstance()[Calendar.DAY_OF_WEEK] == calendar[Calendar.DAY_OF_WEEK]) {
                    getString(R.string.today_ru)
                } else toUpperCaseFirstLetter(dayOfWeek)
                displayedDate = "$dayOfWeek, $date $month"
                header!!.findViewById<TextView>(R.id.day_of_week_header).text = displayedDate
            }

            override fun isHeader(itemPosition: Int): Boolean {
                return data[itemPosition].isHeader
            }
        })
    }

}
