package com.example.schedule.ui.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.SQL.SQLDataTranslator
import com.example.schedule.SQL.SQLManager
import com.example.schedule.SQL.SQLScheduleReader
import com.example.schedule.Util.Time
import com.example.schedule.adapters.ScheduleRecyclerViewAdapter
import com.example.schedule.ui.schedule.ScheduleHeaderItemDecorator.StickyHeaderInterface
import com.example.schedule.viewModel.SimpleScheduleModel
import java.util.*

class ScheduleFragment : Fragment(), IFragmentMovement, ScheduleRecyclerView, OnScheduleChangedListener {

    private lateinit var adapter: ScheduleRecyclerViewAdapter
    private val presenter = ScheduleFragmentPresenter(this)
    private lateinit var data: MutableList<SimpleScheduleModel>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        val pref = requireActivity().getSharedPreferences(SQLManager.SHARED_PREF_DB_TABLE_NAME, Context.MODE_PRIVATE)
        val table = pref.getString(SQLManager.SHARED_PREF_TABLE_NAME_KEY, "")
        val context = requireContext()
        data = SQLDataTranslator.getRawListSimpleScheduleModel(
                SQLScheduleReader(context, table!!, SQLManager.VERSION)
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(getDecorator(recyclerView, data))
        adapter = ScheduleRecyclerViewAdapter(data, this)
        recyclerView.adapter = adapter

        val dayOfWeek = Time.USDayOfWeekToEU(Calendar.getInstance()[Calendar.DAY_OF_WEEK]) - 1
        recyclerView.scrollToPosition(getActualPosition(data, dayOfWeek))
        presenter.prepareData(data)

        root.findViewById<ImageButton>(R.id.addLesson).setOnClickListener {
            onMove(ScheduleEditFragment(this, null, -1), IFragmentMovement.CREATE_INTENTION)
        }
        return root
    }

    override fun onScheduleIsChanged(pos: Int, intention: String, lesson: SimpleScheduleModel?) {
        presenter.addLessonToSchedule(data, lesson!!)
    }

    override fun onItemAdded() {
        adapter.notifyDataSetChanged()
    }

    override fun onItemChanged() {
        //This fun is already implemented in ScheduleRecyclerViewAdapter
    }

    override fun onItemRemoved(pos: Int) {
        //This fun is already implemented in ScheduleFragment
    }

    override fun onMove(fragment: Fragment, intention: String) {
        val data = Bundle()
        data.putString("key", intention)
        fragment.arguments = data
        this.parentFragmentManager
                .beginTransaction()
                .hide(this)
                .addToBackStack("editFragment")
                .add(this.id, fragment)
                .show(fragment)
                .commit()
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

            override fun bindHeaderData(header: View, headerPosition: Int) {
                val calendar = Calendar.getInstance()
                calendar[Calendar.DAY_OF_WEEK] = Time.EUDayOfWeekToUS(data[headerPosition].dayOfWeek)
                var dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                val date = calendar[Calendar.DATE]
                val displayedDate: String
                dayOfWeek = if (Calendar.getInstance()[Calendar.DAY_OF_WEEK] == calendar[Calendar.DAY_OF_WEEK]) {
                    getString(R.string.today_ru)
                } else dayOfWeek!!.capitalize(Locale.getDefault())
                displayedDate = "$dayOfWeek, $date $month"
                header.findViewById<TextView>(R.id.day_of_week_header).text = displayedDate
            }

            override fun isHeader(itemPosition: Int): Boolean {
                return data[itemPosition].isHeader
            }
        })
    }

}
