package com.example.schedule.ui.schedule

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleReader
import com.example.schedule.Utils.Time.convertEUDayOfWeekToUS
import com.example.schedule.Utils.Time.convertUSDayOfWeekToEU
import com.example.schedule.Utils.toUpperCaseFirstLetter
import com.example.schedule.ui.MainActivity
import com.example.schedule.ui.schedule.ScheduleHeaderItemDecorator.StickyHeaderInterface
import java.util.*

class ScheduleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val pref = requireActivity().getSharedPreferences(SQLManager.SHARED_PREFERENCES_TABLES, Context.MODE_PRIVATE)
        val table = pref.getString(SQLManager.SHARED_PREFERENCES_TABLE, "")
        val data = fillSchedule(table)
        recyclerView.addItemDecoration(getDecorator(recyclerView, data))
        val adapter = ScheduleRecyclerViewAdapter(ArrayList(data), this)
        recyclerView.adapter = adapter
        val dayOfWeek = convertUSDayOfWeekToEU(Calendar.getInstance()[Calendar.DAY_OF_WEEK]) - 1
        val dataDayOfWeek = data[data.size - 1].dayOfWeek - 1
        var pos = 0
        if (dayOfWeek == dataDayOfWeek) pos = data.size - 1 else if (dayOfWeek < dataDayOfWeek) {
            while (data[pos].dayOfWeek != dayOfWeek + 1) {
                pos++
            }
        }
        recyclerView.scrollToPosition(pos)
        (requireActivity() as MainActivity?)!!.supportActionBar!!.hide()
        return root
    }

    private fun getDecorator(recyclerView: RecyclerView, data: List<SimpleScheduleModel>): ScheduleHeaderItemDecorator {
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
                (header!!.findViewById<View>(R.id.day_of_week_header) as TextView).text = displayedDate
            }

            override fun isHeader(itemPosition: Int): Boolean {
                return data[itemPosition].isHeader
            }
        })
    }

    private fun fillSchedule(tableName: String?): List<SimpleScheduleModel> {
        val columns = arrayOf(
                SQLManager.ID,  //index 0
                SQLManager.SUBJECT,  //index 1
                SQLManager.TEACHER,  //index 2
                SQLManager.FROM,  //index 3
                SQLManager.TO,  //index 4
                SQLManager.AUDITORY,  //index 5
                SQLManager.COUNTER,  //index 6
                SQLManager.TYPE_OF_SUBJECT,  // index 7
                SQLManager.OPTIONALLY // index 8
        )
        val week: MutableList<SimpleScheduleModel> = ArrayList()
        val weekStatus = SQLManager.NUMERATOR
        //todo
        val isNumerator = weekStatus == SQLManager.NUMERATOR
        val reader = SQLScheduleReader(requireContext(), tableName!!, SQLManager.VERSION)
        for (dayOfWeek in 1..6) {
            val c = reader.getScheduleByDay(columns, dayOfWeek, weekStatus)
            val s = SimpleScheduleModel()
            s.dayOfWeek = dayOfWeek
            s.counter = -1
            s.isHeader = true
            SimpleScheduleModel.isNumerator = isNumerator
            week.add(s)
            while (c.moveToNext()) {
                week.add(fillModel(c, dayOfWeek))
            }
            c.close()
        }
        reader.close()
        return week
    }

    private fun fillModel(c: Cursor, dayOfWeek: Int): SimpleScheduleModel {
        val model = SimpleScheduleModel()
        model.from = c.getString(3)
        model.to = c.getString(4)
        model.auditory = c.getString(5)
        model.subject = c.getString(1)
        model.teacher = c.getString(2)
        model.counter = c.getInt(6)
        model.typeOfSubject = c.getString(7)
        model.dayOfWeek = dayOfWeek
        model.setOptionally(c.getInt(8))
        model.id = c.getInt(0)
        return SimpleScheduleModel(model)
    }
}
