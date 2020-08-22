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
import com.example.schedule.Utils
import com.example.schedule.Utils.toUpperCaseFirstLetter
import com.example.schedule.ui.MainActivity
import com.example.schedule.ui.schedule.ScheduleHeaderItemDecorator.StickyHeaderInterface
import java.util.*

class ScheduleFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val pref = requireActivity().getSharedPreferences(SQLManager.SHARED_PREFERENCES_TABLES, Context.MODE_PRIVATE)
        val table = pref.getString(SQLManager.SHARED_PREFERENCES_TABLE, "")
        val data: MutableList<SimpleScheduleModel> = SQLDataTranslator.getRawListSimpleScheduleModel(
                SQLScheduleReader(context, table!!, SQLManager.VERSION)
        )
        ScheduleFragmentPresenter(null).prepareData(data)
        recyclerView.addItemDecoration(getDecorator(recyclerView, data))
        val adapter = ScheduleRecyclerViewAdapter(data, this,)
        recyclerView.adapter = adapter
        val dayOfWeek = Utils.Time.convertUSDayOfWeekToEU(Calendar.getInstance()[Calendar.DAY_OF_WEEK]) - 1
        recyclerView.scrollToPosition(getActualPosition(data, dayOfWeek))

        (requireActivity() as MainActivity?)!!.supportActionBar!!.hide()
        return root
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
                calendar[Calendar.DAY_OF_WEEK] = Utils.Time.convertEUDayOfWeekToUS(data[headerPosition].dayOfWeek)
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
